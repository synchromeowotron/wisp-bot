package org.mogwai.wisp.nopepe;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class PepeTester {
    public static boolean testForPepe(String url) {
        NoPepeModel nopepe = new NoPepeModel();
        try {
            URL input = new URL(url);
            URL res = PepeTester.class.getClassLoader().getResource("nopepe.onnx");
            assert res != null;
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            nopepe.load_model(absolutePath);
            BufferedImage inputImage = ImageIO.read(input);
            float[] inputData = ImageLoader.imageToRGBFloatBuffer(inputImage);
            return nopepe.isPepe(inputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
