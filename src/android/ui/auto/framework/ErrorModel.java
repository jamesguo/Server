package android.ui.auto.framework;

public class ErrorModel {
	public String target = "error";
	public TestCase testCase;
	public ErrorModel(TestCase testCase, String error1) {
		this.testCase = testCase;
		target = error1.replace("goto:", "").trim();
	}

	public void goError() {
		testCase.caseStepArray.add(testCase.testSteps.get(target));
	}
}
