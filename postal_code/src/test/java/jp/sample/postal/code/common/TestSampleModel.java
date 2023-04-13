/**
 * 
 */
package jp.sample.postal.code.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author nbkzk
 *
 */
@Data
@Builder
public class TestSampleModel {

    private String value;
    private int intValue;
    private long longValue;
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;
    private boolean booleanValue;
    private String[] stringArray;
    private int[] intArray;
    private List<String> stringList;
    private List<Integer> intList;

}
