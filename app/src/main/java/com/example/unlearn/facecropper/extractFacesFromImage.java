package com.example.unlearn.facecropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;


public class extractFacesFromImage {

    public  extractFacesFromImage(String imagePath ,  String OutPutPath , Context context){
        File folder = new File(OutPutPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        try {
            detectFacesInImage(imagePath , OutPutPath, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private  void detectFacesInImage(String imagePath ,  String OutPutPath){
        //ImageWindow[] imageWindow = null;
        Bitmap image =  Image.bitmapFromJpg(imagePath);
        FaceDetector.Face[] faces = detectFaces(image);
        for(FaceDetector.Face fs:faces)
        {
            if(fs == null){continue;}
            PointF midPoint=new PointF();
            fs.getMidPoint(midPoint);
            float eyeDistance=fs.eyesDistance();

            int left = (int)(midPoint.x - (float)(1.4 * eyeDistance));
            int top = (int)(midPoint.y - (float)(1.8 * eyeDistance));

            Bitmap bmFace = Bitmap.createBitmap(image, (int) left, (int) top, (int) (2.8 * eyeDistance), (int) (3.6 * eyeDistance));
            Bitmap bmp= bmFace.createBitmap(bmFace.getWidth(), bmFace.getHeight(), Bitmap.Config.ARGB_8888);
            Image.saveBitmapToJpg(bmp,OutPutPath, "face_" +  System.currentTimeMillis()  +".jpg" ,256,256);

            //ImageWindow Iw = new ImageWindow(fs.)

        }
        //return  imageWindow;
    }*/


    private  void detectFacesInImage(String imagePath ,  String OutPutPath, Context context) throws Exception{
        //ImageWindow[] imageWindow = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(imagePath),null,bmOptions);
        Bitmap image =  bitmap;
        if(image == null){
            Log.i("Sahil", "returning " + imagePath);
            return;
        }
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if(!path.exists()){
            path.mkdirs();
            Log.d("Sahil","created");
        }
        SparseArray<com.google.android.gms.vision.face.Face> faces = detectFaces(image,context);
        Log.i("Sahil", "faceloop");
        for (int i = 0; i < faces.size(); ++i) {
            com.google.android.gms.vision.face.Face face = faces.valueAt(i);
            if(face == null){continue;}
            try {

                Bitmap bmFace = Bitmap.createBitmap(image, (int) face.getPosition().x, (int) face.getPosition().y, (int) face.getWidth(), (int) face.getHeight());
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-" + n + ".jpg";
                File file = new File(OutPutPath, fname);
                Log.i("Sahil", "" + file);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bmFace.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e){
                e.printStackTrace();

            }

        }

        new File(imagePath).delete();
    }




    private SparseArray<com.google.android.gms.vision.face.Face> detectFaces(Bitmap image , Context context) {

        int h = image.getHeight();
        int w = image.getWidth();
        int max = 10;
        Frame frame = new Frame.Builder().setBitmap(image).build();
        com.google.android.gms.vision.face.FaceDetector detector = new com.google.android.gms.vision.face.FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_LANDMARKS)
                .build();
        SparseArray<com.google.android.gms.vision.face.Face> faces = detector.detect(frame);
        detector.release();
        return  faces;




    }

    private FaceDetector.Face[] detectFaces(Bitmap image ) {

        int h = image.getHeight();
        int w = image.getWidth();
        int max = 10;

        FaceDetector detector = new FaceDetector(w, h, max);
        FaceDetector.Face[] faces = new FaceDetector.Face[max];

        Log.i("Sahil", "found " + faces.length);
        int facesFound = detector.findFaces(image, faces);
        return  faces;


    }


}
