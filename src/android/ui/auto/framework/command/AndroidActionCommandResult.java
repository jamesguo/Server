package android.ui.auto.framework.command;

import org.json.JSONException;
import org.json.JSONObject;

import android.ui.auto.framework.log.LogUtil;


public class AndroidActionCommandResult extends AndroidActionCommand {

	public AndroidActionCommandResult(final WDStatus status, AndroidActionCommand actionCommand) {
		try {
			json = new JSONObject();
			json.put("value", status.message());
			this.SeqNo = actionCommand.SeqNo;
			this.result = status.result();
			this.actionCode = actionCommand.actionCode;
			this.body = json.toString();
		} catch (final JSONException e) {
			LogUtil.error(null, "Couldn't create android command result!");
		}
	}

	public AndroidActionCommandResult(final WDStatus status, final JSONObject val, AndroidActionCommand actionCommand) {
		json = new JSONObject();
		try {
			json.put("value", val);
			this.SeqNo = actionCommand.SeqNo;
			this.result = status.result();
			this.actionCode = actionCommand.actionCode;
			this.body = json.toString();
		} catch (final JSONException e) {
			LogUtil.error(null, "Couldn't create android command result!");
		}
	}

	public AndroidActionCommandResult(final WDStatus status, final Object val, AndroidActionCommand actionCommand) {
		json = new JSONObject();
		try {
			json.put("value", val);
			this.SeqNo = actionCommand.SeqNo;
			this.result = status.result();
			this.actionCode = actionCommand.actionCode;
			this.body = json.toString();
		} catch (final JSONException e) {
			LogUtil.error(null, "Couldn't create android command result!");
		}
	}

	public AndroidActionCommandResult(final WDStatus status, final String val, AndroidActionCommand actionCommand) {
		try {
			json = new JSONObject();
			json.put("value", val);
			this.SeqNo = actionCommand.SeqNo;
			this.result = status.result();
			this.actionCode = actionCommand.actionCode;
			this.body = json.toString();
		} catch (final JSONException e) {
			LogUtil.error(null, "Couldn't create android command result!");
		}
	}

	@Override
	public String toString() {
		return json.toString();
	}

}
