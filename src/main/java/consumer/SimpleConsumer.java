package consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * loop 구문 중 while(true) 기본적으로 무한루프를 도는것이 내구 규칙이다. 
 * ./bin/windows/kafka-console-producer.bat --bootstrap-server my-kafka:9092 --topic test
 * 커맨드라인툴로 테스트를 진행해보자.
 * 아래와 같이 구독된 정보를 볼 수 있다. 
 * record: {} : ConsumerRecord(topic = test, partition = 3, leaderEpoch = 0, offset = 0, CreateTime = 1736502167271, serialized key size = -1, serialized value size = 37, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = hey get my value consumer application)
 */

public class SimpleConsumer {
	private final static Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private final static String GROUP_ID = "test-group";

	public static void main(String[] args) {
		
		Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);

        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("record:{}", record);
                System.out.println("record: {} : " + record);
            }
        }
		

	}

}
