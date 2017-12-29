package jdbc;


import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.SQLException;

import static java.sql.DriverManager.getDriver;

public class DataSourceInitializer {

    public DataSource getDataSource(String dbUrl, String dbUser, String dbPassword){
        DataSource dataSource = createDataSource(dbUrl, dbUser, dbPassword);
        return dataSource;
    }

    public DatabasePopulator createDatabasePopulator(String sqlScriptWithPath) {

        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        //databasePopulator.setContinueOnError(true);
        databasePopulator.setContinueOnError(false);
        databasePopulator.addScript(new ClassPathResource(sqlScriptWithPath));
        return databasePopulator;
    }

    private SimpleDriverDataSource createDataSource(String dbUrl, String dbUser, String dbPassword) {

        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();

        try {
            simpleDriverDataSource.setDriverClass(getDriver(dbUrl).getClass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //simpleDriverDataSource.setDriverClass(org.apache.hive.jdbc.HiveDriver.class);
        //simpleDriverDataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        //simpleDriverDataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        simpleDriverDataSource.setUrl(dbUrl);
        simpleDriverDataSource.setUsername(dbUser);
        simpleDriverDataSource.setPassword(dbPassword);
        return simpleDriverDataSource;
    }
}
