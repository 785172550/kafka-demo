* zookeeper
```
// windows
./bin/windows/zookeeper-server-start.bat config/zookeeper.properties

./bin/zookeeper-server-start.sh config/zookeeper.properties
./bin/zookeeper-server-stop.sh
```
---

* kafka server(broker)
```
// windows
./bin/windows/kafka-server-start.bat config/server.properties

bin/kafka-server-start.sh config/server.properties [--override property=value]*
nohup bin/kafka-server-start.sh config/server.properties > kafka.log 2>&1 &

// check kafka status:
jps －lm 


bin/windows/kafka-configs.bat

// retention time config
bin/kafka-configs.sh --zookeeper localhost:2181 --alter --entity-type topics --entity-name test_topic --add-config retention.ms=1000

// describe topic
bin/kafka-configs.sh --zookeeper localhost:2181 --describe --entity-type topics --entity-name test_topic

```

---

* kafka topic
```
// windows
bin/windows/kafka-topics.bat

bin/windows/kafka-topics.bat --describe --zookeeper localhost:2181 --topic test_topic

bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test_topic

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic test_topic

bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic test_topic

bin/kafka-topics.sh --list --zookeeper localhost:2181

bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic test_topic --partitions 3

```

---

* kafka console-consumer
```
// start console-coonsumer:
bin/windows/kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning
// specific partition:
bin/windows/kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning --partition 0

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning --partition 0

```

---

* kafka console-producer
```
bin/windows/kafka-console-producer.bat --broker-list localhost:9092 --topic test

bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

// from file:
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test < file-input.txt

```
