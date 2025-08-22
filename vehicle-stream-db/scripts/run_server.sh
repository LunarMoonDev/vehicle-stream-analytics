#!/bin/bash
cd "$(dirname "$0")"

echo "Starting vehicle stream db server..."
docker compose -f ../docker-compose.yaml up --build -d