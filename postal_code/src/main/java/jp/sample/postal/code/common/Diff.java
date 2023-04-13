/**
 * 
 */
package jp.sample.postal.code.common;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jp.sample.postal.code.common.enums.ErrorType;
import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Log4j2
public class Diff {

    /**
     * @param <T>
     * @param clazz
     * @param elem1
     * @param elem2
     * @return
     * @throws PostalCodeException
     */
    public static <T> boolean diff(Class<T> clazz, T elem1, T elem2) throws PostalCodeException {
        return diff(clazz, elem1, elem2, new ArrayList<String>());
    }

    /**
     * @param <T>
     * @param clazz
     * @param elem1
     * @param elem2
     * @param ignoreFieldList
     * @return
     * @throws PostalCodeException
     */
    public static <T> boolean diff(Class<T> clazz, T elem1, T elem2, List<String> ignoreFieldList) throws PostalCodeException {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(ignoreFieldList, "ignoreFieldList must not be null");
        if (Objects.isNull(elem1) && Objects.isNull(elem2)) {
            return true;
        }
        if (Objects.isNull(elem1) || Objects.isNull(elem2)) {
            return false;
        }
        if (clazz.getName().startsWith("java.lang.")
                || clazz.getName().startsWith("[Ljava.lang.")
                || clazz.getName().startsWith("java.util.")) {
            return Objects.deepEquals(elem1, elem2);
        }
        Field[] fieldList = clazz.getDeclaredFields();
        for (Field field : fieldList) {
            if (ignoreFieldList.contains(field.getName())) {
                continue;
            }
            PropertyDescriptor pd = getPropertyDescriptor(clazz, field);
            Method readMethod = pd.getReadMethod();
            Object value1 = getValue(elem1, readMethod);
            Object value2 = getValue(elem2, readMethod);
            boolean diffFlag = diffValue(value1, value2);
            if (!diffFlag) {
                log.debug(String.format("judge: %s = (%s, %s)", field.getName(), getVal(value1), getVal(value2)));
                return false;
            }
        }
        return true;
    }

    /**
     * @param value1
     * @return
     */
    private static String getVal(Object value) {
        if (Objects.isNull(value)) {
            return "null";
        }
        if (value instanceof Object[]) return Arrays.toString((Object[]) value);
        else if (value instanceof String[]) return Arrays.toString((String[]) value);
        else if (value instanceof Integer[]) return Arrays.toString((Integer[]) value);
        else if (value instanceof Number[]) return Arrays.toString((Number[]) value);
        else if (value instanceof Long[]) return Arrays.toString((Long[]) value);
        else if (value instanceof Float[]) return Arrays.toString((Float[]) value);
        else if (value instanceof Double[]) return Arrays.toString((Double[]) value);
        else if (value instanceof Short[]) return Arrays.toString((Short[]) value);
        else if (value instanceof Byte[]) return Arrays.toString((Byte[]) value);
        else if (value instanceof Boolean[]) return Arrays.toString((Boolean[]) value);
        else if (value instanceof int[]) return Arrays.toString((int[]) value);
        else if (value instanceof long[]) return Arrays.toString((long[]) value);
        else if (value instanceof double[]) return Arrays.toString((double[]) value);
        else if (value instanceof float[]) return Arrays.toString((float[]) value);
        else if (value instanceof short[]) return Arrays.toString((short[]) value);
        else if (value instanceof byte[]) return Arrays.toString((byte[]) value);
        else if (value instanceof boolean[]) return Arrays.toString((boolean[]) value);;
        return value.toString();
    }

    /**
     * @param value1
     * @param value2
     * @return
     */
    private static boolean diffValue(Object value1, Object value2) {
        if (Objects.isNull(value1) && Objects.isNull(value2)) {
            return true;
        }
        if (Objects.isNull(value1) || Objects.isNull(value2)) {
            return false;
        }
        return Objects.deepEquals(value1, value2);
    }

    /**
     * @param <T>
     * @param elem1
     * @param readMethod
     * @return
     * @throws PostalCodeException
     */
    private static <T> Object getValue(T elem, Method readMethod) throws PostalCodeException {
        try {
            return readMethod.invoke(elem, (Object[]) null);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new PostalCodeException(ErrorType.INVOKE, e);
        }
    }

    /**
     * @param <T>
     * @param clazz
     * @param field
     * @return
     * @throws PostalCodeException
     */
    private static <T> PropertyDescriptor getPropertyDescriptor(Class<T> clazz, Field field) throws PostalCodeException {
        try {
            return new PropertyDescriptor(field.getName(), clazz);
        }
        catch (IntrospectionException e) {
            log.error(e.getMessage(), e);
            throw new PostalCodeException(ErrorType.INTROSPECTION, e);
        }
    }

}
