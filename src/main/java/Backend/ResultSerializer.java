package Backend;

import org.json.JSONObject;

public class ResultSerializer {
    private static final String LINGER_MS_CONFIG = "LINGER_MS_CONFIG";
    private static final String BATCH_SIZE_CONFIG = "BATCH_SIZE_CONFIG";
    private static final String COMPRESSION_TYPE_CONFIG = "COMPRESSION_TYPE_CONFIG";
    private static final String ACKS_CONFIG = "ACKS_CONFIG";
    private static final String ENABLE_IDEMPOTENCE_CONFIG = "ENABLE_IDEMPOTENCE_CONFIG";
    private static final String NUMBER_OF_RECORDS = "NUMBER_OF_RECORDS";
    private static final String DURATION = "DURATION";

    public static String serialize(Result result) {
        JSONObject obj=new JSONObject();
        obj.put(LINGER_MS_CONFIG, result.getLingerMs());
        obj.put(BATCH_SIZE_CONFIG, result.getBatchSize());
        obj.put(COMPRESSION_TYPE_CONFIG, result.getCompressionType());
        obj.put(ACKS_CONFIG, result.getAck());
        obj.put(ENABLE_IDEMPOTENCE_CONFIG, result.isIdempotent());
        obj.put(NUMBER_OF_RECORDS, result.getNumberOfRecords());
        obj.put(DURATION, result.getDuration());
        return obj.toString();
    }

    public static Result deserialize(String json) {
        JSONObject obj = new JSONObject(json);
        int linger = obj.getInt(LINGER_MS_CONFIG);
        int batchSize = obj.getInt(BATCH_SIZE_CONFIG);
        String compression = obj.getString(COMPRESSION_TYPE_CONFIG);
        String ack = obj.getString(ACKS_CONFIG);
        boolean idempotent = obj.getBoolean(ENABLE_IDEMPOTENCE_CONFIG);
        int numberOfRecords = obj.getInt(NUMBER_OF_RECORDS);
        float duration = obj.getFloat(DURATION);
        return new Result(linger, batchSize, compression, ack, idempotent, numberOfRecords, duration);
    }
}
