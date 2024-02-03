package ep;


import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.functions.api.Record;
import org.slf4j.Logger;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import java.sql.*;
import org.json.JSONObject;
import java.util.*;

public class DbSinkFunction implements Function<String, Void> {


    private static DataSource dataSource;
    private static Logger logger;

   
    @Override
    public void initialize(Context context) throws Exception {
        dataSource = GetDataSource(
                (String)context.getUserConfigValueOrDefault("user", ""),
                (String)context.getUserConfigValueOrDefault("password", ""),
                (String)context.getUserConfigValueOrDefault("dbHost", ""),
                (String)context.getUserConfigValueOrDefault("port", ""),
                (String)context.getUserConfigValueOrDefault("dbName", ""),
                (String)context.getUserConfigValueOrDefault("poolName", ""));

        logger = context.getLogger();
    }

   

    @Override
    public Void process(String input, Context context) throws Exception {
        
        try {

            // Accessing record properties
            Record<String> record = (Record<String>)context.getCurrentRecord();

            logger.info("Processing record from topic " + record.getTopicName().get());
            logger.info("Message value is: " + input);

            String tableName = GetLastPartOfTopic(record.getTopicName().get());
            Map<String,String> keyValsMap = ParseJsonToDictionary(input);
            InsertRecord(dataSource.getConnection(), GenerateInsertStatement(keyValsMap,tableName), logger );
                 
        } catch (Exception e) {
            context.getLogger().error(e.toString());
        }
        return (null) ;
    }

    
    private static String GetLastPartOfTopic(String input) {
        int lastSlashIndex = input.lastIndexOf("/");
        return input.substring(lastSlashIndex + 1);
    }



    private static String GenerateInsertStatement(Map<String, String> keyVals, String tableName) {
       
        String statement = "INSERT INTO " + tableName + " (";
        String values = " VALUES (";
        for (String key : keyVals.keySet()) {
            statement += key + ",";
            values += "'" + keyVals.get(key) + "',";
        }
        statement = statement.substring(0, statement.length() - 1) + ")";
        values = values.substring(0, values.length() - 1) + ")";
        statement += values;
        return statement;
    }


    //connect to Postgresql database using Jdbc Connection
    private static void InsertRecord(Connection conn, String statement, Logger logger)
    {
        try
        {
            logger.info("about to execute following statement:  " + statement);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(statement);
            stmt.close();
           
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
    }

    private static DataSource GetDataSource(String username, String password, String dbHost, String port, String dbName, String poolName){

        final PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setServerNames(new String[]{dbHost});
        dataSource.setPortNumbers(new int[]{Integer.parseInt(port)});
        dataSource.setDatabaseName(dbName);
        dataSource.setApplicationName(poolName);

        return dataSource;  

    }

    private static Map<String, String> ParseJsonToDictionary(String inputString){
        Map<String, String> props = new Hashtable<String, String>();

        JSONObject jsonObject = new JSONObject(inputString);
        for (String key : jsonObject.keySet()) {
            String value = jsonObject.getString(key);
            props.put(key, value);
        }

        return props;
    } 

}
