package android.ui.auto.framework.command;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Iterator;

public class AndroidActionCommand {
    public static int elementID;
    public int actionCode = AndroidActionCommandType.NULL;
    public int SeqNo = 0;
    /**
     * 0 为成功
     * 1 为失败
     *
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

}
