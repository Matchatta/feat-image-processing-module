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
        String input = "./no.jpg";

        // To Read the image
        Mat source = Imgcodecs.imread(input);
//        Imgcodecs.imwrite("./test.jpg", source);
        BgRemoval b = new BgRemoval();
        b.doBackgroundRemoval(source);


        // Creating the empty destination matrix
//        Mat gray = new Mat();
//        Mat canny = new Mat();
//        Mat sobel = new Mat();
//        Mat scharr = new Mat();
//        Mat laplacian = new Mat();
//        Mat wide_can = new Mat();
//        Mat wide_sol = new Mat();
//        Mat wide_sch = new Mat();
//        Mat wide_lap = new Mat();

//        // Reduce noise by blurring with a Gaussian filter (kernel size = 5)
////        Imgproc.GaussianBlur( source, source, new Size(5, 5), 0, 0, Core.BORDER_DEFAULT );
//
        // Converting the image to gray scale and
        // saving it in the dst matrix
//        Imgproc.cvtColor(source, gray, Imgproc.COLOR_RGB2GRAY);
//
//        //Canny EdgeDetection
//        Imgproc.Canny(gray, wide_can, 20, 100, 3, false);
//        wide_can.convertTo(canny, CvType.CV_8UC3);
//
//        //Sobel EdgeDetection (Applying sobel derivative with values x:0 y:1)
//        Imgproc.Sobel(gray, wide_sol, -1, 0, 1, 3);
//        wide_sol.convertTo(sobel, CvType.CV_8UC3);
//
//        //Scharr EdgeDetection (Applying scharr derivative with values x:1 y:0)
//        Imgproc.Scharr(gray, wide_sch, Imgproc.CV_SCHARR, 1, 0);
//        wide_sch.convertTo(scharr, CvType.CV_8UC3);
//
//        //Laplacian
//        Imgproc.Laplacian(gray, wide_lap, CvType.CV_16S, 3, 3, 0, Core.BORDER_DEFAULT);
//        wide_lap.convertTo(laplacian, CvType.CV_8UC3);


        // Writing the image
//        Imgcodecs.imwrite("./gray.jpg", gray);
//        Imgcodecs.imwrite("./CannyEdge.jpg",canny);
//        Imgcodecs.imwrite("./SobelEdge.jpg", sobel);
//        Imgcodecs.imwrite("./ScharrEdge.jpg", scharr);
//        Imgcodecs.imwrite("./LaplacianEdge.jpg", laplacian);
//        Imgcodecs.imwrite("./h.jpg", source);
//        System.out.println("Convert image is successfully");

    }


}