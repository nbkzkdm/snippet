/**
 * 
 */
package jp.sample.holiday.domain.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nbkzk
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "date", "note" })
public class HolidayCsv {

    @JsonProperty("date" )
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date date;
    @JsonProperty("note")
    private String note;

}
