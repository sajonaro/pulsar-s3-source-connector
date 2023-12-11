package ep;


import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.functions.api.Record;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

public class FanOutFunction implements Function<String, String> {


    @Override
    public Void process(String input, Context context) throws Exception {
        
       // Accessing record properties
        Record<JsonFileRecord> record = (Record<JsonFileRecord>)context.getCurrentRecord();
        
        // Accessing properties
        String propertyValue = record.getProperties().get("propertyName");

        // Log the property value
        context.getLogger().info("Property Value: " + propertyValue);

      
      
      
      
      
      
      
      
        String[] publishTopics = ((String)context.getUserConfigValueOrDefault("publish-topics", "log-topic")).split(",");
        String output = String.format("%s!", input);
        try {
            for(String topic: publishTopics){
                context.newOutputMessage(topic, Schema.STRING).value(output).sendAsync();                    
            }
 
        } catch (PulsarClientException e) {
            context.getLogger().error(e.toString());
        }
        return null;
    }

    
}


