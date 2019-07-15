package producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.KafkaConfig;

public class ProducerDemo {
  static final Logger logger = LoggerFactory.getLogger(ProducerDemo.class);

  public static void main(String[] args) {

//	 KafkaTemplate high level
    // create the producer
    KafkaProducer<String, String> producer =
        new KafkaProducer<String, String>(KafkaConfig.producerConfig());

    // create a producer record
    ProducerRecord<String, String> record =
        new ProducerRecord<String, String>("first_topic", "test msg");
    // thread safe, if need to use multi-thread, it can use only one instance to send message.
    // one producer has a message buffer, monitoring with 'buffer-total-bytes', 'buffer-available-bytes'

    producer.send(record); // send data - asynchronous

    // sendWithKey(producer);

    // sendWithCallback(producer, record);

    // flush data
    producer.flush();
    // flush and close producer
    producer.close();
  }

  public static void sendWithKey(KafkaProducer<String, String> producer) {
    // send with key
    for (int i = 0; i < 10; i++) {
      String key = "id_" + i;
      String value = "test msg: " + Integer.toString(i);

      ProducerRecord<String, String> record =
          new ProducerRecord<String, String>("first_topic", key, value);
      producer.send(record);
    }
  }

  public static void sendWithCallback(
      KafkaProducer<String, String> producer, ProducerRecord<String, String> record) {

    producer.send(
        record,
        new Callback() {
          public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            // executes every time a record is successfully sent or an exception is thrown
            if (e == null) {
              // the record was successfully sent
              logger.info(
                  "Received new metadata. \n"
                      + "Topic:"
                      + recordMetadata.topic()
                      + "\n"
                      + "Partition: "
                      + recordMetadata.partition()
                      + "\n"
                      + "Offset: "
                      + recordMetadata.offset()
                      + "\n"
                      + "Timestamp: "
                      + recordMetadata.timestamp());
            } else {
              logger.error("Error while producing", e);
            }
          }
        });
  }
}
