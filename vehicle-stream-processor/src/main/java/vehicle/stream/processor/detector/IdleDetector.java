package vehicle.stream.processor.detector;

import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vehicle.stream.avro.EnrichedEvent;
import vehicle.stream.avro.IdleEvent;
import vehicle.stream.processor.constants.Store;
import vehicle.stream.processor.utils.MapperUtil;

import java.time.Duration;

public class IdleDetector implements FixedKeyProcessor<String, EnrichedEvent, IdleEvent> {
    private static final Logger logger = LoggerFactory.getLogger(IdleDetector.class);
    private final MapperUtil mUtil;

    private FixedKeyProcessorContext<String, IdleEvent> context;
    private KeyValueStore<String, EnrichedEvent> store;

    public IdleDetector(MapperUtil mUtil) {
        this.mUtil = mUtil;
    }

    @Override
    public void init(FixedKeyProcessorContext<String, IdleEvent> context) {
        this.context = context;
        this.store = context.getStateStore(Store.VEHICLE_IDLE_STORE);
    }

    @Override
    public void process(FixedKeyRecord<String, EnrichedEvent> record) {
        EnrichedEvent prev = store.get(record.key());

        logger.debug("Checking current record {} with prev {}", record.value(), prev);
        if( record.value().getSpeed() == 0 ) {
            if( prev != null ) {
                Duration duration = Duration.between(prev.getEventTime(), record.value().getEventTime());
                logger.debug("Duration from last event: {}", duration.getSeconds());

                if(duration.toMinutes() >= 5) {
                    IdleEvent idleEvent = mUtil.createIdleEvent(record.value(), duration);
                    logger.info("Pushing an idle event {}", idleEvent);
                    context.forward(record.withValue(idleEvent));
                }
            } else {
                logger.debug("Saving current record in state store: {}", Store.VEHICLE_IDLE_STORE);
                store.put(record.key(), record.value());
            }
        } else {
            logger.debug("Deleting current record in state store: {}", Store.VEHICLE_IDLE_STORE);
            store.delete(record.key());
        }
    }
}
