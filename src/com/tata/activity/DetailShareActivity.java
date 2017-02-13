package com.tata.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.adapterAndListener.ShareAdapter;
import com.tata.bean.ShareComment;
import com.tata.bean.ShareMessage;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.view.PullToRefreshListView;
import com.tata.view.emoji.Emojicon;
import com.tata.view.emoji.EmojiconEditText;
import com.tata.view.emoji.EmojiconsPopup;
import com.tata.view.emoji.EmojiconGridView.OnEmojiconClickedListener;
import com.tata.view.emoji.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import com.tata.view.emoji.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class DetailShareActivity extends BaseActivity {

	private DisplayImageOptions options;
	private ListView listView;
	private SharedPreferences mySharedPreferences;
	private String phoneNumber;
	private String userName;
	private EmojiconEditText emojiconEditText;
	private View comment_ly;
	private ImageView send;
	protected int shareID;
	protected int parid;
	protected String ParName;
	private ImageView emojiButton;
	private ShareAdapter pullAdapter;
	private ArrayList<ShareMessage> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_detail_share);
		ShareMessage shareMessage = (ShareMessage) getIntent()
				.getSerializableExtra("share");
		if (MainApplication.collectShares.contains(shareMessage.getShareID())) {
			shareMessage.setMycollect(1);
		}
		setTopText(shareMessage.getName() + "的分享");
		initOption();
		Constants.delete = false;
		list = new ArrayList<ShareMessage>();
		list.add(shareMessage);
		mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		userName = mySharedPreferences.getString("userName", "");
		listView = (ListView) findViewById(R.id.shareList);
		pullAdapter = new ShareAdapter(true, handler, list, this, options);
		listView.setAdapter(pullAdapter);
		final EmojiconsPopup popup = new EmojiconsPopup(getCenterViewlayout(),
				this);

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
		comment_ly = findViewById(R.id.comment_ly);
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
						final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
				final OnClickListener clickListener = this;
				// TODO Auto-generated method stub
				final String commentContent = emojiconEditText.getText()
						.toString().trim();
				if (!commentContent.equals("")) {
					send.setOnClickListener(null);
					System.out.println(commentContent);
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
									System.out.println(arg2);
									try {
										if (new JSONObject(arg2).getInt("code") == 2) {
											Toast.makeText(
													DetailShareActivity.this,
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
									Toast.makeText(DetailShareActivity.this,
											"评论失败,请重试", Toast.LENGTH_SHORT)
											.show();
								}
							});
				}
			}
		});
	}

	private void changeEmojiKeyboardIcon(ImageView iconToBeChanged,
			int drawableResourceId) {
		iconToBeChanged.setImageResource(drawableResourceId);
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
				Intent intent = new Intent(DetailShareActivity.this,
						PersonActivity.class);
				intent.putExtra("phoneNumber", phoneNumber);
				intent.putExtra("userName", userName);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
				break;
			}
		};
	};
	protected InputMethodManager imm;

	private void showKeyBoard() {
		handler.postDelayed(new Runnable() {
			public void run() {
				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(emojiconEditText,
						InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		}, 100);
	}

}
