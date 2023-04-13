/**
 * 
 */
package jp.sample.postal.code.common.enums;

/**
 * 更新の表示（注6）
 * <ul>
 * <li>「0」は変更なし</li>
 * <li>「1」は変更あり</li>
 * <li>「2」廃止（廃止データのみ使用）</li>
 * </ul>
 * <pre>※6 「変更あり」とは追加および修正により更新されたデータを示すものです。</pre>
 * @author nbkzk
 *
 */
public enum UpdateIndicationType {

    変更なし(0, "変更なし"),
    変更あり(1, "変更あり"),
    廃止(2, "廃止（廃止データのみ使用）"),
    ;
    private final int value;
    private final String summary;
    private UpdateIndicationType(int value, String summary) {
        this.value = value;
        this.summary = summary;
    }
    public int getValue() {
        return value;
    }
    public String getSummary() {
        return summary;
    }

}
