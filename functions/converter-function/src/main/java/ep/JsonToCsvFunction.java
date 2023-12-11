package ep;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;


public class JsonToCsvFunction implements Function<JsonFileRecord, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger;

    @Override
    public String process(JsonFileRecord input, Context context) throws Exception {
        try {

            logger = context.getLogger();
            logger.info("Converting function: "+ input );
        

            // Parse JSON string to JsonNode using Jackson
            JsonNode jsonNode = objectMapper.readTree(input.getValue());

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
        String jsonString = "{\"EntityTypeName\": \"Position\", \"CorrelationId\": \"b4f9969b-5570-47f8-bbc4-a12e81dcff23\", \"ObjectId\": \"1002682\", \"CompanyId\": \"205\", \"SharedData\": \"CompanyData\", \"LogActivityId\": 141723039, \"IsUDFData\": false, \"Deleted\": false, \"Position_AccountingCategory_ID\": 0, \"Position_AddDate\": \"2013-07-02T15:47:47.66\", \"Position_AddID\": \"1002682-Position-Add\", \"Position_Administrator_ID\": 0, \"Position_AllowDirtyPricing\": 0, \"Position_AmortDate\": \"2020-07-10T00:00:00\", \"Position_Asset_ID\": 529647, \"Position_AssetClass_ID\": 0, \"Position_AssetDetail_ID\": 466178, \"Position_AssetDetailType_ID\": 4, \"Position_AssetDetailTypeName\": \"tblFacility\", \"Position_CloseDate\": \"2014-03-31T00:00:00\", \"Position_ConstantYieldType_ID\": 1, \"Position_CurrencyType_ID\": 1, \"Position_DataFeed_ID\": 0, \"Position_Description\": \"1002682-Position-Description\", \"Position_GlobalPercent\": 2.978406552494417e-12, \"Position_ID\": 1002682, \"Position_Issuer_ID\": 271855, \"Position_LastChangeDate\": \"2014-02-03T17:40:31.367\", \"Position_LastChangeID\": \"1002682-Position-Las\", \"Position_Name\": \"1002682-Position-Position\", \"Position_NonPerformingDate\": null, \"Position_NonPerformingEndDate\": \"9999-09-09T00:00:00\", \"Position_Notes\": \"1002682-Position-Notes\", \"Position_OpenDate\": \"2013-07-02T00:00:00\", \"Position_OwnerType_ID\": 1, \"Position_ParentInstitution_ID\": 0, \"Position_ParentPosition_ID\": 0, \"Position_Portfolio_ID\": 51249, \"Position_PositionSwapType_D\": 0, \"Position_PositionType_ID\": 1, \"Position_Servicer_ID\": 0, \"Position_StopAccrualDate\": null, \"Position_StopAccrualEndDate\": \"9999-09-09T00:00:00\", \"Position_StopAmortizationDate\": null, \"Position_StopAmortizationEndDate\": \"9999-09-09T00:00:00\", \"Position_StopPremiums\": false, \"Position_StopProjectionDate\": null, \"Position_StopProjectionEndDate\": \"9999-09-09T00:00:00\", \"Position_SwapPosition_ID\": 0}";

        try {
            String csvData = convertJsonToCsv(objectMapper.readTree(jsonString));
            System.out.println(csvData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
