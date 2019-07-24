package connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import config.ConfigCenter;

public class FileStreamSourceTask extends SourceTask {
  private String filename;
  private InputStream stream;
  private String topic;

  @Override
  public String version() {
    return null;
  }

  @Override
  public void start(Map<String, String> props) {
    filename = props.get("file_name");
    stream = openFileStream(filename);
    topic = props.get("topic_config");
  }

  private InputStream openFileStream(String name) {

    // classpath
    java.net.URL url = getClass().getClassLoader().getResource(name);
    File file = new File(url.getFile());
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return fis;
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
	  // todo 
    return null;
  }

  @Override
  public void stop() {}
}
