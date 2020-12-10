package Backend;

import Backend.POJO.Error;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

public class ErrorsGenerator {

    public static final int BOUND = 20;

    public static void main(String[] args) throws InterruptedException {
        final Producer<String, String> errorProducer = createErrorProducer();
        final Random random = new Random();
        while (true) {
            List<ProducerRecord> errors = IntStream.range(1, 21)
                    .mapToObj(i -> new ProducerRecord("test", String.valueOf(random.nextInt(1000)), generateError(random.nextInt(BOUND))))
                    .collect(Collectors.toList());
            errors.parallelStream().forEach(errorProducer::send);
            sleep(5000);
        }
    }

    private static String generateError(int i) {
        if (i == 20 || i == 19 || i == 18 || i == 17 || i == 16) {
            return Error.ErrorType.ERROR.name();
        }
        if (i == 15 || i == 14 || i == 13 || i == 12 || i == 11 || i == 10) {
            return Error.ErrorType.WARN.name();
        }
        return Error.ErrorType.DEBUG.name();
    }

    public static Producer<String, String> createErrorProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "test");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

}
