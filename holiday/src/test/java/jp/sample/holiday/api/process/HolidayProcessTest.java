/**
 * 
 */
package jp.sample.holiday.api.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.sample.holiday.api.http.HolidayEntity;
import jp.sample.holiday.common.HolidayException;
import jp.sample.holiday.domain.model.HolidayDateModel;
import jp.sample.holiday.domain.repository.HolidayDateModelRepository;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@SpringBootTest
@Log4j2
class HolidayProcessTest {

    @Autowired
    private HolidayDateModelRepository holidayDateRepository;
    @Autowired
    private HolidayProcess holidayProcess;

    /**
     * {@link jp.sample.holiday.api.process.HolidayProcess#updateHoridayDate()} のためのテスト・メソッド。
     */
    @Test
    void testUpdateHoridayDate() {
        try {
            List<HolidayDateModel> res0 = holidayProcess.updateHoridayDate();
            int expected = res0.size();
            assertTrue(0 < expected);
            List<HolidayDateModel> res1 = holidayProcess.updateHoridayDate();
            assertEquals(expected, res1.size());
            List<HolidayDateModel> res2 = holidayDateRepository.findAll();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneMonthAgo = LocalDateTime.of(now.getYear(), now.getMonthValue(), 1,
                    now.getHour(), now.getMinute(), now.getSecond()).minusMonths(1);
            List<HolidayDateModel> res3 = res2.stream().map(e -> setCreateAt(e, oneMonthAgo)).toList();
            List<HolidayDateModel> res4 = holidayDateRepository.saveAll(res3);
            List<HolidayDateModel> res5 = holidayProcess.updateHoridayDate();
            assertEquals(res3.size(), res4.size());
            assertEquals(res4.size(), res5.size());
            assertEquals(expected, res5.size());
            List<HolidayEntity> test = holidayProcess.findYear(2023);
            for (HolidayEntity model : test) {
                log.debug(model.toString());
            }
        }
        catch (HolidayException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    public HolidayDateModel setCreateAt(HolidayDateModel item, LocalDateTime date) {
        item.setCreateAt(date);
        return item;
    }

}
