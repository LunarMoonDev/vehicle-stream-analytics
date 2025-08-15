package vehicle.stream.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class VehicleStreamProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleStreamProcessorApplication.class, args);
	}

}
