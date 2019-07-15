package customize;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class CustomProducerInterceptor implements ProducerInterceptor<String, String> {

  @Override
  public void configure(Map<String, ?> configs) {}

  @Override
  public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
    // add prefix: logan-
    String modifiedValue = "logan-" + record.value();
    return new ProducerRecord<>(
        record.topic(),
        record.partition(),
        record.timestamp(),
        record.key(),
        modifiedValue,
        record.headers());
  }

  @Override
  public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    // TODO Auto-generated method stub
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub
  }
}
