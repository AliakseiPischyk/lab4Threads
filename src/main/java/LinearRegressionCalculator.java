
import java.util.List;
import java.util.stream.IntStream;

public class LinearRegressionCalculator {

    private float learningRate;
    private float stepSizeToStop;

    public LinearRegressionCalculator(final float learningRate, final float stepSizeToStop) {
        this.learningRate = learningRate;
        this.stepSizeToStop = stepSizeToStop;
    }

    public Float calcInnerProduct(final List<Float> lhs, final List<Float> rhs, final Float initialSum) {
        if (lhs.size() != rhs.size()) {
            throw new IllegalArgumentException("Data size not equal");
        }

        final int dataSize = lhs.size();
        Float[] sumsOfPairs = new Float[dataSize];
        IntStream.range(0, dataSize)
                .parallel()
                .forEach(idx -> sumsOfPairs[idx] = lhs.get(idx) * rhs.get(idx));


        return calcSumOfElems(sumsOfPairs);
    }

    public Float calcSumOfElems(final Float[] values) {
        float sum = 0;
        for (float value : values) {
            sum += value;
        }
        return sum;
    }

    public float calculateDerivativeOfRSSFuncRespectToIntercept(List<Float> dependentData, List<Float> independentData, float slope, float intercept) {

        if (dependentData.size() != independentData.size()) {
            throw new IllegalArgumentException("Data size not equal");
        }

        final int dataSize = dependentData.size();
        Float[] eachRSS = new Float[dataSize];
        IntStream.range(0, dataSize)
                .parallel()
                .forEach(idx -> eachRSS[idx] = (dependentData.get(idx) - intercept - slope * independentData.get(idx)) * (-2));

        return calcSumOfElems(eachRSS);
    }

    public float calculateDerivativeOfRSSFuncRespectToSlope(List<Float> dependentData, List<Float> independentData, float slope, float intercept) {

        if (dependentData.size() != independentData.size()) {
            throw new IllegalArgumentException("Data size not equal");
        }

        final int dataSize = dependentData.size();
        Float[] eachRSS = new Float[dataSize];
        IntStream.range(0, dataSize)
                .parallel()
                .forEach(idx -> eachRSS[idx] = (dependentData.get(idx) - intercept - slope * independentData.get(idx)) * ((-2) * independentData.get(idx)));

        return calcSumOfElems(eachRSS);
    }

    public LinearEquation determineBestEquation(final List<Float> dependentData,
                                                final List<Float> independentData,
                                                final float slopeHint,
                                                final float interceptHint,
                                                final int maxNumOfAttempts) {

        float currentIntercept = interceptHint;
        float currSlope = slopeHint;

        float stepSizeIntercept;
        float stepSizeSlope;

        int currAttempt = 0;
        do {
            float derivativeOfRSSFuncRespectToIntercept = calculateDerivativeOfRSSFuncRespectToIntercept(dependentData, independentData, currSlope, currentIntercept);
            float derivativeOfRSSFuncRespectToSlope = calculateDerivativeOfRSSFuncRespectToSlope(dependentData, independentData, currSlope, currentIntercept);

            stepSizeIntercept = derivativeOfRSSFuncRespectToIntercept * learningRate;
            stepSizeSlope = derivativeOfRSSFuncRespectToSlope * learningRate;

            currentIntercept -= stepSizeIntercept;
            currSlope -= stepSizeSlope;
        } while (currAttempt++ < maxNumOfAttempts && !ShouldStopBecauseOfStepSize(currentIntercept, currSlope));

        return new LinearEquation(currentIntercept, currSlope);
    }

    private boolean ShouldStopBecauseOfStepSize(float lhs, float rhs) {
        return IsSmallEnough(lhs) || IsSmallEnough(rhs);
    }

    private boolean IsSmallEnough(float value) {
        return value < stepSizeToStop;
    }
}
