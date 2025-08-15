package vehicle.stream.ingest.config;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vehicle.stream.avro.VehicleEvent;
import vehicle.stream.avro.VehicleMetadata;
import vehicle.stream.ingest.utils.LogUtil;
import vehicle.stream.ingest.utils.StringUtil;

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
}
