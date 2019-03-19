package util;

import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * !禁止直接调用本类，所有去系统配置方法已经移入SysCahce
 *
 * @author Michael
 */
public class SysConfig {

    private static Properties prop;

    static {
        prop = new Properties();
        FileInputStream configFileInputStream = null;
        try {
            prop.load(SysConfig.class.getClassLoader().getResourceAsStream("conf.properties"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String getString(String key) {
        String result = prop.getProperty(key.toLowerCase(), "");
        if (StringUtils.isBlank(result)) {
            result = prop.getProperty(key, "");
        }
        return result;
    }

    public static Integer getInteger(String key) {
        String value = SysConfig.getString(key);
        Integer result = null;
        if (value != null && value.length() > 0) {
            result = Integer.parseInt(value);
        }
        return result;
    }

    public static Boolean getBoolean(String key) {
        String value = SysConfig.getString(key);
        Boolean result = false;
        if (value != null && value.length() > 0) {
            if (value.trim().equals("true")) {
                result = true;
            }
        }
        return result;
    }

}