import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MultiThreadLinearRegressionCalculatorTest {

    List<Float> dependentData = new ArrayList<>();
    List<Float> independentData = new ArrayList<>();
    final float epsilon = 0.05f;

    MultiThreadLinearRegressionCalculator calculator= new MultiThreadLinearRegressionCalculator();

    @org.junit.Before
    public void setUp() throws Exception {
        dependentData.add(1.4f);
        dependentData.add(1.9f);
        dependentData.add(3.2f);

        independentData.add(0.5f);
        independentData.add(2.3f);
        independentData.add(2.9f);
    }

    @Test
    public void determineLinearEquation() {
        LinearEquation equation = calculator.determineLinearEquation(
                dependentData,
                independentData,
                500,
                0.00001f,
                0.01f);

        float realSlope = equation.getSlope();
        final float expectedSlope = 0.64f;
        assertTrue(expectedSlope - epsilon < realSlope && realSlope < expectedSlope + epsilon);

        float realIntercept = equation.getInterceptY();
        final float expectedIntercept= 0.95f;
        assertTrue(expectedIntercept - epsilon < realIntercept && realIntercept < expectedIntercept + epsilon);
    }
}