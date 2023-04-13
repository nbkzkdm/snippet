/**
 * 
 */
package jp.sample.postal.code.domain.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.sample.postal.code.common.PostalCodeException;
import jp.sample.postal.code.domain.model.PostalCodeCsv;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@SpringBootTest
@Log4j2
class PostalCodeServiceCallApiTest {

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
    private PostalCodeService postalCodeService;

    @Test
    void testGetPostalCodeAllList() {
        try {
            List<PostalCodeCsv> list = postalCodeService.getPostalCodeAllList();
            assertNotNull(list);
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    void testGetPostalCodeAddList() {
        try {
            List<PostalCodeCsv> addList = postalCodeService.getPostalCodeAddList();
            assertNotNull(addList);
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    void testGetPostalCodeDelList() {
        try {
            List<PostalCodeCsv> delList = postalCodeService.getPostalCodeDelList();
            assertNotNull(delList);
        }
        catch (PostalCodeException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}
