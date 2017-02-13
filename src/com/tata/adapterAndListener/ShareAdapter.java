package com.tata.adapterAndListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tata.R;
import com.tata.R.color;
import com.tata.activity.BanjiActivity;
import com.tata.activity.LoginActivity;
import com.tata.activity.MyCollectActivity;
import com.tata.activity.PersonActivity;
import com.tata.activity.ShowPicActivity;
import com.tata.bean.Dianzan;
import com.tata.bean.ShareComment;
import com.tata.bean.ShareMessage;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.ToastUtil;
import com.tata.utils.Tools;
import com.tata.utils.jsonTools;
import com.tata.view.emoji.EmojiconTextView;
import com.unionpay.mobile.android.widgets.ba;

public class ShareAdapter extends BaseAdapter {

	private List<ShareMessage> list;
	private ShareHolder holder;
	private Context mContext;
	private Button delete;
	private Button collect;
	private PopupWindow popupWindow;
	public int[] pictures;
	DisplayImageOptions options;
	// private String myphoneNumber;
	// LatLng locationLatLng = new LatLng(30.598428, 114.311831);
	// private LatLng itemLatLng;
	private int index;
	// private double distance;
	private mypopWindow mMorePopupWindow;
	private int mShowMorePopupWindowWidth;
	private int mShowMorePopupWindowHeight;
	boolean flag = false;
	private View itemView;
	private Handler handler;
	private Boolean back;
	// private String userName;
	private SharedPreferences sp;

	static class ShareHolder {
		private ImageView figure;
		private ImageView picture;
		private ImageView gender;
		private ImageView overflow;
		private ImageView comment;
		private TextView nickName;
		private TextView sharelocation;
		private TextView distance;
		private TextView time;
		private TextView content;
		private GridView gridView;
		private TextView dianzan;
		private LinearLayout commentAndLike;
		private LinearLayout like_layout;
		private LinearLayout commentLayout;

	}

	public ShareAdapter(Handler handler, List<ShareMessage> list,
			Context context, DisplayImageOptions options) {
		this.list = list;
		mContext = context;
		this.options = options;
		this.handler = handler;
		sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		this.back = false;
		// userName=context.getSharedPreferences("user",
		// Context.MODE_PRIVATE).getString("userName", "");
		// myphoneNumber = context.getSharedPreferences("user",
		// Context.MODE_PRIVATE).getString("phoneNumber", "");
	}

