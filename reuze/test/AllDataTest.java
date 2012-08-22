package reuze.test;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDataTest {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllDataTest.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(ArrayUtilTest.class);
        suite.addTestSuite(SetWeightedRandomTest.class);
        suite.addTestSuite(PrefUtilsTest.class);
        // $JUnit-END$
        return suite;
    }

}