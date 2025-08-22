#!/bin/bash
cd "$(dirname "$0")"

REGISTRY_HOST="${REGISTRY_HOST:-localhost}"
REGISTRY_CONTAINER="${REGISTRY_CONTAINER:-schema_registry}"
REGISTRY_PORT="${REGISTRY_PORT:-8081}"

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No color

if ! docker ps --format '{{.Names}}' | grep -q "^${REGISTRY_CONTAINER}$"; then
    echo -e "${RED}❌ Error: Registry container '${REGISTRY_CONTAINER}' is not running.${NC}"
    exit 1
fi

declare -A TOPICS=(
    ["vehicle-event-topic"]="../avro/vehicle_event.avsc"
    ["vehicle-metadata-topic"]="../avro/vehicle_metadata.avsc"
    ["vehicle-speeding-topic"]="../avro/enriched_vehicle.avsc"
    ["vehicle-braking-topic"]="../avro/braking_vehicle.avsc"
    ["vehicle-idle-topic"]="../avro/idle_vehicle.avsc"
    ["vehicle-enriched-topic"]="../avro/enriched_vehicle.avsc"
)

for TOPIC in "${!TOPICS[@]}"; do
    schema_file="${TOPICS[$TOPIC]}"
    SCHEMA=$(jq -Rs . < "$schema_file")

    echo "Registering schema for topic '$TOPIC' ..."

    curl -X POST http://"$REGISTRY_HOST":"$REGISTRY_PORT"/subjects/"${TOPIC}-value"/versions \
        -H "Content-Type: application/vnd.schemaregistry.v1+json" \
        -d "{\"schema\": $SCHEMA}" | jq
done

echo -e "${GREEN}✅ Done registering schemas to registry '$REGISTRY_CONTAINER'${NC}"