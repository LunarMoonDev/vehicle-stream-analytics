package vehicle.stream.processor.utils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LogUtil {

    @Autowired
    private StringUtil sUtil;

    public void logBean(Logger logger, String bean, Map<String, Object> properties, List<String> banned) {
        logger.info("Creating {} with the following properties: ", bean);

        properties.forEach((key, value) -> {
            String sValue = (value instanceof String)? (String) value: value.toString();
            sValue = banned.contains(key)? sUtil.mask(sValue): sValue;

            logger.info("\t {}: {}", key, sValue);
        });
    }
}
