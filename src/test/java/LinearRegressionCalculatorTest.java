import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LinearRegressionCalculatorTest {

    List<Float> dependentData = new ArrayList<>();
    List<Float> independentData = new ArrayList<>();
    final float epsilon = 0.05f;

    LinearRegressionCalculator linearRegressionCalculator = new LinearRegressionCalculator(0.01f,0.00001f);

    @org.junit.Before
    public void setUp() throws Exception {
        dependentData.add(1.4f);
        dependentData.add(1.9f);
        dependentData.add(3.2f);

        independentData.add(0.5f);
        independentData.add(2.3f);
        independentData.add(2.9f);
    }

    @org.junit.Test
    public void calcInnerProduct() {
        final float real = linearRegressionCalculator.calcInnerProduct(dependentData, independentData, 0f);
        final float expected = 14.35f;
        assertTrue(expected - epsilon < real && real < expected + epsilon);
    }

    @org.junit.Test
    public void calcSumOfElems() {
        Float[] arr = new Float[3];
        arr[0] = 0.5f;
        arr[1] = 1.5f;
        arr[2] = -0.1f;
        final float real = linearRegressionCalculator.calcSumOfElems(arr);
        final float expected = 1.9f;
        assertTrue(expected - epsilon < real && real < expected + epsilon);

    }


    @org.junit.Test
    public void calculateDerivativeOfRSSFuncRespectToIntercept() {
        final float real = linearRegressionCalculator.calculateDerivativeOfRSSFuncRespectToIntercept(dependentData, independentData, 1.0f, 0.0f);
        final float expected = -1.6f;
        assertTrue(expected - epsilon < real && real < expected + epsilon);

    }

    @org.junit.Test
    public void calculateDerivativeOfRSSFuncRespectToSlope() {
        final float real = linearRegressionCalculator.calculateDerivativeOfRSSFuncRespectToSlope(dependentData, independentData, 1.0f, 0.0f);
        final float expected = -0.8f;
        assertTrue(expected - epsilon < real && real < expected + epsilon);
    }

    @org.junit.Test
    public void determineBestEquation() {
        LinearEquation realEquation = linearRegressionCalculator.determineBestEquation(dependentData, independentData, 1.0f, 0.0f, 5000);

        final float realSlope = realEquation.getSlope();
        final float realIntercept = realEquation.getInterceptY();

        final float expectedSlope = 0.64f;
        final float expectedIntercept = 0.95f;

        assertTrue(expectedIntercept - epsilon < realIntercept && realIntercept < expectedIntercept + epsilon);
        assertTrue(expectedSlope - epsilon < realSlope && realSlope < expectedSlope + epsilon);
    }
}