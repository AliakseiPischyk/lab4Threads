import java.util.List;
import java.util.concurrent.Future;

public class LinearRegressionCalculatorRunner implements Runnable {
    final List<Float> dependentData;
    final List<Float> independentData;
    final float slopeHint;
    final float interceptHint;
    final int maxNumOfAttempts;
    private LinearRegressionCalculator linearRegressionCalculator;

    public LinearEquation getLinearEquation() {
        return linearEquationResult;
    }

    private LinearEquation linearEquationResult;

    public LinearRegressionCalculatorRunner(List<Float> dependentData,
                                            List<Float> independentData,
                                            float slopeHint,
                                            float interceptHint,
                                            int maxNumOfAttempts,
                                            float stepSizeToStop,
                                            float learningRate) {
        this.dependentData = dependentData;
        this.independentData = independentData;
        this.slopeHint = slopeHint;
        this.interceptHint = interceptHint;
        this.maxNumOfAttempts = maxNumOfAttempts;
        this.linearRegressionCalculator = new LinearRegressionCalculator(learningRate,stepSizeToStop);
    }

    @Override
    public void run(){
        linearEquationResult = linearRegressionCalculator.determineBestEquation(dependentData,independentData,slopeHint,interceptHint,maxNumOfAttempts);
    }
}
