/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.h2.ejb;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author jvkampen
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TrainingDataGeneratorTest.class})
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        //$JUnit-BEGIN$

        //$JUnit-END$
        return suite;
    }
}