import logging
import json
import time

from confluent_kafka import Producer
from config import conf

# Configure logging
logger = logging.getLogger(__name__)

class KafkaService:
    """Service class for managing Kafka operations.
    """

    def __init__(self, delay: bool = True) -> None:
        """Initializes the producer instance"""
        self.producer = Producer(
            {
                "bootstrap.servers": conf.kafka_host,
                "client.id": conf.kafka_client_id
            }
        )
        self.delay = delay
    
    def send_event(self, event: dict, topic: str) -> None:
        """Sends an event to the specified Kafka topic.

        Args:
            topic (str): topic for the event
            event (dict): event itself
        """

        try:
            if self.delay:
                time.sleep(conf.kafka_delay)

            logger.info("Sending event to Kafka topic %s: %s", topic, event)
            self.producer.produce(
                topic=topic,
                value=json.dumps(event).encode("utf-8"),
                key=event.get("vehicle_id", None),
                on_delivery=lambda err, msg: logging.error("Error sending message: %s", err) if err else None,
            )

            self.producer.flush()
            logger.info("Event sent successfully to Kafka topic %s", topic)
        except Exception as e:
            logger.error("Failed to send event to Kafka: %s", e)
