/**
 * 
 */
package jp.sample.postal.code.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.sample.postal.code.api.http.CityTownVillageDataModel;
import jp.sample.postal.code.api.http.PrefectureDataModel;
import jp.sample.postal.code.api.http.ResponseData;
import jp.sample.postal.code.api.process.CallProcess;
import jp.sample.postal.code.common.PostalCodeException;
import jp.sample.postal.code.common.enums.ZipcodeUrl;
import jp.sample.postal.code.domain.model.PostalCodeModel;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Controller
@RequestMapping("/postalcode")
@Log4j2
public class PostalController {

    @Autowired
    private CallProcess callProcess;

    @GetMapping("/search/{postalCode}")
    public ResponseEntity<ResponseData<List<PostalCodeModel>>> postalCode(
            @PathVariable("postalCode") String postalCode) {
        ResponseData<List<PostalCodeModel>> res = new ResponseData<>();
        res.setData(callProcess.search(postalCode));
        res.setMessage("郵便番号取得");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/prefecture/{name}")
    public ResponseEntity<ResponseData<PrefectureDataModel>> prefecture(@PathVariable("name") String name) {
        ResponseData<PrefectureDataModel> res = new ResponseData<>();
        res.setData(callProcess.searchByPrefectureName(name));
        res.setMessage("都道府県配下の市区町村データの取得");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/cityTownVillage/{name}")
    public ResponseEntity<ResponseData<CityTownVillageDataModel>> cityTownVillage(@PathVariable("name") String name) {
        ResponseData<CityTownVillageDataModel> res = new ResponseData<>();
        res.setData(callProcess.searchByCityTownVillage(name));
        res.setMessage("市区町村配下の町名取得");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/prefectureList")
    public ResponseEntity<ResponseData<List<String>>> prefectureList() {
        ResponseData<List<String>> res = new ResponseData<>();
        res.setMessage("都道府県情報取得");
        res.setData(ZipcodeUrl.getPrefectureList());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseData<Integer>> update() {
        ResponseData<Integer> res = new ResponseData<>();
        try {
            res.setData(callProcess.update());
            res.setMessage("最新データ取得");
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            res.setMessage(e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
