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
        String input = "./left_sole.jpg";

        // To Read the image
        Mat source = Imgcodecs.imread(input);

//        // Reduce noise by blurring with a Gaussian filter (kernel size = 5)
//        Imgproc.GaussianBlur( source, source, new Size(5, 5), 0, 0, Core.BORDER_DEFAULT );


        //Call bg removal class
        BgRemoval b = new BgRemoval();
        b.doBackgroundRemoval(source);
        b.detectEdge(source);

    }


}