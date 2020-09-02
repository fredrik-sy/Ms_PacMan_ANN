package pacman.entries.pacman.artificialNeuralNetwork;

public final class Sigmoid implements ActivationFunction {
    public float calc(float x) {
        return (float) (1 / (1 + Math.pow(Math.E, -x)));
    }

    public float calcDerivative(float y) {
        return calc(y) * (1 - calc(y));
    }
}
