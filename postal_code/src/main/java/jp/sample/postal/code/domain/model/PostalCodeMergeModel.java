/**
 * 
 */
package jp.sample.postal.code.domain.model;

import jp.sample.postal.code.common.Merge;

/**
 * @author nbkzk
 *
 */
public class PostalCodeMergeModel implements Merge<PostalCodeModel> {

    private PostalCodeModel value;

    @Override
    public String key() {
        return String.format("%s-%s-%s_%s-%s-%s-%s-%s-%s",
                this.value().getPostalId(), this.value().getOldPostalCode(), this.value().getPostalCode(),
                this.value().getCombinedZipCodeType(), this.value().getDetailedStreetAddressingType(),
                this.value().getAvenueAndStreetNumberingType(), this.value().getMultiAreaZipCodeType(),
                this.value().getUpdateIndicationType(), this.value().getReasonForChangeType());
    }

    @Override
    public PostalCodeModel value() {
        return this.value;
    }

    @Override
    public Merge<PostalCodeModel> set(PostalCodeModel value) {
        this.value = value;
        return this;
    }

    @Override
    public Merge<PostalCodeModel> merge(PostalCodeModel other) {
        this.value.setStreetNameHwK(this.value.getStreetNameHwK().concat(other.getStreetNameHwK()));
        this.value.setStreetName(this.value.getStreetName().concat(other.getStreetName()));
        return this;
    }

}
