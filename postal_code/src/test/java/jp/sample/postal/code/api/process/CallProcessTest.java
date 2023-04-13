/**
 * 
 */
package jp.sample.postal.code.api.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.sample.postal.code.common.PostalCodeException;
import jp.sample.postal.code.domain.model.PostalCodeModel;
import jp.sample.postal.code.domain.repository.PostalCodeModelRepository;
import jp.sample.postal.code.domain.service.PostalCodeDbService;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@SpringBootTest
@Log4j2
class CallProcessTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @AfterAll
    static void tearDownAfterClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {}

    @Autowired
    private CallProcess callProcess;
    @Autowired
    private PostalCodeDbService postalCodeDbService;
    @Autowired
    private PostalCodeModelRepository postalCodeModelRepository;

    @Test
    void testUpdate() {
        List<PostalCodeModel> list0 = callProcess.search("610");
        assertEquals(0, list0.size());
        try {
            int res1 = callProcess.update();
            assertTrue(120000 < res1);
            Thread.sleep(3000);

            int res2 = callProcess.update();
            assertEquals(0, res2);
            List<PostalCodeModel> list1 = postalCodeDbService.findAll();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneMonthAgo = LocalDateTime.of(now.getYear(), now.getMonthValue(), 1,
                    now.getHour(), now.getMinute(), now.getSecond()).minusMonths(1);
            // .map(e -> e.setCreateAt(oneMonthAgo))
            List<PostalCodeModel> updateList = list1.stream().map(e -> e.createAtSetter(oneMonthAgo)).toList();
            postalCodeDbService.createList(updateList);
            Thread.sleep(3000);

            int res3 = callProcess.update();
            log.debug(String.format("update1: %06d", res1));
            log.debug(String.format("update2: %06d", res2));
            log.debug(String.format("update3: %06d", res3));
            List<PostalCodeModel> list2 = this.postalCodeModelRepository.maxCreateAt();
            int sum = 0;
            for (PostalCodeModel item : list2) {
                List<PostalCodeModel> data = postalCodeModelRepository.findByPostalCode(item.getPostalCode());
                int counta = 1;
                for (PostalCodeModel d : data) {
                    log.debug(String.format("data[%d] = %s", counta++, d.toString()));
                    sum++;
                }
            }
            assertEquals(list2.size(), sum);
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        List<PostalCodeModel> list9 = callProcess.search("610");
        for (PostalCodeModel item : list9) {
            log.debug(String.format("id: %s[%s %s %s]",
                    item.getPostalCode(), item.getPrefectureName(), item.getCityTownVillage(), item.getStreetName()));
        }
        assertEquals(93, list9.size());
    }

}
