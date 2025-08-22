#!/bin/bash
cd "$(dirname "$0")"

echo "Showing logs for Minio server..."
docker compose -f ../docker-compose.yaml logs --tail=100 --follow minio