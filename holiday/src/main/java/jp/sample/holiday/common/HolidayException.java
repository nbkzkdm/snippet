/**
 * 
 */
package jp.sample.holiday.common;

import jp.sample.holiday.common.enums.ErrorType;

/**
 * @author nbkzk
 *
 */
@SuppressWarnings("serial")
public class HolidayException extends Exception {

    private ErrorType errorType;

    public ErrorType getErrorType() {
        return errorType;
    }

    public HolidayException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public HolidayException(ErrorType errorType, Throwable e) {
        super(errorType.getError(), e);
        this.errorType = errorType;
    }

    public HolidayException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public HolidayException(ErrorType errorType, String message, Throwable e) {
        super(message, e);
        this.errorType = errorType;
    }

    public HolidayException() {
    }

    public HolidayException(String message) {
        super(message);
    }

    public HolidayException(Throwable cause) {
        super(cause);
    }

    public HolidayException(String message, Throwable cause) {
        super(message, cause);
    }

    public HolidayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
