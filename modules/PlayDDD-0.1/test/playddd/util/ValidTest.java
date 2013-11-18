package playddd.util;

import org.junit.*;

import play.test.*;

public class ValidTest extends UnitTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testNotNullString() throws Exception {
        new Valid("").notNull();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNotNullObject() throws Exception {
        new Valid(null).notNull();
    }
}
