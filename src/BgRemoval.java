import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

public class BgRemoval {

    //BACKGROUND REMOVAL
    public Mat doBackgroundRemoval(Mat source)
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
        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 255.0, Imgproc.THRESH_BINARY_INV);
        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));

        // dilate to fill gaps, erode to smooth edges
        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, 1), 6);
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, 1), 6);

        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 255.0, Imgproc.THRESH_BINARY_INV);

        // create the new image
        Mat foreground = new Mat(source.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        source.copyTo(foreground, thresholdImg);
        Imgcodecs.imwrite("bg remove.jpg", foreground);

        return foreground;
    }

    //Histogram
    public double getHistAverage(Mat hsvImg, Mat hueValues)
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
