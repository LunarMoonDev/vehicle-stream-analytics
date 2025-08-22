package vehicle.stream.processor.detector;

import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vehicle.stream.avro.EnrichedEvent;
import vehicle.stream.avro.SuddenBrakingEvent;
import vehicle.stream.processor.constants.Store;
import vehicle.stream.processor.utils.MapperUtil;

import java.time.Duration;

public class SuddenBrakeDetector implements FixedKeyProcessor<String, EnrichedEvent, SuddenBrakingEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SuddenBrakeDetector.class);
    private final MapperUtil mUtil;

    private FixedKeyProcessorContext<String, SuddenBrakingEvent> context;
    private KeyValueStore<String, EnrichedEvent> store;

    public SuddenBrakeDetector(MapperUtil mUtil) {
        this.mUtil = mUtil;
    }

    @Override
    public void init(FixedKeyProcessorContext<String, SuddenBrakingEvent> context) {
        this.context = context;
        this.store = context.getStateStore(Store.VEHICLE_SPEED_STORE);
    }

    @Override
    public void process(FixedKeyRecord<String, EnrichedEvent> record) {
        EnrichedEvent prev = store.get(record.key());

        logger.debug("Checking current record {} with prev {}", record.value(), prev);
        if( prev != null ){
            Duration duration = Duration.between(prev.getEventTime(), record.value().getEventTime());
            logger.debug("Duration from last event: {}", duration.getSeconds());
            int speedDrop = prev.getSpeed() - record.value().getSpeed();
            logger.debug("Difference in speed change: {}", speedDrop);

            if (duration.getSeconds() <= 2 && speedDrop >= 30) {
                SuddenBrakingEvent brakingEvent = mUtil.createBrakingEvent(record.value(), duration, speedDrop);
                logger.info("Pushing a sudden braking event {}", brakingEvent);
                context.forward(record.withValue(brakingEvent));
            }
        }

        logger.debug("Saving current record in state store: {}", Store.VEHICLE_SPEED_STORE);
        store.put(record.key(), record.value());
    }
}
