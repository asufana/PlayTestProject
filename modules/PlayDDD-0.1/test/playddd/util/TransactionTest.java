package playddd.util;

import static org.hamcrest.CoreMatchers.is;

import javax.persistence.*;

import org.junit.*;

import play.test.*;
import playddd.models.*;

public class TransactionTest extends UnitTest {
    
    @Before
    public void defore() {
        Fixtures.deleteDatabase();
        
        assertThat(TestEntity.count(), is(0L));
    }
    
    @Test
    public void testCommit() {
        final Transaction tran = new Transaction();
        tran.open();
        new TestEntity().save();
        tran.commit();
        
        assertThat(TestEntity.count(), is(1L));
    }
    
    @Test
    public void testRollback() {
        final Transaction tran = new Transaction();
        tran.open();
        new TestEntity().save();
        tran.rollback();
        
        assertThat(TestEntity.count(), is(0L));
    }
    
    @Entity
    private class TestEntity extends EntityModels<TestEntity> {
        @Override
        public void isSatisfied() {}
    }
}
