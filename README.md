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
4. Bash scripts are created for each sub-projects, just simply use them following the steps describe below

## 🚀 How I run this project

1. **Start Kafka brokers:**
   - Run the main Kafka broker:  
     `vehicle-stream-analytics/scripts/run_server.sh`
   - *(Optional)* Watch logs from broker1 and broker2:  
     `vehicle-stream-analytics/scripts/show_broker1_logs.sh`  
     `vehicle-stream-analytics/scripts/show_broker2_logs.sh`

2. **Set up Schema Registry (`vehicle-stream-registry`):**
   - Run Schema Registry with pre-setup:  
     `vehicle-stream-registry/scripts/run_server.sh`
   - *(Optional)* Watch logs:  
     `vehicle-stream-registry/scripts/show_logs.sh`
   - Register schemas:  
     `vehicle-stream-registry/scripts/post_setup.sh`

3. **Set up Kafka Connect (`vehicle-stream-connect`):**
   - Run Kafka Connect and Minio with pre-setup:  
     `vehicle-stream-connect/scripts/run_server.sh`
   - *(Optional)* Watch logs:  
     `vehicle-stream-connect/scripts/show_connect_logs.sh`  
     `vehicle-stream-connect/scripts/show_minio_logs.sh`
   - Register connectors:  
     `vehicle-stream-connect/scripts/post_setup.sh`

4. **Set up DBT and Postgres (`vehicle-stream-dbt`):**
   - Start Postgres:  
     `vehicle-stream-db/scripts/run_server.sh`
   - *(Optional)* Watch dbt server logs:  
     `vehicle-stream-db/scripts/show_logs.sh`

5. **Start stream services:**
   - **Ingest service (`vehicle-stream-ingest`):**  
     `vehicle-stream-ingest/scripts/run_server.sh`  
     `vehicle-stream-ingest/scripts/show_logs.sh`
   - **Processor service (`vehicle-stream-processor`):**  
     `vehicle-stream-processor/scripts/run_server.sh`  
     `vehicle-stream-ingest/scripts/show_logs.sh`


⚠️ **Note:** 
Above setup is relying on dummy data of jsonl that sits on the minio bucket for inputs. If you want to ingest data continously as a simulation, simply use `vehicle-stream-raw`.
It has scripts similar to other streaming services where you run it via Docker. (`run_server.sh` and etc.)

⚠️ **Another Note:**
For saving files in minio, you can use `aws` console to upload the files and use AWS API for S3 operations - just make sure to set your credentials. For easy access, here's the comamnd i used
to upload my files in minio server

```bash
$ export AWS_PROFILE=minio
$ aws s3api create-bucket --bucket s3://raw
$ aws s3 cp vehicle_events.jsonl s3://raw/data/events/2025_08_22.jsonl
$ aws s3 cp vehicle_metadatas.jsonl s3://raw/data/metadata/2025_08_22.jsonl
```

⚠️ **Another (2x) Note:**
`clean.sh` are all safe delete, so in cae you accidentally deleted the files via this script, backups are available for restoration


## Commands I used for Debugging:

Following are commands that helps with debugging the services:
``` bash
# produce sample kafka event
docker exec kafka bash -c "echo '{\"vehicle_id\":\"v1\",\"driver_id\":\"d1\",\
  \"lat\":37.7749,\"lon\":-122.4194,\"speed\":82,\"event_time\":1690000001000}' \
  | /opt/kafka/bin/kafka-console-producer.sh \
  --broker-list localhost:19092 \
  --topic vehicle-event-json \
  --property parse.key=false \
  --property value.serializer=org.apache.kafka.common.serialization.StringSerializer"

# list topics created
docker exec kafka bash -c "/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:19092 \
  --list"

# connect as consumer to a topic
docker exec kafka bash -c "/opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:19092 \
  --topic vehicle-event-json \
  --from-beginning \
  --property print.key=false \
  --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"

# register a connector in kafka connect
curl -X POST http://CONNECT_HOST:CONNECT_PORT/connectors \
  -H "Content-Type: application/json" \
  -d @connector-config.json

# update a connector
curl -X PUT http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}/config \
  -H "Content-Type: application/json" \
  -d @updated-config.json

# patches a change to a connector, make sure to change the payload to just the changes
curl -X PATCH http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}/config \
  -H "Content-Type: application/json" \
  -d @patch-config.json

# restarts a connector
curl -X POST http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}/restart

# deletes a certain connector
curl -X DELETE http://CONNECT_HOST:CONNECT_PORT/connectors/{connector_name}
```
