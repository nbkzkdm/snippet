/**
 * 
 */
package jp.sample.postal.code.common.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author nbkzk
 *
 */
class ZipcodeUrlTest {

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

    /**
     * {@link jp.sample.postal.code.common.enums.ZipcodeUrl#zipFileName()} のためのテスト・メソッド。
     */
    @Test
    void testZipFileName() {
        assertEquals("ken_all.zip", ZipcodeUrl.KEN_ALL.zipFileName(), "全国一括");
        assertEquals("01hokkai.zip", ZipcodeUrl.HOKKAI.zipFileName(), "北海道");
        // TODO 2023/04中なら、2303ですが、実施日のひとつ前の月表示になります。
        assertEquals("del_2303.zip", ZipcodeUrl.DEL_YYMM.zipFileName(), "廃止");
    }

}
