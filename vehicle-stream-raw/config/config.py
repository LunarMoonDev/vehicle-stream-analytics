import datetime
from pydantic import Field
from pydantic_settings import BaseSettings


class AppConfig(BaseSettings):
    """Configuration for the vehicle stream analytics application."""

    kafka_host: str = Field("localhost:19092", description="Kafka host address")
    kafka_event_topic: str = Field("vehicle-event-json", description="Kafka topic for vehicle events")
    kafka_metadata_topic: str = Field("vehicle-metadata-json", description="Kafka topic for vehicle metadata")
    kafka_delay: int = Field(1, description="Delay for Kafka operations in seconds")
    kafka_client_id: str = Field("vehicle-stream-raw", description="Kafka client ID")

    start_date: datetime.date = Field("2025-02-01", description="Start date for simulation in YYYY-MM-DD format")
    end_date: datetime.date = Field("2025-02-02", description="End date for simulation in YYYY-MM-DD format")
    start_time: datetime.time = Field("00:00:00", description="Start time for simulation in HH:MM:SS format")
    end_time: datetime.time = Field("23:59:59", description="End time for simulation in HH:MM:SS format")

    early_exit_probability: float = Field(0.1, description="Probability of early exit from simulation")
    random_seed: int = Field(63, description="Seed for random number generator for reproducibility")
    kafka_delay: bool = Field(True, description="Enable or disable delay in Kafka operations")

    class Config:
        """Configuration for Pydantic settings."""

        env_file = ".env"
        env_file_encoding = "utf-8"
        case_sensitive = False
