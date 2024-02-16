package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ExtractMessage {

    private Bitmap bmpWithMassage;

    public ExtractMessage(Bitmap bmpWithMassage) {
        this.bmpWithMassage = bmpWithMassage;
    }

    public String getMassage() {

        String message = "";

        // loop through every pixel of the image
        for (int x = 0; x < 1; x++) {
            for (int y = 0; y < 128; y++) {

                // Get the pixel
                int pixel = bmpWithMassage.getPixel(x, y);

                // Get red channel of the pixel
                int red = Color.red(pixel);

                //get the lsb number of the red channel
                int lsbRed = red & 1;

                //add the new char to the string msg
                message += Integer.toString(lsbRed);

            }
        }

        String extractedMessage = convertBinaryToString(message);
        Log.d("POC", "TheExtractedMessage: "+message);
        Log.d("POC", "TheExtractedMessage: "+extractedMessage);


        //
        //
        //add checkIfEnd function - after every byte
        //
        //

        return extractedMessage;
    }

    public boolean checkIfEnd(){
        return false;
    }


    private String convertBinaryToString(String binaryMsg) {

        String massage ="";
        int numOfBytes = binaryMsg.length()/8;

        for (int i = 0; i < numOfBytes; i++) {

            //creating a string with 8 char - bit
            String Byte = binaryMsg.substring(i*8, (i*8+8));
            Log.d("POC", "TheExtractedMessage: "+Byte);

            massage += convertBinaryToChar(Byte);
        }

        return massage;
    }

    private char convertBinaryToChar(String binaryString) {

        // Parse the binary string into an integer - 2 for binary (base)
        int intValue = Integer.parseInt(binaryString, 2);
        Log.d("POC", "TheExtractedMessage: "+intValue);

        // Cast the integer to a char
        char character = (char)intValue;

        return character;
    }
}


