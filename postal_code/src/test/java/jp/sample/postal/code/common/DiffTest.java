/**
 * 
 */
package jp.sample.postal.code.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@SpringBootTest
@Log4j2
class DiffTest {

    @Nested
    class Diff_シンプルテスト {
        @Test
        void testDiff_all_null() {
            assertThrows(NullPointerException.class, () -> Diff.diff(null, null, null));
        }

        @Test
        void testDiff_String_null_null() {
            try {
                assertTrue(Diff.diff(String.class, null, null), "testDiff_String_null_null");
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_Object_null_null() {
            try {
                assertTrue(Diff.diff(Object.class, null, null), "testDiff_Object_null_null");
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }
    }

    @Nested
    @SuppressWarnings("rawtypes")
    class Diff_theoryテスト {

        public static Fixture[] stringProvider() {
            return new Fixture[] {
                    new Fixture<String>(null, null, true),
                    new Fixture<String>(null, "", false),
                    new Fixture<String>("", null, false),
                    new Fixture<String>("", "", true),
                    new Fixture<String>("1", "a", false),
                    new Fixture<String>("aa", "aa", true),
                    new Fixture<String>("aab", "aac", false),
            };
        }

        @ParameterizedTest
        @MethodSource("stringProvider")
        void testDiff_String(Fixture<String> f) {
            try {
                assertEquals(f.expectedFlag, Diff.diff(String.class, f.param1, f.param2));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        public static Fixture[] objectProvider() {
            Object obj = new Object();
            return new Fixture[] {
                    new Fixture<Object>(null, null, true),
                    new Fixture<Object>(null, new Object(), false),
                    new Fixture<Object>(new Object(), null, false),
                    new Fixture<Object>(new Object(), new Object(), false),
                    new Fixture<Object>(obj, obj, true),
            };
        }

        @ParameterizedTest
        @MethodSource("objectProvider")
        void testDiff_Object(Fixture<Object> f) {
            try {
                assertEquals(f.expectedFlag, Diff.diff(Object.class, f.param1, f.param2));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        public static Fixture[] integerProvider() {
            Integer obj = Integer.valueOf(111);
            return new Fixture[] {
                    new Fixture<Integer>(null, null, true),
                    new Fixture<Integer>(null, obj, false),
                    new Fixture<Integer>(obj, null, false),
                    new Fixture<Integer>(Integer.valueOf(3), Integer.valueOf(4), false),
                    new Fixture<Integer>(obj, obj, true),
            };
        }

        @ParameterizedTest
        @MethodSource("integerProvider")
        void testDiff_Integer(Fixture<Integer> f) {
            try {
                assertEquals(f.expectedFlag, Diff.diff(Integer.class, f.param1, f.param2));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        public static Fixture[] listProvider() {
            List obj1 = List.of("123", "abc", "あいう", "ABC", "ＡＢＣ");
            List obj2 = List.of("123", "abc", "あいう", "ABC", "ＡＢＣ");
            List obj3 = List.of("123", "abc", "あいう");
            return new Fixture[] {
                    new Fixture<List>(null, null, true),
                    new Fixture<List>(null, obj1, false),
                    new Fixture<List>(obj1, null, false),
                    new Fixture<List>(obj1, obj2, true),
                    new Fixture<List>(obj1, obj1, true),
                    new Fixture<List>(obj1, obj3, false),
            };
        }

        @ParameterizedTest
        @MethodSource("listProvider")
        void testDiff_List(Fixture<List> f) {
            try {
                assertEquals(f.expectedFlag, Diff.diff(List.class, f.param1, f.param2));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        public static Fixture[] arrayProvider() {
            String[] obj1 = {
                    "123", "abc", "あいう", "ABC", "ＡＢＣ"
            };
            String[] obj2 = {
                    "123", "abc", "あいう", "ABC", "ＡＢＣ"
            };
            String[] obj3 = {
                    "123", "abc", "あいう"
            };
            return new Fixture[] {
                    new Fixture<String[]>(null, null, true), new Fixture<String[]>(null, obj1, false),
                    new Fixture<String[]>(obj1, null, false), new Fixture<String[]>(obj1, obj2, true),
                    new Fixture<String[]>(obj1, obj1, true), new Fixture<String[]>(obj1, obj3, false),
                    new Fixture<String[]>(obj3, obj1, false),
            };
        }

        @ParameterizedTest
        @MethodSource("arrayProvider")
        void testDiff_Array(Fixture<String[]> f) {
            try {
                assertEquals(f.expectedFlag, Diff.diff(String[].class, f.param1, f.param2),
                        String.format("%s : %s", Arrays.toString(f.param1), Arrays.toString(f.param2)));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

    }

    @Nested
    class Diff_objectテスト {
        @Test
        void testDiff_TestSampleModel_null_null() {
            try {
                assertTrue(Diff.diff(TestSampleModel.class, null, null), "testDiff_TestSampleModel_null_null");
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_new_null() {
            try {
                assertFalse(Diff.diff(TestSampleModel.class, TestSampleModel.builder().build(), null),
                        "testDiff_TestSampleModel_new_null");
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_null_new() {
            try {
                assertFalse(Diff.diff(TestSampleModel.class, null, TestSampleModel.builder().build()),
                        "testDiff_TestSampleModel_null_new");
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_new_new() {
            try {
                assertTrue(Diff.diff(TestSampleModel.class,
                        TestSampleModel.builder().build(),
                        TestSampleModel.builder().build()),
                        "testDiff_TestSampleModel_new_new");
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_item_item() {
            try {
                assertFalse(Diff.diff(TestSampleModel.class,
                        expectedItem(),
                        TestSampleModel.builder().build()),
                        "testDiff_TestSampleModel_item_item");
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_booleanValue() {
            try {
                TestSampleModel test = expectedItem();
                test.setBooleanValue(false);
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_intArray() {
            try {
                TestSampleModel test = expectedItem();
                test.setIntArray(new int[] {1,2,3,5,6});
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_intList() {
            try {
                TestSampleModel test = expectedItem();
                test.setIntList(List.of(1,2,3,4,5,6,7,8,9,0,11));
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_intValue() {
            try {
                TestSampleModel test = expectedItem();
                test.setIntValue(32198);
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_localDate() {
            try {
                TestSampleModel test = expectedItem();
                test.setLocalDate(LocalDate.of(2023, 1, 1));
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_localDateTime() {
            try {
                TestSampleModel test = expectedItem();
                test.setLocalDateTime(LocalDateTime.of(2024, 1, 3, 9, 24, 41));
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_localTime() {
            try {
                TestSampleModel test = expectedItem();
                test.setLocalTime(LocalTime.of(4, 43));
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_longValue() {
            try {
                TestSampleModel test = expectedItem();
                test.setLongValue(654321);
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_stringArray() {
            try {
                TestSampleModel test = expectedItem();
                test.setStringArray(new String[] {"123", "あいう", "ＡＢＣ"});
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_stringList() {
            try {
                TestSampleModel test = expectedItem();
                test.setStringList(List.of("123", "abc", "あいう", "0123456789", "abcdefghijklmnopqrstuvwzxy"));
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_value() {
            try {
                TestSampleModel test = expectedItem();
                test.setValue("sample test 文字列");
                assertFalse(Diff.diff(TestSampleModel.class, expectedItem(), test));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        @Test
        void testDiff_TestSampleModel_ignore() {
            try {
                TestSampleModel test = expectedItem();
                test.setValue("sample test 文字列");
                assertTrue(Diff.diff(TestSampleModel.class, expectedItem(), test, List.of("value")));
            }
            catch (PostalCodeException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        public TestSampleModel expectedItem() {
            return TestSampleModel.builder()
                    .booleanValue(true)
                    .intArray(new int[] {1,2,3})
                    .intList(List.of(4,5,6))
                    .intValue(99)
                    .localDate(LocalDate.of(2023, 4, 12))
                    .localDateTime(LocalDateTime.of(2023, 5, 25, 12, 34, 56))
                    .localTime(LocalTime.of(15, 32))
                    .longValue(34l)
                    .stringArray(new String[] {"123", "abc", "あいう", "ABC", "ＡＢＣ"})
                    .stringList(List.of("123", "abc", "あいう"))
                    .value("TestValue")
                    .build();
        }

    }

    static class Fixture<T> {
        public final T param1;
        public final T param2;
        public final boolean expectedFlag;

        public Fixture(T param1, T param2, boolean expectedFlag) {
            this.param1 = param1;
            this.param2 = param2;
            this.expectedFlag = expectedFlag;
        }
    }

}
