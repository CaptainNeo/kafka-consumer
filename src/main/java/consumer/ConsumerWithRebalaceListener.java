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

import consumer.listener.RebalanceListener;

/*
 * 리밸런스 발생을 감지하기 위해 카프카 라이브러리는 ConsumerRebalanceListener 인터페이스를 지원한다.
 * ConsumerRebalanceListener 인터페이스로 구현된 클래스는 onPartitionAssigned() 메서드와 onPartitionRevoked() 메서드로 이루어져 있다.
 * onPartitionAssigned() : 리밸런스가 끝난 뒤에 파티션이 할당 완료되면 호출되는 메서드
 * onPartitionRevoked() : 리밸런스가 시작되기 직전에 호출되는 메서드. 마지막으로 처리한 레코드를 기준으로 커밋을 하기 위해서는 리밸런스가 시작하기
 *                        직전에 커밋을 하면 되므로 onPartitionRevoked 메서드에 커밋을 구현하여 처리할 수 있다.
 * 리밸런싱이 자주 발생하면 안된다...
 */
public class ConsumerWithRebalaceListener {
	
	private final static Logger logger = LoggerFactory.getLogger(ConsumerWithRebalaceListener.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private final static String GROUP_ID = "test-group";
    
    private static KafkaConsumer<String, String> consumer;


	public static void main(String[] args) {
		
		Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList(TOPIC_NAME), new RebalanceListener()); // 리밸런스 리스너를 추가해야 한다.
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}", record);
                System.out.println(record.toString());
                
            }
        }

	}

}
