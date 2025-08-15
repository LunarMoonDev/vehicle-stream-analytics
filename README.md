# vehicle-stream-analytics
This is a project demonstrating my capability in implementing streaming microservices



Need to install jq


``` bash
docker exec kafka bash -c "echo '{\"vehicle_id\":\"v1\",\"driver_id\":\"d1\",\"lat\":37.7749,\"lon\":-122.4194,\"speed\":82,\"event_time\":1690000001000}' | /opt/kafka/bin/kafka-console-producer.sh --broker-list localhost:19092 --topic vehicle-event-json --property parse.key=false --property value.serializer=org.apache.kafka.common.serialization.StringSerializer"

docker exec kafka bash -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:19092 --list"


docker exec kafka bash -c "/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic vehicle-event-json --from-beginning --property print.key=false --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"


docker exec kafka bash -c "/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:19192 --topic vehicle-event-topic --from-beginning --property print.key=false --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"

curl -X POST http://CONNECT_HOST:CONNECT_PORT/connectors -H "Content-Type: application/json" -d @connector-config.json


curl -X PUT http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}/config -H "Content-Type: application/json" -d @updated-config.json


curl -X PATCH http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}/config -H "Content-Type: application/json" -d @patch-config.json


curl -X POST http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}/restart


curl -X DELETE http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}
```
