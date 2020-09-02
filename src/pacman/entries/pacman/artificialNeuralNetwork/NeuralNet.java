package pacman.entries.pacman.artificialNeuralNetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Random;

public class NeuralNet {
    private static final Random RANDOM = new Random();
    private static final float LEARNING_RATE = 0.1f;
    private static final float MOMENTUM = 0.9f;
    private static final Sigmoid SIGMOID = new Sigmoid();

    private Layer[] layers;
    private Layer inputLayer;
    private Layer outputLayer;

    public NeuralNet(int nInputNeurons, int nOutputNeurons, int nHiddenLayers, int nHiddenNeuronsPerLayer) {
        layers = new Layer[nHiddenLayers + 2];
        inputLayer = layers[0] = new Layer(nInputNeurons, SIGMOID);

        /* Hidden layers */
        for (int i = 1; i < layers.length - 1; i++)
            layers[i] = new Layer(nHiddenNeuronsPerLayer, SIGMOID, layers[i - 1].neurons.length);

        outputLayer = layers[layers.length - 1] = new Layer(nOutputNeurons, SIGMOID, layers[layers.length - 2].neurons.length);

        initializeWeights(nInputNeurons, nOutputNeurons);
    }

    private void initializeWeights(int nInputNeurons, int nOutputNeurons) {
        /* Xavier initialization */
        for (int i = 1; i < layers.length; i++)
            for (int j = 0; j < layers[i].neurons.length; j++)
                for (int k = 0; k < layers[i].neurons[j].weights.length; k++)
                    layers[i].neurons[j].weights[k] = (float) (RANDOM.nextGaussian() * Math.sqrt(1.0 / (nInputNeurons + nOutputNeurons)));
    }

    public void train(float[] in, float[] out) {
        setInput(in);
        forwardPropagation();
        findError(out);
        backPropagation();
    }

    public float[] getOutput(float[] in) {
        setInput(in);
        forwardPropagation();

        float[] out = new float[outputLayer.neurons.length];

        for (int i = 0; i < out.length; i++)
            out[i] = outputLayer.neurons[i].value;

        return out;
    }

    private void setInput(float... in) {
        if (in.length != inputLayer.neurons.length)
            throw new InputMismatchException();

        for (int i = 0; i < inputLayer.neurons.length; i++)
            inputLayer.neurons[i].value = in[i];
    }

    private void findError(float[] out) {
        if (out.length != outputLayer.neurons.length)
            throw new InputMismatchException();

        for (int i = 0; i < outputLayer.neurons.length; i++) {
            float outValue = outputLayer.neurons[i].value;
            float errorDifference = out[i] - outValue;
            outputLayer.neurons[i].error = outputLayer.activationFunction.calcDerivative(outValue) * errorDifference;
        }
    }

    private void forwardPropagation() {
        for (int i = 0; i < layers.length - 1; i++)
            layers[i].forwardPropagation(layers[i + 1]);
    }

    private void backPropagation() {
        for (int i = layers.length - 1; i > 0; i--)
            layers[i].backPropagate(layers[i - 1]);

        for (int i = 1; i < layers.length; i++)
            layers[i].adjustWeights(layers[i - 1], LEARNING_RATE, MOMENTUM);
    }

    public void writeToFile(String path) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                writer.print(layers[i].neurons[j].weights[0]);

                for (int k = 1; k < layers[i].neurons[j].weights.length; k++)
                    writer.print(";" + layers[i].neurons[j].weights[k]);

                writer.println();
            }
        }

        writer.close();
    }

    public void readFromFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                String[] line = reader.readLine().split(";");

                for (int k = 0; k < layers[i].neurons[j].weights.length; k++)
                    layers[i].neurons[j].weights[k] = Float.parseFloat(line[k]);
            }
        }

        reader.close();
    }
}
