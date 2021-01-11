import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Test {
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String input = "./left_sole.jpg";

        // To Read the image
        Mat source = Imgcodecs.imread(input);

        // Creating the empty destination matrix
        Mat destination = new Mat();

        // Converting the image to gray scale and
        // saving it in the dst matrix
        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);

        // Writing the image
        Imgcodecs.imwrite("./gray.jpg", destination);
        System.out.println("The image is successfully to Grayscale");

    }
}