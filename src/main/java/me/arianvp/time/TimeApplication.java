package me.arianvp.time;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 * Created by arian on 3/26/16.
 */
public class TimeApplication extends Application<TimeConfiguration>{

    @Override
    public void initialize(Bootstrap<TimeConfiguration> bootstrap) {
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.getObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @Override
    public void run(TimeConfiguration config, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "sqlite");

        final JerseyEnvironment jersey = environment.jersey();
        jersey.register(new TimeResource(java.time.Clock.systemUTC()));
    }

    @Override
    public String getName() {
        return "time";
    }

    public static void main(String[]args) throws Exception {
        new TimeApplication().run(args);
    }
}
