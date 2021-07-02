package backend.service;

import backend.jdbi.VideoProvider;
import backend.model.Video;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Path("scan")
public class ScanService {

    private final static Logger logger = Log.getLogger(ScanService.class);

    private final File mediaPath;
    private final VideoProvider videoProvider;

    public ScanService(String mediaPath, VideoProvider videoProvider){
        this.mediaPath = new File(mediaPath);
        this.videoProvider = videoProvider;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ExceptionMetered
    public List<Video> scan() {
        videoProvider.clearVideos();

        logger.info("Scanning folder " + mediaPath.getAbsolutePath());

        File[] files = mediaPath.listFiles();

        if (files != null)
            for (File file : files) {
                try {
                    HashCode hash = Files.asByteSource(file).hash(Hashing.md5());
                    videoProvider.createVideo(file.getName(), hash.asBytes());
                    logger.info("Found new video file: " + file.getName());
                } catch (IOException e) {
                    logger.warn(e);
                }
            }

        return videoProvider.getVideos();
    }

    @OPTIONS
    public void options() {
    }
}
