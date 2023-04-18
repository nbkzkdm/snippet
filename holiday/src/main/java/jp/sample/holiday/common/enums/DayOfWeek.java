/**
 * 
 */
package jp.sample.holiday.common.enums;

/**
 * @author nbkzk
 *
 */
public enum DayOfWeek {

    MONDAY(1, "月", false),
    TUESDAY(2, "火", false),
    WEDNESDAY(3, "水", false),
    THURSDAY(4, "木", false),
    FRIDAY(5, "金", false),
    SATURDAY(6, "土", true),
    SUNDAY(7, "日", true),
    ;
    private final int day;
    private final String value;
    private final boolean hodliday;
    DayOfWeek(int day, String value, boolean hodliday) {
        this.day = day;
        this.value = value;
        this.hodliday = hodliday;
    }
    public int getDay() {
        return day;
    }
    public String getValue() {
        return value;
    }
    public boolean isHodliday() {
        return hodliday;
    }

    public static DayOfWeek of(int day) {
        DayOfWeek[] array = values();
        for (DayOfWeek item : array) {
            if (day == item.getDay()) {
                return item;
            }
        }
        return null;
    }

    public String toLog() {
        return String.format("%s曜日", getValue());
    }

}
