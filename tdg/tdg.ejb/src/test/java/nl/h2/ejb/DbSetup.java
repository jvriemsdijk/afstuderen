/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.h2.ejb;

import junit.framework.TestCase;
import nl.h2.testutil.SysteemParameters;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.postgresql.Driver;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author jvkampen
 */
public class DbSetup extends TestCase {

    public DbSetup() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String omgeving = System.getenv("OMGEVING");

        SysteemParameters sp = new SysteemParameters(omgeving);
        System.out.println("### parameters ###");
        System.out.println(sp);

        DriverManager.registerDriver(new Driver());

        Connection conn = DriverManager.getConnection(sp.getDatabaseURL(),
                sp.getDatabaseUser(),
                sp.getDatabasePassword());
        IDatabaseConnection connection = new DatabaseConnection(conn, sp.getDatabaseUser());

        DatabaseConfig dbConfig = connection.getConfig();
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
//        dbConfig.setProperty(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, true);

        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet dataSet = builder.build(new File("src/test/resources/input.xml"));

        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            connection.close();
        }
    }

    @Test
    public void test() {
        System.out.println("DBSetup gereed");
    }
}
