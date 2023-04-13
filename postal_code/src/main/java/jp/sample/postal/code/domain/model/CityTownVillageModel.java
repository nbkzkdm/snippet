/**
 * 
 */
package jp.sample.postal.code.domain.model;

import lombok.Data;

/**
 * @author nbkzk
 *
 */
@Data
public class CityTownVillageModel {

    /** 全国地方公共団体コード（JIS X0401、X0402）………　半角数字 */
    private String localGovernmentCode;
    /** 都道府県名　…………　半角カタカナ（コード順に掲載）　（注1） */
    private String prefectureNameHwK;
    /** 市区町村名　…………　半角カタカナ（コード順に掲載）　（注1） */
    private String cityTownVillageHwK;
    /** 都道府県名　…………　漢字（コード順に掲載）　（注1,2） */
    private String prefectureName;
    /** 市区町村名　…………　漢字（コード順に掲載）　（注1,2） */
    private String cityTownVillage;

    public static CityTownVillageModel convert(PostalCodeModel item) {
        CityTownVillageModel value = new CityTownVillageModel();
        value.setLocalGovernmentCode(item.getLocalGovernmentCode());
        value.setPrefectureNameHwK(item.getPrefectureNameHwK());
        value.setCityTownVillageHwK(item.getCityTownVillageHwK());
        value.setPrefectureName(item.getPrefectureName());
        value.setCityTownVillage(item.getCityTownVillage());
        return value;
    }

}
