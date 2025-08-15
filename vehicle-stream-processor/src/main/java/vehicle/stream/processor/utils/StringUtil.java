package vehicle.stream.processor.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    public String mask(String given) {
        if (given == null || given.length() <= 2) {
            return given;
        }

        int maskLength = given.length() - 2;
        return "*".repeat(maskLength) + given.substring(maskLength);
    }
}
