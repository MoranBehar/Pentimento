package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ImageLsbManipulation {

    private String MessageToEmbed;
    private Bitmap imageToAddMassage;
    private Bitmap bmpWithMassage;

    //End Of Message terminating string
    public static final String EOM = "$$$EOM$$$";

    private TextUtils utils = new TextUtils();



    //Embed Message
    public ImageLsbManipulation(String Message, Bitmap bmpImage){
        this.MessageToEmbed = Message + EOM;
        this.imageToAddMassage = bmpImage;
        this.bmpWithMassage = bmpWithMassage;
    }

    //Extract Message
    public ImageLsbManipulation(Bitmap bmpWithMassage){
        this.bmpWithMassage = bmpWithMassage;
    }


    public Bitmap EmbedMessageAction() {
        String binaryMessage = utils.convertStringToBinary(this.MessageToEmbed);
        Log.d("POC", "execute: "+binaryMessage);

        return hideMessageInLSB(imageToAddMassage, binaryMessage);
    }

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

    public String getMassage() {

        String message = "";
        String endingCodeOfMsg ="$$$EOM$$$";

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

                String extractedPartOfMessage = utils.convertBinaryToString(message);

                //if the message includes the ending code - stop looping the image
                if(checkIfEnd(extractedPartOfMessage, endingCodeOfMsg))
                {
                    break;
                }

            }
        }

        String extractedMessage = utils.convertBinaryToString(message);
        Log.d("POC", "TheMessage: "+message);
        Log.d("POC", "TheExtractedMessage: "+extractedMessage);

        String extractedMessageWithoutEndingCode = removeEndingCode(extractedMessage, endingCodeOfMsg);
        Log.d("POC", "TheExtractedMessageWithoutEnding: "+extractedMessageWithoutEndingCode);

        return extractedMessageWithoutEndingCode;
    }

    public boolean checkIfEnd(String partOfMsg, String endingCode){

        return partOfMsg.endsWith(endingCode);
    }

    public String removeEndingCode(String msgWithEndingCode, String endingCode)
    {
        //find the last index of the ending code in the message
        int lastIndexOfRealMsg = msgWithEndingCode.length() - endingCode.length();

        //extract the sub string from the beginning of the message up to the last index
        return msgWithEndingCode.substring(0, lastIndexOfRealMsg);
    }

}
