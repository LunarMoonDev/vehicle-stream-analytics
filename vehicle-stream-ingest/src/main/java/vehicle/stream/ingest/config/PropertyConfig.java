package vehicle.stream.ingest.config;

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

    @Value("${vehicle.stream.event.json.topic}")
    private String eventJsonTopic;

    @Value("${vehicle.stream.event.avro.topic}")
    private String eventAvroTopic;

    @Value("${vehicle.stream.metadata.json.topic}")
    private String metadataJsonTopic;

    @Value("${vehicle.stream.metadata.avro.topic}")
    private String metadataAvroTopic;
}
