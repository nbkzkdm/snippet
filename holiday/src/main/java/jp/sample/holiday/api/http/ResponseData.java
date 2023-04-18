/**
 * 
 */
package jp.sample.holiday.api.http;

import lombok.Data;

/**
 * @author nbkzk
 * @param <T>
 *
 */
@Data
public class ResponseData<T> {

    /** メッセージ */
    private String message;
    /**  */
    T data;

}
