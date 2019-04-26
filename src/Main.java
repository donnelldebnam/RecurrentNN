
/**
 * @author Donnell R. Debnam Jr
 */

public class Main {

    public static void main(String[] args) {
        
        // Letter A
        float[][] input_pattern1 = { 
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

        int input_size = input_pattern1.length * input_pattern1[1].length;

        NN NeuralNetwork = new NN(input_size);
        
        NeuralNetwork.feedForward(10, input_pattern1);
        NeuralNetwork.Backpropage();

    }
}
