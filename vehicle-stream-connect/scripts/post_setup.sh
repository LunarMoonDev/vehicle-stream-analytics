#!/bin/bash

set -euo pipefail

cd "$(dirname "$0")"

# Global variables
KAFKA_CONTAINER="${KAFKA_CONTAINER:-kafka_connect}"
PARENT_DIR=$(realpath "$(pwd)/../connectors/")

GREEN='\033[0;32m'
RED='\033[0;31m'
ORANGE='\033[0;33m'
NC='\033[0m' # No color

# Check container exists
# REMOVE: for production deployment
if ! docker ps --format '{{.Names}}' | grep -q "^${KAFKA_CONTAINER}$"; then
    echo -e "${RED}❌ Error: Kafka container '${KAFKA_CONTAINER}' is not running.${NC}"
    exit 1
fi

declare -A CONNECTORS=(
    ["vehicle-event-s3-source"]="s3-source-events.json"
    ["vehicle-metadata-s3-source"]="s3-source-metadatas.json"
    ["db-sink-speeding-events"]="db-sink-speeding-events.json"
    ["db-sink-braking-events"]="db-sink-braking-events.json"
    ["db-sink-idle-events"]="db-sink-idle-events.json"
    ["db-sink-enriched-events"]="db-sink-enriched-events.json"
    ["db-sink-avro-events"]="db-sink-avro-events.json"
    ["db-sink-avro-metadatas"]="db-sink-avro-metadatas.json"
)

for CONNECTOR in "${!CONNECTORS[@]}"; do
    connector_file="${CONNECTORS[$CONNECTOR]}"

    echo "Checking if connector '$CONNECTOR' already exists ..."
    response_code=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8083/connectors/"$CONNECTOR")
    
    echo "Registering connector '$CONNECTOR' with configuration file '$connector_file' ..."
    if [ "$response_code" -eq 404 ]; then
        curl -X POST http://localhost:8083/connectors \
            -H "Content-Type: application/json" \
            -d @"$PARENT_DIR/$connector_file" | jq .
            
        echo -e "${GREEN}✅ Connector '$CONNECTOR' registered successfully.${NC}"
    else
        echo -e "${GREEN}⚠️ Connector '$CONNECTOR' already exists. Skipping registration.${NC}"
    fi
done