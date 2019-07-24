package connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import config.ConfigCenter;

public class FileStreamSourceConnector extends SourceConnector {
  private String filename;
  private String topic;

  @Override
  public String version() {
    return null;
  }

  @Override
  public void start(Map<String, String> props) {
    filename = props.get(ConfigCenter.FILE_NAME);
    topic = props.get(ConfigCenter.TOPIC_CONFIG);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return null;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    ArrayList<Map<String, String>> configs = new ArrayList<>();
    // Only one input partition makes sense.
    Map<String, String> config = new HashMap<>();
    if (filename != null) config.put("file_name", filename);
    config.put("topic_config", topic);
    configs.add(config);
    return configs;
  }

  @Override
  public void stop() {}

  @Override
  public ConfigDef config() {
    return null;
  }
}
