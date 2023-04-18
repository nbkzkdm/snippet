/**
 * 
 */
package jp.sample.holiday.common;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author nbkzk
 *
 */
public class DateUtil {

    public static LocalDate date2LocalDate(final Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


}
