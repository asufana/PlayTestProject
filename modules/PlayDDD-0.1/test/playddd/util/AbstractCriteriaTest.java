package playddd.util;

import static org.hamcrest.CoreMatchers.is;

import java.util.*;

import javax.persistence.*;

import org.junit.*;

import play.test.*;
import playddd.models.*;
import playddd.util.AbstractCriteria.OrderBy;

public class AbstractCriteriaTest extends UnitTest {
    
    @Before
    public void defore() {
        Fixtures.deleteDatabase();
        
        final Group group1 = new Group("group1").save();
        final Group group2 = new Group("group2").save();
        
        new User(group1, "foo").save();
        new User(group1, "bar").save();
        new User(group2, "baz").save();
    }
    
    @Test
    //最大返却数設定
    public void testMaxResult() {
        final List<User> usersAsc = new UserCriteria(User.class).maxResult(1)
                                                                .setLike("name",
                                                                         "b")
                                                                .search();
        assertThat(usersAsc.size(), is(1));
    }
    
    @Test
    //ソート順設定
    public void testOrderBy() {
        //昇順
        final List<User> usersAsc = new UserCriteria(User.class).orderBy("name",
                                                                         OrderBy.ASC)
                                                                .setLike("name",
                                                                         "b")
                                                                .search();
        assertThat(usersAsc.size(), is(2));
        assertThat(usersAsc.get(0).name, is("bar"));
        
        //降順
        final List<User> usersDesc = new UserCriteria(User.class).orderBy("name",
                                                                          OrderBy.DESC)
                                                                 .setLike("name",
                                                                          "b")
                                                                 .search();
        assertThat(usersDesc.size(), is(2));
        assertThat(usersDesc.get(0).name, is("baz"));
        
        //昇順->降順
        final List<User> usersAscDesc = new UserCriteria(User.class).orderBy("name",
                                                                             OrderBy.ASC)
                                                                    .orderBy("name",
                                                                             OrderBy.DESC)
                                                                    .setLike("name",
                                                                             "b")
                                                                    .search();
        assertThat(usersAscDesc.size(), is(2));
        assertThat(usersAscDesc.get(0).name, is("baz"));
    }
    
    @Test
    public void testEqual() {
        final List<User> users = new UserCriteria(User.class).setEqual("name",
                                                                       "foo")
                                                             .search();
        assertThat(users.size(), is(1));
    }
    
    @Test
    public void testJoinedEqual() {
        final List<User> users = new UserCriteria(User.class).setEqual("group.name",
                                                                       "group1")
                                                             .search();
        assertThat(users.size(), is(2));
    }
    
    @Test
    public void testLike() {
        final List<User> users = new UserCriteria(User.class).setLike("name",
                                                                      "f")
                                                             .search();
        assertThat(users.size(), is(1));
    }
    
    @Test
    public void testJoinedLike() {
        final List<User> users = new UserCriteria(User.class).setLike("group.name",
                                                                      "1")
                                                             .search();
        assertThat(users.size(), is(2));
    }
    
    @Test
    public void testIn() {
        final List<User> users = new UserCriteria(User.class).setIn("name",
                                                                    Arrays.asList("foo",
                                                                                  "bar"))
                                                             .search();
        assertThat(users.size(), is(2));
    }
    
    @Test
    public void testJoinedIn() {
        final List<User> users = new UserCriteria(User.class).setIn("group.name",
                                                                    Arrays.asList("group1"))
                                                             .search();
        assertThat(users.size(), is(2));
    }
    
    //-----------------------------------------
    
    private class UserCriteria extends AbstractCriteria<User> {
        public UserCriteria(final Class clazz) {
            super(clazz);
        }
    }
    
    @Entity
    public class User extends EntityModels<User> {
        
        private final String name;
        
        @ManyToOne
        private final Group group;
        
        public User(final Group group, final String name) {
            this.group = group;
            this.name = name;
        }
        
        @Override
        public void isSatisfied() {}
    }
    
    @Entity
    public class Group extends EntityModels<Group> {
        
        @SuppressWarnings("unused")
        private final String name;
        
        public Group(final String name) {
            this.name = name;
        }
        
        @Override
        public void isSatisfied() {}
    }
}
