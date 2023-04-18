/**
 * 
 */
package jp.sample.holiday.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.sample.holiday.api.http.HolidayEntity;
import jp.sample.holiday.api.http.ResponseData;
import jp.sample.holiday.api.process.HolidayProcess;
import jp.sample.holiday.common.HolidayException;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Controller
@RequestMapping("/holiday")
@Log4j2
public class HolidayController {

    @Autowired
    private HolidayProcess holidayProcess;

    @GetMapping("/search/{year}")
    public ResponseEntity<ResponseData<List<HolidayEntity>>> postalCode(
            @PathVariable("year") int year) {
        ResponseData<List<HolidayEntity>> res = new ResponseData<>();
        try {
            res.setData(holidayProcess.findYear(year));
            res.setMessage(String.format("%04d年の国民の休日", year));
        }
        catch (HolidayException e) {
            log.error(e.getMessage(), e);
            res.setMessage(e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseData<Integer>> update() {
        ResponseData<Integer> res = new ResponseData<>();
        try {
            res.setData(holidayProcess.updateHoridayDate().size());
            res.setMessage("最新データ取得");
        }
        catch (HolidayException e) {
            log.error(e.getMessage(), e);
            res.setMessage(e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
