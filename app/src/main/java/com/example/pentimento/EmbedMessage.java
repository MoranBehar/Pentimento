package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class EmbedMessage {


    private String MessageToEmbed;
    private Bitmap imageToAddMassage;

    //End Of Message terminating string
    public static final String EOM = "$$$EOM$$$";


    public EmbedMessage(String Message, Bitmap bmpImage) {
        this.MessageToEmbed = Message + EOM;
        this.imageToAddMassage = bmpImage;
    }

    public Bitmap execute() {
        String binaryMessage = convertStringToBinary(this.MessageToEmbed);
        Log.d("POC", "execute: "+binaryMessage);

        return hideMessageInLSB(imageToAddMassage, binaryMessage);
    }

    private String convertStringToBinary(String text) {

        String convertedText ="";

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            convertedText += convertCharToBinary(c);
        }

        return convertedText;

    }

    private String convertCharToBinary(char ch) { 

        // Casting char to int ascii value
        int intValue = ch;

        // Convert the ascii value to binary
        String binaryString = Integer.toBinaryString(intValue);

        // format the binary to an 8 digit string
        String paddedBinaryString = String.format("%8s", binaryString).replace(' ', '0');

        return paddedBinaryString;
    }


    //
    // need to check if working!!!
    //
    //
    public Bitmap hideMessageInLSB(Bitmap bmp, String msg) {

        //duplicate the bitMap so i could do manipulation on it
        Bitmap newBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);

        int changeBit = 0;

        // loop through every char of the massage
        for(int x = 0; x < msg.length(); x++)
        {
            //find the char we need to implement in the pixel
            changeBit =  Integer.valueOf(msg.substring(x,x+1));

            //find the pixel place (x, y)
            int pixelX = x/(newBitmap.getHeight());
            int pixelY = x%(newBitmap.getWidth());

            // Get the pixel
            int pixel = newBitmap.getPixel(pixelX,pixelY);

            // Get each channel of the pixel
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            int blue = Color.blue(pixel);

            int redLSBChanged;

            if(changeBit == 0)
            {
                // Set LSB to 0
                redLSBChanged = red & 0xFE;
            }
            else
            {
                // Set LSB to 1
                redLSBChanged = red | 0x01;
            }

            // replace pixel with the manipulated pixel
            newBitmap.setPixel(pixelX, pixelY,
                    Color.rgb(redLSBChanged, green, blue));

//            Log.d("POC", String.format("x:%d ,y:%d, red:%d ",pixelX,pixelY,redLSBChanged));

        }

        return newBitmap;
    }

}
