import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.WritableRaster;

import static org.opencv.core.CvType.CV_8UC1;

public class cutting {
    public Mat Cut (Mat image){
        Mat testbg = new Mat();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int count_black = 0;
        int count_white = 0;
        WritableRaster wr = image.getRaster();
        for (int y=0 ; y < image.getHeight() ; y++)
            for (int x=0 ; x < image.getWidth() ; x++)
                wr.getSample(x, y, 0);
        Imgcodecs.imwrite("./testclass.jpg", image);
        return image;
    }

}
