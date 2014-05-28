package android.ui.auto.framework;

public class ErrorModel {
    public String target = "error";
    public TestCase testCase;

    public ErrorModel(TestCase testCase, String error1) {
        this.testCase = testCase;
        target = error1.replace("goto:", "").trim();
    }

    public void goError() {
        testCase.caseStepArray.add(testCase.getStep(target));
    }

    public ErrorModel cloneError(TestCase cloneCase) {
        // TODO Auto-generated method stub
        ErrorModel cloneModel = new ErrorModel(cloneCase, "");
        cloneModel.target = target;
        return cloneModel;
    }
}
