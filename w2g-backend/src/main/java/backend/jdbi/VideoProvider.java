package backend.jdbi;

import backend.model.Video;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface VideoProvider {

    @SqlQuery("SELECT * from videos")
    @RegisterConstructorMapper(Video.class)
    List<Video> getVideos();

    @SqlQuery("SELECT * from videos WHERE ID = :videoID")
    @RegisterConstructorMapper(Video.class)
    Video queryVideo(long videoID);

    @SqlUpdate("INSERT INTO videos (filename,hash) VALUES (:filename,:hash)")
    void createVideo(String filename, byte[] hash);

    @SqlUpdate("DELETE FROM videos")
    void clearVideos();

}
