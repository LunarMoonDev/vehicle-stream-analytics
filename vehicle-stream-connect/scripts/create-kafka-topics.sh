#!/bin/bash

set -euo pipefail

# Global variables
KAFKA_CONTAINER="${KAFKA_CONTAINER:-kafka}"
BROKER_LIST="${BROKER_LIST:-kafka:9092,kafka1:9192}"

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No color

# Check container exists
# REMOVE: for production deployment
if ! docker ps --format '{{.Names}}' | grep -q "^${KAFKA_CONTAINER}$"; then
    echo -e "${RED}❌ Error: Kafka container '${KAFKA_CONTAINER}' is not running.${NC}"
    exit 1
fi

declare -A TOPICS=(
    ["docker-connect-configs"]="1:2:compact"
    ["docker-connect-offset"]="1:2:compact"
    ["docker-connect-status"]="1:2:compact"
    ["vehicle-event-json"]="1:2:delete"
    ["vehicle-metadata-json"]="1:2:delete"
)

for TOPIC in "${!TOPICS[@]}"; do
    IFS=":" read PARTITIONS REPLICATION CLEANUP <<< "${TOPICS[$TOPIC]}"

    echo "Creating topic '$TOPIC' with partition ($PARTITIONS), replication ($REPLICATION), and cleanup policy ($CLEANUP) ..."

    docker exec "$KAFKA_CONTAINER" bash -c "/opt/kafka/bin/kafka-topics.sh \
        --create \
        --bootstrap-server "$BROKER_LIST" \
        --topic "$TOPIC" \
        --replication-factor "$REPLICATION" \
        --partitions "$PARTITIONS" \
        --config cleanup.policy="$CLEANUP" \
        --if-not-exists"
done

echo -e "${GREEN}✅ Done creating topics inside container '$KAFKA_CONTAINER'${NC}"
