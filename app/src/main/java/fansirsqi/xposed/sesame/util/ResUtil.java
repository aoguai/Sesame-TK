package fansirsqi.xposed.sesame.util;
import org.json.JSONException;
import org.json.JSONObject;
public class ResUtil {
    private static final String TAG = ResUtil.class.getSimpleName();
    // 写一个将str转换为jsonobj的方法
    public static String strToJson(String str) {
        try {
            return new JSONObject(str).toString();
        } catch (JSONException e) {
            Log.printStackTrace(TAG, e);
            return null;
        }
    }
    public static Boolean checkResCode(String str) throws JSONException {
        JSONObject jsonObj = new JSONObject(str);
        return checkResCode(TAG, jsonObj);
    }
    public static Boolean checkResCode(String TAG, String str) throws JSONException {
        JSONObject jsonObj = new JSONObject(str);
        return checkResCode(TAG, jsonObj);
    }
    public static Boolean checkResCode(JSONObject jo) {
        return checkResCode(TAG, jo);
    }
    /**
     * 检查JSON对象中的响应码（resultCode），并根据其类型和值返回处理结果。
     *
     * <p>
     * 该方法首先尝试从JSON对象中获取resultCode字段，然后根据其类型（整数或字符串）进行检查。
     * 如果resultCode的值符合预期（整数类型为200，字符串类型为"SUCCESS"或"100"），则返回true。
     * 如果resultCode不存在、类型不匹配或值不符合预期，将记录错误信息并返回false。
     * 如果在处理过程中发生异常，将捕获异常并记录错误信息，然后返回false。
     *
     * @param TAG 用来标识日志的标签
     * @param jo  包含resultCode的JSON对象
     * @return 如果resultCode符合预期，则返回true；否则返回false
     */
    public static Boolean checkResCode(String TAG, JSONObject jo) {
        try {
            Object resCode = jo.opt("resultCode");
            if (resCode == null) {
                Log.record(TAG + "checkResCode err: resultCode不存在");
                return false;
            }
            if (resCode instanceof Integer) {
                if ((Integer) resCode != 200) {
                    recordError(TAG, jo, "resultMsg", "checkResCode Integer err");
                    return false;
                }
                return true;
            } else if (resCode instanceof String) {
                if (!((String) resCode).matches("(?i)SUCCESS|100")) {
                    recordError(TAG, jo, "resultDesc", "checkResCode String err");
                    return false;
                }
                return true;
            }
            Log.record(TAG + "checkResCode Type fail: " + jo);
            return false;
        } catch (Throwable t) {
            Log.runtime(TAG, "checkResCode error: " + t.getMessage());
        }
        return false;
    }
    public static Boolean checkSuccess(String str) throws JSONException {
        JSONObject jo = new JSONObject(str);
        return checkSuccess(TAG, jo);
    }
    public static Boolean checkSuccess(String tag, String str) throws JSONException {
        JSONObject jo = new JSONObject(str);
        return checkSuccess(tag, jo);
    }
    public static Boolean checkSuccess(JSONObject jo) {
        return checkSuccess(TAG, jo);
    }
    public static Boolean checkSuccess(String tag, JSONObject jo) {
        if (!jo.optBoolean("success") && !jo.optBoolean("isSuccess")) {
            logErrorDetails(tag, jo);
            return false;
        }
        return true;
    }
    private static void recordError(String TAG, JSONObject jo, String key, String prefix) throws JSONException {
        if (jo.has(key)) {
            Log.record(TAG + prefix + ": " + jo.getString(key));
        } else if (jo.has("resultView")) {
            Log.record(TAG + prefix + ": " + jo.getString("resultView"));
        } else {
            Log.record(TAG + prefix + ": " + jo);
        }
    }
    private static void logErrorDetails(String tag, JSONObject jo) {
        try {
            if (jo.has("errorMsg")) {
                Log.error(tag, "errorMsg: " + jo.getString("errorMsg"));
            } else if (jo.has("errorMessage")) {
                Log.error(tag, "errorMessage: " + jo.getString("errorMessage"));
            } else if (jo.has("desc")) {
                Log.error(tag, "desc: " + jo.getString("desc"));
            } else if (jo.has("resultDesc")) {
                Log.error(tag, "resultDesc: " + jo.getString("resultDesc"));
            } else if (jo.has("resultView")) {
                Log.error(tag, "resultView: " + jo.getString("resultView"));
            } else {
                Log.runtime(tag, jo.toString());
            }
        } catch (JSONException e) {
            Log.error(tag, "logErrorDetails err:");
            Log.printStackTrace(e);
        }
    }
}
