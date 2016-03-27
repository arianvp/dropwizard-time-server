import me.arianvp.time.resources.TimeResource;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

import java.time.*;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
/**
 * Created by arian on 3/26/16.
 */


public class TimeResourceTest {

    private Clock clock;
    private TimeResource resource;


    @Before
    public void setUp() {
        clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);
        resource = new TimeResource(clock);
    }

    @Test
    public void itShouldReportTheCurrentTimeInUTCWhenNoParamsGiven() {

        assertEquals(LocalDateTime.now(clock), resource.getTime(Optional.empty()));
    }

    @Test
    public void itShouldReportCurrentTimeInZone() {
        resource.getTime(Optional.of("Europe/Amsterdam"));
    }

    @Test
    public void itShouldCrashOnIncorrectTimeZone() {

        Throwable thrown = catchThrowable(() -> resource.getTime(Optional.of("aidwqj")));

        assertThat(thrown).isInstanceOf(WebApplicationException.class);

        int status = ((WebApplicationException)thrown).getResponse().getStatus();

        assertThat(status).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

}
