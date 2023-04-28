/**
 * 
 */
package jp.sample.postal.code.common;

import java.util.List;

/**
 * @author nbkzk
 *
 */
public interface Splitting<T> {

    public Splitting<T> set(T value);
    public boolean isSplit();
    public List<T> splitValues();

}
