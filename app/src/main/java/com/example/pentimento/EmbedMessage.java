package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class EmbedMessage {


    private String MessageToEmbed;


    public EmbedMessage(String Message) {
        this.MessageToEmbed = Message;
    }

    public void execute() {
        String binaryMessage = convertStringToBinary(this.MessageToEmbed);
        Log.d("POC", "execute: "+binaryMessage);
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

        Bitmap newBmp = bmp;
        char changeBit = 0;

        // loop through every char of the massage
        for(int x = 0; x < msg.length(); x++)
        {
            //find the char we need to implement in the pixel
            changeBit = msg.charAt(x);

            //find the pixel place (x, y)
            int pixelX = x/(newBmp.getHeight());
            int pixelY = x%(newBmp.getWidth());

            // Get the pixel
            int pixel = newBmp.getPixel(pixelX,pixelY);

            // Get each channel of the pixel
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            int blue = Color.blue(pixel);

            int redLSBChanged = red & changeBit;

            // replace pixel with the manipulated pixel
            newBmp.setPixel(pixelX, pixelY,
                    Color.rgb(redLSBChanged, green, blue));
        }

        return newBmp;
    }

}
