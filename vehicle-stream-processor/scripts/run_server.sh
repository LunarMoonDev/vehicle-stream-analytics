#!/bin/bash
cd "$(dirname "$0")"

echo "Starting vehicle stream processor..."
docker compose -f ../docker-compose.yaml up --build -d