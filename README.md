# 🚕 Vehicle Stream Analytics 🚗

Vehicle Stream Analytics is a **toy project** for experimenting with ingesting, processing, and analyzing vehicle event data using Apache Kafka, Kafka Connect, and related technologies. It demonstrates how to stream vehicle events from sources such as S3 (MinIO) into Kafka topics, enabling scalable analytics and integration with downstream systems in a local development environment.

## Key Components

- **Kafka (KRaft mode):** Handles event streaming and topic management, configured for high availability.
- **Kafka Connect:** Integrates external data sources (e.g., S3, JDBC) with Kafka using connectors.
- **Schema Registry:** Manages Avro/JSON schemas for Kafka topics.
- **MinIO:** Provides S3-compatible object storage for raw vehicle event data.
- **Docker Compose:** Orchestrates all services for local development and testing.
- **Makefile:** Provides convenient commands for building, running, and managing the project.

## ❗❗❗ Requirements

- **Docker & Docker Compose:** For running the services locally.
- **jq:** Some scripts require [`jq`](https://stedolan.github.io/jq/) for processing and prettifying JSON responses.  
  Install with your package manager or download from the official site.

## Features

- Stream vehicle events from S3 buckets into Kafka topics.
- Transform and route events using Kafka Connect.
- Schema management for consistent data serialization.
- Easily extensible with additional connectors and sinks.

## Usage

1. Clone the repository.
2. Use Docker Compose to start all services.
3. Configure connectors to ingest and process vehicle event data.
4. Use the provided Makefile for common tasks—run `make` or `make help` to see available commands.

## 🚀 How I run this project

1. **Start Kafka brokers:**
   - Run the main Kafka broker:  
     `make run_servers`
   - *(Optional)* Watch logs from broker1 and broker2:  
     `make show_server1_logs`  
     `make show_server2_logs`

2. **Set up Schema Registry (`vehicle-stream-registry`):**
   - Pre-setup for Kafka topic creation:  
     `make pre_setup`
   - Start Schema Registry:  
     `make run_servers`
   - *(Optional)* Watch logs:  
     `make show_logs`
   - Register schemas:  
     `make post_setup`

3. **Set up Kafka Connect (`vehicle-stream-connect`):**
   - Pre-setup for Kafka topic creation:  
     `make pre_setup`
   - Start Kafka Connect with MinIO:  
     `make run_servers`
   - *(Optional)* Watch logs:  
     `make show_connect_logs`  
     `make show_minio_logs`
   - Register connectors:  
     `make post_setup`

4. **Set up DBT and Postgres (`vehicle-stream-dbt`):**
   - Start Postgres:  
     `make run_servers`
   - *(Optional)* Watch dbt server logs:  
     `make show_logs`

5. **Start stream services:**
   - **Ingest service (`vehicle-stream-ingest`):**  
     `make run_servers`  
     `make show_logs`
   - **Processor service (`vehicle-stream-processor`):**  
     `make run_servers`  
     `make show_logs`


## Note:

Following are commands that helps with debugging the services:
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