	public ShareAdapter(Boolean back, Handler handler, List<ShareMessage> list,
			Context context, DisplayImageOptions options) {
		this.list = list;
		this.back = back;
		mContext = context;
		this.options = options;
		this.handler = handler;
		sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		// userName=context.getSharedPreferences("user",
		// Context.MODE_PRIVATE).getString("userName", "");
		// myphoneNumber = context.getSharedPreferences("user",
		// Context.MODE_PRIVATE).getString("phoneNumber", "");
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ShareMessage item = list.get(position);
		// if (convertView == null) {
		holder = new ShareHolder();
		if (item.getType() == 0) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.share_item, null);
		} else if (item.getType() == 1) {
			if (item.getShareImg().size() == 1) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.share_item_picture, null);
				holder.picture = (ImageView) convertView
						.findViewById(R.id.picture);
			} else if (item.getShareImg().size() == 4) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.share_item_four_picture, null);
				holder.gridView = (GridView) convertView
						.findViewById(R.id.picture);
				holder.gridView
						.setSelector(new ColorDrawable(Color.TRANSPARENT));
			} else {

				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.share_item_morepicture, null);
				holder.gridView = (GridView) convertView
						.findViewById(R.id.picture);
				holder.gridView
						.setSelector(new ColorDrawable(Color.TRANSPARENT));
			}
		}
		holder.figure = (ImageView) convertView.findViewById(R.id.figure);
		holder.figure.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String phoneNumber = item.getPhoneNumber();
				String userName = item.getName();
				Intent intent = new Intent(mContext, PersonActivity.class);
				intent.putExtra("phoneNumber", phoneNumber);
				intent.putExtra("userName", userName);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		holder.nickName = (TextView) convertView.findViewById(R.id.nickName);
		holder.nickName.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phoneNumber = item.getPhoneNumber();
				String userName = item.getName();
				Intent intent = new Intent(mContext, PersonActivity.class);
				intent.putExtra("phoneNumber", phoneNumber);
				intent.putExtra("userName", userName);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		holder.gender = (ImageView) convertView.findViewById(R.id.gender);
		holder.overflow = (ImageView) convertView.findViewById(R.id.overflow);
		holder.comment = (ImageView) convertView.findViewById(R.id.comment);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.content = (TextView) convertView.findViewById(R.id.content);
		holder.distance = (TextView) convertView.findViewById(R.id.distance);
		holder.sharelocation = (TextView) convertView
				.findViewById(R.id.sharelocation);
		ImageLoader.getInstance().displayImage(item.getUserImg(),
				holder.figure, options);
		holder.nickName.setText(item.getName());
		holder.commentAndLike = (LinearLayout) convertView
				.findViewById(R.id.commentAndLike);
		holder.like_layout = (LinearLayout) convertView
				.findViewById(R.id.like_layout);
		holder.commentLayout = (LinearLayout) convertView
				.findViewById(R.id.commentLayout);
		holder.dianzan = (TextView) convertView.findViewById(R.id.dianzan);
		if (item.getGender() == 0) {
			holder.gender.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.male));
		} else {
			holder.gender.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.female));
		}
		holder.time.setText(item.getTime());
		if (!item.getContent().equals("")) {
			holder.content.setText(item.getContent());
		} else {
			holder.content.setVisibility(View.GONE);
		}
		// holder.sharelocation.setText(item.getShareLocation());
		final Tools tools = new Tools(handler);
		if (item.getDianzan().size() == 0 && item.getShareComment().size() == 0) {
			holder.commentAndLike.setVisibility(View.GONE);
		} else if (item.getDianzan().size() != 0) {

			holder.dianzan.setMovementMethod(LinkMovementMethod.getInstance());
			holder.dianzan.setText(new Tools(handler)
					.getDianzanSpannableString(item.getDianzan()));
			if (item.getShareComment().size() != 0) {
				EmojiconTextView textView;
				convertView.findViewById(R.id.fenge)
						.setVisibility(View.VISIBLE);
				for (int i = 0; i < item.getShareComment().size(); i++) {

					if (i == 10) {
						if (!item.isShowAllComment())
							break;
					}
					ShareComment shareComment = item.getShareComment().get(i);
					textView = new EmojiconTextView(mContext);
					textView.setEmojiconSize(28);
					android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
							android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
					params.leftMargin = 2;
					params.bottomMargin = 3;
					textView.setLayoutParams(params);
					textView.setClickable(true);
					textView.setMovementMethod(LinkMovementMethod.getInstance());
					if (shareComment.getParName().equals("")) {
						textView.setText((tools.getClickableSpan(0, i, item,
								sp.getString("phoneNumber", ""),
								shareComment.getUserNameC() + "："
										+ shareComment.getCommentContent())));
					} else {
						textView.setText(tools.getClickableSpan(1, i, item,
								sp.getString("phoneNumber", ""),
								shareComment.getUserNameC() + "回复"
										+ shareComment.getParName() + "："
										+ shareComment.getCommentContent()));
					}
					// textView.setBackgroundResource(R.drawable.huifu);
					if (shareComment.getPhoneNumberC().equals(
							sp.getString("phoneNumber", ""))) {
						textView.setOnLongClickListener(new MylongClickListener(
								position, item.getShareID(), shareComment
										.getCommentID(), shareComment
										.getCommentContent()));
					} else {
						textView.setOnLongClickListener(new CopylongClickListener(
								shareComment.getCommentContent()));
					}
					holder.commentLayout.addView(textView);
				}
				if (item.getShareComment().size() > 10) {
					final TextView moreCommet = (TextView) convertView
							.findViewById(R.id.moreCommet);
					moreCommet.setVisibility(View.VISIBLE);
					if (item.isShowAllComment()) {
						moreCommet.setText("点击恢复");
					} else {
						moreCommet.setText("点击加载更多");
					}
					moreCommet.setOnClickListener(new OnClickListener() {
						EmojiconTextView textView;

						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (!item.isShowAllComment()) {
								item.setShowAllComment(true);
								notifyDataSetChanged();
								// for (int i = 10; i <
								// item.getShareComment().size(); i++) {
								// ShareComment
								// shareComment=item.getShareComment().get(i);
								// textView=new EmojiconTextView(mContext);
								// textView.setEmojiconSize(25);
								// android.widget.LinearLayout.LayoutParams
								// params=new
								// LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
								// params.leftMargin=2;
								// params.bottomMargin=3;
								// textView.setLayoutParams(params);
								// textView.setClickable(true);
								// textView.setMovementMethod(LinkMovementMethod.getInstance());
								// if(shareComment.getParName().equals("")){
								// textView.setText((tools.getClickableSpan(0,i,item,myphoneNumber,shareComment.getUserNameC()+"："+shareComment.getCommentContent())));
								// }else {
								// textView.setText(tools.getClickableSpan(1,i,item,myphoneNumber,shareComment.getUserNameC()+"回复"+shareComment.getParName()+"："+shareComment.getCommentContent()));
								// }
								// //
								// textView.setBackgroundResource(R.drawable.huifu);
								// holder.commentLayout.addView(textView);
								// }
								// holder.commentLayout.invalidate();
							} else {
								// holder.commentLayout.removeViews(10,
								// item.getShareComment().size()-10);
								item.setShowAllComment(false);
								// holder.commentLayout.invalidate();
								notifyDataSetChanged();
							}
						}
					});

				}
			} else {
				convertView.findViewById(R.id.fenge).setVisibility(View.GONE);
			}
		} else {
			EmojiconTextView textView;
			convertView.findViewById(R.id.fenge).setVisibility(View.GONE);
			holder.like_layout.setVisibility(View.GONE);
			for (int i = 0; i < item.getShareComment().size(); i++) {
				if (i == 10) {
					if (!item.isShowAllComment())
						break;
				}
				ShareComment shareComment = item.getShareComment().get(i);
				textView = new EmojiconTextView(mContext);
				textView.setEmojiconSize(28);
				android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				params.leftMargin = 2;
				params.bottomMargin = 3;
				textView.setLayoutParams(params);
				textView.setClickable(true);
				textView.setMovementMethod(LinkMovementMethod.getInstance());
				if (shareComment.getParName().equals("")) {
					textView.setText((tools.getClickableSpan(
							0,
							i,
							item,
							sp.getString("phoneNumber", ""),
							shareComment.getUserNameC() + "："
									+ shareComment.getCommentContent())));
				} else {
					textView.setText(tools.getClickableSpan(
							1,
							i,
							item,
							sp.getString("phoneNumber", ""),
							shareComment.getUserNameC() + "回复"
									+ shareComment.getParName() + "："
									+ shareComment.getCommentContent()));
				}
				// textView.setBackgroundResource(R.drawable.huifu);

				if (shareComment.getPhoneNumberC().equals(
						sp.getString("phoneNumber", ""))) {
					textView.setOnLongClickListener(new MylongClickListener(
							position, item.getShareID(), shareComment
									.getCommentID(), shareComment
									.getCommentContent()));
				} else {
					textView.setOnLongClickListener(new CopylongClickListener(
							shareComment.getCommentContent()));
				}

				holder.commentLayout.addView(textView);
			}
			if (item.getShareComment().size() > 10) {
				final TextView moreCommet = (TextView) convertView
						.findViewById(R.id.moreCommet);
				moreCommet.setVisibility(View.VISIBLE);
				moreCommet.setOnClickListener(new OnClickListener() {
					EmojiconTextView textView;
					boolean flag = false;

					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!flag) {
							for (int i = 10; i < item.getShareComment().size(); i++) {
								ShareComment shareComment = item
										.getShareComment().get(i);
								textView = new EmojiconTextView(mContext);
								textView.setEmojiconSize(25);
								android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
										android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
										android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
								params.leftMargin = 2;
								params.bottomMargin = 3;
								textView.setLayoutParams(params);
								textView.setClickable(true);
								textView.setTextSize(18);
								textView.setMovementMethod(LinkMovementMethod
										.getInstance());
								if (shareComment.getParName().equals("")) {
									textView.setText((tools.getClickableSpan(
											0,
											i,
											item,
											sp.getString("phoneNumber", ""),
											shareComment.getUserNameC()
													+ "："
													+ shareComment
															.getCommentContent())));
								} else {
									textView.setText(tools.getClickableSpan(
											1,
											i,
											item,
											sp.getString("phoneNumber", ""),
											shareComment.getUserNameC()
													+ "回复"
													+ shareComment.getParName()
													+ "："
													+ shareComment
															.getCommentContent()));
								}
								// textView.setBackgroundResource(R.drawable.huifu);
								if (shareComment.getPhoneNumberC().equals(
										sp.getString("phoneNumber", ""))) {
									textView.setOnLongClickListener(new MylongClickListener(
											position, item.getShareID(),
											shareComment.getCommentID(),
											shareComment.getCommentContent()));
								} else {
									textView.setOnLongClickListener(new CopylongClickListener(
											shareComment.getCommentContent()));
								}
								holder.commentLayout.addView(textView);
							}
							// holder.commentLayout.invalidate();
							moreCommet.setText("点击恢复");
							flag = true;
						} else {
							holder.commentLayout.removeViews(10, item
									.getShareComment().size() - 10);
							flag = false;
							// holder.commentLayout.invalidate();
							moreCommet.setText("点击加载更多");
						}
					}
				});

			}
		}

		// if(!item.getLocationXY().equals("")){
		// index = item.getLocationXY().indexOf("/");
		// itemLatLng = new LatLng(Double.parseDouble(item.getLocationXY()
		// .substring(0, index)), Double.parseDouble(item.getLocationXY()
		// .substring(index + 1)));
		// distance = DistanceUtil.getDistance(itemLatLng, locationLatLng);
		// int juli = (int) distance;
		// if (juli < 1000) {
		// holder.distance.setText(juli + "米");
		// } else {
		// holder.distance
		// .setText(String.format("%.1f", juli / 1000.0) + "公里");
		// }
		// }
		if (item.getShareLocation() == null) {
			holder.distance.setVisibility(View.INVISIBLE);
		} else {
			holder.distance.setText(item.getShareLocation());
		}
		if (item.getType() == 1) {
			if (item.getShareImg().size() == 1) {
				ImageLoader.getInstance().displayImage(
						item.getShareImg().get(0), holder.picture, options);
				holder.picture.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						byte[] result;
						List<byte[]> data = new ArrayList<byte[]>();
						Drawable drawable = ((ImageView) v).getDrawable();
						BitmapDrawable bd = (BitmapDrawable) drawable;
						Bitmap bitmap = bd.getBitmap();
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						bitmap.compress(CompressFormat.JPEG, 100, output);
						result = output.toByteArray();
						data.add(result);
						try {
							output.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Constants.data = data;
						Intent intent = new Intent(mContext,
								ShowPicActivity.class);
						intent.putExtra("position", 0);
						mContext.startActivity(intent);
					}
				});
			} else {

				holder.gridView.setAdapter(new MyAdapter(mContext,
						holder.gridView, item.getShareImg()));
				holder.gridView
						.setOnItemClickListener(new MyOnitemClickListener(item
								.getShareImg().size()));
			}
		}
		holder.overflow.setOnClickListener(new myonclickListener(sp.getString(
				"phoneNumber", ""), item));
		itemView = convertView;
		holder.comment.setOnClickListener(new OnClickListener() {
			private View commentIcon;
			private int state;
			private int myPraiseID;

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!flag) {

					LayoutInflater li = (LayoutInflater) mContext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View content = li
							.inflate(R.layout.layout_more, null, false);

					mMorePopupWindow = new mypopWindow(content,
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					mMorePopupWindow
							.setBackgroundDrawable(new BitmapDrawable());
					mMorePopupWindow.setOutsideTouchable(true);
					mMorePopupWindow.setTouchable(true);

					content.measure(View.MeasureSpec.UNSPECIFIED,
							View.MeasureSpec.UNSPECIFIED);
					mShowMorePopupWindowWidth = content.getMeasuredWidth() + 10;
					mShowMorePopupWindowHeight = content.getMeasuredHeight();
					int heightMoreBtnView = v.getHeight();
					mMorePopupWindow
							.showAsDropDown(
									v,
									-mShowMorePopupWindowWidth,
									-(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
					flag = true;
					View parent = mMorePopupWindow.getContentView();
					LinearLayout like = (LinearLayout) parent
							.findViewById(R.id.like);
					TextView dianzanState = (TextView) like
							.findViewById(R.id.dianzanState);
					ImageView imageView = (ImageView) like
							.findViewById(R.id.imageState);
					LinearLayout comment = (LinearLayout) parent
							.findViewById(R.id.comment);
					state = 1;
					for (int i = 0; i < item.getDianzan().size(); i++) {
						if (item.getDianzan().get(i).getPraisePhoneNumber()
								.equals(sp.getString("phoneNumber", ""))) {
							dianzanState.setText("取消");
							imageView
									.setBackgroundResource(R.drawable.like_press);
							state = 0;
							myPraiseID = item.getDianzan().get(i).getPraiseID();
							break;
						}
					}
					// 点赞的监听器
					like.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							mMorePopupWindow.dismiss();
							if (sp.getString("phoneNumber", "").equals("")) {
								Intent intent = new Intent(mContext,
										LoginActivity.class);
								mContext.startActivity(intent);
								((Activity) mContext)
										.overridePendingTransition(
												R.anim.slide_left_in,
												R.anim.slide_left_out);
								ToastUtil.show(mContext, "请先登录！");
								return;
							}
							if (holder.commentAndLike.getVisibility() == View.GONE) {
								holder.commentAndLike
										.setVisibility(View.VISIBLE);
							}
							if (item.getDianzan().size() == 0) {
								holder.like_layout.setVisibility(View.VISIBLE);
							} else {
								// if(state==1)
								// holder.dianzan.setText(holder.dianzan.getText()+"、"+userName);
							}
							View view = itemView.findViewById(R.id.fenge);
							if (item.getShareComment().size() == 0) {
								view.setVisibility(View.GONE);
								System.out.println("无评论");
							}
							if (state == 0) {
								for (int i = 0; i < item.getDianzan().size(); i++) {
									if (item.getDianzan()
											.get(i)
											.getPraisePhoneNumber()
											.equals(sp.getString("phoneNumber",
													""))) {
										item.getDianzan().remove(i);
									}
								}
								if (item.getDianzan().size() == 0) {
									holder.like_layout.setVisibility(View.GONE);
									view.setVisibility(View.GONE);
									if (item.getShareComment().size() == 0) {
										holder.commentAndLike
												.setVisibility(View.GONE);
									}
								}
							}
							AsyncHttpClient httpClient = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put("phoneNumber",
									sp.getString("phoneNumber", ""));
							params.put("shareID", item.getShareID());
							String url;
							if (state == 1) {
								url = "http://120.24.254.127/tata/data/praise.php";
								params.put("praiseLocation",
										MainApplication.Lng + "/"
												+ MainApplication.Lat);
							} else {
								url = "http://120.24.254.127/tata/deletePraise";
								params.put("praiseID", myPraiseID);
							}
							httpClient.post(url, params,
									new TextHttpResponseHandler() {

										@Override
										public void onSuccess(int arg0,
												Header[] arg1, String arg2) {
											// TODO Auto-generated method stub
											System.out.println(arg2);
											if (state == 1) {
												try {
													JSONObject jsonObject = new JSONObject(
															arg2);
													int praiseID = ((JSONObject) jsonObject
															.get("data"))
															.getInt("newPraiseID");
													Dianzan dianzan = new Dianzan(
															praiseID,
															sp.getString(
																	"userName",
																	""),
															sp.getString(
																	"phoneNumber",
																	""));
													item.getDianzan().add(
															dianzan);
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
											notifyDataSetChanged();
										}

										@Override
										public void onFailure(int arg0,
												Header[] arg1, String arg2,
												Throwable arg3) {
											// TODO Auto-generated method stub
											System.out.println(arg2);
										}
									});
						}
					});
					commentIcon = v;
					// 评论的监听器
					comment.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							mMorePopupWindow.dismiss();
							if (sp.getString("phoneNumber", "").equals("")) {
								Intent intent = new Intent(mContext,
										LoginActivity.class);
								mContext.startActivity(intent);
								((Activity) mContext)
										.overridePendingTransition(
												R.anim.slide_left_in,
												R.anim.slide_left_out);
								ToastUtil.show(mContext, "请先登录！");
								return;
							}
							if (handler != null) {
								Message message = new Message();
								Bundle bundle = new Bundle();
								bundle.putInt("shareID", item.getShareID());
								message.setData(bundle);
								message.what = 1;
								handler.sendMessage(message);
							}
						}
					});
				} else {
					mMorePopupWindow.dismiss();
				}

			}

		});

		convertView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(2);
			}
		});
		return convertView;
	}

	static class Holder {

		ImageView img = null;
	}

	class MyAdapter extends BaseAdapter {

		private List<String> shareImg = null;
		private Context context = null;
		private LayoutInflater inflater = null;
		GridView gridView;

		public MyAdapter(Context context, GridView gridView,
				List<String> shareImg) {
			this.shareImg = shareImg;
			this.context = context;
			inflater = LayoutInflater.from(context);
			this.gridView = gridView;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return shareImg.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// 获得holder以及holder对象中tv和img对象的实例
			if (shareImg.size() == 1) {
				gridView.setNumColumns(1);
				convertView = inflater.inflate(R.layout.share_gridview_onlypic,
						null);
				ImageView imageView = (ImageView) convertView
						.findViewById(R.id.gridview_img);
				ImageLoader.getInstance().displayImage(shareImg.get(0),
						imageView, options);
				return convertView;
			} else if (shareImg.size() == 2) {
				Holder holder;
				if (convertView == null) {
					gridView.setNumColumns(2);
					holder = new Holder();
					convertView = inflater.inflate(
							R.layout.share_gridview_twopic, null);
					holder.img = (ImageView) convertView
							.findViewById(R.id.gridview_img);
					convertView.setTag(holder);
				} else {
					holder = (Holder) convertView.getTag();
				}
				ImageLoader.getInstance().displayImage(shareImg.get(position),
						holder.img, options);
				return convertView;
			} else if (shareImg.size() == 3) {

				Holder holder;
				if (convertView == null) {
					gridView.setNumColumns(3);
					convertView = inflater.inflate(
							R.layout.share_gridview_threepic, null);
					holder = new Holder();
					holder.img = (ImageView) convertView
							.findViewById(R.id.gridview_img);

					convertView.setTag(holder);

				} else {
					holder = (Holder) convertView.getTag();

				}
				ImageLoader.getInstance().displayImage(shareImg.get(position),
						holder.img, options);
				// 注意 默认为返回null,必须得返回convertView视图
				return convertView;
			} else {
				Holder holder;
				if (convertView == null) {
					gridView.setNumColumns(2);
					convertView = inflater.inflate(
							R.layout.share_gridview_pic_item, null);
					holder = new Holder();
					holder.img = (ImageView) convertView
							.findViewById(R.id.gridview_img);

					convertView.setTag(holder);

				} else {
					holder = (Holder) convertView.getTag();

				}
				ImageLoader.getInstance().displayImage(shareImg.get(position),
						holder.img, options);
				// 注意 默认为返回null,必须得返回convertView视图
				return convertView;
			}
		}
	}

	class myonclickListener implements OnClickListener {

		private String phoneNumber;
		private String myPhoneNumber;
		private int shareID;
		private int myfoucs;
		private int mycollect;
		private ShareMessage item;
		private Button cancel;

		public myonclickListener(String myPhoneNumber, ShareMessage item) {
			this.phoneNumber = item.getPhoneNumber();
			this.myPhoneNumber = myPhoneNumber;
			this.shareID = item.getShareID();
			this.myfoucs = item.getMyfouce();
			this.mycollect = item.getMycollect();
			this.item = item;
		}

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.overflow:
				View contentView;
				if (myPhoneNumber.equals(phoneNumber)) {
					contentView = LayoutInflater.from(mContext).inflate(
							R.layout.my_popwinodw, null);
					delete = (Button) contentView.findViewById(R.id.delete);
					delete.setOnClickListener(this);
				} else {
					contentView = LayoutInflater.from(mContext).inflate(
							R.layout.my_popwinodw2, null);
					collect = (Button) contentView.findViewById(R.id.collect);
					if (mycollect == 1) {
						collect.setText("取消收藏");
					} else {
						collect.setText("收藏");
					}
					collect.setOnClickListener(this);
				}
				popupWindow = new PopupWindow(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.setFocusable(true);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setContentView(contentView);
				popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0,
						0);
				// collect = (Button) contentView.findViewById(R.id.collect);
				// if (mycollect == 1) {
				// collect.setText("已收藏");
				// } else {
				// collect.setText("收藏");
				// }
				// collect.setOnClickListener(this);
				contentView.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});
				cancel = (Button) contentView.findViewById(R.id.cancel);
				cancel.setOnClickListener(this);
				break;
			case R.id.delete:

				AsyncHttpClient httpClient = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("phoneNumber", myPhoneNumber);
				params.put("shareID", shareID);
				String url = "http://120.24.254.127/tata/data/deleteShare.php";
				httpClient.post(url, params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println(arg2);
						try {
							if (new JSONObject(arg2).getInt("code") == 1) {

								if (back) {
									((Activity) mContext).finish();
									Constants.delete = true;
								}
								list.remove(item);
								notifyDataSetChanged();
								ToastUtil.show(mContext, "删除成功");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						ToastUtil.show(mContext, "删除失败 请重试！");
					}
				});
				popupWindow.dismiss();
				break;
			case R.id.collect:
				if (sp.getString("phoneNumber", "").equals("")) {
					Intent intent = new Intent(mContext, LoginActivity.class);
					mContext.startActivity(intent);
					((Activity) mContext).overridePendingTransition(
							R.anim.slide_left_in, R.anim.slide_left_out);
					ToastUtil.show(mContext, "请先登录！");
					break;
				}
				if (phoneNumber.equals(myPhoneNumber)) {
					ToastUtil.show(mContext, "不能收藏自己的分享");
					popupWindow.dismiss();
					break;
				} else {
					if (collect.getText().toString().equals("收藏")) {
						AsyncHttpClient httpClient2 = new AsyncHttpClient();
						RequestParams params2 = new RequestParams();
						params2.put("phoneNumber", myPhoneNumber);
						params2.put("shareID", shareID);
						String url2 = "http://120.24.254.127/tata/data/collect.php";
						httpClient2.post(url2, params2,
								new TextHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, String arg2) {
										// TODO Auto-generated method stub
										System.out.println(arg2);
										try {
											if (new JSONObject(arg2)
													.getInt("code") == 1) {
												ToastUtil.show(mContext, "已收藏");
												for (int i = 0; i < list.size(); i++) {
													if (list.get(i)
															.getShareID() == (shareID)) {
														list.get(i)
																.setMycollect(1);
													}
												}
												if (back) {
													MainApplication.collectShares
															.add(shareID);
													Constants.collectList.add(shareID);
													Intent mIntent = new Intent("collect.data.person.myself");
													mContext.sendBroadcast(mIntent);
												}
												notifyDataSetChanged();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

									@Override
									public void onFailure(int arg0,
											Header[] arg1, String arg2,
											Throwable arg3) {
										// TODO Auto-generated method stub

									}
								});
						// collect.setText("取消收藏");
					} else {
						AsyncHttpClient httpClient2 = new AsyncHttpClient();
						RequestParams params2 = new RequestParams();
						params2.put("phoneNumber", myPhoneNumber);
						params2.put("shareID", shareID);
						String url2 = "http://120.24.254.127/tata/data/deleteCollect.php";
						httpClient2.post(url2, params2,
								new TextHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, String arg2) {
										// TODO Auto-generated method stub
										System.out.println(arg2);
										try {
											if (new JSONObject(arg2)
													.getInt("code") == 1) {
												ToastUtil
														.show(mContext, "取消成功");
												for (int i = 0; i < list.size(); i++) {
													if (list.get(i)
															.getShareID() == (shareID)) {
														list.get(i)
																.setMycollect(0);
													}
												}
												if(back){
													Constants.collectList.remove((Integer)shareID);
													Intent mIntent = new Intent("data.person.myself");
													mContext.sendBroadcast(mIntent);
												}
												notifyDataSetChanged();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

									@Override
									public void onFailure(int arg0,
											Header[] arg1, String arg2,
											Throwable arg3) {
										// TODO Auto-generated method stub

									}
								});
						// collect.setText("取消收藏");
					}
				}
				popupWindow.dismiss();
				break;
			case R.id.cancel:
				popupWindow.dismiss();
				break;
			}
		}
	}

	class mypopWindow extends PopupWindow {

		public mypopWindow(View contentView, int width, int height) {
			super(contentView, width, height);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void dismiss() {
			// TODO Auto-generated method stub
			flag = false;
			super.dismiss();
		}
	}

	class MyOnitemClickListener implements OnItemClickListener {
		private int size;

		public MyOnitemClickListener(int size) {
			this.size = size;
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			handler.sendEmptyMessage(2);
			byte[] result;
			List<byte[]> data = new ArrayList<byte[]>();
			for (int i = 0; i < size; i++) {
				Drawable drawable = ((ImageView) (parent.getChildAt(i)
						.findViewById(R.id.gridview_img))).getDrawable();
				BitmapDrawable bd = (BitmapDrawable) drawable;
				Bitmap bitmap = bd.getBitmap();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, output);
				result = output.toByteArray();
				data.add(result);
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Constants.data = data;
			Intent intent = new Intent(mContext, ShowPicActivity.class);
			intent.putExtra("position", position);
			mContext.startActivity(intent);
		}
	}

	class MylongClickListener implements OnLongClickListener {
		private int shareID, commentID, index;
		private String content;

		public MylongClickListener(int index, int shareID, int commentID,
				String content) {
			this.shareID = shareID;
			this.commentID = commentID;
			this.index = index;
			this.content = content;
		}

		@SuppressLint("NewApi")
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			MainApplication.longClick = true;
			View contentView = LayoutInflater.from(mContext).inflate(
					R.layout.delete_popwinodw, null);
			// contentView.setBackgroundColor(Color.argb(200, 214, 216,
			// 214));
			final PopupWindow popupWindow = new PopupWindow(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setContentView(contentView);
			popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
			contentView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
			Button copy = (Button) contentView.findViewById(R.id.copy);
			Button delete = (Button) contentView.findViewById(R.id.delete);
			Button cancel = (Button) contentView.findViewById(R.id.cancel);
			copy.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					ClipboardManager copy = (ClipboardManager) mContext
							.getSystemService(Context.CLIPBOARD_SERVICE);
					copy.setText(content);
					Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT);
					popupWindow.dismiss();
				}
			});
			delete.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (sp.getString("phoneNumber", "").equals("")) {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						mContext.startActivity(intent);
						((Activity) mContext).overridePendingTransition(
								R.anim.slide_left_in, R.anim.slide_left_out);
						ToastUtil.show(mContext, "请先登录！");
						return;
					}
					AsyncHttpClient httpClient = new AsyncHttpClient();
					RequestParams params = new RequestParams();
					params.put("phoneNumber", sp.getString("phoneNumber", ""));
					params.put("shareID", shareID);
					params.put("commentID", commentID);
					httpClient
							.post("http://120.24.254.127/tata/data/deleteComment.php",
									params, new TextHttpResponseHandler() {

										@Override
										public void onSuccess(int arg0,
												Header[] arg1, String arg2) {
											// TODO Auto-generated method stub
											System.out.println(arg2);
											try {
												if (new JSONObject(arg2)
														.getInt("code") == 1) {
													for (int i = 0; i < list
															.get(index)
															.getShareComment()
															.size(); i++) {
														if (list.get(index)
																.getShareComment()
																.get(i)
																.getCommentID() == commentID) {
															list.get(index)
																	.getShareComment()
																	.remove(i);
															break;
														}

													}
													notifyDataSetChanged();
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// blockb
												e.printStackTrace();
											}
											popupWindow.dismiss();
										}

										@Override
										public void onFailure(int arg0,
												Header[] arg1, String arg2,
												Throwable arg3) {
											// TODO Auto-generated method stub
											popupWindow.dismiss();
											Toast.makeText(mContext,
													"删除失败，请重试",
													Toast.LENGTH_SHORT);
										}
									});
				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});

			return false;
		}
	}

	class CopylongClickListener implements OnLongClickListener {
		private String content;

		public CopylongClickListener(String content) {
			this.content = content;
		}

		@SuppressLint("NewApi")
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			MainApplication.longClick = true;
			View contentView = LayoutInflater.from(mContext).inflate(
					R.layout.copy_popwinodw, null);
			// contentView.setBackgroundColor(Color.argb(200, 214, 216,
			// 214));
			final PopupWindow popupWindow = new PopupWindow(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setContentView(contentView);
			popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
			contentView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
			Button copy = (Button) contentView.findViewById(R.id.copy);
			Button cancel = (Button) contentView.findViewById(R.id.cancel);
			copy.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					ClipboardManager copy = (ClipboardManager) mContext
							.getSystemService(Context.CLIPBOARD_SERVICE);
					copy.setText(content);
					Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT);
					popupWindow.dismiss();
				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});

			return false;
		}
	}

}
