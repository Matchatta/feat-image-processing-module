import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_8UC1;

public class cutting {
    public Mat Cut (Mat source){
        Mat testbg = new Mat();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int count_black = 0;
        int count_white = 0;
        double ratio;
        for (int y = 0; y < source.width(); y++) {
            for (int x = 0; x < source.height(); x++){
                int[] position;
                double[] color = source.get(x, y);
                if(color[0] == 0.0){
                    count_black++;
                }
                else if(color[0] == 255.0){
                    count_white++;
                }
            }
        }
        System.out.println(count_black);
        System.out.println(count_white);
        Imgcodecs.imwrite("testclass.jpg", source);
        return source;
    }

}
