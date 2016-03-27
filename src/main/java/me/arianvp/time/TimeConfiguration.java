package me.arianvp.time;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.activation.DataSource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by arian on 3/26/16.
 */
public class TimeConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @NotNull
    private String secretKey;


    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
