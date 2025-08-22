#!/bin/bash
cd "$(dirname "$0")"
set -e

MINIO_VOLUME_PATH="../.data/minio"

if [[ -d "$MINIO_VOLUME_PATH" ]]; then
    echo "Removing Minio volume at $MINIO_VOLUME_PATH"
    mv "$MINIO_VOLUME_PATH" "$MINIO_VOLUME_PATH".bak-$(date +%Y%m%d%H%M%S)
else
    echo "Minio volume at $MINIO_VOLUME_PATH does not exist, skipping removal."
fi

echo "Cleanup completed successfully."
mkdir -p "$MINIO_VOLUME_PATH"
echo "Recreated Minio volume."
exit 0