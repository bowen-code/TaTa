package com.tata.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.statistics.NewAppReceiver;

import com.baidu.android.bbalbs.common.a.c;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.tata.R;
import com.tata.activity.AdviceActivity;
import com.tata.activity.LoginActivity;
import com.tata.activity.PersonActivity;
import com.tata.activity.WriteShare;
import com.tata.adapterAndListener.ShareAdapter;
import com.tata.bean.ShareComment;
import com.tata.bean.ShareMessage;
import com.tata.utils.ACache;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.NetWorkHelper;
import com.tata.utils.ToastUtil;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;
import com.tata.view.PullTask;
import com.tata.view.PullToRefreshBase.OnRefreshListener;
import com.tata.view.PullToRefreshListView;
import com.tata.view.emoji.Emojicon;
import com.tata.view.emoji.EmojiconEditText;
import com.tata.view.emoji.EmojiconGridView.OnEmojiconClickedListener;
import com.tata.view.emoji.EmojiconsPopup;
import com.tata.view.emoji.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import com.tata.view.emoji.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;

import android.R.raw;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ShareFragment extends Fragment implements OnClickListener {
	private LinkedList<ShareMessage> list;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView listView;
	private ImageView camera;
	private ShareAdapter pullAdapter;
	private DisplayImageOptions options;
	String url = "http://120.24.254.127/tata/data/getShare.php";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private SharedPreferences mySharedPreferences;
	private String phoneNumber;
	private EmojiconEditText emojiconEditText;
	private View comment_ly;
	private ImageView send;
	private String userName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.share, container, false);// 关联布局文件
		initView(rootView);
		initOption();
		// 注册广播
		registerBoradcastReceiver();
		mySharedPreferences = getActivity().getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		userName = mySharedPreferences.getString("userName", "");
		initData();
		return rootView;
	}

	private void initOption() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.blank) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.blank)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.blank) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// 设置图片的解码类型//
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//设置图片的解码配置
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成

	}

	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			PullTask pullTask = new PullTask(getActivity(),
					mPullToRefreshListView,
					mPullToRefreshListView.getRefreshType(), pullAdapter, list,
					phoneNumber);
			pullTask.getShare();
		}
	};
	protected int shareID;
	protected int parid;
	protected String ParName;
	private ImageView emojiButton;

	private void initView(View rootView) {
		mPullToRefreshListView = (PullToRefreshListView) rootView
				.findViewById(R.id.shareList);
		// mPullToRefreshListView.setOnRefreshListener(mOnrefreshListener);
		listView = mPullToRefreshListView.getRefreshableView();
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
				true, true));
		camera = (ImageView) rootView.findViewById(R.id.writeMsg);
		camera.setOnClickListener(this);
		final EmojiconsPopup popup = new EmojiconsPopup(rootView, getActivity());

		// Will automatically set size according to the soft keyboard size
		popup.setSizeForSoftKeyboard();

		// If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {
				changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
			}
		});

		// If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {

			public void onKeyboardOpen(int keyBoardHeight) {

			}

			public void onKeyboardClose() {
				if (popup.isShowing())
					popup.dismiss();
			}
		});

		// On emoji clicked, add it to edittext
		popup.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

			public void onEmojiconClicked(Emojicon emojicon) {
				if (emojiconEditText == null || emojicon == null) {
					return;
				}

				int start = emojiconEditText.getSelectionStart();
				int end = emojiconEditText.getSelectionEnd();
				if (start < 0) {
					emojiconEditText.append(emojicon.getEmoji());
				} else {
					emojiconEditText.getText().replace(Math.min(start, end),
							Math.max(start, end), emojicon.getEmoji(), 0,
							emojicon.getEmoji().length());
				}
			}
		});

		// On backspace clicked, emulate the KEYCODE_DEL key event
		popup.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0,
						0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				emojiconEditText.dispatchKeyEvent(event);
			}
		});
		comment_ly = rootView.findViewById(R.id.comment_ly);
		emojiconEditText = (EmojiconEditText) comment_ly
				.findViewById(R.id.editText);
		emojiButton = (ImageView) comment_ly.findViewById(R.id.emoji_btn);
		emojiButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// If popup is not showing => emoji keyboard is not visible, we
				// need to show it
				if (!popup.isShowing()) {

					// If keyboard is visible, simply show the emoji popup
					if (popup.isKeyBoardOpen()) {
						popup.showAtBottom();
						changeEmojiKeyboardIcon(emojiButton,
								R.drawable.ic_action_keyboard);
					}

					// else, open the text keyboard first and immediately after
					// that show the emoji popup
					else {
						emojiconEditText.setFocusableInTouchMode(true);
						emojiconEditText.requestFocus();
						popup.showAtBottomPending();
						final InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.showSoftInput(emojiconEditText,
								InputMethodManager.SHOW_IMPLICIT);
						changeEmojiKeyboardIcon(emojiButton,
								R.drawable.ic_action_keyboard);
					}
				}

				// If popup is showing, simply dismiss it to show the undelying
				// text keyboard
				else {
					popup.dismiss();
				}
			}
		});
		send = (ImageView) comment_ly.findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (phoneNumber.equals("")) {
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.slide_left_in, R.anim.slide_left_out);
					ToastUtil.show(getActivity(), "请先登录！");
					return;
				}

				final OnClickListener clickListener = this;
				// TODO Auto-generated method stub
				final String commentContent = emojiconEditText.getText()
						.toString().trim();
				if (!commentContent.equals("")) {
					send.setOnClickListener(null);
					AsyncHttpClient httpClient = new AsyncHttpClient();
					RequestParams params = new RequestParams();
					params.put("commentContent", commentContent);
					params.setContentEncoding("utf-8");
					params.put("shareID", shareID);
					params.put("parid", parid);
					params.put("phoneNumber", phoneNumber);
					params.put("commentLocation", MainApplication.Lng + "/"
							+ MainApplication.Lat);
					httpClient.post(
							"http://120.24.254.127/tata/data/comment.php",
							params, new TextHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										String arg2) {
									// TODO Auto-generated method stub
									imm.hideSoftInputFromWindow(
											emojiconEditText.getWindowToken(),
											0);
									emojiconEditText.getText().clear();
									comment_ly.setVisibility(View.GONE);
									send.setOnClickListener(clickListener);
									try {
										if (new JSONObject(arg2).getInt("code") == 2) {
											Toast.makeText(getActivity(),
													"评论失败,父评论已删除",
													Toast.LENGTH_SHORT).show();
											return;
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									for (int i = 0; i < list.size(); i++) {
										if (list.get(i).getShareID() == shareID) {
											ShareComment shareComment = new ShareComment();
											shareComment
													.setCommentContent(commentContent);
											shareComment.setUserNameC(userName);
											shareComment
													.setPhoneNumberC(phoneNumber);
											if (parid == 0) {
												shareComment.setParName("");
											} else {
												shareComment
														.setParName(ParName);
											}
											list.get(i).getShareComment()
													.add(shareComment);
											pullAdapter.notifyDataSetChanged();
										}
									}
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										String arg2, Throwable arg3) {
									// TODO Auto-generated method stub
									System.out.println(arg2);
									send.setOnClickListener(clickListener);
								}
							});

				}
			}
		});
	}

	Handler handler = new Handler() {
		Bundle bundle;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				comment_ly.setVisibility(View.VISIBLE);
				emojiconEditText.requestFocus();
				showKeyBoard();
				bundle = msg.getData();
				shareID = bundle.getInt("shareID");
				parid = bundle.getInt("parid");
				ParName = bundle.getString("ParName");
				if (parid != 0)
					emojiconEditText.setHint("回复" + ParName + "：");
				break;
			case 2:
				if (imm != null) {
					imm.hideSoftInputFromWindow(
							emojiconEditText.getWindowToken(), 0);
					imm = null;
				}
				emojiconEditText.getText().clear();
				comment_ly.setVisibility(View.GONE);
				break;
			case 3:
				if (imm != null) {
					imm.hideSoftInputFromWindow(
							emojiconEditText.getWindowToken(), 0);
				}
				emojiconEditText.getText().clear();
				comment_ly.setVisibility(View.GONE);
				bundle = msg.getData();
				String phoneNumber = bundle.getString("phoneNumber");
				String userName = bundle.getString("userName");
				Intent intent = new Intent(getActivity(), PersonActivity.class);
				intent.putExtra("phoneNumber", phoneNumber);
				intent.putExtra("userName", userName);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
				break;
			}
		};
	};
	protected InputMethodManager imm;
	private Dialog dialog;

	private void showKeyBoard() {
		handler.postDelayed(new Runnable() {
			public void run() {
				imm = (InputMethodManager) getActivity().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(emojiconEditText,
						InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 100);
	}

	private void initData() {
		list = new LinkedList<ShareMessage>();
		if (NetWorkHelper.checkNetState(getActivity())) {
			dialog=new MyDialog(getActivity()).myDialog();
			dialog.show();
			AsyncHttpClient httpClient = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("num", MainApplication.num);
			params.put("phoneNumber", phoneNumber);
			httpClient.post(url, params, new TextHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					System.out.println(arg2);
					list = jsonTools.getShareMessages(arg2);
					pullAdapter = new ShareAdapter(handler, list,
							getActivity(), options);
					listView.setAdapter(pullAdapter);
					dialog.dismiss();
					mPullToRefreshListView
							.setOnRefreshListener(mOnrefreshListener);
					ACache mCache = ACache.get(getActivity());
					mCache.put("ShareMessages", arg2);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
				}
			});
		} else {
			ACache mCache = ACache.get(getActivity());
			String jsonString = mCache.getAsString("ShareMessages");
			list = jsonTools.getShareMessages(jsonString);
			pullAdapter = new ShareAdapter(handler, list, getActivity(),
					options);
			listView.setAdapter(pullAdapter);
			mPullToRefreshListView.setOnRefreshListener(mOnrefreshListener);
		}

	}

	public void onClick(View v) {
		if (v.getId() != R.id.comment_ly) {
			comment_ly.setVisibility(View.GONE);
			if (imm != null)
				imm.hideSoftInputFromWindow(emojiconEditText.getWindowToken(),
						0);
		}
		switch (v.getId()) {
		case R.id.writeMsg:
			if (NetWorkHelper.checkNetState(getActivity())) {
				Intent intent = new Intent(getActivity(), WriteShare.class);
				startActivityForResult(intent, 1);
				getActivity().overridePendingTransition(R.anim.push_up_in,
						R.anim.push_up_out);
			} else {
				Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1 && Constants.freshFlag) {
			PullTask pullTask = new PullTask(getActivity(),
					mPullToRefreshListView, 1, pullAdapter, list, phoneNumber);
			pullTask.getShare();
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	private void changeEmojiKeyboardIcon(ImageView iconToBeChanged,
			int drawableResourceId) {
		iconToBeChanged.setImageResource(drawableResourceId);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		private Dialog dialog;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("data.person.myself")) {
				dialog = new MyDialog(getActivity()).myDialog();
				dialog.show();
				phoneNumber = mySharedPreferences.getString("phoneNumber", "");
				userName = mySharedPreferences.getString("userName", "");
				MainApplication.num = 1;
				AsyncHttpClient httpClient = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("num", MainApplication.num);
				params.put("phoneNumber", phoneNumber);
				httpClient.post(url, params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println(arg2);
						list.clear();
						List<ShareMessage> data = jsonTools.getShareMessages(arg2);
						for (int i = 0; i < data.size(); i++) {
							list.add(data.get(i));
						}
						dialog.dismiss();
						pullAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}else if (action.equals("delete.data.person.myself")) {
				 for (int i = 0; i < Constants.deleteList.size(); i++) {
					for (int j = 0; j < list.size(); j++) {
						if(Constants.deleteList.get(i)==list.get(j).getShareID()){
							list.remove(list.get(j));
							break;
						}
					}
				} 
				 pullAdapter.notifyDataSetChanged();
			}else if (action.equals("collect.data.person.myself")){
				 for (int i = 0; i < Constants.collectList.size(); i++) {
						for (int j = 0; j < list.size(); j++) {
							if(Constants.collectList.get(i)==list.get(j).getShareID()){
								list.get(j).setMycollect(1);
								break;
							}
						}
					} 
					 pullAdapter.notifyDataSetChanged();
			}else if (action.equals("mydeletecollectShare.data.person.myself")) {
				for (int i = 0; i < Constants.mydeletecollectShare.size(); i++) {
					for (int j = 0; j < list.size(); j++) {
						if(Constants.mydeletecollectShare.get(i)==list.get(j).getShareID()){
							list.get(j).setMycollect(0);
							break;
						}
					}
				} 
				 pullAdapter.notifyDataSetChanged();
			}
		}

	};
	
	

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("data.person.myself");
		myIntentFilter.addAction("delete.data.person.myself");
		myIntentFilter.addAction("collect.data.person.myself");
		myIntentFilter.addAction("mydeletecollectShare.data.person.myself");
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
}
