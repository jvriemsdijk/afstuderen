/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.h2.ejb;

import nl.h2.testutil.SysteemParameters;
import org.junit.*;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author jvkampen
 */
public class TrainingDataGeneratorTest {

    private static EJBContainer container;
    private static TrainingDataGenerator instance;

    public TrainingDataGeneratorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws NamingException {

        String omgeving = System.getenv("OMGEVING");
        SysteemParameters sp = new SysteemParameters(omgeving);

        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContextFactory");

        p.put("java:jboss/datasources/webappDS", "new://Resource?type=DataSource");
        p.put("java:jboss/datasources/webappDS.JdbcDriver", sp.getDatabaseDriver());
        p.put("java:jboss/datasources/webappDS.JdbcUrl", sp.getDatabaseURL());
        p.put("java:jboss/datasources/webappDS.UserName", sp.getDatabaseUser());
        p.put("java:jboss/datasources/webappDS.Password", sp.getDatabasePassword());
        p.put("java:jboss/datasources/webappDS.JtaManaged", "true");

        container = javax.ejb.embeddable.EJBContainer.createEJBContainer(p);

        try {
            instance = (TrainingDataGenerator) container.getContext().lookup("java:global/tdg.ejb/TrainingDataGenerator");
        } catch (NamingException ex) {
            // niet gevonden
            // nog een keer proberen in andere context. Is nodig voor de Sonar tests
            instance = (TrainingDataGenerator) container.getContext().lookup("java:global/cobertura/TrainingDataGenerator");
        }


    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    @Test
    public void testClearData() throws Exception {

        // Clear data
        instance.clearData();
    }


    @Test
    public void testInitDataLoad() throws Exception {

        // Clear data
        instance.clearData();

        // Initial data load
        instance.initialDataLoad();

    }


    @Test
    public void testGenerateTrainingData() throws Exception {

        instance.clearData();
        instance.initialDataLoad();
//        instance.generateTrainingData(50, 1.5, 0.3, 1.0, 0.0, null);


        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 2.5, 0.5, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.2, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(5000, 1.5, 0.3, 1.0, 0.0, null);
        instance.generateTrainingData(2000, 1.5, 0.3, 1.0, 0.0, null);

    }


}