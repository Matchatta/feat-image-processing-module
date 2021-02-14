import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Test {
    public static void main(String[] args) {

        //Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String input = "./no4.jpg";

        // To Read the image
        Mat source = Imgcodecs.imread(input);

//        // Reduce noise by blurring with a Gaussian filter (kernel size = 5)
//        Imgproc.GaussianBlur( source, source, new Size(5, 5), 0, 0, Core.BORDER_DEFAULT );


//        //Call bg removal class
        BgRemoval b = new BgRemoval();
        b.doBackgroundRemoval(source);
        b.detectEdge(source);

//        Mat test = new Mat();
//        Mat wide_test = new Mat(source.rows(),source.cols(),source.type());
//        Imgproc.threshold(source, test, 200,255, Imgproc.THRESH_TOZERO_INV);
//        wide_test.convertTo(test, CvType.CV_8UC3);
//        Imgcodecs.imwrite("./s.jpg",source);
//        Imgcodecs.imwrite("./threshold.jpg",test);

    }


}