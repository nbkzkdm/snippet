/**
 * 
 */
package jp.sample.postal.code.domain.model;

/**
 * @author nbkzk
 *
 */
public interface Merge<T> {

    public String key();
    public T value();
    public Merge<T> set(T value);
    public Merge<T> merge(T other);

}
