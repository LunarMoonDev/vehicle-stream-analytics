package vehicle.stream.processor.streams;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vehicle.stream.avro.EnrichedEvent;
import vehicle.stream.avro.VehicleEvent;
import vehicle.stream.avro.VehicleMetadata;
import vehicle.stream.processor.config.PropertyConfig;
import vehicle.stream.processor.utils.MapperUtil;

@Component
public class EnrichStream {
    private final static Logger logger = LoggerFactory.getLogger(EnrichStream.class);

    private final Serde<String> stringSerde;
    private final SpecificAvroSerde<VehicleEvent> vehicleEventSerde;
    private final SpecificAvroSerde<VehicleMetadata> vehicleMetadataSerde;
    private final SpecificAvroSerde<EnrichedEvent> enrichedSerde;

    @Autowired
    private PropertyConfig propConfig;

    @Autowired
    private MapperUtil mapUtil;

    public EnrichStream(
            @Qualifier("AvroVehicleEventSerde") SpecificAvroSerde<VehicleEvent> vehicleEventSerde,
            @Qualifier("AvroVehicleMetadataSerde") SpecificAvroSerde<VehicleMetadata> vehicleMetadataSerde,
            @Qualifier("AvroEnrichedEventSerde") SpecificAvroSerde<EnrichedEvent> enrichedSerde) {
        this.stringSerde = Serdes.String();
        this.vehicleEventSerde = vehicleEventSerde;
        this.vehicleMetadataSerde = vehicleMetadataSerde;
        this.enrichedSerde = enrichedSerde;
    }

    @Autowired
    public void enrichedEventStream(StreamsBuilder builder) {
        KStream<String, VehicleEvent> eventKStream = builder.stream(
                propConfig.getEventAvroTopic(),
                Consumed.with(stringSerde, vehicleEventSerde));

        KTable<String, VehicleMetadata> metadataTable = builder.table(
                propConfig.getMetadataAvroTopic(),
                Consumed.with(stringSerde, vehicleMetadataSerde));

        KStream<String, EnrichedEvent> enrichedStream = eventKStream
                .join(metadataTable, (event, metadata) -> mapUtil.createEnrichedEvent(event, metadata))
                .peek((key, value) -> logger.info("Created EnrichedEvent: key={}, value={}", key, value));

        enrichedStream.to(
                propConfig.getStreamEnrichedTopic(),
                Produced.with(stringSerde, enrichedSerde));
    }
}
