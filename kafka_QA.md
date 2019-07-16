Kafka的用途有哪些？使用场景如何？
```
MQ消息中间件， 日志， 大数据等，吞吐量大的场景：作用解耦， 削峰缓解压力，限流 同步并发，RPC替代

```

Kafka中的ISR、AR又代表什么？ISR的伸缩又指什么
```
ISR指同步的副本列表，OSR是跟不上的副本
AR(assigned replica) = ISR + OSR

ISR的伸缩: ISR是动态维护的， 落后太多的副本会剔除， 跟上来会加入
```

Kafka中的HW、LEO、LSO、LW等分别代表什么？
```
HW = HighWatermark 高水位 数据被同步到所有的ISR中的副本后，数据才认为已提交，HW更新到该位置
LEO = LogEndOffset 最新数据标识位

HW截断， 在选出了新的leader之后会，截断HW之后的数据

LSO: last stable offset 未完成的事物LSO 在事务中第一条消息卫视，完成的事务 = HW

LW AR 中offset最小的

AR、ISR、OSR、LEO、HW这些信息都被保存在Zookeeper中。
```
Kafka中是怎么体现消息顺序性的？
```
同一个parition的数据是顺序的
同一个group 只有一个消费，也是有序的

```
Kafka中的分区器、序列化器、拦截器是否了解？它们之间的处理顺序是什么？
```
kafka 发送消息体的过程： producer发送消息， 经过拦截器(interceptor)，序列化器， 分区器， 最后到累加器RecordAccumulator， 最后由sender线程发送

ProducerInterceptor接口实现拦截器

序列化器， kafka以字节流传输，需要序列化成字节数组，kafka提供了很多基本类型的序列化器

分区器： 如果消息partition字段没有指定，那么调用default partitioner 根据key取hash，hash % partition_nums 得到分区

```
Kafka生产者客户端的整体结构是什么样子的？
```
KafkaProducer是线程安全的，从代码使用层面上来说你可以在多个线程中共享同一个实例

sender 线程: 所有请求都是通过 sender线程发送的 NIO SelectionKey KafkaChannel

producer -> interceptor -> serialize -> router/partition -> batch: write in cacche(reacord accumulartor append) -> sender thread send

```
Kafka生产者客户端中使用了几个线程来处理？分别是什么？

sender 线程: 所有请求都是通过 sender线程发送的 NIO SelectionKey KafkaChannel

```

```
Kafka的旧版Scala的消费者客户端的设计有什么缺陷？
“消费组中的消费者个数如果超过topic的分区，那么就会有消费者消费不到数据”这句话是否正确？如果不正确，那么有没有什么hack的手段？
```
是的，有的消费者不能，除非 rebalance和 自定义指定的partition



```
消费者提交消费位移时提交的是当前消费到的最新消息的offset还是offset+1?
```
不是+1， 在auto.commit.interval.ms默认是5s的情况下， poll()方法会提交最大偏移量

```
有哪些情形会造成重复消费？
```
重复发送 未去重(在没有开启事务)
提交偏移量小于客户端的偏移量

```
那些情景下会造成消息漏消费？

```
提交偏移量大于 客户端处理的偏移量
```
KafkaConsumer是非线程安全的，那么怎么样实现多线程消费？
```
多线程共享一个consumer同步代价非常大
每个线程持有自己的consumer instance
或者用多进程版本，更稳定，分布式方案首选多进程

新版consumer实际上是双线程的设计————引入了额外的心跳线程
```

简述消费者与消费组之间的关系
```
同一个消费者组 共享一个公共的ID，即group ID。组内的所有消费者协调在一起来消费订阅主题(subscribed topics)的所有分区(partition)
每个分区只能由同一个消费组内的一个consumer来消费

```
当你使用kafka-topics.sh创建（删除）了一个topic之后，Kafka背后会执行什么逻辑？
```
触发 zk上面 /broker/topics/XX 节点的创建
删除 将zk /broker/topics/XX 标记为待删除， 后台有定时的线程删除

```
topic的分区数可不可以增加？如果可以怎么增加？如果不可以，那又是为什么？
topic的分区数可不可以减少？如果可以怎么减少？如果不可以，那又是为什么？
```
可以增加不可以减少

减少时， 是删除这个partition的数据？ 消息的可靠性？
还是 转移消息到其他partition？ 消息的顺序性？ 在消息量很大的时候性能问题

但是副本减少是很好做的

```
创建topic时如何选择合适的分区数？
```
根据集群的机器数量和需要的吞吐量来决定适合的分区数
```
Kafka目前有那些内部topic，它们都有什么特征？各自的作用又是什么？
```
__comsumer_offsets 消费者的偏移

```
优先副本是什么？它有什么特殊的作用？
```
优先副本会 默认是leader

```

