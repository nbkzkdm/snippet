/**
 * 
 */
package jp.sample.postal.code.api.process;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.sample.postal.code.api.http.CityTownVillageDataModel;
import jp.sample.postal.code.api.http.CityTownVillageModel;
import jp.sample.postal.code.api.http.PrefectureDataModel;
import jp.sample.postal.code.common.PostalCodeException;
import jp.sample.postal.code.domain.model.PostalCodeCsv;
import jp.sample.postal.code.domain.model.PostalCodeModel;
import jp.sample.postal.code.domain.service.PostalCodeDbService;
import jp.sample.postal.code.domain.service.PostalCodeService;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Component
@Log4j2
public class CallProcess {

    @Autowired
    private PostalCodeDbService postalCodeDbService;
    @Autowired
    private PostalCodeService postalCodeService;

    private final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 郵便番号の前方一致したものを取得する
     * @param postalCode
     * @return
     */
    public List<PostalCodeModel> search(String postalCode) {
        List<PostalCodeModel> list = postalCodeDbService.findByPostalCodeStartsWith(postalCode);
        return list.stream().sorted((e1, e2) -> e1.getPostalCode().compareTo(e2.getPostalCode())).toList();
    }

    /**
     * 
     * @return
     * @throws PostalCodeException
     */
    public int update() throws PostalCodeException {
        // 前回取得した日付から判断してデータを取得する
        Optional<LocalDateTime> item = this.postalCodeDbService.maxCreateAt();
        LocalDateTime ceateAt = item.get();
        LocalDate maxCreate = LocalDate.from(ceateAt);
        LocalDate lastMonth = makeLastMonth();
        log.debug(String.format("前回の更新日時: %s, 先月の日付: %s", maxCreate.format(yyyyMMdd), lastMonth.format(yyyyMMdd)));
        // 月初より前の日時は差分更新
        if (maxCreate.compareTo(lastMonth) <= 0) {
            List<PostalCodeCsv> delList = this.postalCodeService.getPostalCodeDelList();
            this.postalCodeDbService.postalCodeConvertDelList(delList);
            List<PostalCodeCsv> addList = this.postalCodeService.getPostalCodeAddList();
            List<PostalCodeModel> list = this.postalCodeDbService.postalCodeConvertAddList(addList);
            return list.size();
        }
        // ローカルデータが存在していないと判断して全部をとってくる
        else if (maxCreate.equals(LocalDate.of(9999, 12, 31))) {
            List<PostalCodeCsv> csvList = this.postalCodeService.getPostalCodeAllList();
            List<PostalCodeModel> list = this.postalCodeDbService.postalCodeConvertCreateList(csvList);
            return list.size();
        }
        // ほかの場合は更新しない
        return 0;
    }

    private LocalDate makeLastMonth() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), now.getMonthValue(), 1);
    }

    /**
     * 都道府県指定した市町村情報を取得する
     * @param prefectureName
     * @return
     */
    public PrefectureDataModel searchByPrefectureName(String prefectureName) {
        List<PostalCodeModel> list = postalCodeDbService.findByPrefectureName(prefectureName);
        List<PostalCodeModel> retList = list.stream().sorted((e1, e2) -> e1.getPostalCode().compareTo(e2.getPostalCode())).toList();
        List<CityTownVillageModel> cityTownVillageList = retList.stream().map(e -> CityTownVillageModel.convert(e)).distinct().toList();
        List<CityTownVillageModel> sortedList = cityTownVillageList.stream().sorted(
                (e1, e2) -> e1.getLocalGovernmentCode().compareTo(e2.getLocalGovernmentCode())).toList();
        return PrefectureDataModel.builder().cityTownVillageList(sortedList).prefectureName(prefectureName).build();
    }

    /**
     * 市町村指定した町名情報を取得する
     * @param cityTownVillage
     * @return
     */
    public CityTownVillageDataModel searchByCityTownVillage(String cityTownVillage) {
        List<PostalCodeModel> list = postalCodeDbService.findByCityTownVillage(cityTownVillage);
        PostalCodeModel item = 0 < list.size() ? list.get(0) : new PostalCodeModel();
        List<PostalCodeModel> retList = list.stream().sorted((e1, e2) -> e1.getPostalCode().compareTo(e2.getPostalCode())).toList();
        return CityTownVillageDataModel.builder()
                .localGovernmentCode(item.getLocalGovernmentCode()).prefectureName(item.getPrefectureName())
                .cityTownVillage(cityTownVillage).streetNameList(retList).build();
    }

}
