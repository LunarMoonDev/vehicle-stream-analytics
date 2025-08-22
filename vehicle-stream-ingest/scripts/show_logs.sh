#!/bin/bash
cd "$(dirname "$0")"

echo "Showing logs for vehicle stream ingest server..."
docker compose -f ../docker-compose.yaml logs --tail=100 --follow json_to_avro