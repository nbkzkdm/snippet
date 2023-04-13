/**
 * 
 */
package jp.sample.postal.code.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jp.sample.postal.code.common.enums.MatchType;
import jp.sample.postal.code.common.enums.ReasonForChangeType;
import jp.sample.postal.code.common.enums.UpdateIndicationType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://www.post.japanpost.jp/zipcode/dl/readme.html">郵便番号データの説明</a>
 * 
 * <h3>郵便番号の設定範囲</h3>
 * <p>郵便番号は、町域（町名から○丁目を除く部分、および大字）に設定しており、小字または通称には原則として設定しておりません。
 * ただし、当該小字または通称が実質的に大字または公称町名として扱われ、郵便物への記載が多い等必要な場合は、郵便番号を設定することがあります。
 * そのため、郵便番号を付定している町域名が公称町名であるという保証をするものではありません。</p>
 * <p>また、大口事業所、私書箱および料金受取人払等へ個別の郵便番号を設定しています。</p>
 * 
 * <h3>このデータファイルに入れている郵便番号</h3>
 * <p>このデータファイルには、町域に設定した郵便番号のみを入れており、大口事業所へ個別に設定する郵便番号は入れておりません。</p>
 * <p>なお、大口事業所の個別番号のデータも当ホームページにおいて、検索、ダウンロードする事ができます。</p>
 * 
 * <h3>郵便番号の変更</h3>
 * <p>町域名の変更などがあった場合には、郵便番号が変更される可能性があります。</p>
 * 
 * <h3>郵便番号データファイルの形式等</h3>
 * <p>全角となっている町域部分の文字数が38文字を越える場合、また半角となっているフリガナ部分の文字数が76文字を越える場合は、複数レコードに分割しています。</p>
 * <p>この郵便番号データファイルでは、以下の順に配列しています。</p>
 * <ol>
 * <li>全国地方公共団体コード（JIS X0401、X0402）………　半角数字</li>
 * <li>（旧）郵便番号（5桁）………………………………………　半角数字</li>
 * <li>郵便番号（7桁）………………………………………　半角数字</li>
 * <li>都道府県名　…………　半角カタカナ（コード順に掲載）　（注1）</li>
 * <li>市区町村名　…………　半角カタカナ（コード順に掲載）　（注1）</li>
 * <li>町域名　………………　半角カタカナ（五十音順に掲載）　（注1）</li>
 * <li>都道府県名　…………　漢字（コード順に掲載）　（注1,2）</li>
 * <li>市区町村名　…………　漢字（コード順に掲載）　（注1,2）</li>
 * <li>町域名　………………　漢字（五十音順に掲載）　（注1,2）</li>
 * <li>一町域が二以上の郵便番号で表される場合の表示　（注3）　（「1」は該当、「0」は該当せず）</li>
 * <li>小字毎に番地が起番されている町域の表示　（注4）　（「1」は該当、「0」は該当せず）</li>
 * <li>丁目を有する町域の場合の表示　（「1」は該当、「0」は該当せず）</li>
 * <li>一つの郵便番号で二以上の町域を表す場合の表示　（注5）　（「1」は該当、「0」は該当せず）</li>
 * <li>更新の表示（注6）（「0」は変更なし、「1」は変更あり、「2」廃止（廃止データのみ使用））</li>
 * <li>変更理由　（「0」は変更なし、「1」市政・区政・町政・分区・政令指定都市施行、「2」住居表示の実施、「3」区画整理、「4」郵便区調整等、「5」訂正、「6」廃止（廃止データのみ使用））</li>
 * </ol>
 * <pre>
 * ※1 文字コードには、MS漢字コード（SHIFT JIS）を使用しています。
 * ※2 文字セットとして、JIS X0208-1983を使用し、規定されていない文字はひらがなで表記しています。
 * ※3 「一町域が二以上の郵便番号で表される場合の表示」とは、町域のみでは郵便番号が特定できず、丁目、番地、小字などにより番号が異なる町域のことです。
 * ※4 「小字毎に番地が起番されている町域の表示」とは、郵便番号を設定した町域（大字）が複数の小字を有しており、各小字毎に番地が起番されているため、町域（郵便番号）と番地だけでは住所が特定できない町域のことです。
 * </pre>
 * <table border="1">
 * <tr><th><小字に同一番地が存在する住所></th><td rowspan="4">○○市△△町が郵便番号の表す範囲であり、町域（郵便番号）と番地だけでは住所が特定できません。</td></tr>
 * <tr><th>○○市△△町字A100番地</th></tr>
 * <tr><th>○○市△△町字B100番地</th></tr>
 * <tr><th>○○市△△町字C100番地</th></tr>
 * </table>
 * <pre>
 * ※5 「一つの郵便番号で二以上の町域を表す場合の表示」とは、一つの郵便番号で複数の町域をまとめて表しており、郵便番号と番地だけでは住所が特定できないことを示すものです。
 * ※6 「変更あり」とは追加および修正により更新されたデータを示すものです。
 * ※7 全角となっている町域名の文字数が38文字を超える場合、また、半角カタカナとなっている町域名のフリガナが76文字を越える場合には、複数レコードに分割しています。
 * </pre>
 * 
 * <h3>半角カナ → Half-width kana → HwK</h3>としてます。
 * 
 * @see MatchType
 * @see UpdateIndicationType
 * @see ReasonForChangeType
 * @author nbkzk
 */
