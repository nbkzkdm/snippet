/**
 * 
 */
package jp.sample.holiday.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jp.sample.holiday.common.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nbkzk
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDateModel {

    /** horidayID */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long holidayId;
    /** 祝日 */
    private LocalDate date;
    /** 祝日名 */
    private String note;
    /** 作成日 */
    @Column(nullable=true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    /**
     * 登録前の事前データセット
     */
    @PrePersist
    public void onPrePersist() {
        if (Objects.isNull(this.getCreateAt())) {
            this.setCreateAt(LocalDateTime.now());
        }
    }

    public static HolidayDateModel convert(HolidayCsv item, LocalDateTime now) {
        HolidayDateModel value = new HolidayDateModel();
        value.setDate(DateUtil.date2LocalDate(item.getDate()));
        value.setNote(item.getNote());
        value.setCreateAt(now);
        return value;
    }

}
