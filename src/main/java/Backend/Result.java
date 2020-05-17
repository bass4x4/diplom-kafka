package Backend;

public class Result {

    private final int lingerMs;
    private final int batchSize;
    private final String compressionType;
    private final String ack;
    private final boolean idempotent;

    public Result(int lingerMs, int batchSize, String compressionType, String ack, boolean idempotent) {
        this.lingerMs = lingerMs;
        this.batchSize = batchSize;
        this.compressionType = compressionType;
        this.ack = ack;
        this.idempotent = idempotent;
    }

    public int getLingerMs() {
        return lingerMs;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public String getAck() {
        return ack;
    }

    public boolean isIdempotent() {
        return idempotent;
    }
}
