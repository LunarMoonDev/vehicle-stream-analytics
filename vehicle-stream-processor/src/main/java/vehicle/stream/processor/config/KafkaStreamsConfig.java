package vehicle.stream.processor.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import vehicle.stream.processor.utils.LogUtil;
import vehicle.stream.processor.utils.StringUtil;

import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
public class KafkaStreamsConfig {
    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamsConfig.class);

    @Autowired
    private PropertyConfig propConfig;

    @Autowired
    private StringUtil sUtil;

    @Autowired
    private LogUtil lUtil;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> kafkaProp = Map.of(
                APPLICATION_ID_CONFIG, propConfig.getKafkaStreamApplicationId(),
                BOOTSTRAP_SERVERS_CONFIG, propConfig.getBootstrapServers(),
                DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
                DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
                DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName(),
                AUTO_OFFSET_RESET_CONFIG, "earliest"
        );

        String beanName = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME;
        lUtil.logBean(logger, beanName, kafkaProp, List.of(
                BOOTSTRAP_SERVERS_CONFIG,
                APPLICATION_ID_CONFIG
        ));


        return new KafkaStreamsConfiguration(kafkaProp);
    }
}
