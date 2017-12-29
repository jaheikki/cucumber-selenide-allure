package jdbc;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static microservice.helper.SeleniumHelper.printMethodName;

public class SQLCommon {

    static Logger log = Logger.getLogger(
            SQLCommon.class.getName());


    public static List<String> getColumnNames(Connection conn, String table) throws SQLException {
        List<String> columnNames=null;
        try(Statement metaStatement = conn.createStatement()) {
            ResultSet metaResult = metaStatement.executeQuery("select column_name from INFORMATION_SCHEMA.columns WHERE table_name = '"+table+"'");

            columnNames=new ArrayList<>();
            while(metaResult.next())
                columnNames.add(metaResult.getString(1));


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnNames;
    }

    public static int getTotalDBRowCountFromTable(JdbcTemplate jdbcTemplate,String db, String tableName) {
        printMethodName();

        String text = "get total row count from "+db+"."+tableName+" START";
        System.out.println(text);
        log.info(text);

        int rowCount = jdbcTemplate.queryForObject("select count(*) from "+db+ "." +tableName, Integer.class);
        System.out.println("rowCount: "+rowCount);
        log.info("rowCount: "+rowCount);

        return rowCount;

    }

    public static String sqlQueryForString(JdbcTemplate jdbcTemplate,String SQL) {
        printMethodName();

        String result = jdbcTemplate.queryForObject(SQL,String.class);
        System.out.println("SQL command result: "+result);
        return result;
    }

    public static List<String> showDatabases(JdbcTemplate jdbcTemplate) {
        printMethodName();

        String SQL = "show databases";

        List<String> databaseList = jdbcTemplate.queryForList(SQL, String.class);

        System.out.println("databaseList: "+databaseList);
        log.info("databaseList: "+databaseList);

        return databaseList;

    }

    public static void databaseMustNotExist(String dbName, List<String> databases) {
        printMethodName();

        System.out.println("Searched db: "+dbName);
        for (String database:databases) {
            //System.out.println("database: "+database);
            if (database.equals(dbName)) {
                throw new RuntimeException("Database ["+dbName+"] should not be found");
            }
        }
        System.out.println("Success.");
    }

    public static int updateSQLCommand(JdbcTemplate jdbcTemplate,String SQL) {
        printMethodName();

        String text = "Execute sql command "+SQL+" START";
        System.out.println(text);
        log.info(text);

        int rowsAffected = jdbcTemplate.update(SQL);
        System.out.println("rowsAffected: "+rowsAffected);
        log.info("rowsAffected: "+rowsAffected);

        return rowsAffected;

    }

}