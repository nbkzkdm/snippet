/**
 * 
 */
package jp.sample.postal.code.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.sample.postal.code.common.Merge;
import jp.sample.postal.code.common.Splitting;
import jp.sample.postal.code.common.enums.InputType;
import jp.sample.postal.code.domain.model.PostalCodeCsv;
import jp.sample.postal.code.domain.model.PostalCodeMergeModel;
import jp.sample.postal.code.domain.model.PostalCodeModel;
import jp.sample.postal.code.domain.model.PostalCodeSplitModel;
import jp.sample.postal.code.domain.repository.PostalCodeModelRepository;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Service
@Log4j2
public class PostalCodeDbService {

    @Autowired
    private PostalCodeModelRepository postalCodeModelRepository;

    /**
     * 最新情報のリスト取得（全データはありません）
     * @return
     */
    public Optional<LocalDateTime> maxCreateAt() {
        List<PostalCodeModel> list = this.postalCodeModelRepository.maxCreateAt();
        if (!list.isEmpty() || 0 < list.size()) {
            return Optional.of(list.get(0).getCreateAt());
        }
        return Optional.of(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }

    /**
     * 郵便番号情報リスト取得
     * @return
     */
    public List<PostalCodeModel> findAll() {
        return this.postalCodeModelRepository.findAll();
    }

    /**
     * データのマージ処理
     * 10秒程度かかります。
     * @param entryList
     * @return
     */
    public List<PostalCodeModel> mergeList(List<PostalCodeModel> entryList) {
        List<Merge<PostalCodeModel>> targetList =
                entryList.stream().map(e -> new PostalCodeMergeModel().set(e)).toList();
        Map<String, Merge<PostalCodeModel>> map = new HashMap<>();
        for (Merge<PostalCodeModel> merge : targetList) {
            Merge<PostalCodeModel> item = map.get(merge.key());
            if (Objects.isNull(item)) {
                map.put(merge.key(), merge);
            }
            else {
                map.put(merge.key(), item.merge(merge.value()));
            }
        }
        return map.values().stream().map(e -> e.value()).toList();
    }

    public List<PostalCodeModel> splitList(List<PostalCodeModel> entryList) {
        List<Splitting<PostalCodeModel>> targetList =
                entryList.stream().map(e -> new PostalCodeSplitModel().set(e)).toList();
        List<PostalCodeModel> retList = new ArrayList<>();
        for (Splitting<PostalCodeModel> item : targetList) {
            retList.addAll(item.splitValues());
        }
        return retList;
    }

    /**
     * 郵便番号情報リスト登録
     * @param entryList
     * @return
     */
    public List<PostalCodeModel> createList(List<PostalCodeModel> entryList) {
        return this.postalCodeModelRepository.saveAll(splitList(mergeList(entryList)));
    }

    /**
     * CSVから変更して全国の登録
     * @param csvList
     * @return
     */
    public List<PostalCodeModel> postalCodeConvertCreateList(List<PostalCodeCsv> csvList) {
        List<PostalCodeModel> list = csvList.stream().map(e -> PostalCodeModel.convert(e)).toList();
        return createList(list);
    }

    /**
     * CSVから変更して追加の登録
     * @param csvList
     * @return
     */
    public List<PostalCodeModel> postalCodeConvertAddList(List<PostalCodeCsv> csvList) {
        List<PostalCodeModel> targetList = splitList(mergeList(
                csvList.stream().map(e -> PostalCodeModel.convert(e)).toList()));
        List<PostalCodeModel> list = new ArrayList<>();
        for (PostalCodeModel target : targetList) {
            String postalCode = target.getPostalCode();
            Optional<PostalCodeModel> optionalTarget = this.postalCodeModelRepository.findByUnique(
                    target.getLocalGovernmentCode(), target.getOldPostalCode(), postalCode);
            log.debug(String.format("追加:%s_%s_%s",
                    target.getPrefectureName(), target.getCityTownVillage(), target.getStreetName()));
            PostalCodeModel item = optionalTarget.orElse(new PostalCodeModel());
            long update = item.getPostalId();
            if (0 < update) {
                log.debug(String.format("add: %s[postal code: %s]", update, item.getPostalCode()));
                item.setPrefectureNameHwK(target.getPrefectureNameHwK());
                item.setCityTownVillageHwK(target.getCityTownVillageHwK());
                item.setStreetNameHwK(target.getStreetNameHwK());
                item.setPrefectureName(target.getPrefectureName());
                item.setCityTownVillage(target.getCityTownVillage());
                item.setStreetName(target.getStreetName());
                item.setCombinedZipCodeType(target.getCombinedZipCodeType());
                item.setDetailedStreetAddressingType(target.getDetailedStreetAddressingType());
                item.setAvenueAndStreetNumberingType(target.getAvenueAndStreetNumberingType());
                item.setMultiAreaZipCodeType(target.getMultiAreaZipCodeType());
                item.setUpdateIndicationType(target.getUpdateIndicationType());
                item.setInputType(InputType.追加.getSummary());
                item.setUpdateAt(LocalDateTime.now());
                list.add(this.postalCodeModelRepository.save(item));
            }
            else {
                log.debug(String.format("add: %s[postal code: %s]", 0, postalCode));
                list.add(this.postalCodeModelRepository.save(target));
            }
        }
        return list;
    }

    /**
     * CSVから変更して廃止
     * @param csvList
     * @return
     */
    public void postalCodeConvertDelList(List<PostalCodeCsv> csvList) {
        for (PostalCodeCsv csvItem : csvList) {
            List<PostalCodeModel> deleteList = this.postalCodeModelRepository.findByPostalCode(csvItem.getPostalCode());
            for (PostalCodeModel item : deleteList) {
                log.debug(String.format("del: %s[postal code: %s]", item.getPostalId(), item.getPostalCode()));
                log.debug(String.format("削除:%s_%s_%s", item.getPrefectureName(), item.getCityTownVillage(), item.getStreetName()));
                this.postalCodeModelRepository.deleteById(item.getPostalId());
            }
        }
    }

    /**
     * 郵便番号の前方一致
     * @param postalCode
     * @return
     */
    public List<PostalCodeModel> findByPostalCodeStartsWith(String postalCode) {
        Objects.requireNonNull(postalCode, "postalCode must not be null");
        return this.postalCodeModelRepository.findByPostalCodeStartsWith(postalCode);
    }

    /**
     * 都道府県指定
     * @param prefectureName
     * @return
     */
    public List<PostalCodeModel> findByPrefectureName(String prefectureName) {
        Objects.requireNonNull(prefectureName, "prefectureName must not be null");
        return this.postalCodeModelRepository.findByPrefectureName(prefectureName);
    }

    /**
     * 市区町村名指定
     * @param cityTownVillage
     * @return
     */
    public List<PostalCodeModel> findByCityTownVillage(String cityTownVillage) {
        Objects.requireNonNull(cityTownVillage, "cityTownVillage must not be null");
        return this.postalCodeModelRepository.findByCityTownVillage(cityTownVillage);
    }

}
