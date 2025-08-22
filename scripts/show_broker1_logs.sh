#!/bin/bash
cd "$(dirname "$0")"

echo "Showing logs for Kafka broker..."
docker compose -f ../docker-compose.yaml logs --tail=100 --follow kafka