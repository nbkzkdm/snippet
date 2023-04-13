/**
 * 
 */
package jp.sample.postal.code.api.http;

import java.util.List;

import jp.sample.postal.code.domain.model.PostalCodeModel;
import lombok.Builder;
import lombok.Data;

/**
 * @author nbkzk
 *
 */
@Data
@Builder
public class ResponsePostalCode {

    /** postalId */
    private List<PostalCodeModel> PostalCodeModelList;

}
