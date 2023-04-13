/**
 * 
 */
package jp.sample.postal.code.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jp.sample.postal.code.common.enums.InputType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DB entity
 * @author nbkzk
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostalCodeModel {

    /** postalId */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long postalId;

    /** 1. 全国地方公共団体コード（JIS X0401、X0402）………　半角数字 */
    private String localGovernmentCode;
    /** 2. （旧）郵便番号（5桁）………………………………………　半角数字 */
    private String oldPostalCode;
    /** 3. 郵便番号（7桁）………………………………………　半角数字 */
    private String postalCode;
    /** 4. 都道府県名　…………　半角カタカナ（コード順に掲載）　（注1） */
    private String prefectureNameHwK;
    /** 5. 市区町村名　…………　半角カタカナ（コード順に掲載）　（注1） */
    private String cityTownVillageHwK;
    /** 6. 町域名　………………　半角カタカナ（五十音順に掲載）　（注1） */
    private String streetNameHwK;
    /** 7. 都道府県名　…………　漢字（コード順に掲載）　（注1,2） */
    private String prefectureName;
    /** 8. 市区町村名　…………　漢字（コード順に掲載）　（注1,2） */
    private String cityTownVillage;
    /** 9. 町域名　………………　漢字（五十音順に掲載）　（注1,2） */
    private String streetName;
    /** 10. 一町域が二以上の郵便番号で表される場合の表示　（注3）　（「1」は該当、「0」は該当せず） */
    @JsonIgnore
    private String combinedZipCodeType;
    /** 11. 小字毎に番地が起番されている町域の表示　（注4）　（「1」は該当、「0」は該当せず） */
    @JsonIgnore
    private String detailedStreetAddressingType;
    /** 12. 丁目を有する町域の場合の表示　（「1」は該当、「0」は該当せず） */
    @JsonIgnore
    private String avenueAndStreetNumberingType;
    /** 13. 一つの郵便番号で二以上の町域を表す場合の表示　（注5）　（「1」は該当、「0」は該当せず） */
    @JsonIgnore
    private String multiAreaZipCodeType;
    /** 14. 更新の表示（注6）（「0」は変更なし、「1」は変更あり、「2」廃止（廃止データのみ使用）） */
    @JsonIgnore
    private String updateIndicationType;
    /** 15. 変更理由　（「0」は変更なし、「1」市政・区政・町政・分区・政令指定都市施行、「2」住居表示の実施、「3」区画整理、「4」郵便区調整等、「5」訂正、「6」廃止（廃止データのみ使用）） */
    @JsonIgnore
    private String reasonForChangeType;


    /**
     * 投入タイプ
     * <ul>
     * <li>all</li>
     * <li>add</li>
     * <li>del</li>
     * </ul>
     * @see InputType
     */
    @JsonIgnore
    private String inputType;

    /** 作成日 */
    @Column(nullable=true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    @JsonIgnore
    private LocalDateTime createAt;
    /** 変更日 */
    @JsonIgnore
    private LocalDateTime updateAt;

    public PostalCodeModel createAtSetter(LocalDateTime createA) {
        this.setCreateAt(createA);
        return this;
    }

    public PostalCodeModel inputTypeSetter(InputType inputType) {
        this.setInputType(inputType.getSummary());
        return this;
    }

    /**
     * 登録前の事前データセット
     */
    @PrePersist
    public void onPrePersist() {
        if (Objects.isNull(this.getCreateAt())) {
            this.setCreateAt(LocalDateTime.now());
        }
        if (Objects.isNull(this.getUpdateAt())
                && InputType.追加.equals(InputType.of(getInputType()))) {
            this.setUpdateAt(LocalDateTime.now());
        }
    }

    public static PostalCodeModel convert(PostalCodeCsv item) {
        return convert(item, InputType.全国);
    }

    public static PostalCodeModel convertAdd(PostalCodeCsv item) {
        return convert(item, InputType.追加);
    }

    public static PostalCodeModel convert(PostalCodeCsv item, InputType inputType) {
        PostalCodeModel value = new PostalCodeModel();
        value.setLocalGovernmentCode(item.getLocalGovernmentCode());
        value.setOldPostalCode(item.getOldPostalCode());
        value.setPostalCode(item.getPostalCode());
        value.setPrefectureNameHwK(item.getPrefectureNameHwK());
        value.setCityTownVillageHwK(item.getCityTownVillageHwK());
        value.setStreetNameHwK(item.getStreetNameHwK());
        value.setPrefectureName(item.getPrefectureName());
        value.setCityTownVillage(item.getCityTownVillage());
        value.setStreetName(item.getStreetName());
        value.setCombinedZipCodeType(item.getCombinedZipCodeType());
        value.setDetailedStreetAddressingType(item.getDetailedStreetAddressingType());
        value.setAvenueAndStreetNumberingType(item.getAvenueAndStreetNumberingType());
        value.setMultiAreaZipCodeType(item.getMultiAreaZipCodeType());
        value.setUpdateIndicationType(item.getUpdateIndicationType());
        value.setReasonForChangeType(item.getReasonForChangeType());
        value.setInputType(inputType.getSummary());
        return value;
    }

}
