package com.denfop.api.bee;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class BeeAI {

    private final ActivationFunction activationFunction;
    private final Solver solver;
    private final long seed;
    private double learningRate;
    private double learningRateInit;
    private double momentum;
    private int batchSize;
    private int maxIter;
    private double tol;
    private boolean shuffle;
    private boolean earlyStopping;
    private double validationFraction;
    private int nIterNoChange;
    private int inputSize;
    private int hiddenSize;
    private int outputSize;
    private double[][] weightsInputHidden;
    private double[][] weightsHiddenOutput;
    private double[] weightUpdatesInputHidden; // For momentum
    private double[] weightUpdatesHiddenOutput; // For momentum
    private double[] mInputHidden; // First moment vector for weightsInputHidden
    private double[] vInputHidden; // Second moment vector for weightsInputHidden
    private double[] mHiddenOutput; // First moment vector for weightsHiddenOutput
    private double[] vHiddenOutput; // Second moment vector for weightsHiddenOutput
    private double beta1; // Exponential decay rate for the first moment
    private double beta2; // Exponential decay rate for the second moment
    private double epsilon; // Small constant to prevent division by zero
    private int t; // Time step
    private Random random;
    private double[] answer;

    public enum ActivationFunction {
        IDENTITY,
        LOGISTIC,
        TANH,
        RELU
    }

    public enum Solver {
        SGD,
        ADAM
    }

    public BeeAI(
            int inputSize, int hiddenSize, int outputSize, ActivationFunction activationFunction, Solver solver,
            double learningRateInit, int maxIter, boolean shuffle, double tol, double momentum, int batchSize
    ) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.learningRateInit = learningRateInit;
        this.learningRate = learningRateInit;
        this.momentum = momentum;
        this.activationFunction = activationFunction;
        this.solver = solver;
        this.maxIter = maxIter;
        this.shuffle = shuffle;
        this.tol = tol;
        this.batchSize = batchSize;
        this.weightsInputHidden = new double[inputSize][hiddenSize];
        this.weightsHiddenOutput = new double[hiddenSize][outputSize];
        this.random = new Random();
        this.seed = 4685419461166510361L;
        this.random.setSeed(seed);
        this.weightUpdatesInputHidden = new double[inputSize * hiddenSize];
        this.weightUpdatesHiddenOutput = new double[hiddenSize * outputSize];

        // Initialize Adam variables
        this.mInputHidden = new double[inputSize * hiddenSize];
        this.vInputHidden = new double[inputSize * hiddenSize];
        this.mHiddenOutput = new double[hiddenSize * outputSize];
        this.vHiddenOutput = new double[hiddenSize * outputSize];
        this.beta1 = 0.9;
        this.beta2 = 0.999;
        this.epsilon = 1e-8;
        this.t = 0;

        initializeWeights();
    }

    public double[] getAnswer() {
        return answer;
    }

    public long getSeed() {
        return seed;
    }

    private void initializeWeights() {
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsInputHidden[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightsHiddenOutput[i][j] = random.nextDouble() * 2 - 1;
            }
        }
    }

    private double activate(double x) {
        switch (activationFunction) {
            case IDENTITY:
                return x;
            case LOGISTIC:
                return 1 / (1 + Math.exp(-x));
            case TANH:
                return Math.tanh(x);
            case RELU:
                return Math.max(0, x);
            default:
                throw new IllegalArgumentException("Unknown activation function");
        }
    }

    private double activateDerivative(double x) {
        switch (activationFunction) {
            case IDENTITY:
                return 1;
            case LOGISTIC:
                return x * (1 - x);
            case TANH:
                return 1 - x * x;
            case RELU:
                return x > 0 ? 1 : 0;
            default:
                throw new IllegalArgumentException("Unknown activation function");
        }
    }

    private void shuffleData(double[][] trainingInputs, double[][] trainingOutputs) {
        for (int i = trainingInputs.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            // Swap inputs
            double[] tempInput = trainingInputs[i];
            trainingInputs[i] = trainingInputs[j];
            trainingInputs[j] = tempInput;

            // Swap outputs
            double[] tempOutput = trainingOutputs[i];
            trainingOutputs[i] = trainingOutputs[j];
            trainingOutputs[j] = tempOutput;
        }
    }

    private boolean summered = false;
    private double[] summeredInputMassive = new double[2];


    public double[] forward(double[] inputs) {

        double[] hidden = new double[hiddenSize];
        double[] output = new double[outputSize];

        for (int i = 0; i < hiddenSize; i++) {
            double sum = 0;
            for (int j = 0; j < inputSize; j++) {
                sum += inputs[j] * weightsInputHidden[j][i];
            }
            hidden[i] = activate(sum);
        }


        for (int i = 0; i < outputSize; i++) {
            double sum = 0;
            for (int j = 0; j < hiddenSize; j++) {
                sum += hidden[j] * weightsHiddenOutput[j][i];
            }
            output[i] = activate(sum);
        }

        return output;
    }

    public void train(double[][] trainingInputs, double[][] trainingOutputs) {
        double previousLoss = Double.MAX_VALUE;
        int noImprovementCount = 0;

        for (int iteration = 0; iteration < maxIter; iteration++) {
            if (shuffle) {
                shuffleData(trainingInputs, trainingOutputs);
            }

            for (int t = 0; t < trainingInputs.length; t += batchSize) {
                int currentBatchSize = Math.min(batchSize, trainingInputs.length - t);
                double[][] batchInputs = new double[currentBatchSize][inputSize];
                double[][] batchOutputs = new double[currentBatchSize][outputSize];

                for (int b = 0; b < currentBatchSize; b++) {
                    batchInputs[b] = trainingInputs[t + b];
                    batchOutputs[b] = trainingOutputs[t + b];
                }

                // Forward and backward pass for the batch
                for (int b = 0; b < currentBatchSize; b++) {
                    double[] inputs = batchInputs[b];
                    double[] expectedOutput = batchOutputs[b];
                    double[] hidden = new double[hiddenSize];
                    for (int i = 0; i < hiddenSize; i++) {
                        hidden[i] = activate(calculateHiddenOutput(inputs, weightsInputHidden, i));
                    }
                    double[] output = forward(inputs);

                    double[] outputErrors = new double[outputSize];
                    double[] outputDeltas = new double[outputSize];
                    for (int i = 0; i < outputSize; i++) {
                        outputErrors[i] = expectedOutput[i] - output[i];
                        outputDeltas[i] = outputErrors[i] * activateDerivative(output[i]);
                    }

                    double[] hiddenErrors = new double[hiddenSize];
                    double[] hiddenDeltas = new double[hiddenSize];
                    for (int i = 0; i < hiddenSize; i++) {
                        for (int j = 0; j < outputSize; j++) {
                            hiddenErrors[i] += outputDeltas[j] * weightsHiddenOutput[i][j];
                        }
                        hiddenDeltas[i] = hiddenErrors[i] * activateDerivative(hidden[i]);
                    }

                    // Update weights for output layer
                    for (int i = 0; i < hiddenSize; i++) {
                        for (int j = 0; j < outputSize; j++) {
                            double update = learningRate * outputDeltas[j] * hidden[i];
                            if (solver == Solver.ADAM) {
                                // Adam-specific updates
                                t++; // Increment time step
                                mHiddenOutput[i * outputSize + j] = beta1 * mHiddenOutput[i * outputSize + j] + (1 - beta1) * update;
                                vHiddenOutput[i * outputSize + j] = beta2 * vHiddenOutput[i * outputSize + j] + (1 - beta2) * update * update;

                                double mHat = mHiddenOutput[i * outputSize + j] / (1 - Math.pow(beta1, t));
                                double vHat = vHiddenOutput[i * outputSize + j] / (1 - Math.pow(beta2, t));

                                weightsHiddenOutput[i][j] += learningRate * mHat / (Math.sqrt(vHat) + epsilon);
                            } else { // SGD
                                weightsHiddenOutput[i][j] += update + momentum * weightUpdatesHiddenOutput[i * outputSize + j];
                                weightUpdatesHiddenOutput[i * outputSize + j] = update;
                            }
                        }
                    }

                    // Update weights for input layer
                    for (int i = 0; i < inputSize; i++) {
                        for (int j = 0; j < hiddenSize; j++) {
                            double update = learningRate * hiddenDeltas[j] * inputs[i];
                            if (solver == Solver.ADAM) {
                                // Adam-specific updates
                                mInputHidden[i * hiddenSize + j] = beta1 * mInputHidden[i * hiddenSize + j] + (1 - beta1) * update;
                                vInputHidden[i * hiddenSize + j] = beta2 * vInputHidden[i * hiddenSize + j] + (1 - beta2) * update * update;

                                double mHat = mInputHidden[i * hiddenSize + j] / (1 - Math.pow(beta1, t));
                                double vHat = vInputHidden[i * hiddenSize + j] / (1 - Math.pow(beta2, t));

                                weightsInputHidden[i][j] += learningRate * mHat / (Math.sqrt(vHat) + epsilon);
                            } else { // SGD
                                weightsInputHidden[i][j] += update + momentum * weightUpdatesInputHidden[i * hiddenSize + j];
                                weightUpdatesInputHidden[i * hiddenSize + j] = update;
                            }
                        }
                    }
                }
            }

            double loss = calculateLoss(trainingInputs, trainingOutputs);
            if (Math.abs(previousLoss - loss) < tol) {
                noImprovementCount++;
                if (noImprovementCount >= nIterNoChange) {
                    break; // Early stopping
                }
            } else {
                noImprovementCount = 0;
            }
            previousLoss = loss;
        }
    }

    public double test(double[][] testInputs, double[] testOutputs) {
        int correctPredictions = 0;
        this.answer = new double[testInputs.length];
        for (int i = 0; i < testInputs.length; i++) {
            double[] prediction = forward(testInputs[i]);
            int predictedLabel = getMaxIndex(prediction);
            int actualLabel = (int) testOutputs[i];
            answer[i] = predictedLabel * 1.0;
            if (predictedLabel == actualLabel) {
                correctPredictions++;
            }
        }


        return (double) correctPredictions / testInputs.length;
    }

    public int predict(double[] testInputs) {
        double[] prediction = forward(testInputs);
        return getMaxIndex(prediction);
    }

    // Вспомогательный метод для нахождения индекса максимального значения в массиве
    private int getMaxIndex(double[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private double calculateHiddenOutput(double[] inputs, double[][] weights, int neuronIndex) {
        double sum = 0;
        for (int j = 0; j < inputs.length; j++) {
            sum += inputs[j] * weights[j][neuronIndex];
        }
        return sum;
    }

    private double calculateLoss(double[][] inputs, double[][] outputs) {
        double loss = 0;
        for (int i = 0; i < inputs.length; i++) {
            double[] output = forward(inputs[i]);
            for (int j = 0; j < outputs[i].length; j++) {
                loss += Math.pow(outputs[i][j] - output[j], 2); // Mean squared error
            }
        }
        return loss / inputs.length; // Mean squared error
    }

    public double[] getWeightsInputHidden() {
        return flatten(weightsInputHidden);
    }

    public double[] getWeightsHiddenOutput() {
        return flatten(weightsHiddenOutput);
    }

    private double[] flatten(double[][] matrix) {
        double[] flatArray = new double[matrix.length * matrix[0].length];
        int index = 0;
        for (double[] row : matrix) {
            for (double value : row) {
                flatArray[index++] = value;
            }
        }
        return flatArray;
    }

    public static BeeAI beeAI;

    public static void init() {
        double[][] trainingInputs = {
                {0.9, 0.8, 1.0 / 7D, 0.0, 0.0},
                {0.5, 0.6, 3.0 / 7D, 0.1, 0.2},
                {0.1, 0.2, 1.0, 0.5, 0.5},
                {0.6, 0.3, 4.0 / 7D, 0.0, 0.3},
                {0.3, 0.9, 2.0 / 7D, 0.0, 0.5},
                {0.0, 0.1, 5.0 / 7D, 0.4, 0.3},
                {0.8, 0.4, 0.5 / 7D, 0.0, 0.2},
                {0.2, 0.3, 1.0 / 7D, 0.3, 0.1},
                {0.9, 0.9, 6.0 / 7D, 0.0, 0.4},
                {0.5, 0.5, 4.0 / 7D, 0.2, 0.1},
                {0.4, 0.6, 3.5 / 7D, 0.5, 0.5},
                {0.7, 0.8, 0.0 / 7D, 0.0, 0.2},
                {0.1, 0.1, 2.0 / 7D, 0.6, 0.3},
                {0.6, 0.2, 1.5 / 7D, 0.2, 0.4},
                {0.3, 0.5, 1.0, 0.0, 0.1},
                {0.8, 0.2, 1.0 / 7D, 0.0, 0.4},
                {0.8, 0.2, 1.0 / 7D, 0.3, 0.4},
                {0.8, 0.5, 1.0 / 7D, 0.0, 0.5},
                {0.7, 0.6, 3.0 / 7D, 0.0, 0.6},
                {0.7, 0.6, 3.0 / 7D, 0.0, 0.6},
                {0.6, 0.7, 3.0 / 7D, 0.0, 0.6},

                {0.7, 0.7, 3.0 / 7D, 0.0, 0.6}, // новая
                {0.7, 0.7, 5.0 / 7D, 0.0, 0.6}, // новая
                {0.7, 0.7, 6.0 / 7D, 0.0, 0.6}, // новая
                {0.7, 0.7, 7.0 / 7D, 0.0, 0.6}, // новая
                {0.7, 0.2, 5.0 / 7D, 0.0, 0.6}, // новая
                {0.6, 0.2, 5.0 / 7D, 0.0, 0.6}, // новая
                {0.6, 0.2, 5.0 / 7D, 0.0, 0.6}, // новая
                {0.1, 0.2, 5.0 / 7D, 0.0, 0.6}, // новая
                {0.1, 0.2, 5.0 / 7D, 0.0, 0.1}, // новая
                {0.1, 0.2, 5.0 / 7D, 0.0, 0.1}, // новая
                {0.1, 0.2, 5.0 / 7D, 0.0, 0.1}, // новая
                {0.1, 0.2, 5.0 / 7D, 0.1, 0.1}, // новая
                {0.1, 0.2, 4.0 / 7D, 0.1, 0.1}, // новая
                {0.1, 0.2, 3.0 / 7D, 0.1, 0.1}, // новая
                {0.2, 0.2, 3.0 / 7D, 0.1, 0.2}, // новая
                {0.2, 0.2, 3.0 / 7D, 0.2, 0.2}, // новая
                {0.2, 0.2, 3.0 / 7D, 0.3, 0.2}, // новая
                {0.3, 0.2, 3.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.2, 3.0 / 7D, 0.2, 0.3}, // новая
                {0.4, 0.2, 2.0 / 7D, 0.1, 0.3}, // новая
                {0.4, 0.2, 2.0 / 7D, 0.1, 0.3}, // новая
                {0.4, 0.2, 3.0 / 7D, 0.1, 0.3}, // новая
                {0.4, 0.2, 4.0 / 7D, 0.1, 0.3}, // новая
                {0.4, 0.2, 4.0 / 7D, 0.2, 0.3}, // новая
                {0.4, 0.2, 4.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.2, 5.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.1, 5.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.1, 6.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.1, 7.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.1, 7.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.3, 7.0 / 7D, 0.3, 0.3}, // новая
                {0.4, 0.3, 7.0 / 7D, 0.4, 0.3}, // новая
                {0.4, 0.3, 7.0 / 7D, 0.5, 0.3}, // новая
                {0.4, 0.3, 6.0 / 7D, 0.5, 0.3}, // новая
                {0.4, 0.3, 0.0 / 7D, 0.6, 0.3}, // новая
                {0.5, 0.6, 0.0 / 7D, 0.6, 0.6}, // новая
                {0.5, 0.6, 0.0 / 7D, 0.5, 0.6}, // новая
                {0.3, 0.6, 0.0 / 7D, 0.5, 0.6}, // новая
                {0.2, 0.6, 0.0 / 7D, 0.5, 0.6}, // новая
                {0.2, 0.6, 0.0 / 7D, 0.5, 0.5}, // новая
                {0.2, 0.6, 0.0 / 7D, 0.5, 0.5}, // новая
                {0.1, 0.6, 0.0 / 7D, 0.5, 0.5}, // новая
                {0.1, 0.6, 0.0 / 7D, 0.5, 0.4}, // новая
                ////////////////////////////
                {0.75, 0.65, 1.0 / 7D, 0.1, 0.2},
                {0.55, 0.7, 2.0 / 7D, 0.3, 0.1},
                {0.2, 0.25, 1.0 / 7D, 0.4, 0.5},
                {0.4, 0.35, 5.0 / 7D, 0.1, 0.3},
                {0.6, 0.9, 1.0 / 7D, 0.2, 0.4},
                {0.1, 0.05, 4.0 / 7D, 0.5, 0.2},
                {0.85, 0.55, 2.0 / 7D, 0.0, 0.1},
                {0.25, 0.45, 3.0 / 7D, 0.2, 0.1},
                {0.95, 0.85, 5.0 / 7D, 0.1, 0.3},
                {0.5, 0.55, 4.0 / 7D, 0.1, 0.2},
                {0.45, 0.65, 2.5 / 7D, 0.4, 0.5},
                {0.65, 0.8, 0.0 / 7D, 0.2, 0.2},
                {0.15, 0.15, 3.0 / 7D, 0.3, 0.4},
                {0.5, 0.3, 1.5 / 7D, 0.2, 0.3},
                {0.35, 0.4, 1.0 / 7D, 0.1, 0.1},
                {0.75, 0.15, 1.0 / 7D, 0.1, 0.4},
                {0.9, 0.3, 1.0 / 7D, 0.1, 0.3},
                {0.6, 0.45, 1.0 / 7D, 0.2, 0.5},
                {0.8, 0.55, 2.0 / 7D, 0.3, 0.6},
                {0.6, 0.7, 3.0 / 7D, 0.1, 0.5},
                {0.5, 0.75, 3.0 / 7D, 0.2, 0.4},
                ////////////////////
                {0.8, 0.65, 3.0 / 7D, 0.0, 0.5}, // новый
                {0.9, 0.7, 2.0 / 7D, 0.1, 0.5}, // новый
                {0.85, 0.72, 5.0 / 7D, 0.0, 0.4}, // новый
                {0.75, 0.73, 4.0 / 7D, 0.1, 0.5}, // новый
                {0.55, 0.68, 6.0 / 7D, 0.0, 0.5}, // новый
                {0.6, 0.5, 3.0 / 7D, 0.1, 0.4}, // новый
                {0.55, 0.55, 4.0 / 7D, 0.2, 0.5}, // новый
                {0.25, 0.25, 5.0 / 7D, 0.3, 0.5}, // новый
                {0.15, 0.3, 5.0 / 7D, 0.2, 0.5}, // новый
                {0.35, 0.2, 3.0 / 7D, 0.1, 0.5}, // новый
                {0.4, 0.5, 4.0 / 7D, 0.2, 0.3}, // новый
                {0.7, 0.4, 2.0 / 7D, 0.2, 0.3}, // новый
                {0.45, 0.55, 3.0 / 7D, 0.1, 0.5}, // новый
                {0.3, 0.6, 3.0 / 7D, 0.1, 0.3}, // новый
                {0.1, 0.6, 4.0 / 7D, 0.1, 0.3}, // новый
                {0.2, 0.6, 5.0 / 7D, 0.1, 0.2}, // новый
                {0.8, 0.6, 5.0 / 7D, 0.1, 0.1}, // новый
                {0.9, 0.5, 6.0 / 7D, 0.1, 0.1}, // новый
                {0.75, 0.6, 7.0 / 7D, 0.1, 0.2}, // новый
                {0.3, 0.4, 6.0 / 7D, 0.0, 0.4}, // новый
                {0.2, 0.5, 2.0 / 7D, 0.0, 0.5}, // новый
                {0.15, 0.35, 4.0 / 7D, 0.0, 0.4}, // новый
                {0.4, 0.6, 2.0 / 7D, 0.0, 0.4}, // новый
                {0.6, 0.7, 5.0 / 7D, 0.1, 0.4}, // новый
                {0.5, 0.8, 3.0 / 7D, 0.1, 0.3}, // новый
                {0.4, 0.9, 4.0 / 7D, 0.1, 0.3}, // новый
                {0.4, 0.2, 1.0 / 7D, 0.1, 0.3}, // новый
                {0.6, 0.4, 2.0 / 7D, 0.0, 0.4}, // новый
                {0.5, 0.5, 3.0 / 7D, 0.0, 0.2}, // новый
                {0.75, 0.4, 3.0 / 7D, 0.1, 0.4}, // новый
                {0.85, 0.3, 3.0 / 7D, 0.0, 0.5}, // новый
                {0.7, 0.5, 4.0 / 7D, 0.1, 0.2}, // новый
                {0.55, 0.4, 5.0 / 7D, 0.1, 0.2}, // новый
                {0.8, 0.6, 2.0 / 7D, 0.0, 0.3}, // новый
                //////////////////////////////////////
                {0.1, 0.3, 1.0 / 7D, 0.1, 0.1}, // новый
                {0.2, 0.1, 5.0 / 7D, 0.1, 0.4}, // новый
                {0.25, 0.15, 3.0 / 7D, 0.2, 0.3}, // новый
                {0.35, 0.25, 1.0 / 7D, 0.1, 0.3}, // новый
                {0.15, 0.35, 2.0 / 7D, 0.0, 0.3}, // новый
                {0.4, 0.3, 4.0 / 7D, 0.2, 0.4}, // новый
                {0.5, 0.4, 5.0 / 7D, 0.1, 0.4}, // новый
                {0.3, 0.5, 1.0 / 7D, 0.1, 0.3}, // новый
                {0.8, 0.2, 3.0 / 7D, 0.1, 0.2}, // новый
                {0.4, 0.6, 1.0 / 7D, 0.1, 0.3}, // новый
                {0.2, 0.1, 4.0 / 7D, 0.2, 0.4}, // новый
                {0.1, 0.3, 1.0 / 7D, 0.1, 0.2}, // новый
                {0.2, 0.4, 1.0 / 7D, 0.0, 0.4}, // новый
                {0.4, 0.6, 2.0 / 7D, 0.1, 0.3}, // новый
                {0.5, 0.7, 1.0 / 7D, 0.1, 0.2}, // новый
                {0.9, 0.8, 2.0 / 7D, 0.0, 0.4}, // новый
                {0.7, 0.9, 2.0 / 7D, 0.1, 0.3}, // новый
                {0.4, 0.5, 3.0 / 7D, 0.0, 0.3}, // новый
                {0.6, 0.4, 5.0 / 7D, 0.1, 0.5}, // новый
                {0.3, 0.3, 1.0 / 7D, 0.1, 0.4}, // новый
                //////////////////////////////////////////
                {0.75, 0.65, 1.0 / 7D, 0.1, 0.2},
                {0.55, 0.7, 2.0 / 7D, 0.3, 0.1},
                {0.2, 0.25, 1.0 / 7D, 0.4, 0.5},
                {0.4, 0.35, 5.0 / 7D, 0.1, 0.3},
                {0.6, 0.9, 1.0 / 7D, 0.2, 0.4},
                {0.1, 0.3, 1.0, 0.4, 0.5},
                {0.2, 0.8, 0.0 / 7D, 0.3, 0.2},
                {0.8, 0.5, 0.5 / 7D, 0.1, 0.1},
                {0.9, 0.6, 3.0 / 7D, 0.0, 0.0},
                {0.5, 0.4, 2.0 / 7D, 0.2, 0.3},
                {0.4, 0.3, 6.0 / 7D, 0.1, 0.4},
                {0.6, 0.2, 4.0 / 7D, 0.0, 0.5},
                {0.3, 0.4, 1.0 / 7D, 0.5, 0.6},
                {0.5, 0.7, 5.0 / 7D, 0.2, 0.1},
                {0.8, 0.5, 3.0 / 7D, 0.1, 0.2},
                {0.2, 0.1, 1.0 / 7D, 0.3, 0.3},
                {0.4, 0.5, 4.0 / 7D, 0.1, 0.2},
                {0.7, 0.9, 2.0 / 7D, 0.4, 0.1},
                {0.9, 0.8, 6.0 / 7D, 0.3, 0.4},
                {0.1, 0.2, 0.0 / 7D, 0.2, 0.3},
                {0.6, 0.3, 1.0 / 7D, 0.4, 0.2},
                {0.5, 0.2, 3.0 / 7D, 0.0, 0.1},
                {0.3, 0.3, 5.0 / 7D, 0.0, 0.3},


                {0.4, 0.5, 2.0 / 7D, 0.5, 0.1},
                {0.8, 0.6, 1.0 / 7D, 0.3, 0.2},
                {0.5, 0.9, 4.0 / 7D, 0.2, 0.2},
                {0.2, 0.2, 1.0 / 7D, 0.4, 0.3},
                {0.3, 0.1, 3.0 / 7D, 0.3, 0.1},
                {0.6, 0.4, 6.0 / 7D, 0.0, 0.4},
                {0.1, 0.2, 0.0 / 7D, 0.5, 0.5},
                {0.4, 0.4, 2.0 / 7D, 0.2, 0.1},
                {0.5, 0.7, 1.0 / 7D, 0.0, 0.3},
                {0.3, 0.2, 4.0 / 7D, 0.0, 0.2},
                {0.1, 0.4, 0.5 / 7D, 0.3, 0.4},
                {0.2, 0.1, 2.0 / 7D, 0.4, 0.3},
                {0.4, 0.8, 1.0 / 7D, 0.5, 0.5},
                {0.9, 0.9, 5.0 / 7D, 0.2, 0.3},
                {0.7, 0.6, 3.0 / 7D, 0.3, 0.4},
                {0.5, 0.5, 2.0 / 7D, 0.4, 0.1},
                {0.4, 0.3, 1.0 / 7D, 0.2, 0.3},
                {0.8, 0.7, 3.0 / 7D, 0.0, 0.2},
                {0.6, 0.4, 5.0 / 7D, 0.3, 0.5},
                {0.3, 0.1, 2.0 / 7D, 0.2, 0.4},
                {0.4, 0.5, 1.0 / 7D, 0.1, 0.3},
                {0.7, 0.6, 6.0 / 7D, 0.1, 0.2},
                {0.3, 0.5, 2.0 / 7D, 0.0, 0.1},
                {0.5, 0.2, 3.0 / 7D, 0.4, 0.4},
                {0.4, 0.6, 5.0 / 7D, 0.3, 0.5},
                {0.9, 0.9, 1.0 / 7D, 0.5, 0.3},
                {0.1, 0.2, 3.0 / 7D, 0.3, 0.1},
                //////
                {0.3, 0.6, 4.0 / 7D, 0.5, 0.4},
                {0.5, 0.9, 0.0 / 7D, 0.0, 0.1},
                {0.4, 0.5, 2.0 / 7D, 0.2, 0.2},
                {0.5, 0.3, 1.0, 0.3, 0.3},
                {0.2, 0.2, 6.0 / 7D, 0.0, 0.1},
                {0.6, 0.4, 5.0 / 7D, 0.2, 0.5},
                {0.8, 0.3, 3.0 / 7D, 0.4, 0.4},
                {0.9, 0.9, 1.0, 0.1, 0.2},
                {0.5, 0.6, 4.0 / 7D, 0.1, 0.5},
                {0.4, 0.7, 0.5 / 7D, 0.0, 0.4},
                {0.3, 0.8, 1.0 / 7D, 0.3, 0.2},
                {0.8, 0.5, 1.0 / 7D, 0.0, 0.1},
                {0.7, 0.6, 2.0 / 7D, 0.4, 0.5},
                {0.5, 0.9, 0.0 / 7D, 0.2, 0.3},
                {0.2, 0.2, 1.0 / 7D, 0.1, 0.5},
                {0.1, 0.3, 4.0 / 7D, 0.0, 0.6},
                {0.2, 0.1, 5.0 / 7D, 0.0, 0.4},
                {0.8, 0.2, 2.0 / 7D, 0.3, 0.3},
                {0.5, 0.2, 1.0 / 7D, 0.4, 0.1},
                {0.4, 0.6, 3.0 / 7D, 0.1, 0.5},
                {0.6, 0.1, 6.0 / 7D, 0.2, 0.4},
                {0.3, 0.3, 0.0 / 7D, 0.0, 0.6},
                {0.7, 0.5, 5.0 / 7D, 0.1, 0.2},
                {0.5, 0.5, 1.0 / 7D, 0.4, 0.3},
                {0.2, 0.2, 2.0 / 7D, 0.1, 0.2},
                {0.7, 0.3, 4.0 / 7D, 0.0, 0.4},
                {0.6, 0.4, 0.0 / 7D, 0.3, 0.3},
                {0.5, 0.3, 3.0 / 7D, 0.4, 0.2},
                {0.1, 0.1, 6.0 / 7D, 0.0, 0.5},
                {0.9, 0.7, 5.0 / 7D, 0.3, 0.1},
                {0.4, 0.2, 0.0 / 7D, 0.4, 0.4}, //
                {0.5, 0.6, 1.0 / 7D, 0.2, 0.3},
                {0.6, 0.9, 4.0 / 7D, 0.1, 0.5},
                {0.3, 0.4, 5.0 / 7D, 0.3, 0.2},
                {0.2, 0.3, 2.0 / 7D, 0.0, 0.5},
                {0.1, 0.1, 3.0 / 7D, 0.2, 0.6},
                {0.4, 0.5, 1.0 / 7D, 0.3, 0.4},
                {0.5, 0.7, 0.0 / 7D, 0.0, 0.3},
                {0.6, 0.6, 3.0 / 7D, 0.2, 0.4},
                {0.8, 0.2, 4.0 / 7D, 0.1, 0.1},
                {0.9, 0.8, 5.0 / 7D, 0.0, 0.1},
                {0.3, 0.3, 6.0 / 7D, 0.2, 0.5},

                {0.3, 0.6, 4.0 / 7D, 0.5, 0.4},
                {0.5, 0.9, 0.0 / 7D, 0.0, 0.1},
                {0.4, 0.5, 2.0 / 7D, 0.2, 0.2},
                {0.5, 0.3, 1.0, 0.3, 0.3},
                {0.2, 0.2, 6.0 / 7D, 0.0, 0.1},
                {0.6, 0.4, 5.0 / 7D, 0.2, 0.5},
                {0.8, 0.3, 3.0 / 7D, 0.4, 0.4},
                {0.9, 0.9, 1.0, 0.1, 0.2},
                {0.5, 0.6, 4.0 / 7D, 0.1, 0.5},
                {0.4, 0.7, 0.5 / 7D, 0.0, 0.4},
                {0.3, 0.8, 1.0 / 7D, 0.3, 0.2},
                {0.8, 0.5, 1.0 / 7D, 0.0, 0.1},
                {0.7, 0.6, 2.0 / 7D, 0.4, 0.5},
                {0.5, 0.9, 0.0 / 7D, 0.2, 0.3},
                {0.2, 0.2, 1.0 / 7D, 0.1, 0.5},
                {0.1, 0.3, 4.0 / 7D, 0.0, 0.6},
                {0.2, 0.1, 5.0 / 7D, 0.0, 0.4},
                {0.8, 0.2, 2.0 / 7D, 0.3, 0.3},
                {0.5, 0.2, 1.0 / 7D, 0.4, 0.1},
                {0.4, 0.6, 3.0 / 7D, 0.1, 0.5},
                {0.6, 0.1, 6.0 / 7D, 0.2, 0.4},
                {0.3, 0.3, 0.0 / 7D, 0.0, 0.6},
                {0.7, 0.5, 5.0 / 7D, 0.1, 0.2},
                {0.5, 0.5, 1.0 / 7D, 0.4, 0.3},
                {0.2, 0.2, 2.0 / 7D, 0.1, 0.2},
                {0.7, 0.3, 4.0 / 7D, 0.0, 0.4},
                {0.6, 0.4, 0.0 / 7D, 0.3, 0.3},
                {0.5, 0.3, 3.0 / 7D, 0.4, 0.2},
                {0.1, 0.1, 6.0 / 7D, 0.0, 0.5},
                {0.9, 0.7, 5.0 / 7D, 0.3, 0.1},
                {0.4, 0.2, 0.0 / 7D, 0.4, 0.4}, //
                {0.5, 0.6, 1.0 / 7D, 0.2, 0.3},
                {0.6, 0.9, 4.0 / 7D, 0.1, 0.5},
                {0.3, 0.4, 5.0 / 7D, 0.3, 0.2},
                {0.2, 0.3, 2.0 / 7D, 0.0, 0.5},
                {0.1, 0.1, 3.0 / 7D, 0.2, 0.6},
                {0.4, 0.5, 1.0 / 7D, 0.3, 0.4},
                {0.5, 0.7, 0.0 / 7D, 0.0, 0.3},
                {0.6, 0.6, 3.0 / 7D, 0.2, 0.4},
                {0.8, 0.2, 4.0 / 7D, 0.1, 0.1},
                {0.9, 0.8, 5.0 / 7D, 0.0, 0.1},
                {0.3, 0.3, 6.0 / 7D, 0.2, 0.5},


        };
        String[] tasks = {
                "Собирать еду",      // 0
                "Защищать улей",     // 1
                "Лечить пчел",       // 2
                "Увеличивать рой",   // 3
                "Отдыхать"          // 4
        };

        double[][] trainingOutputs = {
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1}, // новая
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                /////////////////
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                ///////////////
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                ////////////////////
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                ////////////////
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},//
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},

                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0}, //
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                /////////////
                {0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0}, //
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                /////////////
                {0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0}, //
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
        };


        // дождь, процент їжі, процент бджіл, відстань від ближчих мобів, процент хворих, процент маточного молока, шанс на полёт
        double[][] testInputs = {

                {0.4, 0.5, 1.5 / 7D, 0.1, 0.3},
                {0.9, 0.9, 0.0 / 7D, 0.0, 0.3},
                {0.5, 0.4, 5.0 / 7D, 0.2, 0.4},
                {0.7, 0.3, 2.0 / 7D, 0.0, 0.4},
                {0.9, 0.6, 1.0 / 7D, 0.0, 0.5},  // Новый случай
                {0.8, 0.3, 2.5 / 7D, 0.1, 0.3},  // Нет дождя, высокая еда 15
                {0.4, 0.5, 0.0 / 7D, 0.3, 0.4},  // Хорошие условия для пчел 18
                {0.3, 0.2, 1.0, 0.0, 0.3},  // Условия далеки от оптимальных 19
                {0.5, 0.1, 2.0 / 7D, 0.2, 0.1},  // Пчелы не в лучшем состоянии 20
                {0.7, 0.9, 5.0 / 7D, 0.0, 0.2},  // Хорошие условия для пчел с дождем 21
                {0.1, 0.9, 5.0 / 7D, 0.0, 0.2},
                {0.1, 0.9, 5.0 / 7D, 0.0, 0.2},
                {0.1, 0.9, 5.0 / 7D, 0.0, 0.2},
                {0.1, 0.9, 5.0 / 7D, 0.1, 0.2},
                {0.1, 0.9, 6.0 / 7D, 0.1, 0.2},
                {0.1, 0.9, 7.0 / 7D, 0.1, 0.2},
                {0.2, 0.9, 7.0 / 7D, 0.1, 0.3},
                {0.2, 0.9, 6.0 / 7D, 0.2, 0.3},
                {0.2, 0.9, 6.0 / 7D, 0.3, 0.3},
                {0.1, 0.9, 6.0 / 7D, 0.3, 0.3},
                {0.1, 0.9, 5.0 / 7D, 0.3, 0.3},
                {0.2, 0.9, 5.0 / 7D, 0.3, 0.3},
                {0.2, 0.9, 5.0 / 7D, 0.4, 0.3},
                {0.2, 0.9, 5.0 / 7D, 0.4, 0.3},
                {0.2, 0.1, 5.0 / 7D, 0.4, 0.3},
                {0.2, 0.1, 2.0 / 7D, 0.3, 0.3},
                {0.2, 0.1, 2.0 / 7D, 0.2, 0.3},
                {0.2, 0.1, 0.0 / 7D, 0, 0.3},
                {0.4, 0.1, 0.0 / 7D, 0, 0.4},
                {0.4, 0.6, 0.0 / 7D, 0, 0.4},
                {0.4, 0.6, 0.0 / 7D, 0, 0.4},
                /////
                {0.8, 0.3, 1.0 / 7D, 0.0, 0.1},
                {0.2, 0.4, 3.0 / 7D, 0.1, 0.4},
                {0.6, 0.5, 2.0 / 7D, 0.2, 0.2},
                {0.3, 0.9, 5.0 / 7D, 0.1, 0.1},
                {0.5, 0.4, 4.0 / 7D, 0.3, 0.3},
                {0.7, 0.1, 0.0 / 7D, 0.4, 0.2},
                {0.4, 0.7, 6.0 / 7D, 0.0, 0.4},
                {0.9, 0.8, 1.0, 0.1, 0.3},
                {0.1, 0.2, 2.0 / 7D, 0.2, 0.1},
                {0.2, 0.3, 5.0 / 7D, 0.0, 0.5},
                {0.6, 0.8, 3.0 / 7D, 0.1, 0.2},
                ///////
                {0.9, 0.8, 2.0 / 7D, 0.1, 0.3},
                {0.4, 0.2, 2.0 / 7D, 0.2, 0.1},
                {0.5, 0.3, 5.0 / 7D, 0.0, 0.5},
                {0.6, 0.8, 3.0 / 7D, 0, 0.6},

                {0.6, 0.6, 3.0 / 7D, 0.2, 0.4},
                {0.3, 0.3, 6.0 / 7D, 0.2, 0.5},
                {0.6, 0.8, 6.0 / 7D, 0, 0.8},

                {0.2, 0.4, 1.0 / 7D, 0.0, 0.4}, // новый
                {0.4, 0.6, 2.0 / 7D, 0.1, 0.3}, // новый
                {0.5, 0.2, 3.0 / 7D, 0.0, 0.1},
                {0, 0.1, 0 / 7D, 0.0, 0.0},
                {0, 0.2, 0 / 7D, 0.0, 0.0},
                {0.1, 0.2, 0 / 7D, 0.0, 0.1},
                {0.1, 0.2, 0 / 7D, 0.0, 0.1},
                {0.7, 0.2, 0 / 7D, 0.0, 0.7},
                {0.5, 0.2, 0 / 7D, 0.0, 0.6},
                {0.5, 0.2, 3.0 / 7D, 0.0, 0.0},
                {0.5, 0.2, 6.0 / 7D, 0.0, 0.0},
        };

        double[] outputTest = new double[]{
                2,  // Пример 2
                4,  // Пример 5
                2,  // Пример 7
                3,  // Пример 10
                4,  // Пример 11
                3,  // Пример 15
                2,  // Пример 18
                3,  // Пример 19
                3,  // Пример 20
                0,   // Пример 21
                0,  // Пример 22
                0,  // Пример 23
                0,  // Пример 24
                0,  // Пример 25
                0,  // Пример 26
                1,  // Пример 27
                1,  // Пример 28
                0,  // Пример 29
                2,  // Пример 30
                0,  // Пример 31
                0,  // Пример 32
                2,  // Пример 33
                2,  // Пример 34
                2,  // Пример 35
                3,  // Пример 36
                3,  // Пример 37
                3,  // Пример 38
                3,  // Пример 39
                3,  // Пример 40
                4,  // Пример 41
                4,  // Пример 42
                0, // 43
                0, // 44
                0, // 45
                0, // 46
                2, // 47
                3, // 48
                1, // 49
                1, // 50
                0, // 51
                0, // 52
                2, // 53
                2, // 54
                3, // 55
                3, // 56
                4, // 57
                2, // 58
                2, // 61
                1, // 62
                0, // 63
                2,
                0, // 63
                0, // 64
                0, // 65
                0, // 66
                0, // 67
                3, // 66
                3, // 67
                0, // 68
                0, // 69

        };
        double percent = 0;
        int attempt = 0;
        int bestattempt = 0;
        BeeAI bestPerceptron = null;
        double bestPercent = 0;
        long startTime = System.currentTimeMillis();
        beeAI = new BeeAI(5, 50, 5, ActivationFunction.TANH, Solver.SGD, 0.025, 128, true, 1e-3, 0.95, 1);
        beeAI.weightsInputHidden[0] = new double[]{-0.6123375857948672, -0.5658981213023044, 0.0974483754412152, 0.5308559610728447, -0.41693582193707457, 0.3537565742711275, 1.3434527200105957, -0.45021277409535077, 0.2053855586870333, 0.28404766166028406, 0.4217145335272433, 0.4701639969167235, 0.062266952568260196, 0.38885943153663255, -0.9067211548920927, -0.03048539950985502, 1.7925055789531539, -0.09747098116468456, -0.37506285270640977, -0.9652122157723461, 0.38056219893456283, 0.6691257832082957, -0.2758049101247871, -0.13430892942024736, -0.6379246885835392, 0.19599782371176955, 0.1984386632280184, 0.5405995654369473, 0.37216712300157884, 0.7668871030735012, 0.0737210764935781, 0.10051349433262491, -0.747054273393504, -0.29669936910645145, -0.30239730337357273, -0.3033089415927927, -0.8070199598224678, -0.7846500068864927, -0.787191346733449, 1.4495366744457323, -0.8707390291112523, 0.41801242031312397, 0.8204519984342101, 0.5143335476635075, -0.025894867496949456, -0.14573630692206116, -0.9664438605080616, 1.3992089694480208, -4.331574805095261, 0.458076716680923 };
        beeAI.weightsInputHidden[1] = new double[]{0.11127530758111025, 0.5958655135048775, 0.500199889285599, -0.5801057422424257, 0.37877341479299786, -0.7691537766537478, 2.188120293684022, 0.3513731949883791, -0.6348124791508376, -0.8997171202534774, -0.40947860651998236, -0.05997777190806985, -0.22536309204851387, -1.2928179826990158, 3.211500442135601, -0.4008042556102194, -0.8041072852344644, 0.3776737721412062, 0.2719798370948174, 0.2938437171582487, 0.8806865490646101, -0.04337383243403114, -0.29221176416210987, -0.1369328921221021, -0.1610892632800529, -0.49030276504052805, 0.32534510761966706, -0.06670043039143651, -1.0074088059099326, 0.5117902745293386, -0.19487436715671175, 0.3239522937088819, 0.7809621663306975, -0.1411881889767716, 0.6111593797641969, -0.12492567219285101, -0.39571035239057567, 0.005410726827506058, 0.5799827705495348, -0.46634566288856555, 1.1043449435901909, 0.05822436612186946, 0.14504042079013316, -0.3384957105474495, -0.34403632852683314, -0.21489609358802025, 0.6597161213169324, -0.7662762254617169, 0.51096508213993, -0.42769363437341273};
        beeAI.weightsInputHidden[2] = new double[]{0.3330701167655725, -0.3792565795295458, -0.31360361146977883, -0.4029673390292618, 0.2713535019303578, 0.2491521231240939, 2.050047561833419, 0.03628084568948384, -0.18054883142505151, -0.19100279228793368, 1.495841057712744, 0.2687829340206771, 0.43779209132167063, -1.000319922804089, 0.5852583693589413, 1.3459038089649136, 0.44558412696904864, -0.24342311108281398, 0.37499804077291027, 0.5245088864121464, -1.1133122404287699, -0.4446124678434421, -0.4695627221006776, -0.05691650053436046, -0.33572939986546024, 0.18603424345068187, -0.6616772169272077, -0.3091237837870309, 0.32952471891966806, -0.45209537677227757, -0.0880995432499414, -0.5100090981757582, -0.026297286729497323, 0.5718060458830942, 0.3159556964400011, 0.34800017990998233, 0.5435068959293851, 0.9169807460753568, 0.05299893729841556, -0.6224843272872699, -0.1692351356089454, -0.5056725412668555, -0.3310062122693127, 0.042602459361983445, 0.2266396539957613, 1.0525596159174704, 0.5981703596794122, 1.1696929243572611, -0.6904624863263752, -0.20488434900435973 };
        beeAI.weightsInputHidden[3] = new double[]{0.1578790742882585, 0.274842526974044, -0.025589443705421093, -0.11313939860521247, -0.9835724764837759, 0.3768833573910964, 0.31975728206679616, -0.013940601086473336, 0.6711915207928967, -1.0514763429172218, 0.10982235082589076, -0.7921101459216118, -0.459730957900185, 0.3413085506354481, 1.4317556435429304, 0.4028121696762229, -0.9987633631230943, -0.6246574650113234, -0.07664938249729807, -0.650965893326102, 1.0068216166252106, 1.6188452688484272, -0.9356523856524878, 0.13267873023523472, -0.22275798607729472, 0.6596898227519443, 0.7596143135282326, 0.04392864849368739, -0.07091393104958484, -1.7661114697793294, 0.1763749794306232, 0.04600480191091868, 0.3224341081503123, 0.6608760843458482, -0.9840875550911401, 0.027950000197703054, 0.11051025731166286, 0.2608384806561938, -1.013810368573967, -1.912522323426427, 0.11028458199852396, -0.007230921734355188, -1.147883329376884, 0.35529543553893894, -0.34881964934115556, 1.3772755847343314, -0.09435304148840876, -1.402166103663657, 0.040729114689012795, 0.3086385392589057 };
        beeAI.weightsInputHidden[4] = new double[]{0.6966343524113282, 0.5451278738790426, -0.27383559715165556, 0.45867649054011445, 0.0589657624020563, 0.5397652783835947, 1.2790452187616688, 0.5976180267023902, 0.38092008434913205, 0.316335602100132, -0.15625672805417667, -0.3911726060548059, -0.05602316684228256, 0.240162137763842, -0.6135950273409942, -0.7412652529360557, -1.375840758645294, 0.4378973684407905, -0.4458338452174241, 0.830776336147518, -0.48881398065193743, 1.2597297592933168, 1.2928949644415901, 0.4881446552519976, 1.4068432000829727, -0.25447681058446314, -0.1845801943403738, -0.4358945625202506, 0.23479639611536787, 0.9059926548996625, 0.07165383811092164, -0.07013344375527937, 0.2411282055484601, -0.7751185139417489, -0.3949417599072628, 0.26924391630897593, 0.5584749111447797, 2.5322322642959905, 0.6362048256267137, -0.505797503618192, -0.3781252782868911, 0.010611513713993012, 0.2979033124656167, -0.5430317314985437, 0.5616531245993942, -0.788862046095608, -0.05250776096142891, -1.7583926639099159, 1.2442452327144213, -0.20363116996770927 };
        beeAI.weightsHiddenOutput[0] = new double[]{0.03371667916905146, 0.40213397987036137, 0.03887249309387919, 0.3116227146615621, 0.2540019683278715 };
        beeAI.weightsHiddenOutput[1] = new double[]{0.025042382889258225, -0.11552124519775878, -0.025179137859823838, -0.06653908927627646, 0.01701175349084135 };
        beeAI.weightsHiddenOutput[2] = new double[]{-0.16109599996892718, 0.009754244322197193, -0.10491650496319194, 0.01918415888654419, 0.04514238492777206 };
        beeAI.weightsHiddenOutput[3] = new double[]{0.4438507437884789, -0.1006372150172551, 0.41354006805810556, 0.27832989691739296, 0.4777473815507421 };
        beeAI.weightsHiddenOutput[4] = new double[]{-0.31867232912627086, -0.28661685195376985, -0.3757978250988507, -0.5610662101057609, -0.14327743430363896 };
        beeAI.weightsHiddenOutput[5] = new double[]{0.035178890881118516, 0.35495509644111317, 0.22607646388923383, -0.27385464836039053, 0.0743837234248935 };
        beeAI.weightsHiddenOutput[6] = new double[]{2.438002781233153, -0.13544173337104645, -1.2878969050554863, 1.1340160579435736, -0.2497273858889447 };
        beeAI.weightsHiddenOutput[7] = new double[]{0.17228298092294067, 0.42475772708422915, 0.1596969278024204, 0.37598882067421374, 0.32467404220960694 };
        beeAI.weightsHiddenOutput[8] = new double[]{-0.28322876620255305, -0.308068030445568, -0.2277663242576937, -0.2981106367841022, -0.41007246031770717 };
        beeAI.weightsHiddenOutput[9] = new double[]{0.021244512825171546, -0.8038725386314263, 0.09009971214706416, 0.5980174192675283, 0.2584211449132078 };
        beeAI.weightsHiddenOutput[10] = new double[]{0.2586993588425983, -0.8122064832462538, 0.6928337343813393, -0.3849576076074629, 0.2860093669156676 };
        beeAI.weightsHiddenOutput[11] = new double[]{0.16677743466399886, 0.5478646576417712, 0.1527704799155593, -0.2331276922340449, 0.21363341874189168 };
        beeAI.weightsHiddenOutput[12] = new double[]{0.21818661308916437, 0.4556660869404976, 0.1680042395563399, 0.07851973680712818, 0.1135844848080505 };
        beeAI.weightsHiddenOutput[13] = new double[]{-0.8062200866031817, 1.0232406008249377, 0.1341266205301754, 0.14564772277060634, -0.7367687956076368};
        beeAI.weightsHiddenOutput[14] = new double[]{0.375664621560029, 0.1671279049117032, 0.7499136745668273, -1.7567234189877852, 0.18149079803250803};
        beeAI.weightsHiddenOutput[15] = new double[]{0.3678942740489441, -0.2494201644671409, 0.5269970693294461, 0.6362888985156624, 0.2581944574060748 };
        beeAI.weightsHiddenOutput[16] = new double[]{-0.014458648907888224, 0.40972726657639774, -0.6882804872654519, -0.00406389717110564, 0.11735911239710238 };
        beeAI.weightsHiddenOutput[17] = new double[]{0.07634581004694396, 0.174417043559698, 0.11454073904699448, 0.2650310700902883, 0.09905819910281624 };
        beeAI.weightsHiddenOutput[18] = new double[]{-0.10390824655955105, 0.046538759667825676, -0.10963690329422804, 0.11230180949063047, -0.21813710193259145 };
        beeAI.weightsHiddenOutput[19] = new double[]{-0.3512362853963244, 0.2599078653370985, -0.1996379448713675, 0.23130791696882114, 0.02131643189764256 };
        beeAI.weightsHiddenOutput[20] = new double[]{-0.1991659445272788, -0.37302036985310577, 0.21106735741297766, -0.10061913497341636, 0.25786947651774533};
        beeAI.weightsHiddenOutput[21] = new double[]{-1.0421377488022785, 0.3699466720279545, -0.8463293667021103, 0.3978811938087139, -0.1793876125862239};
        beeAI.weightsHiddenOutput[22] = new double[]{0.18627468767378674, -0.6341927549566663, -0.34134528370171896, -0.2883374149266033, 0.13806947150602245 };
        beeAI.weightsHiddenOutput[23] = new double[]{0.38223140328155336, 0.12162976186874128, 0.28228431450128333, 0.16973595699236377, 0.22272387148201792 };
        beeAI.weightsHiddenOutput[24] = new double[]{0.4781644760787568, -0.33342594219501664, 0.18613057135888875, 0.5689813891120304, 0.2929924907694147 };
        beeAI.weightsHiddenOutput[25] = new double[]{-0.08965893542999463, -0.08887740060299146, -0.014244141007324051, -0.17052839137701173, -0.20284806198384225 };
        beeAI.weightsHiddenOutput[26] = new double[]{0.10698890025788468, -0.3268023356546963, 0.12722809128992577, 0.13089223141517356, 0.10330319444502756 };
        beeAI.weightsHiddenOutput[27] = new double[]{0.17869439558649966, -0.0036455752276992877, 0.21918974716452463, 0.21692900968212162, 0.18544959852223708 };
        beeAI.weightsHiddenOutput[28] = new double[]{-0.22415950238168586, -0.1260833096329306, -0.028548830647122665, -0.7183381231475542, -0.39729152905889364};
        beeAI.weightsHiddenOutput[29] = new double[]{-0.0740808156228451, -0.0655093346415285, 0.5538065932247885, 0.05356045065813064, -0.910764298457972};
        beeAI.weightsHiddenOutput[30] = new double[]{-0.08016968001944319, -0.2234581080603659, 0.03203681072993543, -0.20505219439375144, 0.07645573269983647};
        beeAI.weightsHiddenOutput[31] = new double[]{-0.23574595982955013, -0.37532215871170077, -0.18587079808275767, -0.18208681152635828, -0.13761976197978454 };
        beeAI.weightsHiddenOutput[32] = new double[]{0.12397656830207573, 0.26333378715476585, 0.05358687031227286, 0.24779217334316578, 0.1677151784441544 };
        beeAI.weightsHiddenOutput[33] = new double[]{-0.07197683164080003, -0.0723046300790989, -0.08820816115775823, 0.2015020783767792, 0.10851022750925737 };
        beeAI.weightsHiddenOutput[34] = new double[]{-0.018650829132811336, 0.08435878376066361, -0.05556428881281401, -0.2947442672150042, 0.09673102022249405 };
        beeAI.weightsHiddenOutput[35] = new double[]{0.24054457360388798, 0.24045287722776515, 0.13770696833704665, 0.033334914322725014, 0.0549391739632469};
        beeAI.weightsHiddenOutput[36] = new double[]{0.04395456360130387, -0.12728349989669707, -0.26235505054905245, -0.19348336950339268, -0.5245991416952014 };
        beeAI.weightsHiddenOutput[37] = new double[]{-1.350938079751989, -0.026917676748663952, -0.05164042640978658, 0.2625620172464095, 0.544931514446964};
        beeAI.weightsHiddenOutput[38] = new double[]{-0.38227440885261094, -0.29831133463209214, -0.41863344452910956, -0.2742795091486683, -0.03594645941395548 };
        beeAI.weightsHiddenOutput[39] = new double[]{-0.176939180342817, -0.518334829768319, -0.4653319922925196, 0.34654300845254515, 0.6986152381151464 };
        beeAI.weightsHiddenOutput[40] = new double[]{0.07377977486049868, -0.3036186553216694, -0.23355442419444966, 0.5836695304633848, -0.09324198580862883};
        beeAI.weightsHiddenOutput[41] = new double[]{0.21991834161281704, -0.050741176989670136, 0.17392973642321682, 0.1971167662834934, 0.2515086195745985};
        beeAI.weightsHiddenOutput[42] = new double[]{-0.021735275703260514, 0.32012114150299226, 0.22501420205168554, -0.20971976975528853, -0.5583729462914075 };
        beeAI.weightsHiddenOutput[43] = new double[]{0.026125634897162707, 0.04259294394744904, 0.0536094724512519, -0.11170893981889807, -0.076424751670109 };
        beeAI.weightsHiddenOutput[44] = new double[]{0.04196337247870009, 0.20018260003332797, 0.04625107581945887, -0.05169004107557463, 0.07285824890368722};
        beeAI.weightsHiddenOutput[45] = new double[]{-0.1716200478591159, -0.3674860716239584, 0.2492521620737087, -0.013057654155134176, -0.45486465872258663 };
        beeAI.weightsHiddenOutput[46] = new double[]{-0.45647064208748317, 0.14861618525305814, -0.3986504989295352, 0.01921965674250935, -0.2928346074325877 };
        beeAI.weightsHiddenOutput[47] = new double[]{0.12261542297265776, 0.14218219390619347, -0.8370759989950862, 0.3569705855329967, -0.1894153274111537 };
        beeAI.weightsHiddenOutput[48] = new double[]{1.7957137704333443, 0.049852127871452864, -0.9654027905059233, -0.3279397948191602, -0.0798838625678441 };
        beeAI.weightsHiddenOutput[49] = new double[]{-0.13298863401340608, -0.32004759295030244, -0.06234880026859117, -0.09149542665483724, -0.1615184083498991 };

        percent = beeAI.test(testInputs, outputTest);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Training completed in " + duration + " milliseconds.");
        System.out.println(percent + "% " + bestattempt);
        System.out.println(Arrays.toString(outputTest));
        System.out.println(Arrays.toString(beeAI.getAnswer()));
        System.out.println(beeAI.getSeed());
    }

}
