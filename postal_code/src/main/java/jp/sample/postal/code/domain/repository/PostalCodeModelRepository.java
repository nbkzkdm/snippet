/**
 * 
 */
package jp.sample.postal.code.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.sample.postal.code.domain.model.PostalCodeModel;

/**
 * postal code をDBとの接続をする
 * @author nbkzk
 *
 */
public interface PostalCodeModelRepository extends JpaRepository<PostalCodeModel, Long> {

    /**
     * 都道府県名で絞込
     * @param prefectureName
     * @return
     */
    public List<PostalCodeModel> findByPrefectureName(String prefectureName);

    /**
     * 市区町村名で絞込
     * @param cityTownVillage
     * @return
     */
    public List<PostalCodeModel> findByCityTownVillage(String cityTownVillage);

    /**
     * 郵便番号（7桁）で前方一致
     * @param postalCode
     * @return
     */
    public List<PostalCodeModel> findByPostalCodeStartsWith(String postalCode);

    /**
     * 郵便番号（7桁）
     * @param postalCode
     * @return
     */
    public List<PostalCodeModel> findByPostalCode(String postalCode);
    /**
     * 一意
     * @param postalCode
     * @return
     */
    @Query(value = "select t.* "
            + "from  postal_code_model as t "
            + "where t.local_government_code = :localGovernmentCode "
            + "and   t.old_postal_code = :oldPostalCode "
            + "and   t.postal_code = :postalCode ",
            nativeQuery = true)
    public Optional<PostalCodeModel> findByUnique(
            @Param("localGovernmentCode") String localGovernmentCode,
            @Param("oldPostalCode") String oldPostalCode,
            @Param("postalCode") String postalCode);

    /**
     * 最大作成日のデータを取得する
     * @return
     */
    @Query(value = "select t.* "
            + "from  postal_code_model as t "
            + "where t.create_at = (select max(internal.create_at) from postal_code_model as internal) ",
            nativeQuery = true)
    public List<PostalCodeModel> maxCreateAt();

    /**
     * 郵便番号でIN検索
     * @param postalCodeList
     * @return
     */
    public List<PostalCodeModel> findByPostalCodeIn(List<String> postalCodeList);

}
