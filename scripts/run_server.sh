#!/bin/bash
cd "$(dirname "$0")"

echo "Starting Kafka servers..."
docker compose -f ../docker-compose.yaml up --build -d