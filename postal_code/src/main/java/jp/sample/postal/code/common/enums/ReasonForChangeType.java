/**
 * 
 */
package jp.sample.postal.code.common.enums;

/**
 * 変更理由
 * <ul>
 * <li>「0」は変更なし</li>
 * <li>「1」市政・区政・町政・分区・政令指定都市施行</li>
 * <li>「2」住居表示の実施</li>
 * <li>「3」区画整理</li>
 * <li>「4」郵便区調整等</li>
 * <li>「5」訂正</li>
 * <li>「6」廃止（廃止データのみ使用）</li>
 * </ul>
 * @author nbkzk
 *
 */
public enum ReasonForChangeType {

    変更なし(0, "変更なし"),
    政施行(1, "市政・区政・町政・分区・政令指定都市施行"),
    住居表示(2, "住居表示の実施"),
    区画整理(3, "区画整理"),
    調整等(4, "郵便区調整等"),
    訂正(5, "訂正"),
    廃止(6, "廃止（廃止データのみ使用）"),
    ;
    private final int value;
    private final String summary;
    private ReasonForChangeType(int value, String summary) {
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
