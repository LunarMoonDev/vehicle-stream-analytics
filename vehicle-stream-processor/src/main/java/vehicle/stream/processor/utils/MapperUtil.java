package vehicle.stream.processor.utils;

import org.springframework.stereotype.Component;
import vehicle.stream.avro.*;

import java.time.Duration;

@Component
public class MapperUtil {

    public EnrichedEvent createEnrichedEvent(VehicleEvent event, VehicleMetadata metadata) {
        EnrichedEvent enrichedEvent = new EnrichedEvent();
        enrichedEvent.setDriverId(event.getDriverId());
        enrichedEvent.setVehicleId(event.getVehicleId());
        enrichedEvent.setEventTime(event.getEventTime());
        enrichedEvent.setLat(event.getLat());
        enrichedEvent.setLon(event.getLon());
        enrichedEvent.setSpeed(event.getSpeed());
        enrichedEvent.setModel(metadata.getModel());
        enrichedEvent.setDriverName(metadata.getDriverName());
        enrichedEvent.setMaxSpeed(metadata.getMaxSpeed());
        enrichedEvent.setType(metadata.getType());

        return enrichedEvent;
    }

    public SuddenBrakingEvent createBrakingEvent(EnrichedEvent event, Duration duration, int speedDrop) {
        SuddenBrakingEvent brakingEvent = new SuddenBrakingEvent();
        brakingEvent.setDriverId(event.getDriverId());
        brakingEvent.setVehicleId(event.getVehicleId());
        brakingEvent.setEventTime(event.getEventTime());
        brakingEvent.setLat(event.getLat());
        brakingEvent.setLon(event.getLon());
        brakingEvent.setSpeed(event.getSpeed());
        brakingEvent.setModel(event.getModel());
        brakingEvent.setDriverName(event.getDriverName());
        brakingEvent.setMaxSpeed(event.getMaxSpeed());
        brakingEvent.setType(event.getType());
        brakingEvent.setBrakingDurationSec(duration.getSeconds());
        brakingEvent.setSpeedDrop(speedDrop);

        return brakingEvent;
    }

    public IdleEvent createIdleEvent(EnrichedEvent event, Duration duration) {
        IdleEvent idleEvent = new IdleEvent();
        idleEvent.setDriverId(event.getDriverId());
        idleEvent.setVehicleId(event.getVehicleId());
        idleEvent.setEventTime(event.getEventTime());
        idleEvent.setLat(event.getLat());
        idleEvent.setLon(event.getLon());
        idleEvent.setSpeed(event.getSpeed());
        idleEvent.setModel(event.getModel());
        idleEvent.setDriverName(event.getDriverName());
        idleEvent.setMaxSpeed(event.getMaxSpeed());
        idleEvent.setType(event.getType());
        idleEvent.setIdleDurationSec(duration.getSeconds());

        return idleEvent;
    }
}
