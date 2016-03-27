import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import me.arianvp.time.auth.JwtAuthenticator;
import me.arianvp.time.core.User;
import me.arianvp.time.db.UserDao;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import java.security.Security;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;
/**
 * Created by arian on 3/26/16.
 */
public class AuthenticatorTest {


    @Test
    public void itShouldAcceptAValidToken() throws AuthenticationException {
        SecretKey key = MacProvider.generateKey();
        UserDao dao = mock(UserDao.class);

        User user = new User(1, "username", BCrypt.hashpw("password", BCrypt.gensalt()));
        when(dao.getUserByName("username"))
                .thenReturn(user);
        JwtBuilder builder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, key)
                .setSubject("username");

        String token = builder.compact();
        JwtAuthenticator authenticator = new JwtAuthenticator(key, dao);

        assertThat(authenticator.authenticate(token)).isEqualTo(Optional.of(user));
    }

    @Test
    public void itShouldRejectAnInvalidToken() throws AuthenticationException {
        UserDao dao = mock(UserDao.class);

        User user = new User(1, "username", BCrypt.hashpw("password", BCrypt.gensalt()));
        when(dao.getUserByName("username"))
                .thenReturn(user);

        SecretKey key = MacProvider.generateKey();
        SecretKey key2 = MacProvider.generateKey();

        String token = Jwts.builder().signWith(SignatureAlgorithm.HS512,key).setSubject("username").compact();

        JwtAuthenticator authenticator = new JwtAuthenticator(key2, dao);


        assertThat(authenticator.authenticate(token)).isEqualTo(Optional.empty());

    }
}