Kafka有哪几处地方有分区分配的概念？简述大致的过程及原理
```
创建topic 
route 消息
消费者组

```

简述Kafka的日志目录结构
Kafka中有那些索引文件？
```
partition 就是一个文件夹
包含4类文件：
.index 稀疏的 相对偏移的索引, 和文件中的物理位置
.log  消息数据 ->  log是分段的
.timeindex 时间索引
.snapshot 
leader-epoch-checkpoint 每一任leader开始写入消息时的offset(更大的当选)

```

如果我指定了一个offset，Kafka怎么查找到对应的消息？
```
先根据文件名字找到 对应的seg的index
根据index 找到附近的消息，然后逐条寻找
```

如果我指定了一个timestamp，Kafka怎么查找到对应的消息？
```
两种createTime, logAppendTime 类型index
引入时间戳， 用于过期策略

具体的格式是 时间戳T + 位移offset
表示 位移offset之内的消息，最晚时间是T

```
聊一聊你对Kafka的Log Retention的理解
聊一聊你对Kafka的Log Compaction的理解
```
kafka留存策略包括 删除和压缩两种
删除: 根据时间和大小两个方式进行删除 大小是整个partition日志文件的大小
超过的会从老到新依次删除 时间指日志文件中的最大时间戳而非文件的最后修改时间

压缩: 相同key的value只保存一个 压缩过的是clean未压缩的dirty 
压缩之后的偏移量不连续 未压缩时连续

```

聊一聊你对Kafka底层存储的理解（页缓存、内核层、块层、设备层）

聊一聊Kafka的延时操作的原理

聊一聊Kafka控制器的作用
```

```

消费再均衡的原理是什么？（提示：消费者协调器和消费组协调器）

Kafka中的幂等是怎么实现的
```
pid + 全局sequenceID 单个producer内幂等
消费者可以过滤
producer 重发机制

```

Kafka中的事务是怎么实现的（这题我去面试6加被问4次，照着答案念也要念十几分钟，面试官简直凑不要脸。实在记不住的话...只要简历上不写精通Kafka一般不会问到，我简历上写的是“熟悉Kafka，了解RabbitMQ....”）

Kafka中有那些地方需要选举？这些地方的选举策略又有哪些？

失效副本是指什么？有那些应对措施？

多副本下，各个副本中的HW和LEO的演变过程

为什么Kafka不支持读写分离？
```
读写分离可以做，但是读写分离 更适合 读多写少的环境
MQ 使用场景很多，无法确定读写多少
```
Kafka在可靠性方面做了哪些改进？（HW, LeaderEpoch）

Kafka中怎么实现死信队列和重试队列？

Kafka中的延迟队列怎么实现（这题被问的比事务那题还要多！！！听说你会Kafka，那你说说延迟队列怎么实现？）

Kafka中怎么做消息审计？

Kafka中怎么做消息轨迹？

Kafka中有那些配置参数比较有意思？聊一聊你的看法

Kafka中有那些命名比较有意思？聊一聊你的看法

Kafka有哪些指标需要着重关注？
```
生产者关注MessagesInPerSec、BytesOutPerSec、BytesInPerSec 消费者关注消费延迟Lag
```
怎么计算Lag？(注意read_uncommitted和read_committed状态下的不同)
```
LAG = HW - current-offset

bin/kafka-consumer-groups.sh可以查看
kafkaOffsetMonitor等监控工具
```
Kafka的那些设计让它有如此高的性能？
```
零拷贝，页缓存，顺序写
```

Kafka有什么优缺点？

还用过什么同质类的其它产品，与Kafka相比有什么优缺点？

为什么选择Kafka?在使用Kafka的过程中遇到过什么困难？怎么解决的？

怎么样才能确保Kafka极大程度上的可靠性？
聊一聊你对Kafka生态的理解
```
confluent全家桶(connect/kafka stream/ksql/center/rest proxy等)，开源监控管理工具kafka-manager,kmanager等

```
