package playddd.models;

import static org.apache.commons.lang.StringUtils.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.commons.lang.builder.*;

/**
 * 値オブジェクト抽象基底クラス
 */
public abstract class ValueObject<T> implements Serializable {
    
    public boolean sameValueAs(final T other) {
        //このオブジェクトのフィールド一覧
        final Map<String, Object> thisMap = getFields(this);
        //比較対象オブジェクトのフィールド一覧
        final Map<String, Object> otherMap = getFields(other);
        
        //比較
        for (final Map.Entry<String, Object> e : thisMap.entrySet()) {
            //同名フィールドが存在しなければ同一でない
            if (!otherMap.containsKey(e.getKey())) {
                return false;
            }
            //同名フィールド値が一致しなければ同一でない（NULL一致）
            if (e.getValue() == null && otherMap.get(e.getKey()) == null) {
                return true;
            }
            //同名フィールド値が一致しなければ同一でない（NULL不一致）
            if (e.getValue() == null && otherMap.get(e.getKey()) != null) {
                return false;
            }
            //同名フィールド値が一致しなければ同一でない（NULL不一致）
            if (e.getValue() != null && otherMap.get(e.getKey()) == null) {
                return false;
            }
            //同名フィールド値が一致しなければ同一でない（値不一致）
            if (!e.getValue().equals(otherMap.get(e.getKey()))) {
                return false;
            }
        }
        return true;
    }
    
    //フィールド一覧を取得
    private Map<String, Object> getFields(final Object o) {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final Field f : o.getClass().getDeclaredFields()) {
            //staticフィールドは除外
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            f.setAccessible(true);
            try {
                map.put(f.getName(), f.get(o));
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final T other = (T) o;
        return sameValueAs(other);
    }
    
    @Override
    public int hashCode() {
        final Map<String, Object> thisMap = getFields(this);
        final HashCodeBuilder builder = new HashCodeBuilder();
        for (final Map.Entry<String, Object> e : thisMap.entrySet()) {
            builder.append(e.getValue());
        }
        return builder.toHashCode();
    }
    
    @Override
    public String toString() {
        final Map<String, Object> thisMap = getFields(this);
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, Object> e : thisMap.entrySet()) {
            if (isNotEmpty(sb.toString())) {
                sb.append(",");
            }
            sb.append(e.getKey());
            sb.append(":");
            sb.append(e.getValue());
        }
        return "[" + this.getClass().getSimpleName() + "]" + sb.toString();
    }
    
}
