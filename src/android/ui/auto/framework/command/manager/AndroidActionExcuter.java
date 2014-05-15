package android.ui.auto.framework.command.manager;

import android.ui.auto.framework.command.AndroidActionCommand;

public class AndroidActionExcuter {
	public static AndroidActionCommand excutor(AndroidActionCommand actionCommand) {
		return actionCommand.excutor();
	}
}
