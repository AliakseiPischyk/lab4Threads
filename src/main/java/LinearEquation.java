public class LinearEquation {
    public float getInterceptY() {
        return interceptY;
    }

    private float interceptY;

    public float getSlope() {
        return slope;
    }

    private float slope;

    public LinearEquation(final float interceptY, final float slope) {
        this.interceptY = interceptY;
        this.slope = slope;
    }
}
