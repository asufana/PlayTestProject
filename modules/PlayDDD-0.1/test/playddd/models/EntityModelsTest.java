package playddd.models;

import static org.hamcrest.CoreMatchers.is;

import javax.persistence.*;

import org.junit.*;

import play.test.*;

public class EntityModelsTest extends UnitTest {
    
    @Test
    public void testEquals() {
        final TestEntity test01 = new TestEntity().save();
        final TestEntity test02 = new TestEntity().save();
        assertThat(test01.equals(test02), is(false));
        assertThat(test01.id().equals(test02.id()), is(false));
    }
    
    @Entity
    private class TestEntity extends EntityModels<TestEntity> {
        @Override
        public void isSatisfied() {}
    }
}
