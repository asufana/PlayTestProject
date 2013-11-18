package playddd.models;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import javax.persistence.*;

import org.junit.*;

import play.test.*;

public class ModelsTest extends UnitTest {
    
    @Test
    public void testConstructor() {
        final TestEntity test = new TestEntity("test").save();
        assertThat(test.id(), is(not(nullValue())));
        assertThat(test.createDate(), is(not(nullValue())));
        assertThat(test.modifyDate(), is(not(nullValue())));
        assertThat(test.isDisable(), is(false));
        test.disable();
        test.save();
        assertThat(test.isDisable(), is(true));
    }
    
    @Test(expected = RuntimeException.class)
    public void testIsSatisfied() {
        new TestEntity(null).save();
    }
    
    @Entity
    private class TestEntity extends EntityModels<TestEntity> {
        
        private final String name;
        
        public TestEntity(final String name) {
            this.name = name;
        }
        
        @Override
        public void isSatisfied() {
            if (isEmpty(name)) {
                throw new RuntimeException();
            }
        }
    }
}
