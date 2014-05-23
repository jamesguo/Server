package android.ui.auto.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.ui.auto.framework.command.AndroidActionCommand;
import android.ui.auto.framework.command.AndroidActionCommandType;
import android.ui.auto.framework.log.LogUtil;
import android.ui.auto.framework.util.TypeConvertUtil;

public class TestCaseNode {
	public TestCase testCase;
	public String actionStr = "finish";
	public int action = AndroidActionCommandType.NULL;
	public String arg = "";
	public String[] args;
	public int index = 0;

	public TestCaseNode(TestCase testCase) {
		this.testCase = testCase;
	}

	public void creatActionCommand(AndroidActionCommand nextNode, AndroidActionCommand preResponse, TestCaseNode lastCaseNode) {

		if (preResponse.actionCode == AndroidActionCommandType.SCREENSHOT) {
			JSONObject json = new JSONObject(preResponse.body);
			String imageData = json.optString("ImageData", "");
			byte[] imageBytes = TypeConvertUtil.hexStringToBytes(imageData);
			File image = new File(testCase.outPath + File.separatorChar + testCase.deviceName + File.separatorChar + TypeConvertUtil.getFormatImageTime() + "_" + lastCaseNode.arg);
			if (image.exists()) {
				image.delete();
			}
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
		}else if(preResponse.actionCode == AndroidActionCommandType.DEVICEINFO){
			JSONObject json = new JSONObject(preResponse.body);
			testCase.deviceName = json.optString("NAME");
			testCase.deviceOS = json.optString("OS");
			File fileDir = new File(testCase.outPath + File.separatorChar + testCase.deviceName);
			fileDir.mkdirs();
			File devicInfo = new File(testCase.outPath + File.separatorChar + testCase.deviceName + File.separatorChar + "deviceInfo.txt");
			try {
				FileOutputStream fos = new FileOutputStream(devicInfo);
				fos.write(json.toString().getBytes("UTF-8"));
				fos.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (arg.startsWith("$")) {
			arg = GlobalContent.getConfig(arg, testCase.deviceOS);
		}
		if (arg == null) {
			LogUtil.error(testCase, "第" + nextNode.SeqNo + "步，案例参数错误");
			return;
		}
		nextNode.actionCode = action;
		nextNode.result = 0;
		JSONObject jsonObject = new JSONObject();
		JSONObject paramObject = new JSONObject();
		switch (action) {
		case AndroidActionCommandType.CLICK:
			if (preResponse.actionCode == AndroidActionCommandType.FIND) {
				JSONObject json = new JSONObject(preResponse.body);
				String elementsList = json.optString("elements", "");
				JSONArray jsonArray = new JSONArray(elementsList);
				if (jsonArray.length() > index) {
					JSONObject jsonItem = (JSONObject) jsonArray.get(index);
					paramObject.put("elementId", jsonItem.opt("id"));
					jsonObject.put("params", paramObject.toString());
					jsonObject.put("action", "click");
				}
			} else {
				LogUtil.error(testCase, "第" + nextNode.SeqNo + "步，案例错误，click前需先查找元素");
			}
			break;
		case AndroidActionCommandType.FIND:
			if (preResponse.actionCode == AndroidActionCommandType.CLICK) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (actionStr.toUpperCase().equals("FINDVIEW")) {
				paramObject.put("findType", 0);
			} else {
				paramObject.put("findType", 1);
			}
			paramObject.put("value", arg);
			paramObject.put("multiple", true);
			if(testCase.currentStep.name.equals("waitProcess")){
				paramObject.put("timeout", 3);
			} else {
				if (testCase.deviceOS.equals("Android")) {
					paramObject.put("timeout", 15);
				} else {
					paramObject.put("timeout", 10);
				}
			}
			jsonObject.put("params", paramObject.toString());
			jsonObject.put("action", "find");
			break;
		case AndroidActionCommandType.SCREENSHOT:

			break;
		case AndroidActionCommandType.WAIT:

			break;
		case AndroidActionCommandType.SEE:
			if (preResponse.actionCode == AndroidActionCommandType.CLICK) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (actionStr.toUpperCase().equals("SEEVIEW")) {
				paramObject.put("findType", 0);
			} else {
				paramObject.put("findType", 1);
			}
			paramObject.put("value", arg);
			paramObject.put("multiple", true);
			jsonObject.put("params", paramObject.toString());
			jsonObject.put("action", "see");
			break;
		case AndroidActionCommandType.WAITTODISAPPEAR:
			if (preResponse.actionCode == AndroidActionCommandType.FIND) {
				JSONObject json = new JSONObject(preResponse.body);
				String elementsList = json.optString("elements", "");
				JSONArray jsonArray = new JSONArray(elementsList);
				if (jsonArray.length() > index) {
					JSONObject jsonItem = (JSONObject) jsonArray.get(index);
					paramObject.put("elementId", jsonItem.opt("id"));
					jsonObject.put("params", paramObject.toString());
					jsonObject.put("action", "wait to disappear");
				}
			} else {
				LogUtil.error(testCase, "第" + nextNode.SeqNo + "步，案例错误，wait to disappear前需先查找元素");
			}
			break;
		case AndroidActionCommandType.PRESSKEY:
			if (preResponse.actionCode == AndroidActionCommandType.FIND) {
				JSONObject json = new JSONObject(preResponse.body);
				String elementsList = json.optString("elements", "");
				JSONArray jsonArray = new JSONArray(elementsList);
				if (jsonArray.length() > index) {
					JSONObject jsonItem = (JSONObject) jsonArray.get(index);
					paramObject.put("elementId", jsonItem.opt("id"));
					paramObject.put("text", arg);
					jsonObject.put("params", paramObject.toString());
					jsonObject.put("action", "presskey");
				}
			} else {
				paramObject.put("elementId", 0);
				paramObject.put("text", arg);
				jsonObject.put("params", paramObject.toString());
				jsonObject.put("action", "presskey");
				// LogUtil.error("第" + nextNode.SeqNo + "步，案例错误，press前需先查找元素");
			}
			break;
		default:
			break;
		}
		jsonObject.put("params", paramObject.toString());
		nextNode.json = jsonObject;
	}

	public void assetSuccess() {
		if (args != null) {
			String target = args[1].trim().replace("goto:", "");
			testCase.caseStepArray.add(testCase.getStep(target));
			if (target.equals("waitProcess")) {
				testCase.caseStepArray.add(testCase.currentStep);
			}
		}
	}

	public TestCaseNode cloneNode(TestCase cloneCase) {
		TestCaseNode cloneNode = new TestCaseNode(cloneCase);
		cloneNode.actionStr = actionStr;
		cloneNode.action = action;
		cloneNode.arg = arg;
		cloneNode.args = args;
		cloneNode.index = index;
		return cloneNode;
	}
}
