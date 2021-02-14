import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Rect;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class BgRemoval {

    //BACKGROUND REMOVAL
    public Mat doBackgroundRemoval(Mat source)
    {

        double alpha = -1;
        double beta = 150;
        // init
        Mat hsvImg = new Mat();
        List<Mat> hsvPlanes = new ArrayList<>();
        Mat thresholdImg = new Mat();

        //brightness
        Mat blur = new Mat();
        Imgproc.GaussianBlur(source, blur, new Size(27, 27), 10, 10, Core.BORDER_DEFAULT);
        Mat bright = new Mat(blur.rows(),blur.cols(), blur.type());
        Mat bctest = new Mat();
        source.convertTo(bright, 0, alpha, beta);
        Imgcodecs.imwrite("./brightness.jpg", bright);
        Imgproc.Canny(bright, bctest, 20, 100, 3, false);
        bctest.convertTo(bright, CvType.CV_8UC3);
        Imgcodecs.imwrite("./bctest.jpg", bctest);

        //HSV Image
        // threshold the image with the histogram average value
        hsvImg.create(source.size(), CvType.CV_8U);
        Imgproc.cvtColor(source, hsvImg, Imgproc.COLOR_BGR2HSV);
        // enhance
        Imgproc.GaussianBlur( hsvImg, hsvImg, new Size(27, 27), 10, 10, Core.BORDER_DEFAULT );
        Core.split(hsvImg, hsvPlanes);
        Imgcodecs.imwrite("./test.jpg", hsvImg);

        double threshValue = this.getHistAverage(bright, hsvPlanes.get(0));
        // a new binary threshold
        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 255, Imgproc.THRESH_BINARY_INV);
        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));

        // dilate to fill gaps, erode to smooth edges
        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 1);
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 3);

        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 255, Imgproc.THRESH_BINARY_INV);

        // create the new image
        Mat foreground = new Mat(source.size(), CvType.CV_8UC1, new Scalar(0, 0, 0));
        source.copyTo(foreground, thresholdImg);

        Imgcodecs.imwrite("bg remove.jpg", foreground);

        //Post
        Imgproc.cvtColor(foreground, source, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(source, source, threshValue, 255.0, Imgproc.THRESH_BINARY_INV);
//        String file = "./asd.jpg";
        double kernelSize = source.width() / 220;
        Mat kernel = new Mat(new Size(kernelSize, kernelSize), CvType.CV_8UC1);
        Imgproc.morphologyEx(source, source, Imgproc.MORPH_OPEN, kernel);
//        imageCodecs.imwrite(file, image);
        Imgcodecs.imwrite("bg remove11.jpg", source);

        return foreground;
    }

    public Mat extractFoot(Mat image, String fileNameWithCompletePath, int xOne, int xTwo, int yOne, int yTwo) throws CvException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Imgproc.cvtColor(image,image,Imgproc.COLOR_RGBA2RGB, 0);
        Scalar co = new Scalar(0.0, 0.0, 0.0);
        Size s = new Size(image.width(), image.height());
        Rect rectangle = new Rect(xOne, yOne, xTwo, yTwo);
        Mat result = new Mat();
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
        Imgproc.grabCut(image, result, rectangle, bgdModel, fgdModel, 8, Imgproc.GC_INIT_WITH_RECT);
        Core.compare(result, source, result, Core.CMP_EQ);
        Mat foreground = new Mat(image.size(), CvType.CV_8UC3, new Scalar(0, 0, 0));
        image.copyTo(foreground, result);
        Imgcodecs.imwrite(fileNameWithCompletePath, foreground);
        return foreground;
    }

    //Histogram
    private double getHistAverage(Mat bright, Mat hueValues)
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

        return average = average / bright.size().height / bright.size().width;
    }


    public Mat detectEdge(Mat source) {
        Mat edge = new Mat();
        Imgproc.Canny(source, edge, 20, 100, 3, false);
        Imgcodecs.imwrite("canny.jpg", edge);
        return edge;
    }

}
