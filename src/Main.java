
/**
 * @author Donnell R. Debnam Jr
 */

public class Main {

    public static void main(String[] args) {
        
        // Dimensions of the patterns (5x5 Grid)
        final int patternHeight = 5;
        final int patternWidth = 5;

        // Blank Input
        float[][] input_pattern0 = { 
                { 0, 0, 0, 0, 0 }, 
                { 0, 0, 0, 0, 0 }, 
                { 0, 0, 0, 0, 0 }, 
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0 } };

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


        // New Neural Network; input size of pattern L x W.
        NN NeuralNetwork = new NN(patternHeight * patternWidth);
        
        // Train each pattern
        NeuralNetwork.Train(4, input_pattern0, new float[] { 0, 0 });
        NeuralNetwork.Train(4, input_pattern1, new float[] { 1, 0 });
        NeuralNetwork.Train(4, input_pattern2, new float[] { 0, 1 });

    }
    
}
