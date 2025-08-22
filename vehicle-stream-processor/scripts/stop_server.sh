#!/bin/bash
cd "$(dirname "$0")"

echo "Stopping vehicle stream processor server..."
docker compose -f ../docker-compose.yaml down