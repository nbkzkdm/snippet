/**
 * 
 */
package jp.sample.postal.code.common.enums;

/**
 * @author nbkzk
 *
 */
public enum ExtensionType {

    ZIP("zip"),
    CSV("csv"),
    ;

    private final String extension;
    private ExtensionType(String extension) {
        this.extension = extension;
    }
    public String getExtension() {
        return extension;
    }

}
