/**
 * 
 */
package jp.sample.postal.code.common.enums;

import java.util.Objects;

/**
 * 
 * <ul>
 * <li>all</li>
 * <li>add</li>
 * <li>del</li>
 * </ul>
 * 
 * @author nbkzk
 *
 */
public enum InputType {

    全国("all"),
    追加("add"),
    廃止("del"),
    ;

    private final String summary;
    private InputType(String summary) {
        this.summary = summary;
    }
    public String getSummary() {
        return summary;
    }

    public static InputType of(String summary) {
        Objects.requireNonNull(summary, "summary must not be null");
        InputType[] array = values();
        for (InputType item : array) {
            if (item.getSummary().toLowerCase().equals(summary.toLowerCase())) {
                return item;
            }
        }
        return null;
    }
}
