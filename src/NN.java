
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

    /* Custom Constructor for NeuralNetwork. */
    public NN(int inputNeurons) {
        input = new InputLayer(inputNeurons);
        nn.add(input);
    }

    /* Appends new layer to end of Network (LList). */
    void append(Layer layer) {
        nn.add(layer);
        size++;
    }

    /* Returns value crunched through sigmoid function */
    static double sigmoid(float x) {
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

    public static void main(String[] args) {

        /* Network with 25 input neurons */
        NN network = new NN(25);

        /* Number of Hidden Layers. */
        final int HIDDEN_LAYERS = 3;

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

        /* Input Matrix for Letter A */
        float[][] letterA = { 
                { 0, 1, 1, 1, 0 },
                { 0, 1, 0, 1, 0 },
                { 0, 1, 1, 1, 0 },
                { 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0 } };

        /* Input Matrix for Letter Z */
        float[][] letterZ = { 
                { 1, 1, 1, 1, 1 },
                { 0, 0, 0, 1, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 1, 0, 0, 0 },
                { 1, 1, 1, 1, 1 } };

        Matrix A = new Matrix(); // Create new Matrix for letter A
        A.matrix = letterA; // Set matrix to 'A' pattern

        A.Flatten(); // Flatten Matrix to 1D
        network.input.vector = A; // Set 'A' pattern to input layer.

        /* Copy letterA pattern to input layer. */
        for (int i = 0; i < network.input.neurons; i++) 
            network.input.activations[i] = A.matrix[i][0];
        
        // Set HiddenLayer(1) activations!
        Matrix input_to_firsthidden = new Matrix(network.input, network.nn.get(1));
        float[] hidden1_activations = network.multiply(network.input.activations, input_to_firsthidden.matrix);
        network.nn.get(1).activations = hidden1_activations;

        // Set HiddenLayer(2) activations!
        Matrix input_to_secondhidden = new Matrix(network.nn.get(1), network.nn.get(2));
        float[] hidden2_activations = network.multiply(hidden1_activations, input_to_secondhidden.matrix);
        network.nn.get(2).activations = hidden2_activations;

        // Set HiddenLayer(3) activations!
        Matrix input_to_thirdhidden = new Matrix(network.nn.get(2), network.nn.get(3));
        float[] hidden3_activations = network.multiply(hidden2_activations, input_to_thirdhidden.matrix);    
        network.nn.get(3).activations = hidden3_activations; 
        
        // Get Outputs!
        Matrix lasthidden_to_output = new Matrix(network.nn.get(3), network.nn.get(4));
        float[] output_activations = network.multiply(hidden3_activations, lasthidden_to_output.matrix);
        network.nn.get(4).activations = output_activations;

        // Print output neurons
        for (float a : output_activations)
            System.out.println(a);

    }

}
