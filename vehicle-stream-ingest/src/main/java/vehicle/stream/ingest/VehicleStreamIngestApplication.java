package vehicle.stream.ingest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class VehicleStreamIngestApplication {
	private static final Logger logger = LoggerFactory.getLogger(VehicleStreamIngestApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Kafka Stream Application: Json to Avro Classes streams....");
		SpringApplication.run(VehicleStreamIngestApplication.class, args);
	}
}
