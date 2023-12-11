package ep;


import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Iterator;

public class JsonToCsvFunction implements Function<String, String> {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger;


    @Override
    public String process(String input, Context context) {
        try {

            logger = context.getLogger();
            logger.info("Converting function: "+ input );
        

            // Parse JSON string to JsonNode using Jackson
            JsonNode jsonNode = objectMapper.readTree(input);

            // Convert JsonNode to CSV string
            String csvData = convertJsonToCsv(jsonNode);

            // Log the CSV data if needed
            logger.info("Converted CSV data: " + csvData);

            //publish to output topic
            return  csvData;

        } catch (Exception e) {
            // Handle the exception as needed
            logger.error("Error converting JSON to CSV: " + e.getMessage());
            return "error";
        }
    }


    private static String convertJsonToCsv(JsonNode jsonNode) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        // Create CSV header
        StringBuilder csv = new StringBuilder();
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            csv.append("\"").append(fieldNames.next()).append("\",");
        }
        csv.deleteCharAt(csv.length() - 1); // Remove the trailing comma
        csv.append("\n");

        // Create CSV rows
        csv.append(convertJsonNodeToCsvRow(jsonNode));

        return csv.toString();
    }

    private static String convertJsonNodeToCsvRow(JsonNode jsonNode) {
        StringBuilder csvRow = new StringBuilder();

        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<String> fieldNames = objectNode.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = objectNode.get(fieldName);

                if (fieldValue.isTextual()) {
                    csvRow.append("\"").append(fieldValue.textValue()).append("\",");
                } else if (fieldValue.isNumber()) {
                    csvRow.append(fieldValue.toString()).append(",");
                } else if (fieldValue.isBoolean()) {
                    csvRow.append(fieldValue.booleanValue()).append(",");
                } else if (fieldValue.isObject()) {
                    csvRow.append(convertJsonNodeToCsvRow(fieldValue)).append(",");
                } else if (fieldValue.isNull()) {
                    csvRow.append("null").append(",");
                }
            }

            // Remove the trailing comma
            if (csvRow.length() > 0) {
                csvRow.deleteCharAt(csvRow.length() - 1);
            }
        }

        return csvRow.toString();
    }

    public static void main(String[] args) {
        String jsonString = args[0];

        try {
            String csvData = convertJsonToCsv(objectMapper.readTree(jsonString));
            System.out.println(csvData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
