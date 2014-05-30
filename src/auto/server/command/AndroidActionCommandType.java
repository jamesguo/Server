package auto.server.command;

public class AndroidActionCommandType {
    public final static int NULL = 0x000;
    public final static int CLICK = 0x001;
    public final static int FIND = 0x002;
    public final static int SWIP = 0x003;
    public final static int SCROLLTO = 0x004;
    public final static int SETTEXT = 0x005;
    public final static int GETTEXT = 0x006;
    public final static int PRESSKEY = 0x007;
    public final static int BACK = 0x008;
    public final static int MENU = 0x009;
    public final static int WAIT = 0x00A;
    public final static int WAKEUP = 0x00B;
    public final static int SCREENSHOT = 0x00C;
    public final static int VIEWDUMP = 0x00D;
    public final static int DEVICEINFO = 0x00E;
    public final static int FINISH = 0x00F;
    public static final int SEE = 0x010;
    public static final int WAITTODISAPPEAR = 0x011;

    public static int getActionFromStr(String action) {
        // TODO Auto-generated method stub
        String temp = action.toUpperCase().trim();
        if (temp.equals("CLICK")) {
            return AndroidActionCommandType.CLICK;
        } else if (temp.startsWith("FIND")) {
            return AndroidActionCommandType.FIND;
        } else if (temp.equals("SWIP")) {
            return AndroidActionCommandType.SWIP;
        } else if (temp.equals("SCROLLTO")) {
            return AndroidActionCommandType.SCROLLTO;
        } else if (temp.equals("SETTEXT")) {
            return AndroidActionCommandType.SETTEXT;
        } else if (temp.equals("GETTEXT")) {
            return AndroidActionCommandType.GETTEXT;
        } else if (temp.equals("PRESSKEY")) {
            return AndroidActionCommandType.PRESSKEY;
        } else if (temp.equals("BACK")) {
            return AndroidActionCommandType.BACK;
        } else if (temp.equals("MENU")) {
            return AndroidActionCommandType.MENU;
        } else if (temp.equals("WAIT")) {
            return AndroidActionCommandType.WAIT;
        } else if (temp.equals("WAKEUP")) {
            return AndroidActionCommandType.WAKEUP;
        } else if (temp.equals("SCREENSHOT")) {
            return AndroidActionCommandType.SCREENSHOT;
        } else if (temp.equals("VIEWDUMP")) {
            return AndroidActionCommandType.VIEWDUMP;
        } else if (temp.equals("DEVICEINFO")) {
            return AndroidActionCommandType.DEVICEINFO;
        } else if (temp.equals("FINISH")) {
            return AndroidActionCommandType.FINISH;
        } else if (temp.startsWith("SEE")) {
            return AndroidActionCommandType.SEE;
        } else if (temp.equals("WAITTODISAPPEAR")) {
            return AndroidActionCommandType.WAITTODISAPPEAR;
        }
        return AndroidActionCommandType.FINISH;
    }
}
