package customize;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

/**
 * more partition config refer to: {@link
 * org.apache.kafka.clients.producer.internals。DefaultPartitioner}
 *
 * @author hw83770
 */
public class CustomPartitioner implements Partitioner {

  @Override
  public void configure(Map<String, ?> configs) {}

  @Override
  public int partition(
      String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    return 0;
  }

  @Override
  public void close() {}
}
