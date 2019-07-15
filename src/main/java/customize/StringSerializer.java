package customize;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

public class StringSerializer implements Serializer<String> {

  private String encoding = "UTF8";

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    // TODO Auto-generated method stub
  }

  @Override
  public byte[] serialize(String topic, String data) {
    byte[] bytes = null;
    try {
      bytes = data.getBytes(encoding);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return bytes;
  }

  @Override
  public void close() {}
}
