package vehicle.stream.ingest.streams;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vehicle.stream.avro.VehicleEvent;
import vehicle.stream.avro.VehicleMetadata;
import vehicle.stream.ingest.config.PropertyConfig;

@Component
public class JsonToAvroStreams {
    private static final Logger logger = LoggerFactory.getLogger(JsonToAvroStreams.class);

    private final Serde<String> stringSerde;
    private final SpecificAvroSerde<VehicleEvent> vehicleEventSerde;
    private final SpecificAvroSerde<VehicleMetadata> vehicleMetadataSerde;

    @Autowired
    private PropertyConfig propConfig;

    public JsonToAvroStreams(
            @Qualifier("AvroVehicleEventSerde") SpecificAvroSerde<VehicleEvent> vehicleEventSerde,
            @Qualifier("AvroVehicleMetadataSerde") SpecificAvroSerde<VehicleMetadata> vehicleMetadataSerde) {
        this.stringSerde = Serdes.String();
        this.vehicleEventSerde = vehicleEventSerde;
        this.vehicleMetadataSerde = vehicleMetadataSerde;
    }

    @Autowired
    public void jsonToVehicleEvent(StreamsBuilder builder) {
        KStream<String, String> jsonEvents = builder.stream(propConfig.getEventJsonTopic(),
                Consumed.with(stringSerde, stringSerde));

        KStream<String, VehicleEvent> avroStream = jsonEvents
                .peek((key, value) -> logger.info("Received JSON: keys={}, value={}", key, value))
                .mapValues(json -> {
                    try {
                        Schema schema = VehicleEvent.getClassSchema();
                        SpecificDatumReader<VehicleEvent> reader = new SpecificDatumReader<>(schema);
                        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, json);

                        return reader.read(null, decoder);
                    } catch (Exception e) {
                        logger.error("Failed to parse JSON: {}, error: {}", json, e.getMessage());
                        return null;
                    }
                }).filter((k, v) -> v != null)
                .peek((key, value) -> logger.info("After filter VehicleEvent: key={}, value={}", key, value));

        // Note: this is Fire-and-Forget setup, if we want to control, we have to implement custom producer
        avroStream.to(propConfig.getEventAvroTopic(), Produced.with(Serdes.String(), vehicleEventSerde));
    }

    @Autowired
    public void jsonToVehicleMetadata(StreamsBuilder builder) {
        KStream<String, String> jsonMetadatas = builder.stream(propConfig.getMetadataJsonTopic(),
                Consumed.with(stringSerde, stringSerde));

        KStream<String, VehicleMetadata> avroStream = jsonMetadatas
                .peek((key, value) -> logger.info("Received JSON: keys={}, value={}", key, value))
                .mapValues(json -> {
                    try {
                        Schema schema = VehicleMetadata.getClassSchema();
                        SpecificDatumReader<VehicleMetadata> reader = new SpecificDatumReader<>(schema);
                        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, json);

                        return reader.read(null, decoder);
                    } catch (Exception e) {
                        logger.error("Failed to parse JSON: {}, error: {}", json, e.getMessage());
                        return null;
                    }
                })
                .filter((k, v) -> v != null)
                .peek((key, value) -> logger.info("After filter VehicleEvent: key={}, value={}", key, value));

        avroStream.to(propConfig.getMetadataAvroTopic(), Produced.with(Serdes.String(), vehicleMetadataSerde));
    }
}
