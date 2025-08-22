import random
from datetime import timedelta


def progress_time_quickly() -> timedelta:
    """Return a timedelta that represents a quick passage of time.

    Returns:
        timedelta: object that passes time quickly
    """

    return timedelta(seconds=random.randint(1, 30))

def progress_time_slowly() -> timedelta:
    """Return a timedelta that represents a slow passage of time.

    Returns:
        timedelta: object that passes time slowly
    """

    return timedelta(seconds=random.randint(60, 300))