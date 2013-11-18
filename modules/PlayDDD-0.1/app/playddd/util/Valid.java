package playddd.util;

import static org.apache.commons.lang.StringUtils.*;

/** Validator Util */
public class Valid {
    
    private final Object object;
    
    public Valid(final Object object) {
        this.object = object;
    }
    
    public Valid notNull() {
        return notNull("値を入力してください");
    }
    
    public Valid notNull(final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        if (object instanceof String && isEmpty((String) object)) {
            throw new IllegalArgumentException(message);
        }
        return this;
    }
}
