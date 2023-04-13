/**
 * 
 */
package jp.sample.postal.code.common.enums;

/**
 * <ul>
 * <li>一町域が二以上の郵便番号で表される場合の表示　（注3）　（「1」は該当、「0」は該当せず）</li>
 * <li>小字毎に番地が起番されている町域の表示　（注4）　（「1」は該当、「0」は該当せず）</li>
 * <li>丁目を有する町域の場合の表示　（「1」は該当、「0」は該当せず）</li>
 * <li>一つの郵便番号で二以上の町域を表す場合の表示　（注5）　（「1」は該当、「0」は該当せず）</li>u
 * </ul>
 * <ul>
 * <li>「0」は該当せず</li>
 * <li>「1」は該当</li>
 * </ul>
 * @author nbkzk
 *
 */
public enum MatchType {

    該当せず(0, "該当せず"),
    該当(1, "該当"),
    ;
    private final int value;
    private final String summary;
    private MatchType(int value, String summary) {
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
