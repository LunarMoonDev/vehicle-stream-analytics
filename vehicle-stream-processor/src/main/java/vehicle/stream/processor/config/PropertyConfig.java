package vehicle.stream.processor.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PropertyConfig {
    @Value("${spring.kafka.servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.properties.specific.avro.reader}")
    private String specificAvroReader;

    @Value("${spring.kafka.stream.application.id}")
    private String kafkaStreamApplicationId;

    @Value("${vehicle.stream.event.avro.topic}")
    private String eventAvroTopic;

    @Value("${vehicle.stream.metadata.avro.topic}")
    private String metadataAvroTopic;

    @Value("${vehicle.stream.speeding.topic}")
    private String streamSpeedingTopic;

    @Value("${vehicle.stream.braking.topic}")
    private String streamBrakingTopic;

    @Value("${vehicle.stream.idle.topic}")
    private String streamIdleTopic;

    @Value("${vehicle.stream.enriched.topic}")
    private String streamEnrichedTopic;
}
