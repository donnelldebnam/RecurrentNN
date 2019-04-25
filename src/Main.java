
/**
 * @author Donnell R. Debnam Jr
 */

public class Main {

    public static void main(String[] args) {
        
        // Letter A
        float[][] input_pattern = { 
                { 0, 1, 1, 1, 0 }, 
                { 0, 1, 0, 1, 0 }, 
                { 0, 1, 1, 1, 0 }, 
                { 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0 } };

        // Letter Z
        float[][] input_pattern2 = { 
                { 1, 1, 1, 1, 1 }, 
                { 0, 0, 0, 1, 0 }, 
                { 0, 0, 1, 0, 0 }, 
                { 0, 1, 0, 0, 0 },
                { 1, 1, 1, 1, 1 } };

        int input_size = input_pattern.length * input_pattern[1].length;
        int hidden_layers = 10;

        NN NeuralNetwork = new NN(input_size);

        NeuralNetwork.feedForward(hidden_layers, input_pattern);

    }
}