@Data
@NoArgsConstructor
@JsonPropertyOrder({
    "localGovernmentCode", "oldPostalCode", "postalCode",
    "prefectureNameHwK", "cityTownVillageHwK", "streetNameHwK",
    "prefectureName", "cityTownVillage", "streetName",
    "combinedZipCodeType", "detailedStreetAddressingType", "avenueAndStreetNumberingType",
    "multiAreaZipCodeType", "updateIndicationType", "reasonForChangeType" })
public class PostalCodeCsv {

    /** 1. 全国地方公共団体コード（JIS X0401、X0402）………　半角数字 */
    @JsonProperty("localGovernmentCode")
    private String localGovernmentCode;
    /** 2. （旧）郵便番号（5桁）………………………………………　半角数字 */
    @JsonProperty("oldPostalCode")
    private String oldPostalCode;
    /** 3. 郵便番号（7桁）………………………………………　半角数字 */
    @JsonProperty("postalCode")
    private String postalCode;
    /** 4. 都道府県名　…………　半角カタカナ（コード順に掲載）　（注1） */
    @JsonProperty("prefectureNameHwK")
    private String prefectureNameHwK;
    /** 5. 市区町村名　…………　半角カタカナ（コード順に掲載）　（注1） */
    @JsonProperty("cityTownVillageHwK")
    private String cityTownVillageHwK;
    /** 6. 町域名　………………　半角カタカナ（五十音順に掲載）　（注1） */
    @JsonProperty("streetNameHwK")
    private String streetNameHwK;
    /** 7. 都道府県名　…………　漢字（コード順に掲載）　（注1,2） */
    @JsonProperty("prefectureName")
    private String prefectureName;
    /** 8. 市区町村名　…………　漢字（コード順に掲載）　（注1,2） */
    @JsonProperty("cityTownVillage")
    private String cityTownVillage;
    /** 9. 町域名　………………　漢字（五十音順に掲載）　（注1,2） */
    @JsonProperty("streetName")
    private String streetName;
    /** 10. 一町域が二以上の郵便番号で表される場合の表示　（注3）　（「1」は該当、「0」は該当せず） */
    @JsonProperty("combinedZipCodeType")
    private String combinedZipCodeType;
    /** 11. 小字毎に番地が起番されている町域の表示　（注4）　（「1」は該当、「0」は該当せず） */
    @JsonProperty("detailedStreetAddressingType")
    private String detailedStreetAddressingType;
    /** 12. 丁目を有する町域の場合の表示　（「1」は該当、「0」は該当せず） */
    @JsonProperty("avenueAndStreetNumberingType")
    private String avenueAndStreetNumberingType;
    /** 13. 一つの郵便番号で二以上の町域を表す場合の表示　（注5）　（「1」は該当、「0」は該当せず） */
    @JsonProperty("multiAreaZipCodeType")
    private String multiAreaZipCodeType;
    /** 14. 更新の表示（注6）（「0」は変更なし、「1」は変更あり、「2」廃止（廃止データのみ使用）） */
    @JsonProperty("updateIndicationType")
    private String updateIndicationType;
    /** 15. 変更理由　（「0」は変更なし、「1」市政・区政・町政・分区・政令指定都市施行、「2」住居表示の実施、「3」区画整理、「4」郵便区調整等、「5」訂正、「6」廃止（廃止データのみ使用）） */
    @JsonProperty("reasonForChangeType")
    private String reasonForChangeType;

}
