#!/bin/bash
cd "$(dirname "$0")"

echo "Showing logs for Schema Registry server..."
docker compose -f ../docker-compose.yaml logs --tail=100 --follow schema_registry