package vehicle.stream.processor.config;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vehicle.stream.avro.*;
import vehicle.stream.processor.utils.LogUtil;
import vehicle.stream.processor.utils.StringUtil;

import java.util.Map;

@Configuration
public class AvroRegistryConfig {
    private static final Logger logger = LoggerFactory.getLogger(AvroRegistryConfig.class);

    @Autowired
    private PropertyConfig propConfig;

    @Autowired
    private StringUtil sUtil;

    @Autowired
    private LogUtil lUtil;

    @Bean(name = "AvroMapProperties")
    public Map<String, String> avroMapProperties() {
        logger.info("Creating AvroMapProperties with following properties: ");
        logger.info("\t schema.registry.url: {}", sUtil.mask(propConfig.getSchemaRegistryUrl()));
        return Map.of("schema.registry.url", propConfig.getSchemaRegistryUrl());
    }

    @Bean(name = "AvroVehicleEventSerde")
    public SpecificAvroSerde<VehicleEvent> avroVehicleEventSerde(
            @Qualifier("AvroMapProperties") @Autowired Map<String, String> mapProp) {
        logger.info("Creating AvroVehicleEventSerde with following properties: ");
        logger.info("\t serdeConfig: {}", mapProp == null? null: mapProp.hashCode());

        SpecificAvroSerde<VehicleEvent> avroSerde = new SpecificAvroSerde<>();
        avroSerde.configure(mapProp, false);
        return avroSerde;
    }

    @Bean(name = "AvroVehicleMetadataSerde")
    public SpecificAvroSerde<VehicleMetadata> avroVehicleMetadata(
            @Qualifier("AvroMapProperties") @Autowired Map<String, String> mapProp) {
        logger.info("Creating AvroVehicleMetadataSerde with following properties: ");
        logger.info("\t serdeConfig: {}", mapProp == null? null: mapProp.hashCode());

        SpecificAvroSerde<VehicleMetadata> avroSerde = new SpecificAvroSerde<>();
        avroSerde.configure(mapProp, false);
        return avroSerde;
    }

    @Bean(name = "AvroEnrichedEventSerde")
    public SpecificAvroSerde<EnrichedEvent> avroEnrichedEvent(
            @Qualifier("AvroMapProperties") @Autowired Map<String, String> mapProp) {
        logger.info("Creating AvroEnrichedEventSerde with following properties: ");
        logger.info("\t serdeConfig: {}", mapProp == null? null: mapProp.hashCode());

        SpecificAvroSerde<EnrichedEvent> avroSerde = new SpecificAvroSerde<>();
        avroSerde.configure(mapProp, false);
        return avroSerde;
    }

    @Bean(name = "AvroSuddenBrakingEventSerde")
    public SpecificAvroSerde<SuddenBrakingEvent> avroBrakingEvent(
            @Qualifier("AvroMapProperties") @Autowired Map<String, String> mapProp) {
        logger.info("Creating AvroSuddenBrakingEventSerde with following properties: ");
        logger.info("\t serdeConfig: {}", mapProp == null? null: mapProp.hashCode());

        SpecificAvroSerde<SuddenBrakingEvent> avroSerde = new SpecificAvroSerde<>();
        avroSerde.configure(mapProp, false);
        return avroSerde;
    }

    @Bean(name = "AvroIdleEventSerde")
    public SpecificAvroSerde<IdleEvent> avroIdleEvent(
            @Qualifier("AvroMapProperties") @Autowired Map<String, String> mapProp) {
        logger.info("Creating AvroIdleEventSerde with following properties: ");
        logger.info("\t serdeConfig: {}", mapProp == null? null: mapProp.hashCode());

        SpecificAvroSerde<IdleEvent> avroSerde = new SpecificAvroSerde<>();
        avroSerde.configure(mapProp, false);
        return avroSerde;
    }
}