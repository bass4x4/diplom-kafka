package Backend.POJO;

public class Result {

    private int lingerMs;
    private int batchSize;
    private String compressionType;
    private String ack;
    private boolean idempotent;
    private int numberOfRecords;
    private float duration;

    public Result() {
    }

    public Result(int lingerMs, int batchSize, String compressionType, String ack, boolean idempotent, int numberOfRecords, float duration) {
        this.lingerMs = lingerMs;
        this.batchSize = batchSize;
        this.compressionType = compressionType;
        this.ack = ack;
        this.idempotent = idempotent;
        this.numberOfRecords = numberOfRecords;
        this.duration = duration;
    }

    public int getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(int lingerMs) {
        this.lingerMs = lingerMs;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public boolean isIdempotent() {
        return idempotent;
    }

    public void setIdempotent(boolean idempotent) {
        this.idempotent = idempotent;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
