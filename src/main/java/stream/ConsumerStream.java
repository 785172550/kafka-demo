package stream;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import com.google.gson.JsonParser;

import config.ConfigCenter;

public class ConsumerStream {
  public static void main(String[] args) {
    String topic = "first_topic";

    // create topology
    StreamsBuilder builder = new StreamsBuilder();
    // input topic
    KStream<String, String> inputStream = builder.stream(topic);
    KStream<String, String> filteredStream =
        inputStream.filter(
            (k, v) -> {
              return extractValueJson(v, "name").contains("hello");
              //              return v.contains("hello");
            });
    filteredStream.to("secord_topic"); // send to next topic

    //  build to topology
    Topology topology = builder.build();
    KafkaStreams kafkaStreams = new KafkaStreams(topology, ConfigCenter.streamConsumerConfg());

    // start streams application
    kafkaStreams.start();

    // **** topology is implement like spark DAG ****
    //    topology.addProcessor("PROCESS1", MyProcessor::new, "SOURCE")
    //        .addSink("name", "topic", "process1");

  }

  private static String extractValueJson(String source, String key) {
    JsonParser parser = new JsonParser();
    return parser.parse(source).getAsJsonObject().get(key).getAsString();
  }
}
