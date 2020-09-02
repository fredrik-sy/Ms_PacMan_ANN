package pacman.entries.pacman.artificialNeuralNetwork;

public class Layer {
    public Neuron[] neurons;
    public ActivationFunction activationFunction;

    public Layer(int nNeaurons, ActivationFunction activationFunction) {
        neurons = new Neuron[nNeaurons];
        this.activationFunction = activationFunction;

        for (int i = 0; i < nNeaurons; i++)
            neurons[i] = new Neuron();
    }

    public Layer(int nNeaurons, ActivationFunction activationFunction, int nInputsPerNeauron) {
        this(nNeaurons, activationFunction);

        /* Layer-to-layer so there will be 1 layer that has weights and lastDelta unused */
        for (int i = 0; i < nNeaurons; i++) {
            /* Include bias in weights */
            neurons[i].weights = new float[nInputsPerNeauron + 1];
            neurons[i].lastDelta = new float[nInputsPerNeauron + 1];
        }
    }

    public void forwardPropagation(Layer nextLayer) {
        for (int i = 0; i < nextLayer.neurons.length; i++) {
            float value = 0f;

            for (int j = 0; j < neurons.length; j++)
                value += nextLayer.neurons[i].weights[j] * neurons[j].value;

            /* Add bias (Neuron.weights has 1 extra space) */
            value += nextLayer.neurons[i].weights[neurons.length] * -1f;

            nextLayer.neurons[i].value = activationFunction.calc(value);
        }
    }

    public void backPropagate(Layer nextLayer) {
        float outputVal, error;

        for (int i = 0; i < nextLayer.neurons.length; ++i) {
            outputVal = nextLayer.neurons[i].value;
            error = 0;

            for (int j = 0; j < neurons.length; ++j)
                error += neurons[j].weights[i] * neurons[j].error;

            nextLayer.neurons[i].error = activationFunction.calcDerivative(outputVal) * error;
        }
    }

    public void adjustWeights(Layer layer, float learningRate, float momentum) {
        float value, delta;

        for (int i = 0; i < neurons.length; i++) {
            int nWeights = neurons[i].weights.length;

            for (int j = 0; j < nWeights; j++) {
                /* Last weight (bias) uses -1f value */
                value = (j == nWeights - 1) ? -1f : layer.neurons[j].value;
                delta = momentum * neurons[i].lastDelta[j] + (1 - momentum) * learningRate * neurons[i].error * value;
                neurons[i].weights[j] += delta;
                neurons[i].lastDelta[j] = delta;
            }
        }
    }
}
