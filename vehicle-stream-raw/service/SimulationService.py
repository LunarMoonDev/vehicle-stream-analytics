import random
import logging
from datetime import datetime, timedelta
from service.KafkaService import KafkaService

from utils.MapperUtil import create_event
from utils.TimeUtil import progress_time_quickly
from config import conf

logger = logging.getLogger(__name__)


class SimulationService:
    """Service class for simulating vehicle events."""

    def __init__(self, kafka_service: KafkaService) -> None:
        """Initializes the simulation service

        Args:
            kafka_service (KafkaService): service for sending events to Kafka
        """

        self.kafka_service = kafka_service

    def sim_speeding(self, vehicle_metadata: dict, current_time: datetime, speed: int) -> tuple:
        """ "Simulate a speeding event for a vehicle.

        Args:
            vehicle_metadata (dict): metadata of the vehicle and driver
            current_datetime (datetime): current date and time for simulation
            speed (int): speed of the vehicle

        Returns:
            dict: event data for the speeding event
        """

        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        current_time += progress_time_quickly()
        speed += random.randint(1, abs(speed - vehicle_metadata["max_speed"]))

        current_time += progress_time_quickly()
        speed = random.randint(vehicle_metadata["max_speed"] + 1, vehicle_metadata["max_speed"] + 20)

        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        logger.info(
            "Vehicle %s is speeding at %s km/h at %s",
            vehicle_metadata["vehicle_id"],
            speed,
            current_time,
        )
        return speed, current_time

    def sim_idle(self, vehicle_metadata: dict, current_time: datetime, speed: int) -> tuple:
        """Simulate a idle event for a vehicle.

        Args:
            vehicle_metadata (dict): metadata of the vehicle and driver
            current_datetime (datetime): current date and time for simulation
            speed (int): speed of the vehicle
        Returns:
            tuple: updated speed and current time after idle
        """

        speed /= 2
        current_time += progress_time_quickly()
        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        speed /= 2
        current_time += progress_time_quickly()
        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        speed = 0
        self.kafka_service.send_event(
            create_event(vehicle_metadata, current_time + timedelta(seconds=100), speed), conf.kafka_event_topic
        )
        self.kafka_service.send_event(
            create_event(vehicle_metadata, current_time + timedelta(seconds=300), speed), conf.kafka_event_topic
        )

        current_time += timedelta(seconds=500)
        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        logger.info(
            "Vehicle %s is idle at %s km/h at %s",
            vehicle_metadata["vehicle_id"],
            speed,
            current_time,
        )
        return speed, current_time

    def sim_normal(self, vehicle_metadata: dict, current_time: datetime, speed: int) -> tuple:
        """Simulate a normal driving event for a vehicle.

        Args:
            vehicle_metadata (dict): metadata of the vehicle and driver
            current_datetime (datetime): current date and time for simulation
            speed (int): speed of the vehicle

        Returns:
            tuple: updated speed and current time after normal driving
        """

        # simulate normal driving event
        current_time += progress_time_quickly()
        speed = random.randint(1, vehicle_metadata["max_speed"] / 2)
        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        logger.info(
            "Vehicle %s is driving normally at %s km/h at %s",
            vehicle_metadata["vehicle_id"],
            speed,
            current_time,
        )
        return speed, current_time

    def sim_braking(self, vehicle_metadata: dict, current_time: datetime, speed: int) -> tuple:
        """Simulate a braking event for a vehicle.

        Args:
            vehicle_metadata (dict): metadata of the vehicle and driver
            current_datetime (datetime): current date and time for simulation
            speed (int): speed of the vehicle

        Returns:
            tuple: updated speed and current time after braking
        """

        # simulate brake event
        current_time += progress_time_quickly()
        speed = vehicle_metadata["max_speed"] - 1
        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        current_time += timedelta(seconds=random.randint(1, 2))
        speed -= 30
        self.kafka_service.send_event(create_event(vehicle_metadata, current_time, speed), conf.kafka_event_topic)

        logger.info(
            "Vehicle %s is braking at %s km/h at %s",
            vehicle_metadata["vehicle_id"],
            speed,
            current_time,
        )
        return speed, current_time
