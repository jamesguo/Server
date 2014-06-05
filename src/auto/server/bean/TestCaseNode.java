package auto.server.bean;

import auto.server.command.AndroidActionCommand;
import auto.server.command.AndroidActionCommandType;
import auto.server.log.LogUtil;
import auto.server.util.GlobalContent;
import auto.server.util.TypeConvertUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import plugin.sql.bean.DeviceInfoModel;
import plugin.sql.manager.CaseResultManager;
import plugin.sql.manager.DeviceInfoManager;

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
                    }else{
                        LogUtil.error(testCase, "［" + arg + "］图片内容为空");
                    }
                }
            }).start();

        } else if (preResponse.actionCode == AndroidActionCommandType.DEVICEINFO) {
            JSONObject json = new JSONObject(preResponse.body);
            testCase.deviceName = json.optString("NAME");
            testCase.deviceOS = json.optString("OS");
            File fileDir = new File(testCase.outPath + File.separatorChar + testCase.deviceName + File.separatorChar + testCase.identify);
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
            DeviceInfoModel deviceInfoModel = new DeviceInfoModel();
            deviceInfoModel.device_name = testCase.deviceName;
            deviceInfoModel.device_os = testCase.deviceOS;
            deviceInfoModel.device_version = json.optString("Version");
            deviceInfoModel.device_height = json.optInt("height");
            deviceInfoModel.device_width = json.optInt("width");
            DeviceInfoManager.insertDeviceInfo(deviceInfoModel);
            testCase.excuteResultModel.device_name = testCase.deviceName;
            testCase.excuteResultModel.case_result_path = fileDir.getAbsolutePath();
            CaseResultManager.replaceExcuteResult(testCase.excuteResultModel);
        }

        if (arg.startsWith("$")) {
            arg = GlobalContent.getConfig(arg, testCase.deviceOS,testCase.batchModel.batch_owner);
        }
        if (arg == null) {
            LogUtil.error(testCase, "［" + nextNode.SeqNo + "］操作内容为空");
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
                    LogUtil.error(testCase, "［" + nextNode.SeqNo + "］click前需要先查找");
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
                    LogUtil.error(testCase, "［" + nextNode.SeqNo + "］wait to disappear需要先查找");
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
                }
                break;
            case AndroidActionCommandType.VIEWDUMP:
                if (testCase.deviceOS.equals("IOS")) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
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
        cloneNode.strValue = strValue;
        return cloneNode;
    }
}
