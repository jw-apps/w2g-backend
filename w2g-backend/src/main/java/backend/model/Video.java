package backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

public class Video {
    @NotNull
    @Min(0)
    public long ID;
    @NotEmpty
    public String filename;
    @NotEmpty
    @JsonIgnore
    public byte[] hash;

    @ConstructorProperties({"ID", "filename", "hash"})
    public Video(long ID, String filename, byte[] hash) {
        this.ID = ID;
        this.filename = filename;
        this.hash = hash;
    }
}
