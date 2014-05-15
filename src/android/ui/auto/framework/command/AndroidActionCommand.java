package android.ui.auto.framework.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.ui.auto.framework.log.LogUtil;
import android.ui.auto.framework.util.TypeConvertUtil;

public class AndroidActionCommand {
	public static int elementID;
	public int actionCode = AndroidActionCommandType.NULL;
	public int SeqNo = 0;
	/**
	 * 表示业务成功/失败。 0: 业务成功。Response字段返回业务response报文。
	 * 
	 * 1: 业务失败。Response字段返回一个错误的Response报文。
	 */
	public byte result = 0;
	public String body = "";
	public JSONObject json;

	public AndroidActionCommand() {

	}

	public Hashtable<String, Object> params() throws JSONException {
		final JSONObject paramsObj = json.getJSONObject("params");
		final Hashtable<String, Object> newParams = new Hashtable<String, Object>();
		final Iterator<?> keys = paramsObj.keys();

		while (keys.hasNext()) {
			final String param = (String) keys.next();
			newParams.put(param, paramsObj.get(param));
		}
		return newParams;
	}

	public boolean isElementCommand() {
		try {
			return json.getString("action").startsWith("element:");
		} catch (final JSONException e) {
			return false;
		}
	}
	public AndroidActionCommand excutor() {
		AndroidActionCommand actionCommandResult = new AndroidActionCommand();
		try {
			actionCommandResult.SeqNo = SeqNo + 1;
			switch (SeqNo) {
			case 1:
				if (result == 0) {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO: handle exception
					}
					actionCommandResult.actionCode = AndroidActionCommandType.FIND;
					actionCommandResult.result = 0;
					JSONObject jsonObject = new JSONObject();
					JSONObject paramObject = new JSONObject();
					paramObject.put("findType", 1);
					paramObject.put("context", "");
					paramObject.put("value", "cthome_flight");
					paramObject.put("multiple", true);
					jsonObject.put("params", paramObject.toString());
					jsonObject.put("action", "find");
					actionCommandResult.json = jsonObject;
				}
				break;
			case 2:
				if (result == 0) {
					actionCommandResult.actionCode = AndroidActionCommandType.CLICK;
					actionCommandResult.result = 0;
					json = new JSONObject(body);
					String elementsList = json.optString("elements", "");
					JSONArray jsonArray = new JSONArray(elementsList);
					if (jsonArray.length() > 0) {
						JSONObject jsonItem = (JSONObject) jsonArray.get(0);
						JSONObject jsonObject = new JSONObject();
						JSONObject paramObject = new JSONObject();
						paramObject.put("elementId", jsonItem.opt("id"));
						jsonObject.put("params", paramObject.toString());
						jsonObject.put("action", "click");
						actionCommandResult.json = jsonObject;
					}
				}
				break;
			case 3:
				if (result == 0) {
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
						// TODO: handle exception
					}
					actionCommandResult.actionCode = AndroidActionCommandType.FIND;
					actionCommandResult.result = 0;
					JSONObject jsonObject = new JSONObject();
					JSONObject paramObject = new JSONObject();
					paramObject.put("findType", 1);
					paramObject.put("context", "");
					paramObject.put("value", "查询");
					paramObject.put("multiple", true);
					jsonObject.put("params", paramObject.toString());
					jsonObject.put("action", "find");
					actionCommandResult.json = jsonObject;
				}
				break;
			case 4:
				if (result == 0) {
					actionCommandResult.actionCode = AndroidActionCommandType.CLICK;
					actionCommandResult.result = 0;
					json = new JSONObject(body);
					String elementsList = json.optString("elements", "");
					JSONArray jsonArray = new JSONArray(elementsList);
					if (jsonArray.length() > 0) {
						JSONObject jsonItem = (JSONObject) jsonArray.get(0);
						JSONObject jsonObject = new JSONObject();
						JSONObject paramObject = new JSONObject();
						elementID = (Integer) jsonItem.opt("id");
						paramObject.put("elementId", jsonItem.opt("id"));
						jsonObject.put("params", paramObject.toString());
						jsonObject.put("action", "click");
						actionCommandResult.json = jsonObject;
					}
				}
				// if (result == 0) {
				// actionCommandResult.actionCode =
				// AndroidActionCommandType.PRESSKEY;
				// actionCommandResult.result = 0;
				// json = new JSONObject(body);
				// String elementsList = json.optString("elements", "");
				// JSONArray jsonArray = new JSONArray(elementsList);
				// if (jsonArray.length() > 0) {
				// JSONObject jsonItem = (JSONObject) jsonArray.get(0);
				// JSONObject jsonObject = new JSONObject();
				// JSONObject paramObject = new JSONObject();
				// elementID = (Integer) jsonItem.opt("id");
				// paramObject.put("elementId", jsonItem.opt("id"));
				// paramObject.put("text", "test from server");
				// jsonObject.put("params", paramObject.toString());
				// jsonObject.put("action", "click");
				// actionCommandResult.json = jsonObject;
				// }
				// }
				break;
			// case 5:
			// if (result == 0) {
			// actionCommandResult.actionCode =
			// AndroidActionCommandType.PRESSKEY;
			// actionCommandResult.result = 0;
			// JSONObject jsonObject = new JSONObject();
			// JSONObject paramObject = new JSONObject();
			// paramObject.put("elementId", elementID);
			// paramObject.put("text", "test from server");
			// jsonObject.put("params", paramObject.toString());
			// jsonObject.put("action", "presskey");
			// actionCommandResult.json = jsonObject;
			// }
			// break;
			case 5:
				if (result == 0) {
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
						// TODO: handle exception
					}
					actionCommandResult.actionCode = AndroidActionCommandType.FIND;
					actionCommandResult.result = 0;
					JSONObject jsonObject = new JSONObject();
					JSONObject paramObject = new JSONObject();
					paramObject.put("findType", 1);
					paramObject.put("context", "");
					paramObject.put("value", "东航MU5138 333大");
					paramObject.put("multiple", true);
					jsonObject.put("params", paramObject.toString());
					jsonObject.put("action", "find");
					actionCommandResult.json = jsonObject;
				}
				break;
			case 6:
				if (result == 0) {
					actionCommandResult.actionCode = AndroidActionCommandType.CLICK;
					actionCommandResult.result = 0;
					json = new JSONObject(body);
					String elementsList = json.optString("elements", "");
					JSONArray jsonArray = new JSONArray(elementsList);
					if (jsonArray.length() > 0) {
						JSONObject jsonItem = (JSONObject) jsonArray.get(0);
						JSONObject jsonObject = new JSONObject();
						JSONObject paramObject = new JSONObject();
						elementID = (Integer) jsonItem.opt("id");
						paramObject.put("elementId", jsonItem.opt("id"));
						jsonObject.put("params", paramObject.toString());
						jsonObject.put("action", "click");
						actionCommandResult.json = jsonObject;
					}
				}
				// if (result == 0) {
				// actionCommandResult.actionCode =
				// AndroidActionCommandType.PRESSKEY;
				// actionCommandResult.result = 0;
				// json = new JSONObject(body);
				// String elementsList = json.optString("elements", "");
				// JSONArray jsonArray = new JSONArray(elementsList);
				// if (jsonArray.length() > 0) {
				// JSONObject jsonItem = (JSONObject) jsonArray.get(0);
				// JSONObject jsonObject = new JSONObject();
				// JSONObject paramObject = new JSONObject();
				// paramObject.put("elementId", jsonItem.opt("id"));
				// paramObject.put("text", "input port from server");
				// jsonObject.put("params", paramObject.toString());
				// jsonObject.put("action", "presskey");
				// actionCommandResult.json = jsonObject;
				// }
				// }
				break;
			case 7:
				if (result == 0) {
					actionCommandResult.actionCode = AndroidActionCommandType.SCREENSHOT;
					actionCommandResult.result = 0;
					JSONObject jsonObject = new JSONObject();
					JSONObject paramObject = new JSONObject();
					jsonObject.put("params", paramObject.toString());
					actionCommandResult.json = jsonObject;
				}
				break;
			case 8:
				if (result == 0) {
					json = new JSONObject(body);
					String imageData = json.optString("ImageData", "");
					byte[] imageBytes = TypeConvertUtil.hexStringToBytes(imageData);
					File image = new File("result.png");
					try {
						FileOutputStream fos = new FileOutputStream(image);
						fos.write(imageBytes);
						fos.close();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			default:
				if (result == 0) {
					actionCommandResult.actionCode = AndroidActionCommandType.FINISH;
					actionCommandResult.result = 0;
					JSONObject jsonObject = new JSONObject();
					JSONObject paramObject = new JSONObject();
					jsonObject.put("params", paramObject.toString());
					actionCommandResult.json = jsonObject;
				}
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			LogUtil.error(null, "Request Body:" + body);
		}
		return actionCommandResult;
	}

}
