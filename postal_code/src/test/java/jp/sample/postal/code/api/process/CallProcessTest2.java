/**
 * 
 */
package jp.sample.postal.code.api.process;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.sample.postal.code.api.http.CityTownVillageDataModel;
import jp.sample.postal.code.api.http.PrefectureDataModel;
import jp.sample.postal.code.common.PostalCodeException;
import jp.sample.postal.code.common.enums.ZipcodeUrl;
import jp.sample.postal.code.domain.model.PostalCodeCsv;
import jp.sample.postal.code.domain.model.PostalCodeModel;
import jp.sample.postal.code.domain.service.PostalCodeDbService;
import jp.sample.postal.code.domain.service.PostalCodeService;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@SpringBootTest
@Log4j2
class CallProcessTest2 {

    @Autowired
    private CallProcess callProcess;
    @Autowired
    private PostalCodeDbService postalCodeDbService;
    @Autowired
    private PostalCodeService postalCodeService;

    /**
     * {@link jp.sample.postal.code.api.process.CallProcess#searchByPrefectureName(java.lang.String)} のためのテスト・メソッド。
     */
    @Test
    void testSearchByPrefectureName_0() {
        PrefectureDataModel test1 = callProcess.searchByPrefectureName("沖縄");
        assertEquals("沖縄", test1.getPrefectureName());
        assertEquals(0, test1.getCityTownVillageList().size());
    }

    @Test
    void testSearchByPrefectureName_北海道() {
        int count = 0;
        PrefectureDataModel test1 = callProcess.searchByPrefectureName("北海道");
        assertEquals("北海道", test1.getPrefectureName());
        assertEquals(count, test1.getCityTownVillageList().size());
        try {
            List<PostalCodeCsv> csvList = postalCodeService.getPostalCodeList(ZipcodeUrl.HOKKAI.zipFileName());
            List<PostalCodeModel> list = csvList.stream().map(e -> PostalCodeModel.convert(e)).toList();
            postalCodeDbService.createList(list);
            count = list.size();
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        PrefectureDataModel test2 = callProcess.searchByPrefectureName("北海道");
        assertEquals("北海道", test2.getPrefectureName());
        assertTrue(0 < test2.getCityTownVillageList().size());
        assertTrue(test2.getCityTownVillageList().stream().map(e -> e.getCityTownVillage()).toList().contains("札幌市中央区"));
    }

    /**
     * {@link jp.sample.postal.code.api.process.CallProcess#searchByCityTownVillage(java.lang.String)} のためのテスト・メソッド。
     */
    @Test
    void testSearchByCityTownVillage_横浜市西区() {
        int count = 0;
        CityTownVillageDataModel test1 = callProcess.searchByCityTownVillage("横浜市西区");
        assertNull(test1.getPrefectureName());
        assertEquals("横浜市西区", test1.getCityTownVillage());
        assertNull(test1.getLocalGovernmentCode());
        assertEquals(count, test1.getStreetNameList().size());

        try {
            List<PostalCodeCsv> csvList = postalCodeService.getPostalCodeList(ZipcodeUrl.KANAGA.zipFileName());
            List<PostalCodeModel> list = csvList.stream().map(e -> PostalCodeModel.convert(e)).toList();
            postalCodeDbService.createList(list);
            count = list.size();
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        CityTownVillageDataModel test2 = callProcess.searchByCityTownVillage("横浜市西区");
        assertEquals("神奈川県", test2.getPrefectureName());
        assertEquals("横浜市西区", test2.getCityTownVillage());
        assertEquals("14103", test2.getLocalGovernmentCode());
        assertTrue(0 < test2.getStreetNameList().size());
        assertTrue(test2.getStreetNameList().stream().map(e -> e.getStreetName()).toList()
                .contains("みなとみらいランドマークタワー（地階・階層不明）"));
    }
}
