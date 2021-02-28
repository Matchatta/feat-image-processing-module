import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.opencv.core.CvType.CV_8UC1;

public class cutting {
    public Mat Cut (Mat source){
        Mat testbg = new Mat();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        double current_color = -1;
        int color_switch = 0;
        int count_black = 0;
        int count_white = 0;
        int row_of_finger = 0;
        double ratio;
        for (int y = 0; y < source.height(); y++) {
            color_switch = 0;
            for (int x = 0; x < source.width(); x++){
                double[] color = source.get(y, x);
                if(color[0] != current_color && y < source.height()/3){
                    if(current_color != -1){
                        color_switch++;
                    }
                    current_color = color[0];
                }

//                if(color[0] == 0.0){
//                    count_black++;
//                }
//                else if(color[0] == 255.0){
//                    count_white++;
//                }
            }
            if(color_switch > 2){
                row_of_finger = y;
            }
        }
        System.out.println(row_of_finger);
//        Imgproc.rectangle(source, new Point(0, row_of_finger), new Point(source.width(), row_of_finger + source.height()),
//                new Scalar(0, 0, 0));
//        System.out.println(count_white);
        Rect crop = new Rect(0, row_of_finger, source.width(), (source.height() - row_of_finger));
        Mat image_roi = new Mat(source,crop);
        String filename = "cutted.jpg";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite("testclass.jpg", source);
        Imgcodecs.imwrite(filename, image_roi);
        return source;
    }

//    public static BufferedImage colorToAlpha(BufferedImage raw, Color remove)
//    {
//        int WIDTH = raw.getWidth();
//        int HEIGHT = raw.getHeight();
//        BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
//        int pixels[]=new int[WIDTH*HEIGHT];
//        raw.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
//        for(int i=0; i<pixels.length;i++)
//        {
//            if (pixels[i] == remove.getRGB())
//            {
//                pixels[i] = 0x00ffffff;
//            }
//        }
//        image.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
//        return image;
//    }

}
