package backend.model;


public class TimestampMessage {
    private long videoID;
    private boolean play;
    private long videoTimestamp;
    private long timestamp;

    public TimestampMessage(long videoID, boolean play, long videoTimestamp, long timestamp) {
        this.videoID = videoID;
        this.play = play;
        this.videoTimestamp = videoTimestamp;
        this.timestamp = timestamp;
    }

    public long getVideoID() {
        return videoID;
    }

    public boolean isPlay() {
        return play;
    }

    public long getVideoTimestamp() {
        return videoTimestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
