package auto.server.bean;

import auto.server.command.AndroidActionCommand;
import auto.server.command.AndroidActionCommandType;
import auto.server.config.ServerConfig;
import auto.server.log.LogUtil;
import auto.server.util.TypeConvertUtil;
import plugin.sql.bean.BatchModel;
import plugin.sql.bean.ExcuteResultModel;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.bean.TestCaseStepModel;
import plugin.sql.manager.CaseResultManager;
import sun.rmi.runtime.Log;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by yrguo on 14-5-30.
 */
public class TestCase {
    public String identify;
    public String startTime;
    public String name;
    public String outPath;
    public String deviceName = "";
    public String deviceOS = "";
    public LinkedBlockingQueue<TestCaseStep> caseStepArray = new LinkedBlockingQueue<TestCaseStep>();
    public HashMap<String, TestCaseStep> testSteps = new HashMap<String, TestCaseStep>();
    public TestCaseStep currentStep;
    public TestCaseModel testCaseModel;
    public BatchModel batchModel;
    public ExcuteResultModel excuteResultModel;
    public TestCase(BatchModel batchModel,TestCaseModel testCaseModel) {
        this.testCaseModel = testCaseModel;
        this.batchModel = batchModel;
        this.name = testCaseModel.caseName;
        outPath = ServerConfig.getConfig("ResultOutPath") + File.separatorChar + name;
        File file = new File(outPath);
        file.mkdirs();
        ArrayList<TestCaseStepModel> steps = testCaseModel.steps;
        for (TestCaseStepModel caseStepModel : steps) {
            TestCaseStep testCaseStep = new TestCaseStep(this, caseStepModel);
            testSteps.put(caseStepModel.step_name, testCaseStep);
        }
        startTime = LogUtil.getFormatTime();
        excuteResultModel = new ExcuteResultModel();
        excuteResultModel.batch_number = batchModel.batch_number;
        excuteResultModel.case_number = testCaseModel.case_number;
        excuteResultModel.case_start_time = LogUtil.getFormatTime();
        excuteResultModel.case_excute_state = ExcuteResultModel.Excute_State.INIT;
    }
    public synchronized void startCase() {
//        TestCaseStep caseStep = getStep("deviceInfo");
//        if (caseStep != null) {
//            caseStepArray.add(caseStep);
//        }
        excuteResultModel.case_excute_state = ExcuteResultModel.Excute_State.RUNNING;
        TestCaseStep caseStep = getStep("start");
        if (caseStep != null) {
            caseStepArray.add(caseStep);
        }
    }
    public AndroidActionCommand runNextNode(AndroidActionCommand cmd) {
        if (currentStep == null) {
            currentStep = caseStepArray.poll();
        }
        AndroidActionCommand actionCommand = currentStep.runNextNode(cmd);
        if (actionCommand == null) {
            try {
                TestCaseStep lastCaseStep = currentStep;
                currentStep = caseStepArray.poll(3, TimeUnit.SECONDS);
                while (currentStep != null && currentStep.limitTime <= currentStep.excuteTime) {
                    currentStep.assetModel.goToFail();
                    currentStep = caseStepArray.poll(3, TimeUnit.SECONDS);
                }
                if (currentStep == null) {
                    if (lastCaseStep.name.equals("success") || lastCaseStep.name.equals("error")) {
                        LogUtil.error(this, "Test Case going to finish");
                    } else {
                        currentStep = getStep("error");
                    }
                } else {
                    if (lastCaseStep.name.equals("waitProcess")) {
                        AndroidActionCommand androidActionCommand = new AndroidActionCommand();
                        androidActionCommand.result = 1;
                        androidActionCommand.actionCode = AndroidActionCommandType.WAITTODISAPPEAR;
                        if (currentStep.assetModel != null) {
                            currentStep.assetModel.startTime = System.currentTimeMillis() - 3 * 1000;
                        }
                        actionCommand = currentStep.runNextNode(androidActionCommand);
                    } else {
                        if(currentStep.name.equals("success")){
                            excuteResultModel.case_excute_state = ExcuteResultModel.Excute_State.SUCCESS;
                            excuteResultModel.case_end_time =  LogUtil.getFormatTime();
                            CaseResultManager.replaceExcuteResult(excuteResultModel);
                        }else if(currentStep.name.equals("error")){
                            excuteResultModel.case_excute_state = ExcuteResultModel.Excute_State.FAIL;
                            excuteResultModel.case_end_time =  LogUtil.getFormatTime();
                            CaseResultManager.replaceExcuteResult(excuteResultModel);
                        }
                        actionCommand = currentStep.runNextNode(new AndroidActionCommand());
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return actionCommand;
    }

    public synchronized void reset(boolean start) {
        if (start) {
            LogUtil.error(this, "开始测试 case回置");
        } else {
            LogUtil.error(this, "结束测试 case回置");
        }
        caseStepArray.clear();
        deviceName = "";
        deviceOS = "";
        currentStep = null;
        for (TestCaseStep caseStep : testSteps.values()) {
            caseStep.excuteTime = 0;
            caseStep.currentAction = -1;
            if (caseStep.assetModel != null) {
                caseStep.assetModel.startTime = 0;
            }
        }
    }

    public TestCaseStep getStep(String stepName) {
        // TODO Auto-generated method stub
        TestCaseStep caseStep = testSteps.get(stepName);
        if (caseStep == null) {
            caseStep = testSteps.get(stepName + "_" + deviceOS);
        }
        return caseStep;
    }

    public TestCase cloneCase() {
        TestCase cloneCase = new TestCase(batchModel,testCaseModel);
        cloneCase.identify = TypeConvertUtil.getFormatImageTime();
        cloneCase.outPath = outPath;
        cloneCase.deviceName = deviceName;
        cloneCase.deviceOS = deviceOS;
        cloneCase.caseStepArray = new LinkedBlockingQueue<TestCaseStep>();
        cloneCase.testSteps = new HashMap<String, TestCaseStep>();
        Set<String> keys = testSteps.keySet();
        for (String key : keys) {
            TestCaseStep caseStep = testSteps.get(key);
            TestCaseStep cloneStep = caseStep.cloneStep(cloneCase);
            cloneCase.testSteps.put(key, cloneStep);
        }
        if (currentStep != null) {
            cloneCase.currentStep = currentStep.cloneStep(cloneCase);
        } else {
            cloneCase.currentStep = null;
        }
        return cloneCase;
    }
}
