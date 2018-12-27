 #./bin/kafka-topics --create --topic  -topic-getlink --zookeeper localhost:2181 --partitions 1 --replication-factor 1

./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic getlink
----------------
checkout--------------
123456789
1271
17129
175414
2158848
257
3699
59
780005163
987654321

 ./bin/kafka-topics --create --topic  -topic-returnlink --zookeeper localhost:2181 --partitions 1 --replication-factor 1


./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic getlink

./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic travel-sample-topic  --from-beginning