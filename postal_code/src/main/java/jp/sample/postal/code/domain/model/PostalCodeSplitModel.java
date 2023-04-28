/**
 * 
 */
package jp.sample.postal.code.domain.model;

import java.util.ArrayList;
import java.util.List;

import jp.sample.postal.code.common.Merge;
import jp.sample.postal.code.common.Splitting;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Log4j2
public class PostalCodeSplitModel implements Splitting<PostalCodeModel> {

    private PostalCodeModel value;

    @Override
    public Splitting<PostalCodeModel> set(PostalCodeModel value) {
        this.value = value;
        return this;
    }

    private static final String BRACKET_END_HWK = "\\)";
    private static final String BRACKET_END = "）";

    @Override
    public boolean isSplit() {
//        if (!this.value.getStreetNameHwK().contains(BRACKET_END_HWK)) {
//            return false;
//        }
        if (!this.value.getStreetName().contains(BRACKET_END)) {
            return false;
        }
        log.debug(String.format("item>>%s", this.value.getStreetName()));
        log.debug(String.format("\t>>%s", this.value.getStreetNameHwK()));
        String[] splitHwkArray = splitHwK();
        if (splitHwkArray.length < 2) {
            return false;
        }
        String[] splitArray = split();
        if (splitArray.length < 2) {
            return false;
        }
        if (splitHwkArray.length != splitArray.length) {
            return false;
        }
        if (splitHwkArray[1].isEmpty() || splitArray[1].isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @return
     */
    private String[] splitHwK() {
        return this.value.getStreetNameHwK().split(BRACKET_END_HWK);
    }

    /**
     * @return
     */
    private String[] split() {
        return this.value.getStreetName().split(BRACKET_END);
    }

    @Override
    public List<PostalCodeModel> splitValues() {
        List<PostalCodeModel> list = new ArrayList<>();
        if (isSplit()) {
            String[] splitHwkArray = splitHwK();
            String[] splitArray = split();
            for (int i = 0; i < splitHwkArray.length; i++) {
                list.add(value.toBuilder()
                        .streetNameHwK(splitHwkArray[i].concat(BRACKET_END_HWK))
                        .streetName(splitArray[i].concat(BRACKET_END)).build());
            }
        }
        else {
            list.add(value.toBuilder().build());
        }
        return list;
    }

}
