package me.arianvp.time;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.*;
import java.util.Optional;

/**
 * Created by arian on 3/26/16.
 */

@Path("/time")
@Produces(MediaType.APPLICATION_JSON)
public class TimeResource {
    private final Clock clock;

    public TimeResource(Clock clock) {
        this.clock = clock;
    }

    @GET
    @Timed
    public LocalDateTime getTime(@QueryParam("zone") Optional<String> s) {
        try {
            ZoneId zone = s.map(ZoneId::of).orElse(clock.getZone());
            return LocalDateTime.now(clock.withZone(zone));
        } catch (DateTimeException e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
    }
}
