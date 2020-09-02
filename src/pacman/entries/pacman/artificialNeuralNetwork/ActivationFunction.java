package pacman.entries.pacman.artificialNeuralNetwork;

public interface ActivationFunction {
    float calc(float x);
    float calcDerivative(float y);
}
