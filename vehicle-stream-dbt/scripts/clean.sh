#!/bin/bash
set -e

POSTGRES_VOLUME_PATH=".data/postgres"

if [[ -d "$POSTGRES_VOLUME_PATH" ]]; then
    echo "Removing DB volume at $POSTGRES_VOLUME_PATH"
    mv "$POSTGRES_VOLUME_PATH" "$POSTGRES_VOLUME_PATH".bak-$(date +%Y%m%d%H%M%S)
else
    echo "DB volume at $POSTGRES_VOLUME_PATH does not exist, skipping removal."
fi

echo "Cleanup completed successfully."
mkdir -p "$POSTGRES_VOLUME_PATH"
echo "Recreated DB volumes."
exit 0