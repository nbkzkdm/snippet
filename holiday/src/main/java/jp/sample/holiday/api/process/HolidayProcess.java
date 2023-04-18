/**
 * 
 */
package jp.sample.holiday.api.process;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.sample.holiday.api.http.HolidayEntity;
import jp.sample.holiday.common.HolidayException;
import jp.sample.holiday.domain.model.HolidayCsv;
import jp.sample.holiday.domain.model.HolidayDateModel;
import jp.sample.holiday.domain.service.DonwloadService;
import jp.sample.holiday.domain.service.HolidayDateModelService;

/**
 * @author nbkzk
 *
 */
@Component
public class HolidayProcess {

    @Autowired
    private DonwloadService donwloadService;
    @Autowired
    private HolidayDateModelService holidayDateModelService;

    /**
     * 
     * @param year
     * @return
     * @throws HolidayException
     */
    public List<HolidayEntity> findYear(int year) throws HolidayException {
        List<HolidayDateModel> list = this.holidayDateModelService.findYear(year);
        return list.stream().map(e -> HolidayEntity.convert(e)).toList();
    }

    /**
     * 祝日データのデータをDBから取得<br/>
     * 前回登録時の最大日付を取り出す。その日付が空かひと月前ならデータを再取得する。
     * それ以外はDBのデータを参照
     * @return
     * @throws ReportException
     */
    public List<HolidayDateModel> updateHoridayDate() throws HolidayException {
        Optional<HolidayDateModel> holidayDate = holidayDateModelService.findMax();
        LocalDateTime lastUpdate = holidayDate.get().getCreateAt();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        if (Objects.nonNull(lastUpdate) && 0 <= lastUpdate.compareTo(oneMonthAgo)) {
            return holidayDateModelService.findAll();
        }
        String contents = donwloadService.callApi();
        List<HolidayCsv> list = donwloadService.csv(contents);
        return holidayDateModelService.holidayConvertCreateList(list);
    }

}
