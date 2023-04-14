/**
 * 
 */
package jp.sample.postal.code.api.http;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author nbkzk
 *
 */
@Data
@Builder
public class PrefectureDataModel {

    /** 都道府県名　…………　漢字（コード順に掲載）　（注1,2） */
    private String prefectureName;
    /** 市区町村名リスト */
    private List<CityTownVillageModel> cityTownVillageList;

}
