
/**
 * @author Donnell R. Debnam Jr
 */

import java.util.HashMap;
import java.util.LinkedList;

import java.util.ArrayList;

public class NN {

    /* Attributes of the Network */
    public LinkedList<Layer> nn = new LinkedList<>();

    public InputLayer input;
    public OutputLayer output;

    public int size = 0;
    public float[][] pattern;

    /* Custom Constructor for NeuralNetwork. */
    public NN(int inputNeurons) {
        input = new InputLayer(inputNeurons);
        nn.add(input);
        size++;
    }

    /* Adds new layer to end of Network (LList). */
    void append(Layer layer) {
        nn.add(layer);
        size++;
    }

    /* Returns value crunched through sigmoid function */
    double sigmoid(float x) {
        return 1 / (1 + Math.exp(-x));
    }

    /* Returns products of activations times a weight matrix. */
    float[] multiply(float[] activations, float[][] weights) {
        float[] product = new float[weights[1].length];
        for (int i = 0; i < weights[1].length; i++) {
            float sum = 0;
            int count = 0;
            for (float a : activations) {
                sum += (a * weights[count][i]);
                count++;
            }
            product[i] = (float) sigmoid(sum);
        }
        return product;
    }

    /* Sets pattern to the Network training ground. */
    void setPattern(float[][] pattern) {
        this.pattern = pattern;
    }

    /* Imports pattern to training ground. */
    float[][] importPattern() {
        return pattern;
    }

    /* Entire feedforward algorithm. */
    void feedForward(int hiddenLayers, float[][] p) {

        /* Network with 25 input neurons */
        int s = p.length *  p[1].length;
        NN network = new NN(s);

        /* Number of Hidden Layers. */
        final int HIDDEN_LAYERS = hiddenLayers;

        /* Maps pairs of layers to a weight matrix. */
        HashMap<ArrayList<Layer>, Matrix> map = new HashMap<>();

        /* Add (3) new HiddenLayers to the Network. */
        for (int i = 0; i < HIDDEN_LAYERS; i++) 
            network.append(new HiddenLayer(3));
        
        /* Add Output Layer. */
        network.append(new OutputLayer());

        /* Give each pair of parallel Layers a matrix. */
        int count = 0;
        for (int i = 0; i < network.nn.size(); i++) {
            ArrayList<Layer> l = new ArrayList<>();
            Layer layer_i, layer_j;
            if (count == 0) {
                layer_i = network.input;
                layer_j = network.nn.get(1);
                count++;
            } else {
                layer_i = network.nn.get(i - 1);
                layer_j = network.nn.get(i);
            }
            l.add(layer_i);
            l.add(layer_j);
            map.put(l, new Matrix(layer_i, layer_j));
        }

        /* Input Matrix for unique pattern */
        network.setPattern(p);
        float[][] MY_PATTERN = network.importPattern();

        Matrix pattern = new Matrix(); // Create new Matrix for pattern
        pattern.matrix = MY_PATTERN; // Set matrix to new pattern

        pattern.Flatten(); // Flatten Matrix to 1D
        network.input.vector = pattern; // Set pattern to input layer.

        /* Copy pattern to 1D input layer. */
        for (int i = 0; i < network.input.neurons; i++) 
            network.input.activations[i] = pattern.matrix[i][0];
    
            /**
             * Here, we actually feedforward through the network. Layers are interconnected through the
             * NN's LinkedList structure, in which each layer of activations and weight matrices
             * are multipled and passed along. Finally, the activations of our output layers are printed.
             */

        Matrix weightMatrix;            // Dynamic weight matrix from layer-to-layer
        float[] nextLayerActivations;   // Dynamic activations from layer-to-layer

        // Feed forward through the network.
        for (int i = 0; i < network.size-1; i++) {
            weightMatrix = new Matrix(network.nn.get(i), network.nn.get(i + 1));
            if (i == 0) 
                nextLayerActivations = network.multiply(network.input.activations, weightMatrix.matrix);
            else 
                nextLayerActivations = network.multiply(network.nn.get(i).activations, weightMatrix.matrix);
            network.nn.get(i+1).activations = nextLayerActivations;
        }

        // Print output neurons
        for (float o : network.nn.get(network.size-1).activations)
            System.out.println(o);

    }

    /* BackPropogate through the network. */
    public void Backpropage() {
        // Backpropogation Algorithm
        // This algorithm will be combined with FeedForward to create a 'Train' algorithm.
    }

}
