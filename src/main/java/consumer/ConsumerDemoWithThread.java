package consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.KafkaConfig;

/**
 * multi thread demo
 *
 * @author hw83770
 */
public class ConsumerDemoWithThread {

  public static void main(String[] args) {
    new ConsumerDemoWithThread().run();
  }

  private ConsumerDemoWithThread() {}

  private void run() {
    Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());
    String topic = "first_topic";

    // latch for dealing with multiple threads
    CountDownLatch latch = new CountDownLatch(1);

    // create the consumer runnable
    logger.info("Creating the consumer thread");
    Runnable myConsumerRunnable =
        new ConsumerRunnable(KafkaConfig.consumerMultiTConfig(), topic, latch);

    // start the thread
    Thread myThread = new Thread(myConsumerRunnable);
    myThread.start();

    // add a shutdown hook
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  logger.info("Caught shutdown hook");
                  ((ConsumerRunnable) myConsumerRunnable).shutdown();
                  try {
                    latch.await();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  logger.info("Application has exited");
                }));

    try {
      latch.await();
    } catch (InterruptedException e) {
      logger.error("Application got interrupted", e);
    } finally {
      logger.info("Application is closing");
    }
  }

  public class ConsumerRunnable implements Runnable {

    private CountDownLatch latch;
    private KafkaConsumer<String, String> consumer;
    private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class.getName());

    public ConsumerRunnable(Properties config, String topic, CountDownLatch latch) {
      this.latch = latch;

      // create consumer
      consumer = new KafkaConsumer<String, String>(config);
      // subscribe consumer to our topic(s)
      consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void run() {
      // poll for new data
      try {
        while (true) {
          ConsumerRecords<String, String> records =
              consumer.poll(Duration.ofMillis(100)); // new in Kafka 2.0.0

          for (ConsumerRecord<String, String> record : records) {
            logger.info("Key: " + record.key() + ", Value: " + record.value());
            logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
          }
        }
      } catch (WakeupException e) {
        logger.info("Received shutdown signal!");
      } finally {
        consumer.close();
        // tell our main code we're done with the consumer
        latch.countDown();
      }
    }

    public void shutdown() {
      // the wakeup() method is a special method to interrupt consumer.poll()
      // it will throw the exception WakeUpException
      consumer.wakeup();
    }
  }
}
