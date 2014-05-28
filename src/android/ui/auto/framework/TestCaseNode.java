package android.ui.auto.framework;

import android.ui.auto.framework.command.AndroidActionCommand;
import android.ui.auto.framework.command.AndroidActionCommandType;
import android.ui.auto.framework.log.LogUtil;
import android.ui.auto.framework.util.TypeConvertUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TestCaseNode {
    public TestCase testCase;
    public String actionStr = "finish";
    public int action = AndroidActionCommandType.NULL;
    public String arg = "";
    public String[] args;
    public int index = 0;
    public String strValue = "";

    public TestCaseNode(TestCase testCase) {
        this.testCase = testCase;
    }

    public void creatActionCommand(AndroidActionCommand nextNode, AndroidActionCommand preResponse, TestCaseNode lastCaseNode) {

        if (preResponse.actionCode == AndroidActionCommandType.SCREENSHOT) {
            JSONObject json = new JSONObject(preResponse.body);
            final String imageData = json.optString("ImageData", "");
            final String arg = lastCaseNode.arg;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    if (!imageData.equals("")) {
                        byte[] imageBytes = TypeConvertUtil.hexStringToBytes(imageData);
                        File image = new File(testCase.outPath + File.separatorChar + testCase.deviceName + File.separatorChar + testCase.identify + File.separatorChar + TypeConvertUtil.getFormatImageTime() + "_" + arg);
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
                    }
                }
            }).start();

        } else if (preResponse.actionCode == AndroidActionCommandType.DEVICEINFO) {
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
            LogUtil.error(testCase, "��" + nextNode.SeqNo + "��������������");
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
                    LogUtil.error(testCase, "��" + nextNode.SeqNo + "�����������clickǰ���Ȳ���Ԫ��");
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
                if (testCase.currentStep.name.equals("waitProcess")) {
                    paramObject.put("timeout", 3);
                } else {
                    if (testCase.deviceOS.equals("Android")) {
                        paramObject.put("timeout", 6);
                    } else {
                        paramObject.put("timeout", 6);
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
                    LogUtil.error(testCase, "��" + nextNode.SeqNo + "�����������wait to disappearǰ���Ȳ���Ԫ��");
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
                    // LogUtil.error("��" + nextNode.SeqNo + "�����������pressǰ���Ȳ���Ԫ��");
                }
                break;
            case AndroidActionCommandType.VIEWDUMP:
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
        cloneNode.strValue = strValue;
        return cloneNode;
    }
}
