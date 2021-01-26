//import java.io.IOException;
//
//import org.opencv.android.Utils;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.highgui.Highgui;
//import org.opencv.imgproc.Imgproc;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//public class Grabcut extends Activity implements OnTouchListener {
//    ImageView imageView;
//    Bitmap bitmap;
//    Canvas canvas;
//    Scalar color = new Scalar(255, 0, 0, 255);
//    Point tl, br;
//    int counter;
//    Bitmap bitmapResult, bitmapBackground;
//    Mat dst = new Mat();
//    final String pathToImage  = "/mnt/sdcard/gcut.png";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.grabcut_main);
//        imageView = (ImageView) this.findViewById(R.id.imageView);
//
//        bitmap = BitmapFactory.decodeFile(pathToImage);
//        Toast msg = Toast.makeText(Grabcut.this, "Press top left and bottom right of the foreground image", Toast.LENGTH_LONG);
//
//        msg.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
//
//        msg.show();
//        bitmapResult = bitmap.copy(bitmap.getConfig(), true);
//        canvas = new Canvas(bitmapResult);
//
//        imageView.setImageBitmap(bitmapResult);
//        imageView.setOnTouchListener(this);
//
//        tl = new Point();
//        br = new Point();
//        counter = 0;
//    }
//
//    //@Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (counter == 0) {
//                tl.x = event.getX();
//                tl.y = event.getY();
//                counter++;
//            } else if (counter == 1) {
//                br.x = event.getX();
//                br.y = event.getY();
//                counter++;
//
//                Mat img = new Mat();
//                img = Highgui.imread(pathToImage);
//                Mat background = new Mat();
//                try {
//                    background = Utils.loadResource(getApplicationContext(),
//                            R.drawable.wall );
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//                }
//
//                backgroundSubtracting(img, background);
//                Highgui.imwrite("/mnt/sdcard/GRABCUT/rect.png", dst);
//                Bitmap jpg = BitmapFactory
//                        .decodeFile("/mnt/sdcard/GRABCUT/rect.png");
//
//                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                imageView.setAdjustViewBounds(true);
//                imageView.setPadding(2, 2, 2, 2);
//                imageView.setImageBitmap(jpg);
//                imageView.invalidate();
//            }
//        }
//
//        return true;
//    }
//
//    private void backgroundSubtracting(Mat img, Mat background) {
//        Mat firstMask = new Mat();
//        Mat bgModel = new Mat();
//        Mat fgModel = new Mat();
//        Mat mask;
//        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3.0));
//        dst = new Mat();
//        Rect rect = new Rect(tl, br);
//
//        Imgproc.grabCut(img, firstMask, rect, bgModel, fgModel, 1, 0 /* GC_INIT_WITH_RECT */);
//        Core.compare(firstMask, source/* GC_PR_FGD */, firstMask, Core.CMP_EQ);
//
//        Mat foreground = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255,
//                255, 255));
//        img.copyTo(foreground, firstMask);
//
//        Core.rectangle(img, tl, br, color);
//
//        Mat tmp = new Mat();
//        Imgproc.resize(background, tmp, img.size());
//        background = tmp;
//        mask = new Mat(foreground.size(), CvType.CV_8UC1, new Scalar(255, 255, 255));
//
//        Imgproc.cvtColor(foreground, mask, 6/* COLOR_BGR2GRAY */);
//        Imgproc.threshold(mask, mask, 254, 255, 1 /* THRESH_BINARY_INV */);
//
//        Mat vals = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0.0));
//        background.copyTo(dst);
//
//        background.setTo(vals, mask);
//
//        Core.add(background, foreground, dst, mask);
//
//        firstMask.release();
//        source.release();
//        bgModel.release();
//        fgModel.release();
//        vals.release();
//    }
//
//}