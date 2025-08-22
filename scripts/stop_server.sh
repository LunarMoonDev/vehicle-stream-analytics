#!/bin/bash
cd "$(dirname "$0")"

echo "Stopping Kafka servers..."
docker compose -f ../docker-compose.yaml down