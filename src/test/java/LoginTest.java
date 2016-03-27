import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import me.arianvp.time.core.Login;
import me.arianvp.time.core.User;
import org.junit.Test;

/**
 * Created by arian on 3/26/16.
 */
import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final Login person = new Login("username", "password");

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/login.json"), Login.class));

        assertThat(MAPPER.writeValueAsString(person)).isEqualTo(expected);
    }
}
