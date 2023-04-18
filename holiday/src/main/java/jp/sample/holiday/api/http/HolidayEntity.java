/**
 * 
 */
package jp.sample.holiday.api.http;

import java.time.LocalDate;

import jp.sample.holiday.common.enums.DayOfWeek;
import jp.sample.holiday.domain.model.HolidayDateModel;
import lombok.Builder;
import lombok.Data;

/**
 * @author nbkzk
 *
 */
@Data
@Builder
public class HolidayEntity {

    /** 祝日 */
    private LocalDate date;
    /** 祝日 */
    private String dayOfWeek;
    /** 祝日名 */
    private String note;

    public static HolidayEntity convert(HolidayDateModel item) {
        DayOfWeek dow = DayOfWeek.of(item.getDate().getDayOfWeek().getValue());
        HolidayEntity value = HolidayEntity.builder()
                .date(item.getDate())
                .dayOfWeek(dow.getValue())
                .note(item.getNote())
                .build();
        return value;
    }

}
