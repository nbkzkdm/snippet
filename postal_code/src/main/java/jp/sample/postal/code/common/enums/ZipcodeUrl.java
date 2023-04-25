/**
 * 
 */
package jp.sample.postal.code.common.enums;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @author nbkzk
 *
 */
public enum ZipcodeUrl {

    KEN_ALL(0, "KEN_ALL","全データ", true),
    HOKKAI(1, "HOKKAI","北海道"),
    AOMORI(2, "AOMORI","青森県"),
    IWATE(3, "IWATE","岩手県"),
    MIYAGI(4, "MIYAGI","宮城県"),
    AKITA(5, "AKITA","秋田県"),
    YAMAGATA(6, "YAMAGA","山形県"),
    FUKUSHIMA(7, "FUKUSH","福島県"),
    IBARAKI(8, "IBARAK","茨城県"),
    TOCHIGI(9, "TOCHIG","栃木県"),
    GUMMA(10, "GUMMA","群馬県"),
    SAITAMA(11, "SAITAM","埼玉県"),
    CHIBA(12, "CHIBA","千葉県"),
    TOKYO(13, "TOKYO","東京都"),
    KANAGAWA(14, "KANAGA","神奈川県"),
    NIIGATA(15, "NIIGAT","新潟県"),
    TOYAMA(16, "TOYAMA","富山県"),
    ISHIKAWA(17, "ISHIKA","石川県"),
    FUKUI(18, "FUKUI","福井県"),
    YAMANASHI(19, "YAMANA","山梨県"),
    NAGANO(20, "NAGANO","長野県"),
    GIFU(21, "GIFU","岐阜県"),
    SHIZUOKA(22, "SHIZUO","静岡県"),
    AICHI(23, "AICHI","愛知県"),
    MIE(24, "MIE","三重県"),
    SHIGA(25, "SHIGA","滋賀県"),
    KYOUTO(26, "KYOUTO","京都府"),
    OSAKA(27, "OSAKA","大阪府"),
    HYOGO(28, "HYOGO","兵庫県"),
    NARA(29, "NARA","奈良県"),
    WAKAYAMA(30, "WAKAYA","和歌山県"),
    TOTTORI(31, "TOTTOR","鳥取県"),
    SHIMANE(32, "SHIMAN","島根県"),
    OKAYAMA(33, "OKAYAM","岡山県"),
    HIROSHIMA(34, "HIROSH","広島県"),
    YAMAGUCHI(35, "YAMAGU","山口県"),
    TOKUSHIMA(36, "TOKUSH","徳島県"),
    KAGAWA(37, "KAGAWA","香川県"),
    EHIME(38, "EHIME","愛媛県"),
    KOCHI(39, "KOCHI","高知県"),
    FUKUOKA(40, "FUKUOK","福岡県"),
    SAGA(41, "SAGA","佐賀県"),
    NAGASAKI(42, "NAGASA","長崎県"),
    KUMAMOTO(43, "KUMAMO","熊本県"),
    OITA(44, "OITA","大分県"),
    MIYAZAKI(45, "MIYAZA","宮崎県"),
    KAGOSHIMA(46, "KAGOSH","鹿児島県"),
    OKINAWA(47, "OKINAW","沖縄県"),
    ADD_YYMM(98, "ADD_YYMM","新規追加データ", true),
    DEL_YYMM(99, "DEL_YYMM","廃止データ", true),
    ;
    private final int value;
    private final String name;
    private final String summary;
    private final boolean nonFlag;
    private ZipcodeUrl(int value, String name, String summary) {
        this(value, name, summary, false);
    }
    private ZipcodeUrl(int value, String name, String summary, boolean nonFlag) {
        this.value = value;
        this.name = name;
        this.summary = summary;
        this.nonFlag = nonFlag;
    }
    public int getValue() {
        return value;
    }
    public String getName() {
        return name;
    }
    public String getSummary() {
        return summary;
    }
    public boolean isNonFlag() {
        return nonFlag;
    }

    /**
     * https://www.post.japanpost.jp/zipcode/dl
     * <ol>
     * <li>/kogaki/zip/ken_all.zip</li>
     * <li>/kogaki/zip/add_2303.zip</li>
     * <li>/kogaki/zip/del_2303.zip</li>
     * <li>/kogaki/zip/01hokkai.zip</li>
     * </ol>
     * @return
     */
    public String zipFileName() {
        LocalDate now = LocalDate.now().minusMonths(1);
        DateTimeFormatter yyMM = DateTimeFormatter.ofPattern("yyMM");
        return zipFileName(now.format(yyMM));
    }

    public String zipFileName(String yyMM) {
        String prefix = "";
        if (!isNonFlag()) {
            prefix = String.format("%02d", getValue());
        }
        return String.format("%s%s.zip", prefix, getName().replace("YYMM", yyMM)).toLowerCase();
    }

    public static List<String> getPrefectureList() {
        return Arrays.stream(values()).filter(e -> !e.isNonFlag()).map(e -> e.getSummary()).toList();
    }

}
