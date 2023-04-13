/**
 * 
 */
package jp.sample.postal.code.common;

import jp.sample.postal.code.common.enums.ErrorType;

/**
 * @author nbkzk
 *
 */
@SuppressWarnings("serial")
public class PostalCodeException extends Exception {

    private ErrorType errorType;

    public ErrorType getErrorType() {
        return errorType;
    }

    public PostalCodeException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public PostalCodeException(ErrorType errorType, Throwable e) {
        super(errorType.getError(), e);
        this.errorType = errorType;
    }

    public PostalCodeException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public PostalCodeException(ErrorType errorType, String message, Throwable e) {
        super(message, e);
        this.errorType = errorType;
    }

    public PostalCodeException() {
    }

    public PostalCodeException(String message) {
        super(message);
    }

    public PostalCodeException(Throwable cause) {
        super(cause);
    }

    public PostalCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostalCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
