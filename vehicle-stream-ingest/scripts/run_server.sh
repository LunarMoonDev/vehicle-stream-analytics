#!/bin/bash
cd "$(dirname "$0")"

echo "Starting vehicle stream ingest..."
docker compose -f ../docker-compose.yaml up --build -d