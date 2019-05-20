import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadLinearRegressionCalculator {
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

    public LinearEquation determineLinearEquation(final List<Float> dependentData,
                                                  final List<Float> independentData,
                                                  int maxNumOfAttempts,
                                                  float stepSizeToStop,
                                                  float learningRate) {

        List<LinearRegressionCalculatorRunner> runnables = createLinearCalculatorRunners(dependentData,
                independentData,
                maxNumOfAttempts,
                stepSizeToStop,
                learningRate);

        for (LinearRegressionCalculatorRunner runnable : runnables) {
            executor.execute(runnable);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (final InterruptedException e) {

        }

        List<LinearEquation> linearEquations = new ArrayList<>();
        for (LinearRegressionCalculatorRunner runnable : runnables) {
            linearEquations.add(runnable.getLinearEquation());
        }


        return findBestLinearEquation(linearEquations, dependentData, independentData);
    }

    private LinearEquation findBestLinearEquation(List<LinearEquation> linearEquations,
                                                  List<Float> dependentData,
                                                  List<Float> independentData) {
        LinearEquation bestEquation = null;
        float lastError;
        float minError = Float.MAX_VALUE;
        for (LinearEquation equation : linearEquations) {
            lastError = calculateRss(equation, dependentData, independentData);
            if (lastError < minError) {
                bestEquation = equation;
                minError = lastError;
            }
        }
        return bestEquation;
    }


    private Float calculateRss(LinearEquation equation, List<Float> dependentData, List<Float> independentData) {
        if (dependentData.size()!= independentData.size()){
            throw new IllegalArgumentException("Data size not equal");
        }

        float rss = 0;
        final int dataSize = independentData.size();

        for (int i =0; i < dataSize;i++){
            rss+=Math.pow((dependentData.get(i)-(equation.getSlope()*independentData.get(i)+equation.getInterceptY())),2);
        }
        return rss;
    }

    private List<LinearRegressionCalculatorRunner> createLinearCalculatorRunners(List<Float> dependentData,
                                                                                 List<Float> independentData,
                                                                                 int maxNumOfAttempts,
                                                                                 float stepSizeToStop,
                                                                                 float learningRate) {

        List<LinearRegressionCalculatorRunner> runners = new ArrayList<>();
        for (float i = -10; i <= 10; i += 0.1) {
            for (int j = -100; j <= 100; j += 10) {
                runners.add(new LinearRegressionCalculatorRunner(dependentData,
                        independentData,
                        i,
                        j,
                        maxNumOfAttempts,
                        stepSizeToStop,
                        learningRate));
            }

        }
        return runners;
    }
}
