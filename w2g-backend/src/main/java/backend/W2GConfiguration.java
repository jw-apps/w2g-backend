package backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class W2GConfiguration extends Configuration {

    @NotNull
    @Min(1)
    private int chunkSize;

    @NotEmpty
    private String mediaFolder;

    @JsonProperty
    public int getChunkSize() {
        return chunkSize;
    }

    @JsonProperty
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @JsonProperty
    public String getMediaFolder() {
        return mediaFolder;
    }

    @JsonProperty
    public void setMediaFolder(String mediaFolder) {
        this.mediaFolder = mediaFolder;
    }

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}
