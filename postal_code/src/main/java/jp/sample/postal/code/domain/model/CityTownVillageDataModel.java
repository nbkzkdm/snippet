/**
 * 
 */
package jp.sample.postal.code.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author nbkzk
 *
 */
@Data
@Builder
public class CityTownVillageDataModel {

    /** 全国地方公共団体コード（JIS X0401、X0402）………　半角数字 */
    private String localGovernmentCode;
    /** 都道府県名　…………　漢字（コード順に掲載）　（注1,2） */
    private String prefectureName;
    /** 市区町村名　…………　漢字（コード順に掲載）　（注1,2） */
    private String cityTownVillage;
    /** 町域名リスト */
    private List<PostalCodeModel> streetNameList;

}
