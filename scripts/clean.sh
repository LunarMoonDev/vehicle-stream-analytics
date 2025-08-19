#!/bin/bash
set -e

KAFKA_VOLUME_PATH=".data/kafka"
KAFKA1_VOLUME_PATH=".data/kafka1"

if [[ -d "$KAFKA_VOLUME_PATH" ]]; then
    echo "Removing Kafka volume at $KAFKA_VOLUME_PATH"
    mv "$KAFKA_VOLUME_PATH" "$KAFKA_VOLUME_PATH".bak-$(date +%Y%m%d%H%M%S)
else
    echo "Kafka volume at $KAFKA_VOLUME_PATH does not exist, skipping removal."
fi

if [[ -d "$KAFKA1_VOLUME_PATH" ]]; then
    echo "Removing Kafka1 volume at $KAFKA1_VOLUME_PATH"
    mv "$KAFKA1_VOLUME_PATH" "$KAFKA1_VOLUME_PATH".bak-$(date +%Y%m%d%H%M%S)
else
    echo "Kafka1 volume at $KAFKA1_VOLUME_PATH does not exist, skipping removal."
fi

echo "Cleanup completed successfully."
mkdir -p "$KAFKA_VOLUME_PATH" "$KAFKA1_VOLUME_PATH"
echo "Recreated Kafka volumes."
exit 0