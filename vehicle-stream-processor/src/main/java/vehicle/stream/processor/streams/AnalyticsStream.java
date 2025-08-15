package vehicle.stream.processor.streams;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vehicle.stream.avro.EnrichedEvent;
import vehicle.stream.avro.IdleEvent;
import vehicle.stream.avro.SuddenBrakingEvent;
import vehicle.stream.processor.config.PropertyConfig;
import vehicle.stream.processor.constants.Store;
import vehicle.stream.processor.detector.IdleDetector;
import vehicle.stream.processor.detector.SuddenBrakeDetector;
import vehicle.stream.processor.utils.MapperUtil;

@Component
public class AnalyticsStream {
    private final static Logger logger = LoggerFactory.getLogger(AnalyticsStream.class);

    private final Serde<String> stringSerde;
    private final SpecificAvroSerde<EnrichedEvent> enrichedSerde;
    private final SpecificAvroSerde<SuddenBrakingEvent> brakingSerde;
    private final SpecificAvroSerde<IdleEvent> idleSerde;

    @Autowired
    private PropertyConfig propConfig;

    @Autowired
    private MapperUtil mapperUtil;

    public AnalyticsStream(
            @Qualifier("AvroEnrichedEventSerde") SpecificAvroSerde<EnrichedEvent> enrichedSerde,
            @Qualifier("AvroSuddenBrakingEventSerde") SpecificAvroSerde<SuddenBrakingEvent> brakingSerde,
            @Qualifier("AvroIdleEventSerde") SpecificAvroSerde<IdleEvent> idleSerde) {
        this.stringSerde = Serdes.String();
        this.enrichedSerde = enrichedSerde;
        this.brakingSerde = brakingSerde;
        this.idleSerde = idleSerde;
    }


    @Autowired
    public void speedingStream(StreamsBuilder builder) {
        KStream<String, EnrichedEvent> enrichedStream = builder.stream(
                propConfig.getStreamEnrichedTopic(),
                Consumed.with(stringSerde, enrichedSerde)
        );

        KStream<String, EnrichedEvent> speedingStream = enrichedStream
                .filter((vehicleId, event) -> event.getSpeed() > event.getMaxSpeed())
                .peek((key, value) -> logger.info("Speeding event: key={}, value={}", key, value));

        speedingStream.to(propConfig.getStreamSpeedingTopic(), Produced.with(stringSerde, enrichedSerde));
    }

    @Autowired
    public void brakingStream(StreamsBuilder builder) {
        StoreBuilder<KeyValueStore<String, EnrichedEvent>> speedBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(Store.VEHICLE_SPEED_STORE),
                stringSerde,
                enrichedSerde
        );
        builder.addStateStore(speedBuilder);

        KStream<String, EnrichedEvent> enrichedStream = builder.stream(
                propConfig.getStreamEnrichedTopic(),
                Consumed.with(stringSerde, enrichedSerde)
        );

        KStream<String, SuddenBrakingEvent> suddenBrakeStream = enrichedStream
                .processValues(() -> new SuddenBrakeDetector(mapperUtil), Store.VEHICLE_SPEED_STORE)
                .peek((key, value) -> logger.info("Sudden Braking EnrichedEvent: key={}, value={}", key, value));
        suddenBrakeStream.to(propConfig.getStreamBrakingTopic(), Produced.with(stringSerde, brakingSerde));
    }

    @Autowired
    public void idleStream(StreamsBuilder builder) {
        StoreBuilder<KeyValueStore<String, EnrichedEvent>> idleBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(Store.VEHICLE_IDLE_STORE),
                stringSerde,
                enrichedSerde
        );
        builder.addStateStore(idleBuilder);

        KStream<String, EnrichedEvent> enrichedStream = builder.stream(
                propConfig.getStreamEnrichedTopic(),
                Consumed.with(stringSerde, enrichedSerde)
        );

        KStream<String, IdleEvent> idleStream = enrichedStream
                .processValues(() -> new IdleDetector(mapperUtil), Store.VEHICLE_IDLE_STORE)
                .peek((key, value) -> logger.info("Idle EnrichedEvent: key={}, value={}", key, value));
        idleStream.to(propConfig.getStreamIdleTopic(), Produced.with(stringSerde, idleSerde));
    }
}
