package com.tata.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.a.a.a.c;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tata.bean.Dianzan;
import com.tata.bean.FoucsInfo;
import com.tata.bean.NearTravel;
import com.tata.bean.PersonInfo;
import com.tata.bean.PrivateOrder;
import com.tata.bean.Reserve;
import com.tata.bean.Route;
import com.tata.bean.ShareComment;
import com.tata.bean.ShareMessage;

import android.R.integer;
import android.content.Context;

public class jsonTools {

	private static String indentTime;
	private static Calendar calendar;
	private Context mContext;
	
	public static String toJSONString(String[][] data){
		
		JSONArray array = new JSONArray();
			for (int i = 0; i < data.length; i++) {
				JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为所有对象  
				jsonarray.put(data[i][0]);
				jsonarray.put(data[i][1]);
				array.put(jsonarray);
			}
		return array.toString(); 
	}
	
	
	

	public jsonTools(Context mContext) {
	}

	public static String[] getImgUrls(String jsonString) {
		String[] list = new String[4];
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray array = jsonObject.getJSONArray("data");
			for (int i = 0; i < 4; i++) {
				String url = array.getJSONObject(i).getString("imgUrl");
				list[i] = url;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	public static PersonInfo getPersonInfo(String jsonString) {

		PersonInfo pInfo = new PersonInfo();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONObject data = jsonObject.getJSONObject("data");
			pInfo.setPhoneNumber(data.getString("phoneNumber"));
			pInfo.setUserName(data.getString("userName"));
			pInfo.setUserImgUrl(data.getString("userImgUrl"));
			pInfo.setBirthday(data.getString("birthday"));
			pInfo.setGender(data.getInt("gender"));
			pInfo.setSchool(data.getString("school"));
			 pInfo.setBalance(data.getDouble("balance"));
			 pInfo.setBonus(data.getDouble("bonus"));
			 pInfo.setIntegration(data.getInt("integration"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pInfo;
	}

	public static List<NearTravel> getNearTravels(String jsonString) {
		List<NearTravel> data = new ArrayList<NearTravel>();
		JSONObject jsonObject;
		NearTravel nearTravel;
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// // 设置百分数精确度2即保留两位小数
		// nt.setMinimumFractionDigits(0);
		try {
			JSONObject datajson = new JSONObject(jsonString)
					.getJSONObject("data");
			JSONObject collectProductObject;
			JSONArray collectProductjson = datajson.getJSONArray("collectProducts");
			System.out.println(collectProductjson.toString());
			for (int i = 0; i < collectProductjson.length(); i++) {
//				collectProductjson.getString(i)
				collectProductObject = (JSONObject) collectProductjson.get(i);
				if(!MainApplication.collectProducts.contains(collectProductObject.getString("productID")))
				MainApplication.collectProducts.add(collectProductObject.getString("productID"));
			}
			JSONArray productjson = datajson.getJSONArray("product");
			for (int i = 0; i < productjson.length(); i++) {
				nearTravel = new NearTravel();
				jsonObject = (JSONObject) productjson.get(i);
				JSONArray routeArray = jsonObject.getJSONArray("route");
				List<Route> routeList = new ArrayList<Route>();
				Route route;
				for (int j = 0; j < routeArray.length(); j++) {
					JSONObject object = (JSONObject) routeArray.get(j);
					if (object.getString("img1").equals("")) {
						route = new Route(object.getInt("dayID"),
								object.getString("generalize"),
								object.getString("describe"),
								object.getString("remarkr"));
					} else {
						List<String> images = new ArrayList<String>();
						images.add(object.getString("img1"));
						if (!object.getString("img2").equals("")) {
							images.add(object.getString("img2"));
						}
						if (!object.getString("img3").equals("")) {
							images.add(object.getString("img3"));
						}
						if (!object.getString("img4").equals("")) {
							images.add(object.getString("img4"));
						}
						route = new Route(object.getInt("dayID"),
								object.getString("generalize"),
								object.getString("describe"),
								object.getString("remarkr"), images);
					}
					routeList.add(route);
				}
				JSONObject jsonObject2=jsonObject.getJSONObject("cost");
				JSONArray dayArray=jsonObject.getJSONArray("day");
				List<String> days=new ArrayList<String>();
				for (int j = 0; j < dayArray.length(); j++) {
					JSONObject object=(JSONObject)dayArray.get(j);
					days.add(object.getString("useDay"));
				}
				data.add(new NearTravel(jsonObject.getString("serviceTel1"),jsonObject.getString("serviceTel2"),jsonObject.getString("productID"),
						jsonObject.getString("remarkp"), jsonObject
								.getInt("typeID"), 
//								jsonObject
//								.getString("cityName"), 
								jsonObject
								.getString("route"),
//								jsonObject
//								.getString("description"),
								jsonObject
								.getInt("price"),
						jsonObject.getInt("priceTwo"), jsonObject
								.getString("title"), new String[] {
								jsonObject.getString("productImg1"),
								jsonObject.getString("productImg2"),
								jsonObject.getString("productImg3") },
						routeList, jsonObject2.getString("include"), jsonObject2
								.getString("noContain"), jsonObject2
								.getString("refund"), jsonObject
								.getString("booking"),days));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	public static NearTravel getAgainProduct(String jsonString) {
		NearTravel nearTravel;
		try {
			JSONObject jsonObject = new JSONObject(jsonString)
			.getJSONObject("data");
				JSONArray routeArray = jsonObject.getJSONArray("route");
				List<Route> routeList = new ArrayList<Route>();
				Route route;
				for (int j = 0; j < routeArray.length(); j++) {
					JSONObject object = (JSONObject) routeArray.get(j);
					if (object.getString("img1").equals("")) {
						route = new Route(object.getInt("dayID"),
								object.getString("generalize"),
								object.getString("describe"),
								object.getString("remarkr"));
					} else {
						List<String> images = new ArrayList<String>();
						images.add(object.getString("img1"));
						if (!object.getString("img2").equals("")) {
							images.add(object.getString("img2"));
						}
						if (!object.getString("img3").equals("")) {
							images.add(object.getString("img3"));
						}
						if (!object.getString("img4").equals("")) {
							images.add(object.getString("img4"));
						}
						route = new Route(object.getInt("dayID"),
								object.getString("generalize"),
								object.getString("describe"),
								object.getString("remarkr"), images);
					}
					routeList.add(route);
				}
				JSONObject jsonObject2=jsonObject.getJSONObject("cost");
				JSONArray dayArray=jsonObject.getJSONArray("day");
				List<String> days=new ArrayList<String>();
				for (int j = 0; j < dayArray.length(); j++) {
					JSONObject object=(JSONObject)dayArray.get(j);
					days.add(object.getString("useDay"));
				}
				nearTravel=new NearTravel(jsonObject.getString("serviceTel1"),jsonObject.getString("serviceTel2"),jsonObject.getString("productID"),
						jsonObject.getString("remarkp"), jsonObject
						.getInt("typeID"), 
//								jsonObject
//								.getString("cityName"), 
						jsonObject
						.getString("route"),
//								jsonObject
//								.getString("description"),
						jsonObject
						.getInt("price"),
						jsonObject.getInt("priceTwo"), jsonObject
						.getString("title"), new String[] {
					jsonObject.getString("productImg1"),
					jsonObject.getString("productImg2"),
					jsonObject.getString("productImg3") },
					routeList, jsonObject2.getString("include"), jsonObject2
					.getString("noContain"), jsonObject2
					.getString("refund"), jsonObject
					.getString("booking"),days);
				return nearTravel;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<Reserve> getNoPayReserve(String jsonString) {
		List<Reserve> data = new ArrayList<Reserve>();
		Reserve reserve;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				if (object.getInt("indentState") == 1) {
					reserve = new Reserve();
					reserve.setIndentState(1);
					if(!object.isNull("id"))
					reserve.setId(object.getString("id"));
					reserve.setIndentID(object.getInt("indentID"));
					reserve.setProductState(object.getInt("productState"));
					reserve.setProductID(object.getString("productID"));
					reserve.settitle(object.getString("title"));
					indentTime = object.getString("indentTime").trim()
							.substring(0, 10);
					reserve.setIndentTime(indentTime);
					reserve.setTicketsum(object.getInt("ticketNum"));
					reserve.setPrice(object.getInt("price"));
					reserve.setStartTime(object.getString("startDate"));
					reserve.setContactsName(object.getString("contactsName"));
					reserve.setContactsPho(object.getString("contactsPho"));
					if(!object.isNull("payIndentID"))
					reserve.setPayIndentID(object.getInt("payIndentID"));
					String[][] traveller=new String[object.getJSONArray("travellers").length()][2];
					for (int j = 0; j < object.getJSONArray("travellers").length(); j++) {
						traveller[j][0]=object.getJSONArray("travellers").getJSONObject(j).getString("name");
						traveller[j][1]=object.getJSONArray("travellers").getJSONObject(j).getString("IDcard");
					}
					reserve.setTraveller(traveller);
					data.add(reserve);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public static List<Reserve> getNoTravelReserve(String jsonString) {
		List<Reserve> data = new ArrayList<Reserve>();
		Reserve reserve;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				if (object.getInt("indentState") == 2) {
					reserve = new Reserve();
					reserve.setIndentState(2);
					if(!object.isNull("id"))
						reserve.setId(object.getString("id"));
					reserve.setProductState(object.getInt("productState"));
					reserve.setIndentID(object.getInt("indentID"));
					reserve.settitle(object.getString("title"));
					reserve.setProductID(object.getString("productID"));
					indentTime = object.getString("indentTime").trim()
							.substring(0, 10);
					reserve.setIndentTime(indentTime);
					reserve.setTicketsum(object.getInt("ticketNum"));
//					reserve.setStartTime(object.getString("startTime"));
//					reserve.setReturnTime(object.getString("returnTime"));
					reserve.setPrice(object.getInt("price"));
					reserve.setStartTime(object.getString("startDate"));
					reserve.setContactsName(object.getString("contactsName"));
					reserve.setContactsPho(object.getString("contactsPho"));
					if(!object.isNull("payIndentID"))
					reserve.setPayIndentID(object.getInt("payIndentID"));
					String[][] traveller=new String[object.getJSONArray("travellers").length()][2];
					for (int j = 0; j < object.getJSONArray("travellers").length(); j++) {
						traveller[j][0]=object.getJSONArray("travellers").getJSONObject(j).getString("name");
						traveller[j][1]=object.getJSONArray("travellers").getJSONObject(j).getString("IDcard");
					}
					reserve.setTraveller(traveller);
					data.add(reserve);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public static List<Reserve> getTravelledReserve(String jsonString) {
		List<Reserve> data = new ArrayList<Reserve>();
		Reserve reserve;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				if (object.getInt("indentState") == 3||object.getInt("indentState") == 4) {
					reserve = new Reserve();
					if(!object.isNull("id"))
						reserve.setId(object.getString("id"));
					reserve.setProductState(object.getInt("productState"));
					reserve.setIndentState(object.getInt("indentState"));
					reserve.setIndentID(object.getInt("indentID"));
					reserve.setProductID(object.getString("productID"));
					reserve.settitle(object.getString("title"));
					indentTime = object.getString("indentTime").trim()
							.substring(0, 10);
					reserve.setIndentTime(indentTime);
					reserve.setTicketsum(object.getInt("ticketNum"));
					reserve.setStartTime(object.getString("startDate"));
					reserve.setContactsName(object.getString("contactsName"));
					reserve.setContactsPho(object.getString("contactsPho"));
					if(!object.isNull("payIndentID"))
					reserve.setPayIndentID(object.getInt("payIndentID"));
					String[][] traveller=new String[object.getJSONArray("travellers").length()][2];
					for (int j = 0; j < object.getJSONArray("travellers").length(); j++) {
						traveller[j][0]=object.getJSONArray("travellers").getJSONObject(j).getString("name");
						traveller[j][1]=object.getJSONArray("travellers").getJSONObject(j).getString("IDcard");
					}
					reserve.setTraveller(traveller);
					reserve.setPrice(object.getInt("price"));
					data.add(reserve);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public static LinkedList<ShareMessage> getShareMessages(String jsonString) {
		LinkedList<ShareMessage> list = new LinkedList<ShareMessage>();
		List<String> attentionList = new ArrayList<String>();
		List<Integer> collectList = new ArrayList<Integer>();
		JSONObject jsonObject;
		ShareMessage shareMessage;
		calendar = Calendar.getInstance();
		try {
			jsonObject = new JSONObject(jsonString).getJSONObject("data");

			JSONArray jsonArray = jsonObject.getJSONArray("share");
//			if (!jsonObject.isNull("attentions")) {
//				JSONArray attentions = jsonObject.getJSONArray("attentions");
//				for (int i = 0; i < attentions.length(); i++) {
//					JSONObject object = (JSONObject) attentions.get(i);
//					attentionList.add(object.getString("phoneNumberB"));
//				}
//			}
			if (!jsonObject.isNull("collects")) {
				JSONArray collects = jsonObject.getJSONArray("collects");
				for (int i = 0; i < collects.length(); i++) {
					JSONObject object = (JSONObject) collects.get(i);
					collectList.add(object.getInt("shareID"));
				}
				MainApplication.collectShares=collectList;
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				shareMessage = new ShareMessage();
//				for (int j = 0; j < attentionList.size(); j++) {
//					if (attentionList.get(j).equals(
//							object.getString("phoneNumber"))) {
//						shareMessage.setMyfouce(1);
//						break;
//					}
//					shareMessage.setMyfouce(0);
//				}
				for (int j = 0; j < collectList.size(); j++) {
					if (collectList.get(j)==object.getInt("shareID")) {
						shareMessage.setMycollect(1);
						break;
					}
					shareMessage.setMycollect(0);
				}
				shareMessage.setName(object.getString("userName"));
				shareMessage.setPhoneNumber(object.getString("phoneNumber"));
				shareMessage.setUserImg(object.getString("userImg"));
				shareMessage.setGender(object.getInt("gender"));
				// if (object.getString("birthday") != null) {
				//
				// shareMessage.setBirthday(object.getString("birthday"));
				// // shareMessage.setAge(calendar.get(Calendar.YEAR)
				// // - Integer.parseInt(object.getString("birthday")
				// // .substring(0, 4)));
				// }
				shareMessage.setContent(object.getString("shareContent"));
				if (!object.isNull("shareLocation")) {
					shareMessage.setShareLocation(object
							.getString("shareLocation"));
				}
				shareMessage.setShareID(object.getInt("shareID"));
				shareMessage.setTime(TimeDeal.getTime(object
						.getString("shareTime")));
//				shareMessage.setLocationXY(object.getString("locationX")+"/"+object.getString("locationY"));
				if (object.getString("shareImg1").equals("")) {
					shareMessage.setType(0);
				} else {
					shareMessage.setType(1);
					List<String> shareImg = new ArrayList<String>();
					shareImg.add(object.getString("shareImg1"));
					if (!object.getString("shareImg2").equals("")) {
						shareImg.add(object.getString("shareImg2"));
					}
					if (!object.getString("shareImg3").equals("")) {
						shareImg.add(object.getString("shareImg3"));
					}
					if (!object.getString("shareImg4").equals("")) {
						shareImg.add(object.getString("shareImg4"));
					}
					shareMessage.setShareImg(shareImg);
				}
				if (!object.isNull("praise")) {
					JSONArray praise = object.getJSONArray("praise");
					List<Dianzan> dianzan = new ArrayList<Dianzan>();
					for (int j = 0; j < praise.length(); j++) {
						Dianzan dianzan2 = new Dianzan();
						JSONObject name = (JSONObject) praise.get(j);
						dianzan2.setPraiseID(name.getInt("praiseID"));
						dianzan2.setPraisePhoneNumber(name
								.getString("praisePhoneNumber"));
						dianzan2.setUserName(name.getString("userName"));
						dianzan.add(dianzan2);
					}
					shareMessage.setDianzan(dianzan);
				}
				if (!object.isNull("comment")) {
					JSONArray praise = object.getJSONArray("comment");
					ShareComment shareComment;
					List<ShareComment> comment = new ArrayList<ShareComment>();
					for (int j = 0; j < praise.length(); j++) {
						JSONObject name = (JSONObject) praise.get(j);
						shareComment = new ShareComment();
						shareComment.setCommentID(name.getInt("commentID"));
						shareComment.setUserNameC(name.getString("userNameC"));
						shareComment.setParName(name.getString("parName"));
						shareComment.setCommentContent(name
								.getString("commentContent"));
						shareComment.setPhoneNumberC(name
								.getString("phoneNumberC"));
						shareComment.setParPhoneNumber(name
								.getString("parPhoneNumber"));
						comment.add(shareComment);
					}
					shareMessage.setShareComment(comment);
				}

				list.add(shareMessage);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static LinkedList<ShareMessage> getNewShareMessages(String jsonString) {
		LinkedList<ShareMessage> list = new LinkedList<ShareMessage>();
		JSONObject jsonObject;
		ShareMessage shareMessage;
		calendar = Calendar.getInstance();
		JSONArray jsonArray2 = new JSONArray();
		try {
			jsonObject = new JSONObject(jsonString).getJSONObject("data");

			JSONArray jsonArray = jsonObject.getJSONArray("share");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				shareMessage = new ShareMessage();
				shareMessage.setMyfouce(0);
				shareMessage.setMycollect(0);
				shareMessage.setName(object.getString("userName"));
				shareMessage.setPhoneNumber(object.getString("phoneNumber"));
				shareMessage.setUserImg(object.getString("userImg"));
				shareMessage.setGender(object.getInt("gender"));
				// if (object.getString("birthday") != null) {
				//
				// shareMessage.setBirthday(object.getString("birthday"));
				// // shareMessage.setAge(calendar.get(Calendar.YEAR)
				// // - Integer.parseInt(object.getString("birthday")
				// // .substring(0, 4)));
				// }
				shareMessage.setContent(object.getString("shareContent"));

				if (!object.isNull("shareLocation")) {
					shareMessage.setShareLocation(object
							.getString("shareLocation"));
				}
				shareMessage.setShareID(object.getInt("shareID"));
				shareMessage.setTime(TimeDeal.getTime(object
						.getString("shareTime")));
//				shareMessage.setLocationXY(object.getString("locationX")+"/"+object.getString("locationY"));
				if (object.getString("shareImg1").equals("")) {
					shareMessage.setType(0);
				} else {
					shareMessage.setType(1);
					List<String> shareImg = new ArrayList<String>();
					shareImg.add(object.getString("shareImg1"));
					if (!object.getString("shareImg2").equals("")) {
						shareImg.add(object.getString("shareImg2"));
					}
					if (!object.getString("shareImg3").equals("")) {
						shareImg.add(object.getString("shareImg3"));
					}
					if (!object.getString("shareImg4").equals("")) {
						shareImg.add(object.getString("shareImg4"));
					}
					shareMessage.setShareImg(shareImg);
				}

				list.add(shareMessage);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static LinkedList<ShareMessage> getMyShareMessages(String jsonString) {
		LinkedList<ShareMessage> list = new LinkedList<ShareMessage>();
		JSONObject jsonObject;
		ShareMessage shareMessage;
		calendar = Calendar.getInstance();
		try {
			jsonObject = new JSONObject(jsonString);
			JSONObject datajsonObject = jsonObject.getJSONObject("data");
			   JSONObject userobject =datajsonObject.getJSONObject("userInformation");
				JSONArray jsonArray =datajsonObject.getJSONArray("share");
				if(jsonArray.length()==0){
					shareMessage = new ShareMessage();
					shareMessage.setName(userobject.getString("userName"));
					shareMessage.setPhoneNumber(userobject.getString("phoneNumber"));
					shareMessage.setUserImg(userobject.getString("userImg"));
					shareMessage.setGender(userobject.getInt("gender"));
					list.add(shareMessage);
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					shareMessage = new ShareMessage();
					shareMessage.setName(userobject.getString("userName"));
					shareMessage.setPhoneNumber(userobject.getString("phoneNumber"));
					shareMessage.setUserImg(userobject.getString("userImg"));
					shareMessage.setGender(userobject.getInt("gender"));
//					shareMessage = new ShareMessage();
				// if (object.getString("birthday") != null) {
				//
				// shareMessage.setBirthday(object.getString("birthday"));
				// // shareMessage.setAge(calendar.get(Calendar.YEAR)
				// // - Integer.parseInt(object.getString("birthday")
				// // .substring(0, 4)));
				// }
					JSONObject object=jsonArray.getJSONObject(i);
				shareMessage.setContent(object.getString("shareContent"));
				if (!object.isNull("shareLocation")) {
					shareMessage.setShareLocation(object
							.getString("shareLocation"));
				}
				shareMessage.setShareID(object.getInt("shareID"));
				shareMessage.setTime(object.getString("shareTime"));
				// shareMessage.setTime(TimeDeal.getTime(object
				// .getString("shareTime")));
//				if (!object.isNull("locationXY")) {
//					shareMessage.setLocationXY(object.getString("locationXY"));
//				}
				if (object.getString("shareImg1").equals("")) {
					shareMessage.setType(0);
				} else {
					shareMessage.setType(1);
					List<String> shareImg = new ArrayList<String>();
					shareImg.add(object.getString("shareImg1"));
					if (!object.getString("shareImg2").equals("")) {
						shareImg.add(object.getString("shareImg2"));
					}
					if (!object.getString("shareImg3").equals("")) {
						shareImg.add(object.getString("shareImg3"));
					}
					if (!object.getString("shareImg4").equals("")) {
						shareImg.add(object.getString("shareImg4"));
					}
					shareMessage.setShareImg(shareImg);
				}

				if (!object.isNull("praise")) {
					JSONArray praise = object.getJSONArray("praise");
					List<Dianzan> dianzan = new ArrayList<Dianzan>();
					for (int j = 0; j < praise.length(); j++) {
						Dianzan dianzan2 = new Dianzan();
						JSONObject name = (JSONObject) praise.get(j);
//						dianzan2.setPraiseID(name.getInt("praiseID"));
						dianzan2.setPraisePhoneNumber(name
								.getString("praisePhoneNumber"));
						dianzan2.setUserName(name.getString("userName"));
						dianzan.add(dianzan2);
					}
					shareMessage.setDianzan(dianzan);
				}
				if (!object.isNull("comment")) {
					JSONArray praise = object.getJSONArray("comment");
					ShareComment shareComment;
					List<ShareComment> comment = new ArrayList<ShareComment>();
					for (int j = 0; j < praise.length(); j++) {
						JSONObject name = (JSONObject) praise.get(j);
						shareComment = new ShareComment();
						shareComment.setCommentID(name.getInt("commentID"));
						shareComment.setUserNameC(name.getString("userNameC"));
						shareComment.setParName(name.getString("parName"));
						shareComment.setCommentContent(name
								.getString("commentContent"));
						shareComment.setPhoneNumberC(name
								.getString("phoneNumberC"));
						shareComment.setParPhoneNumber(name
								.getString("parPhoneNumber"));
						comment.add(shareComment);
					}
					shareMessage.setShareComment(comment);
				}
				
				list.add(shareMessage);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static LinkedList<ShareMessage> getMyCollectMessages(
			String jsonString) {
		LinkedList<ShareMessage> list = new LinkedList<ShareMessage>();
		JSONObject jsonObject;
		ShareMessage shareMessage;
		calendar = Calendar.getInstance();
		try {
			jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				shareMessage = new ShareMessage();
				shareMessage.setName(object.getString("userName"));
				shareMessage.setShareID(object.getInt("shareID"));
				shareMessage.setPhoneNumber(object.getString("phoneNumber"));
				shareMessage.setUserImg(object.getString("userImg"));
				shareMessage.setGender(object.getInt("gender"));
				// if (object.getString("birthday") != null) {
				//
				// shareMessage.setBirthday(object.getString("birthday"));
				// // shareMessage.setAge(calendar.get(Calendar.YEAR)
				// // - Integer.parseInt(object.getString("birthday")
				// // .substring(0, 4)));
				// }
				shareMessage.setContent(object.getString("shareContent"));
				if (!object.isNull("shareLocation")) {
					shareMessage.setShareLocation(object
							.getString("shareLocation"));
				}
				shareMessage.setTime(TimeDeal.getTime(object
						.getString("collectTime")));
				if (object.getString("shareImg1").equals("")) {
					shareMessage.setType(0);
				} else {
					shareMessage.setType(1);
					List<String> shareImg = new ArrayList<String>();
					shareImg.add(object.getString("shareImg1"));
					if (!object.getString("shareImg2").equals("")) {
						shareImg.add(object.getString("shareImg2"));
					}
					if (!object.getString("shareImg3").equals("")) {
						shareImg.add(object.getString("shareImg3"));
					}
					if (!object.getString("shareImg4").equals("")) {
						shareImg.add(object.getString("shareImg4"));
					}
					shareMessage.setShareImg(shareImg);
				}

				list.add(shareMessage);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static List<PrivateOrder> getMyPrivateOrder(String jsonString) {
		List<PrivateOrder> data = new ArrayList<PrivateOrder>();
		JSONObject jsonObject;
		PrivateOrder order;
		try {
			jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = (JSONObject) jsonArray.get(i);
				order = new PrivateOrder();
//				order.setPayPrivateIndentID(jsonObject.getInt("payPrivateIndentID"));
				order.setPrivateID(jsonObject.getInt("privateID"));
				order.setDestination(jsonObject.getString("destination"));
				order.setStartDate(jsonObject.getString("startDate"));
				order.setStartPlace(jsonObject.getString("startPlace"));
				order.setDays(jsonObject.getInt("days"));
				order.setPersonNum(jsonObject.getInt("personNum"));
				order.setBudget(jsonObject.getInt("budget"));
				order.setDescription(jsonObject.getString("description"));
				order.setPrivateIndentState(jsonObject
						.getInt("privateIndentState"));
				order.setPrivateIndentTime(jsonObject
						.getString("privateIndentTime").substring(0,10));
				data.add(order);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public static List<FoucsInfo> getMyFoucsMessages(String jsonString) {
		List<FoucsInfo> data = new ArrayList<FoucsInfo>();
		JSONObject jsonObject;
		FoucsInfo order;
		try {
			jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = (JSONObject) jsonArray.get(i);
				order = new FoucsInfo();
				order.setTime(jsonObject.getString("attentionTime"));
				order.setPhoneNumber(jsonObject.getString("phoneNumber"));
				order.setUsername(jsonObject.getString("userName"));
				order.setUserImg(jsonObject.getString("userImg"));
				order.setGender(jsonObject.getInt("gender"));
				data.add(order);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
