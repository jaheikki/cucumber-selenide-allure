package jdbc;

import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static microservice.helper.SeleniumHelper.printMethodName;

public class MyDatasource {

    private final DataSourceInitializer dataSourceInitializer;
    private final DataSource dataSource;

    public MyDatasource(String dbUrl, String dbUser, String dbPassword) {
        DataSourceInitializer dsi = new DataSourceInitializer();
        this.dataSourceInitializer = dsi;
        this.dataSource = dsi.getDataSource(dbUrl,dbUser,dbPassword);
    }

    public DataSource getDataSource() {

        return dataSource;
    }

    public void runSqlScript(String sqlScript) {
        printMethodName();

        System.out.println("About to run script: "+sqlScript);
        DatabasePopulatorUtils.execute(dataSourceInitializer.createDatabasePopulator(sqlScript), dataSource);

    }

    public Connection getDBConnection() {
        try {
            return dataSource.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get DB connection");
        }
    }



}
