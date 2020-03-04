package  com.example.commonlib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式常用操作
 *
 * @author Like
 * @version 1.0.0
 */
public class DateUtil {

    /**
     * 将Date转换为指定的字符串类型
     *
     * @param date    要转换的Date
     * @param pattern yyyy-MM-dd HH:mm:ss
     * @return 返回格式化后的字符串
     */
    public static String convert(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 将字符串转换为Date类型
     *
     * @param dateStr 要转换的date字符串
     * @param pattern 要格式化的类型，例如yyyy-MM-dd HH:mm:ss
     * @return 返回日期对应的Date
     */
    public static Date convert(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将日期字符串按照格式转化为另一个字符串
     *
     * @param dateStr           要转换的日期字符串
     * @param originallyPattern 原字符串的日期格式，例如yyyy-MM-dd HH:mm:ss
     * @param targetPattern     想要格式化的日期格式，例如yyyy-MM-dd HH:mm:ss
     * @return 返回格式化后的字符串
     */
    public static String convert(String dateStr, String originallyPattern,
                                 String targetPattern) {
        Date date = convert(dateStr, originallyPattern);
        return convert(date, targetPattern);
    }

    /**
     * Calendar转Date
     *
     * @param calendar
     * @return
     */
    public static Date toDate(Calendar calendar) {
        Date date = calendar.getTime();
        return date;
    }

    /**
     * Date转Calendar
     *
     * @param date
     * @return
     */
    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar toCalendar(String dateStr, String pattern) {
        Date date = convert(dateStr, pattern);
        if (date == null)
            return null;
        return toCalendar(date);
    }

    public static String toString(Date date, String dataFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat,
                Locale.getDefault());
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * Calendar按照格式转成字符串
     *
     * @param calendar 要转换的Calendar
     * @param pattern  要格式化的格式，例如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String toString(Calendar calendar, String pattern) {
        Date date = toDate(calendar);
        return toString(date, pattern);
    }

    /**
     * 向指定Calendar添加年，月，日等
     *
     * @param cal   要添加的cal
     * @param type  要添加的类型
     *              Calendar.DATE 天
     *              Calendar.MONTH 月
     *              Calendar.YEAR 年
     * @param count 要添加的数量，可以为负
     * @return 返回改变后的Calendar，注意，返回后的Calendar与之前Calendar地址不同
     */
    public static Calendar add(Calendar cal, int type, int count) {
        Calendar result = (Calendar) cal.clone();
        result.add(type, count);
        return result;
    }

    /**
     * 将指定Date添加年，月，日等
     *
     * @param date  要改变的Date
     * @param type  要添加的类型
     *              Calendar.DATE 天
     *              Calendar.MONTH 月
     *              Calendar.YEAR 年
     * @param count 要添加的数量，可以为负
     * @return
     */
    public static Date add(Date date, int type, int count) {
        Calendar cal = toCalendar(date);
        cal.add(type, count);
        return toDate(cal);
    }

}
