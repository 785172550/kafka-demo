package consumer;

import java.time.Duration;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.KafkaConfig;

public class ConsumerDemo {

  static final Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());

  public static void main(String[] args) {

    String topic = "first_topic";
    // create consumer
    KafkaConsumer<String, String> consumer =
        new KafkaConsumer<String, String>(KafkaConfig.consumerConfig());

    // subscribe consumer to our topic(s)
    consumer.subscribe(Arrays.asList(topic));

    // poll for new data
    //    while (true) {
    //      ConsumerRecords<String, String> records =
    //          consumer.poll(Duration.ofMillis(100));
    //
    //      for (ConsumerRecord<String, String> record : records) {
    //        logger.info("Key: " + record.key() + ", Value: " + record.value());
    //        logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
    //      }
    //    }

    seekDemo(consumer, topic);
  }

  private static void seekDemo(KafkaConsumer<String, String> consumer, String topic) {
    // assign
    TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
    long offsetToReadFrom = 15L;
    consumer.assign(Arrays.asList(partitionToReadFrom));

    // seek
    consumer.seek(partitionToReadFrom, offsetToReadFrom);

    int numberOfMessagesToRead = 5;
    boolean keepOnReading = true;
    int numberOfMessagesReadSoFar = 0;

    // poll for new data
    while (keepOnReading) {
      ConsumerRecords<String, String> records =
          consumer.poll(Duration.ofMillis(100)); // new in Kafka 2.0.0

      for (ConsumerRecord<String, String> record : records) {
        numberOfMessagesReadSoFar += 1;
        logger.info("Key: " + record.key() + ", Value: " + record.value());
        logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
        if (numberOfMessagesReadSoFar >= numberOfMessagesToRead) {
          keepOnReading = false; // to exit the while loop
          break; // to exit the for loop
        }
      }
    }

    logger.info("Exiting the application");
  }
}
