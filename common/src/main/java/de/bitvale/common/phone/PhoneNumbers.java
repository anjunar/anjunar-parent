package de.bitvale.common.phone;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class PhoneNumbers {

    private final Logger log = LoggerFactory.getLogger(PhoneNumbers.class);

    private final Pattern areaCodePattern = Pattern.compile("(?:\\(\\d+\\))?(\\d+)");

    private Set<PhoneNumber> numbers = new HashSet<>();

    public String format(String rawNumber) {
        String number = rawNumber.replaceAll("\\(\\d+\\)", "");
        number = StringUtils.deleteWhitespace(number);
        for (PhoneNumber phoneNumber : numbers) {
            String countryCode = "+" + phoneNumber.getCountryCode();
            String areaCode = phoneNumber.getAreaCode();

            Matcher matcher = areaCodePattern.matcher(areaCode);
            if (matcher.matches()) {
                String prefix = countryCode + matcher.group(1);

                if (number.startsWith(prefix)) {
                    String numberPostfix = number.substring(prefix.length());
                    return countryCode + " " + areaCode + " " + insertSpaces(numberPostfix);
                }
            }
        }

        for (PhoneNumber phoneNumber : numbers) {
            String countryCode = "+" + phoneNumber.getCountryCode();

            if (number.startsWith(countryCode)) {

                String carrier = "(0)" + number.substring(countryCode.length(), countryCode.length() + 3);

                String numberPostfix = number.substring(countryCode.length() + 3, number.length());

                return countryCode + " " + carrier + " " + insertSpaces(numberPostfix);

            }
        }

        return "invalid";
    }

    private String insertSpaces(String numberPostfix) {
        StringBuilder builder = new StringBuilder();
        builder.append(numberPostfix)
                .insert(numberPostfix.length() - 2, " ")
                .insert(numberPostfix.length() - 4, " ");

        if (numberPostfix.length() >= 8) {
            builder.insert(numberPostfix.length() - 6, " ");
        }

        if (numberPostfix.length() >= 10) {
            builder.insert(numberPostfix.length() - 8, " ");
        }

        return builder.toString();
    }

    @PostConstruct
    public void postConstruct() {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        InputStream resource = classLoader.getResourceAsStream("META-INF/globalareacodes.csv");

        try {
            byte[] bytes = IOUtils.toByteArray(resource);
            String string = new String(bytes);

            String[] lines = string.split("\\n");

            for (String line : lines) {
                String[] split = line.split(",");

                if (split.length == 4) {
                    PhoneNumber phoneNumber = new PhoneNumber();

                    phoneNumber.setCountry(split[0].replace("\"", ""));
                    phoneNumber.setCountryCode(split[1].replace("\"", ""));
                    phoneNumber.setArea(split[2].replace("\"", ""));
                    phoneNumber.setAreaCode(split[3].replace("\"", ""));

                    numbers.add(phoneNumber);
                }

            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    public Set<PhoneNumber> getNumbers() {
        return numbers;
    }
}
