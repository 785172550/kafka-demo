package stream;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;

public class MyProcessor extends AbstractProcessor<String, String> {

  @Override
  public void init(ProcessorContext context) {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() {

  }

  @Override
  public void process(String key, String value) { // TODO Auto-generated method stub
  }
}
