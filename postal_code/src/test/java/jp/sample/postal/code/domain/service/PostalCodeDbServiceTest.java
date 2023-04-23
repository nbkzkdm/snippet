/**
 * 
 */
package jp.sample.postal.code.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.sample.postal.code.common.PostalCodeException;
import jp.sample.postal.code.domain.model.PostalCodeCsv;
import jp.sample.postal.code.domain.model.PostalCodeModel;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@SpringBootTest
@Log4j2
class PostalCodeDbServiceTest {

    @Autowired
    private PostalCodeDbService postalCodeDbService;
    @Autowired
    private PostalCodeService postalCodeService;

    /**
     * {@link jp.sample.postal.code.domain.service.PostalCodeDbService#mergeList(java.util.List)} のためのテスト・メソッド。
     */
    @Test
    void testMergeList() {
        try {
            long start = System.currentTimeMillis();
            List<PostalCodeCsv> csvList = postalCodeService.getPostalCodeAllList();
            long process1 = System.currentTimeMillis();
            List<PostalCodeModel> list = csvList.stream().map(e -> PostalCodeModel.convert(e)).toList();
            List<PostalCodeModel> test1 = list.stream().filter(e -> (
                    e.getLocalGovernmentCode().equals("01224")
                    && e.getOldPostalCode().equals("066")
                    && e.getPostalCode().equals("0660005")
                    )).toList();
            assertEquals(3, test1.size());
            String expectedHwK = "";
            String expected = "";
            for (PostalCodeModel item : test1) {
                expectedHwK = expectedHwK.concat(item.getStreetNameHwK());
                expected = expected.concat(item.getStreetName());
                log.debug(String.format("merge pre  : %s >> %s", item.getStreetNameHwK(), item.getStreetName()));
            }
            long process2 = System.currentTimeMillis();
            List<PostalCodeModel> test = postalCodeDbService.mergeList(list);
            long end = System.currentTimeMillis();
            log.debug(String.format("csvList = %d", csvList.size()));
            log.debug(String.format("test = %d", test.size()));
            assertTrue(test.size() < csvList.size());
            List<PostalCodeModel> test2 = test.stream().filter(e -> (
                    e.getLocalGovernmentCode().equals("01224")
                    && e.getOldPostalCode().equals("066")
                    && e.getPostalCode().equals("0660005")
                    )).toList();
            assertEquals(1, test2.size());
            for (PostalCodeModel item : test2) {
                assertEquals(expectedHwK, item.getStreetNameHwK());
                assertEquals(expected, item.getStreetName());
                log.debug(String.format("merge post : %s >> %s", item.getStreetNameHwK(), item.getStreetName()));
            }
            log.debug(String.format("getPostalCodeAllList = %d - %d (%d)", start, process1, process1 - start));
            log.debug(String.format("convert = %d - %d (%d)", process1, process2, process2 - process1));
            log.debug(String.format("mergeList = %d - %d (%d)", process2, end, end - process2));
            log.debug(String.format("total = %d - %d (%d)", start, end, end - start));
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}
