import logging
import datetime
import random
import signal
import threading

from datetime import timedelta, datetime

import colorlog

from service.KafkaService import KafkaService
from service.SimulationService import SimulationService
from utils.TimeUtil import progress_time_slowly

from config import conf

# configuration stuff
handler = colorlog.StreamHandler()
handler.setFormatter(
    colorlog.ColoredFormatter(
        "%(log_color)s%(asctime)s [%(levelname)s] %(name)s: %(message)s",
        log_colors={
            "DEBUG": "cyan",
            "INFO": "green",
            "WARNING": "yellow",
            "ERROR": "red",
            "CRITICAL": "bold_red",
        },
    )
)
logging.basicConfig(level=logging.INFO, handlers=[handler])
logger = logging.getLogger(__name__)

kafka_service = KafkaService(delay=conf.kafka_delay)
simulate_service = SimulationService(kafka_service)

random.seed(conf.random_seed)


def simulate_vehicle_metadata(kakfa_service: KafkaService, vehicle_metadata: dict, stop_event) -> None:
    """Simulate vehicle metadata for a given vehicle.

    Args:
        vehicle_metadata (dict): metadata of the vehicle and driver

    Returns:
        dict: simulated metadata for the vehicle
    """

    if not stop_event.is_set():
        kakfa_service.send_event(vehicle_metadata, conf.kafka_metadata_topic)


def simulate_vehicle(sim_service: SimulationService, vehicle_metadata: dict, current_date: dict, stop_event) -> None:
    """Simulate a vehicle's events for a given date.

    Args:
        vehicle_metadata (dict): metadata of the vehicle and driver
        current_date (dict): current date for simulation
    """
    even_heights = [0.25, 0.2, 0.2, 0.35]

    current_time = datetime.combine(current_date, conf.start_time)
    end_time = datetime.combine(current_date, conf.end_time)
    speed = 0

    while current_time <= end_time and not stop_event.is_set():
        event_type = random.choices(["speed", "idle", "brake", "normal"], weights=even_heights, k=1)[0]

        if event_type == "speed":
            speed, current_time = sim_service.sim_speeding(vehicle_metadata, current_time, speed)
        elif event_type == "idle":
            speed, current_time = sim_service.sim_idle(vehicle_metadata, current_time, speed)
        elif event_type == "brake":
            speed, current_time = sim_service.sim_braking(vehicle_metadata, current_time, speed)
        else:
            speed, current_time = sim_service.sim_normal(vehicle_metadata, current_time, speed)

        if random.random() < conf.early_exit_probability:
            logging.info(
                "Early exit for vehicle %s at %s",
                vehicle_metadata["vehicle_id"],
                current_time,
            )
            break

        current_time += progress_time_slowly()


def main() -> None:
    """Simulate vehicle events over a range of dates."""
    vehicles = [
        {"vehicle_id": "v1", "type": "Truck", "model": "T-2023", "max_speed": 80},
        {"vehicle_id": "v2", "type": "Car", "model": "C-2023", "max_speed": 50},
        {"vehicle_id": "v3", "type": "Car", "model": "C-2021", "max_speed": 90},
    ]

    drivers = [
        {"driver_id": "d1", "driver_name": "Alice"},
        {"driver_id": "d2", "driver_name": "Bob"},
        {"driver_id": "d3", "driver_name": "Charlie"},
        {"driver_id": "d4", "driver_name": "Diana"},
        {"driver_id": "d5", "driver_name": "Ethan"},
        {"driver_id": "d6", "driver_name": "Fiona"},
        {"driver_id": "d7", "driver_name": "George"},
    ]

    stop_event = threading.Event()
    current_date = conf.start_date
    try:
        while current_date <= conf.end_date:
            logging.info("Simulating current date: %s", current_date)

            num_of_vehicles = random.randint(1, len(vehicles))
            selected_vehicles = random.sample(vehicles, num_of_vehicles)
            selected_drivers = random.sample(drivers, num_of_vehicles)
            vehicle_metadata = [{**vehicle, **driver} for vehicle, driver in zip(selected_vehicles, selected_drivers)]

            threads = []
            for vm in vehicle_metadata:
                t_event = threading.Thread(
                    target=simulate_vehicle, args=(simulate_service, vm, current_date, stop_event)
                )
                t_metadata = threading.Thread(target=simulate_vehicle_metadata, args=(kafka_service, vm, stop_event))
                threads.extend([t_event, t_metadata])
                t_event.start()
                t_metadata.start()

            for t in threads:
                t.join(timeout=5)

            current_date += timedelta(days=1)
    except KeyboardInterrupt:
        logging.error("Simulation interrupted by user.")
        stop_event.set()


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        signal.signal(signal.SIGINT, signal.SIG_DFL)
