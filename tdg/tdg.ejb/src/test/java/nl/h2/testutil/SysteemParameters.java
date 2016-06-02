package nl.h2.testutil;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 *
 * @author jvkampen
 */
public class SysteemParameters {

    private static final Logger log = Logger.getLogger(SysteemParameters.class.getName());
    
    private static ResourceBundle configurationBundle;
    
    private String databaseURL = "db.url";
    private String databaseUser = "db.username";
    private String databasePassword = "db.password";
    private String databaseDriver = "db.driver";

    public SysteemParameters(String omgeving) {
                
        if (omgeving == null || omgeving.equals("lokaal")) {
            // lokaal
            System.out.println("Te gebruiken resourcebundle : " + "verwijsindex");
            configurationBundle = ResourceBundle.getBundle("verwijsindex", Locale.getDefault(), SysteemParameters.class.getClassLoader());
        } else {
            // omgevings_specifiek
            System.out.println("Te gebruiken resourcebundle : " + "verwijsindex_"+omgeving);
            configurationBundle = ResourceBundle.getBundle("verwijsindex_"+omgeving, Locale.getDefault(), SysteemParameters.class.getClassLoader());
        }
    }
    
    private String getParameter(String keyName) {
        try {
            return configurationBundle.getString(keyName);
        } catch (MissingResourceException mre) {
            throw new IllegalArgumentException(keyName + " bestaat niet!", mre);
        }
    }

    public String getDatabaseURL() {
        return getParameter(databaseURL);
    }

    public String getDatabaseUser() {
        return getParameter(databaseUser);
    }

    public String getDatabasePassword() {
        return getParameter(databasePassword);
    }

    public String getDatabaseDriver() {
        return getParameter(databaseDriver);
    }

    public String toString() {
        return this.getDatabaseURL() + " / " +
        this.getDatabaseDriver() + " / " +
        this.getDatabasePassword() + " / " +
        this.getDatabaseUser();
    }
}
