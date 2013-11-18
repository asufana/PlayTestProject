package models.users;

import static org.hamcrest.CoreMatchers.*;
import models.*;
import models.users.vos.*;

import org.junit.*;

import play.test.*;

public class UserTest extends UnitTest {
    
    @Before
    public void before() {
        Fixtures.deleteDatabase();
    }
    
    @Test
    public void testConstractor() throws Exception {
        assertThat(User.count(), is(0L));
        new User(T.userName, UserType.STAFF, T.joinedDate).save();
        assertThat(User.count(), is(1L));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsSatisfied() throws Exception {
        new User(null, null, null).save();
    }
}
