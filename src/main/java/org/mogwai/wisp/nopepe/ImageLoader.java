package org.mogwai.wisp.nopepe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

public class ImageLoader {

    private static final int TARGET_WIDTH = 128;
    private static final int TARGET_HEIGHT = 128;

    /**
     * Convert most supported image formats (JPG, PNG, GIF) including animation (will use the first frame)
     * and transparency into a 1D float RGB array, scaling it as necessary prior to the conversion.
     * @param inputImage BufferedImage to be converted (e.g. from ImageIO when reading a file)
     * @return 1D float RGB array of size TARGET_WIDTH*TARGET_HEIGHT*3
     * @throws IOException general exception thrown when something goes wrong when processing the image
     */
    public static float[] imageToRGBFloatBuffer(BufferedImage inputImage) throws IOException {
        // ensure RGBA to RGB conversion with white background
        BufferedImage rgbImage = new BufferedImage(
                inputImage.getWidth(),
                inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        rgbImage.createGraphics().drawImage(inputImage, 0, 0, Color.WHITE, null);
        BufferedImage resizedImage = resizeImage(rgbImage);
        return get1DPixelArray(resizedImage);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage)
            throws IOException {
        Image resultingImage = originalImage.getScaledInstance(TARGET_WIDTH, TARGET_HEIGHT, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private static float[] get1DPixelArray(BufferedImage image) {
        int[] pixelData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int width = image.getWidth();
        int height = image.getHeight();
        int channels = 3;

        float[] result = new float[height*width*channels];

        // fast way to grab pixel values is to read from data buffer, but we need to use binary ops
        // once we load it, we need to put it into the <-1.0, 1.0> range for the neural net, we do that
        // by dividing by half byte (127.5) and subtracting 1
        for (int idx = 0; idx < pixelData.length; idx++) {
            int rgbValue = pixelData[idx];
            int arrayOffset = idx * 3;
            result[arrayOffset] = (((rgbValue >> 16) & 0xFF) / 127.5f) - 1.0f; // red
            result[arrayOffset + 1] = (((rgbValue >> 8) & 0xFF) / 127.5f) - 1.0f; // green
            result[arrayOffset + 2] = ((rgbValue & 0xFF) / 127.5f) - 1.0f; //blue
        }

        return result;
    }
}
