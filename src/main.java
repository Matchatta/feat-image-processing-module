import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Test {
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String input = "./no.jpg";

        // To Read the image
        Mat source = Imgcodecs.imread(input);

        // Creating the empty destination matrix
        Mat gray = new Mat();
        Mat canny = new Mat();
        Mat sobel = new Mat();
        Mat scharr = new Mat();
        Mat laplacian = new Mat();
        Mat wide_can = new Mat();
        Mat wide_sol = new Mat();
        Mat wide_sch = new Mat();
        Mat wide_lap = new Mat();

        // Reduce noise by blurring with a Gaussian filter (kernel size = 5)
        Imgproc.GaussianBlur( source, source, new Size(5, 5), 0, 0, Core.BORDER_DEFAULT );

        // Converting the image to gray scale and
        // saving it in the dst matrix
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_RGB2GRAY);

        //Canny EdgeDetection
        Imgproc.Canny(gray, wide_can, 20, 100, 3, false);
        wide_can.convertTo(canny, CvType.CV_8UC3);

        //Sobel EdgeDetection (Applying sobel derivative with values x:0 y:1)
        Imgproc.Sobel(gray, wide_sol, -1, 0, 1, 3);
        wide_sol.convertTo(sobel, CvType.CV_8UC3);

        //Scharr EdgeDetection (Applying scharr derivative with values x:1 y:0)
        Imgproc.Scharr(gray, wide_sch, Imgproc.CV_SCHARR, 1, 0);
        wide_sch.convertTo(scharr, CvType.CV_8UC3);

        //Laplacian
        Imgproc.Laplacian(gray, wide_lap, CvType.CV_16S, 3, 3, 0, Core.BORDER_DEFAULT);
        wide_lap.convertTo(laplacian, CvType.CV_8UC3);


        // Writing the image
        Imgcodecs.imwrite("./gray.jpg", gray);
        Imgcodecs.imwrite("./CannyEdge.jpg",canny);
        Imgcodecs.imwrite("./SobelEdge.jpg", sobel);
        Imgcodecs.imwrite("./ScharrEdge.jpg", scharr);
        Imgcodecs.imwrite("./LaplacianEdge.jpg", laplacian);
        System.out.println("Convert image is successfully");

    }


    //BACKGROUND REMOVAL
    private Mat doBackgroundRemoval(Mat source)
    {
        // init
        Mat hsvImg = new Mat();
        List<Mat> hsvPlanes = new ArrayList<>();
        Mat thresholdImg = new Mat();

        // threshold the image with the histogram average value
        hsvImg.create(source.size(), CvType.CV_8U);
        Imgproc.cvtColor(source, hsvImg, Imgproc.COLOR_BGR2HSV);
        Core.split(hsvImg, hsvPlanes);

        double threshValue = this.getHistAverage(hsvImg, hsvPlanes.get(0));
        //a new binary threshold
        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);
        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));

        // dilate to fill gaps, erode to smooth edges
        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, 1), 6);
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, 1), 6);

        Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);

        // create the new image
        Mat foreground = new Mat(source.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        source.copyTo(foreground, thresholdImg);

        return foreground;
    }

    //Histogram
    private double getHistAverage(Mat hsvImg, Mat hueValues)
    {
        // init
        double average = 0.0;
        Mat hist_hue = new Mat();
        MatOfInt histSize = new MatOfInt(180);
        List<Mat> hue = new ArrayList<>();
        hue.add(hueValues);

        // compute the histogram
        Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));

        // get the average for each bin
        for (int h = 0; h < 180; h++)
        {
            average += (hist_hue.get(h, 0)[0] * h);
        }

        return average = average / hsvImg.size().height / hsvImg.size().width;
    }
}