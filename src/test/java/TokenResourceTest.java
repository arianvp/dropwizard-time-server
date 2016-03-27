/**
 * Created by arian on 3/27/16.
 */
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import me.arianvp.time.core.Login;
import me.arianvp.time.core.User;
import me.arianvp.time.db.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import me.arianvp.time.resources.TokenResource;

import javax.crypto.SecretKey;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;

public class TokenResourceTest {


    private JwtBuilder builder;
    private JwtParser parser;
    private SecretKey key;
    private TokenResource resource;
    private UserDao dao;
    private User user;
    private Clock clock;

    @Before
    public void setUp() {
        this.key = MacProvider.generateKey();
        this.builder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,key);
        this.parser = Jwts.parser()
                .setSigningKey(key);
        this.dao = mock(UserDao.class);
        this.resource = new TokenResource(dao, key);
        this.user = new User(0, "username", BCrypt.hashpw("password", BCrypt.gensalt()));
        this.clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

    }

    @Test
    public void itShouldIssueAValidTokenWithValidCredentials() {
        when(dao.getUserByName("username")).thenReturn(user);
        Login login = new Login("username", "password");
        String token = resource.newToken(login);
        verify(dao).getUserByName(login.getName());
        parser.requireSubject(login.getName()).parse(token);
    }

    @Test
    public void itShouldThrowWithInvalidCredentials() {
        when(dao.getUserByName("username")).thenReturn(user);
        Login login = new Login("username", "password2");

        Throwable thrown = catchThrowable(() -> resource.newToken(login));

        assertThat(thrown).isInstanceOf(WebApplicationException.class);

        assertThat(((WebApplicationException)thrown).getResponse().getStatus())
                .isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void itShouldAllowRenewal() {
        String token = builder.setSubject(user.getName()).compact();

        String newToken = resource.renewToken(user);

        parser.requireSubject(user.getName()).parse(newToken);
    }
}
