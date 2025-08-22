import random
from datetime import datetime

def create_event(vehicle_metadata: dict, current_datetime: datetime, speed: int) -> dict:
    """Generate an event for a vehicle.
    Args:
        vehicle_metadata (dict): metadata of the vehicle and driver
        current_datetime (datetime): current date and time for simulation
        speed (int): speed of the vehicle
    Returns:
        dict: event data for the vehicle
    """

    return {
        "vehicle_id": vehicle_metadata["vehicle_id"],
        "driver_id": vehicle_metadata["driver_id"],
        "lat": random.uniform(-90, 90),
        "lon": random.uniform(-180, 180),
        "speed": int(round(speed)),
        "event_time": int(current_datetime.timestamp() * 1000),
    }