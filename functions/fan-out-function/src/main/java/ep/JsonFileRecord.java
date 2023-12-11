package ep;

import java.util.Map;
import java.util.Optional;
import org.apache.pulsar.functions.api.Record;

public class JsonFileRecord implements Record<String> {
    private Optional<String> key;
    private String value;
    private Map<String, String> properties;
    private Optional<Long> eventTime;

    public JsonFileRecord(Optional<String> key,
                        String value,
                        Map<String, String> properties,
                        Optional<Long> eventTime) {
        this.key = key;
        this.value = value;
        this.properties = properties;
        this.eventTime = eventTime;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Optional<String> getKey() {
        return this.key;
    }

    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }

    @Override
    public Optional<Long> getEventTime() {
        return this.eventTime;
    }
}