#!/bin/bash
cd "$(dirname "$0")"

echo "Stopping Kafka Connect and Minio server..."
docker compose -f ../docker-compose.yaml down