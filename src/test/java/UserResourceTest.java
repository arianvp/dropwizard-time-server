import me.arianvp.time.core.User;
import me.arianvp.time.db.UserDao;
import me.arianvp.time.resources.UserResource;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.SecurityContext;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by arian on 3/26/16.
 */
public class UserResourceTest {

    private UserDao userDao;
    private UserResource userResource;
    private SecurityContext securityContext;

    @Before
    public void setUp() {
        this.userDao = mock(UserDao.class);
        this.userResource = new UserResource(this.userDao);
        this.securityContext = mock (SecurityContext.class);
    }

    @Test
    public void shouldGetAUserFromDB() {
        User user = new User(0, "hey", "test");
        when(userDao.get(0)).thenReturn(user);
        User user2 = userResource.getUser(user, 0);
        verify(userDao).get(0);
        assertThat(user).isEqualTo(user2);
    }
}
