#!/bin/bash
cd "$(dirname "$0")"

echo "Showing logs for vehicle stream raw server..."
docker compose -f ../docker-compose.yaml logs --tail=100 --follow raw_stream