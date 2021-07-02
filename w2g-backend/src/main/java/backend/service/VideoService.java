package backend.service;

import backend.jdbi.VideoProvider;
import backend.model.Video;
import com.codahale.metrics.annotation.ExceptionMetered;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.List;

@Path("videos")
public class VideoService {

    private final static Logger logger = Log.getLogger(VideoService.class);

    private final VideoProvider videoProvider;
    private final File mediaPath;
    private final int chunkSize;

    public VideoService(VideoProvider videoProvider, String mediaPath, int chunkSize) {
        this.videoProvider = videoProvider;
        this.mediaPath = new File(mediaPath);
        this.chunkSize = chunkSize;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ExceptionMetered
    public List<Video> getVideoList() {
        return videoProvider.getVideos();
    }

    @HEAD
    @Produces("video/mp4")
    @ExceptionMetered
    public Response getVideoStreamHeader(@QueryParam("videoID") @NotNull @Min(0) Integer videoID) {
        Video video = videoProvider.queryVideo(videoID);
        File file = new File(mediaPath, video.filename);

        return Response.ok()
                .status(Response.Status.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_LENGTH, file.length())
                .header("Accept-Ranges", "bytes")
                .build();
    }

    @GET
    @Produces("video/mp4")
    @ExceptionMetered
    public Response getVideoStream(@QueryParam("videoID") @NotNull @Min(0) Integer videoID, @HeaderParam("Range") String range) {
        Video video = videoProvider.queryVideo(videoID);
        if (video == null) return Response.noContent().build();
        File file = new File(mediaPath, video.filename);

        return buildStream(file, range);
    }

    private Response buildStream( final File asset, final String range ) {
        // range not requested: firefox does not send range headers
        if ( range == null ) {
            logger.info("Request does not contain a range parameter!");

            StreamingOutput streamer = output -> {
                try (FileChannel inputChannel = new FileInputStream( asset ).getChannel();
                     WritableByteChannel outputChannel = Channels.newChannel( output ) ) {

                    inputChannel.transferTo( 0, inputChannel.size(), outputChannel );
                }
                catch( IOException io ) {
                    logger.info( io.getMessage() );
                }
            };

            return Response.ok( streamer )
                    .status( Response.Status.OK )
                    .header( HttpHeaders.CONTENT_LENGTH, asset.length() )
                    .build();
        }
        logger.info( "Requested Range: " + range );

        String[] ranges = range.split( "=" )[1].split( "-" );

        int from = Integer.parseInt( ranges[0] );

        // Chunk media if the range upper bound is unspecified
        int to = chunkSize + from;

        if ( to >= asset.length() ) {
            to = (int) ( asset.length() - 1 );
        }

        // uncomment to let the client decide the upper bound
        // we want to send 2 MB chunks all the time
        //if ( ranges.length == 2 ) {
        //    to = Integer.parseInt( ranges[1] );
        //}

        final String responseRange = String.format( "bytes %d-%d/%d", from, to, asset.length() );

        logger.info( "Response Content-Range: " + responseRange + "\n");

        final RandomAccessFile raf;
        try {
            raf = new RandomAccessFile( asset, "r" );
            raf.seek( from );
            final int len = to - from + 1;
            final MediaStreamer mediaStreamer = new MediaStreamer( len, raf );
            return Response.ok( mediaStreamer )
                    .status( Response.Status.PARTIAL_CONTENT )
                    .header( "Accept-Ranges", "bytes" )
                    .header( "Content-Range", responseRange )
                    .header( HttpHeaders.CONTENT_LENGTH, mediaStreamer. getLength() )
                    .header( HttpHeaders.LAST_MODIFIED, new Date( asset.lastModified() ) )
                    .build();
        } catch (IOException e) {
            logger.warn(e.getMessage());
            return Response.serverError().build();
        }
    }

    @OPTIONS
    public void options() {
    }
}
