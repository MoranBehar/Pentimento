package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageProcessor {


    public ImageProcessor() {
    }

    public Bitmap grayScale(Bitmap bmpImage, int level) {

        //duplicate the bitMap so i could do manipulation on it
        Bitmap newBitmap = bmpImage.copy(Bitmap.Config.ARGB_8888, true);
        double levelPrefix = (100-level)/100.0;

        // loop through every pixel of the image
        for (int x = 0; x < newBitmap.getWidth(); x++) {
            for (int y = 0; y < newBitmap.getHeight(); y++) {

                // Get the pixel
                int pixel = newBitmap.getPixel(x, y);

                // Calc gray scale of the pixel
                int gray = (int) (levelPrefix * Color.red(pixel) + levelPrefix * Color.green(pixel) + levelPrefix * Color.blue(pixel));

                // replace pixel with the manipulated pixel
                newBitmap.setPixel(x, y, Color.rgb(gray, gray, gray));
            }
        }

        return newBitmap;

    }

}
