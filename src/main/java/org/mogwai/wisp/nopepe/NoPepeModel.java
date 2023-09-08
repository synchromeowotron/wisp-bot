package org.mogwai.wisp.nopepe;

import ai.onnxruntime.*;
import org.mogwai.wisp.exceptions.InferenceException;
import org.mogwai.wisp.exceptions.ModelLoadFailureException;
import org.mogwai.wisp.models.PepeResult;

import java.util.HashMap;
import java.util.Map;

public class NoPepeModel {
    private static final long[] INPUT_SHAPE = {1, 128, 128, 3};
    private static final String INPUT_NODE_NAME = "image_tensor";
    private static final float DEFAULT_THRESHOLD = 0.4f;

    private OrtEnvironment env;
    private OrtSession session;
    private final float threshold;

    public float getThreshold() {
        return threshold;
    }

    public NoPepeModel(float threshold) {
        this.threshold = threshold;
    }

    public NoPepeModel() {
        this.threshold = DEFAULT_THRESHOLD;
    }

    /**
     * Load a nopepe ONNX model and initialize a session.
     * @param modelPath path to the model file
     * @throws ModelLoadFailureException exception thrown on failure to find or load the model or initialize the runtime
     */
    public void load_model(String modelPath) throws ModelLoadFailureException {
        try {
            env = OrtEnvironment.getEnvironment();
            session = env.createSession(modelPath ,new OrtSession.SessionOptions());
        } catch (OrtException e) {
            throw new ModelLoadFailureException(e);
        }
    }

    /**
     * Primary method for image classification, takes preprocessed float image data.
     * @param inputImageData 1D float image data in range <-1.0, 1.0>, the image should be 64*64 RGB
     * @return PepeResult object containing the confidence and the boolean result
     * @throws InferenceException exception thrown when the inference process fails for any reason
     */
    public PepeResult isPepe(float[] inputImageData) throws InferenceException {
        return new PepeResult(Float.toString(pepeScore(inputImageData)), pepeScore(inputImageData) >= threshold);
    }

    /**
     * Lower-level version of the isPepe method, returning raw score.
     * @param inputImageData 1D float image data in range <-1.0, 1.0>, the image should be 64*64 RGB
     * @return confidence in broad range around 0, approx. <-10.0, 10.0>
     * @throws InferenceException exception thrown when the inference process fails for any reason
     */
    public float pepeScore(float[] inputImageData) throws InferenceException {

        try {
            // create inputs container
            Map<String, OnnxTensor> inputs = new HashMap<>();

            // reshape float array to desired tensor shape
            Object tensorData = OrtUtil.reshape(inputImageData, INPUT_SHAPE);

            // create input tensor
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, tensorData);

            // put the input tensor into inputs container, assigned to the only input we have
            inputs.put(INPUT_NODE_NAME, inputTensor);

            // finally run the process
            OrtSession.Result results = session.run(inputs);

            // extract result from the output tensor (batch 0, output 0)
            return ((OnnxTensor) results.get(0)).getFloatBuffer().get(0);

        } catch (OrtException e) {
            throw new InferenceException(e);
        }
    }
}
