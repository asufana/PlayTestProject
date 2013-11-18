package playddd.models;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.*;

import play.test.*;

public class ValueObjectTest extends UnitTest {
    
    @Test
    public void testConstructor() {
        assertThat(new Code("AAA", "01"), is(not(nullValue())));
        System.out.println(new Code("AAA", "01"));
    }
    
    @Test
    public void testEquals() {
        assertThat(new Code("AAA", "01").equals(new Code("AAA", "01")),
                   is(true));
        assertThat(new Code("AAA", "01").equals(new Code("BBB", "01")),
                   is(false));
        assertThat(new Code("AAA", "01").equals(new Code("AAA", "02")),
                   is(false));
        assertThat(new Code("AAA", "01").equals(new Code("BBB", "02")),
                   is(false));
    }
    
    private static class Code extends ValueObject<Code> {
        @SuppressWarnings("unused")
        private final String code;
        
        @SuppressWarnings("unused")
        private final String type;
        
        public Code(final String code, final String type) {
            this.code = code;
            this.type = type;
        }
    }
    
}
