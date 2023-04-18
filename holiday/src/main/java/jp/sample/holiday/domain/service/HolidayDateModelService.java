/**
 * 
 */
package jp.sample.holiday.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.sample.holiday.common.Diff;
import jp.sample.holiday.common.HolidayException;
import jp.sample.holiday.domain.model.HolidayCsv;
import jp.sample.holiday.domain.model.HolidayDateModel;
import jp.sample.holiday.domain.repository.HolidayDateModelRepository;

/**
 * @author nbkzk
 *
 */
@Service
public class HolidayDateModelService {

    @Autowired
    private HolidayDateModelRepository holidayDateRepository;

    /**
     * csvのリストを祝日リストとして、登録する
     * @param csvList
     * @return
     * @throws HolidayException 
     */
    public List<HolidayDateModel> holidayConvertCreateList(List<HolidayCsv> csvList) throws HolidayException {
        LocalDateTime now = LocalDateTime.now();
        List<HolidayDateModel> list = csvList.stream().map(e -> HolidayDateModel.convert(e, now)).toList();
        return createList(list);
    }

    /**
     * 登録した祝日データの最大値
     * @return
     */
    public Optional<HolidayDateModel> findMax() {
        List<HolidayDateModel> list = this.holidayDateRepository.maxCreateAt();
        return Optional.ofNullable(0 < list.size() ? list.get(0) : new HolidayDateModel());
    }

    /**
     * 祝日データを取り出す
     * @return
     */
    public List<HolidayDateModel> findAll() {
        return this.holidayDateRepository.findAll();
    }

    /**
     * 一年間の休日を取り出す
     * @return
     */
    public List<HolidayDateModel> findYear(int year) {
        return this.holidayDateRepository.findByYear(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
    }

    /**
     * 祝日リストを登録する
     * @param entryList
     * @return
     * @throws HolidayException 
     */
    public List<HolidayDateModel> createList(List<HolidayDateModel> entryList) throws HolidayException {
        List<HolidayDateModel> updateList = new ArrayList<>();
        for (HolidayDateModel model : entryList) {
            Optional<HolidayDateModel> item = this.holidayDateRepository.findByDate(model.getDate());
            HolidayDateModel value = item.orElse(new HolidayDateModel());
            boolean flag = Diff.diff(HolidayDateModel.class, value, model, List.of("holidayId", "createAt"));
            if (!flag) {
                updateList.add(model);
            }
        }
        List<HolidayDateModel> retList =  this.holidayDateRepository.saveAll(updateList);
        if (0 < retList.size()) {
            return retList;
        }
        return this.holidayDateRepository.findAll();
    }

}
