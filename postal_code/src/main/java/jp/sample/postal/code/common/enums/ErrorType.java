/**
 * 
 */
package jp.sample.postal.code.common.enums;

/**
 * @author nbkzk
 *
 */
public enum ErrorType {

    UNSUPPORTED_ENCODING("サポートされていないエンコードです"),
    ZIP_READING("ZIPファイル読み込み中のエラー"),
    CSV_READING("CSVファイル読み込み中のエラー"),
    COULD_NOT_BE_CREATED("ファイルを作成できなかった時のエラー"),
    FILE_READING("ファイルを読み込み中のエラー"),
    FILE_WRITING("ファイルを書き込み中のエラー"),
    INTROSPECTION("イントロスペクション中に例外が発生した場合にスローされます"),
    INVOKE("invoke中のエラーです"),
    ;

    private final String error;

    private ErrorType(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
