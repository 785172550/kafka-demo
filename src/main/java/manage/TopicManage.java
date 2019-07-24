package manage;

import java.util.Properties;
//import kafka.zk.KafkaZkClient;

public class TopicManage {
  public static void createTopic(String topic, int partition, int repilca, Properties properties) {
    //    ZkUtils zkUtils = null;
    //    try {
    //      // 创建zkutil
    //      zkUtils =
    //          ZkUtils.apply(
    //              ZK_CONNECT, SEESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
    //      if (!AdminUtils.topicExists(zkUtils, topic)) {
    //
    //        AdminUtils.createTopic(
    //            zkUtils, topic, partition, repilca, properties,
    // AdminUtils.createTopic$default$6());
    //      }
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    } finally {
    //      zkUtils.close();
    //    }

//    KafkaZkClient client = null;

    //    client.apply(connectString, isSecure, sessionTimeoutMs, connectionTimeoutMs,
    // maxInFlightRequests, time, metricGroup, metricType);

  }
}
