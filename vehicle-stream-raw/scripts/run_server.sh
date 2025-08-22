#!/bin/bash
cd "$(dirname "$0")"

echo "Starting vehicle stream server..."
docker compose -f ../docker-compose.yaml up --build -d