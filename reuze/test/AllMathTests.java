package reuze.test;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllMathTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllMathTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(BilinearTest.class);
        suite.addTestSuite(MathTest.class);
        suite.addTestSuite(MatrixTest.class);
        suite.addTestSuite(RangeTest.class);
        suite.addTestSuite(Vector3Test.class);
        // $JUnit-END$
        return suite;
    }

}