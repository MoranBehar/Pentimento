package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class ImageLsbManipulation {

    private String MessageToEmbed;
    private Bitmap imageToAddMassage;
    private Bitmap bmpWithMessage;

    //End Of Message terminating string
    public static final String EOM = "$$$EOM$$$";

    //Start Of Message terminating string
    public static final String SOM = "$$$SOM$$$";

    private TextUtils utils = new TextUtils();



    //Embed Message
    public ImageLsbManipulation(String Message, Bitmap bmpImage){
        this.MessageToEmbed = SOM + Message + EOM;
        this.imageToAddMassage = bmpImage;
        this.bmpWithMessage = bmpWithMessage;
    }

    //Extract Message
    public ImageLsbManipulation(Bitmap bmpWithMassage){
        this.bmpWithMessage = bmpWithMassage;
    }

    public Bitmap EmbedMessageAction() {
        String binaryMessage = utils.convertStringToBinary(this.MessageToEmbed);
        Log.d("POC", "execute: "+binaryMessage);

        return hideMessageInLSB(imageToAddMassage, binaryMessage);
    }

    public Bitmap hideMessageInLSB(Bitmap bmp, String binaryMessage) {

        //duplicate the bitMap so i could do manipulation on it
        Bitmap newBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);

        int changeBit = 0;

        // loop through every bit of the message
        for(int i = 0; i < binaryMessage.length(); i++)
        {
            // find the bit we need to implement in the pixel
            changeBit =  Integer.valueOf(binaryMessage.substring(i,i+1));

            // find the pixel place (x, y)
            int pixelY = (int)Math.floor(i/(newBitmap.getWidth()));
            int pixelX = i%(newBitmap.getWidth());

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
            newBitmap.setPixel(pixelX, pixelY, Color.rgb(redLSBChanged, green, blue));

            Log.d("POC", String.format("x:%d ,y:%d, bit:%d, red:%d changed:%d",pixelX,pixelY,changeBit,red, redLSBChanged));

        }

        return newBitmap;
    }

    public String getMessage() {

        String message = "";
        int bitCounter = 0;
        boolean SOMFound = false;

        // loop through every pixel of the image
        scanImageLoop:
        for (int y = 0; y < bmpWithMessage.getHeight(); y++) {
            for (int x = 0; x < bmpWithMessage.getWidth(); x++) {

                // Get the pixel
                int pixel = bmpWithMessage.getPixel(x, y);

                // Get red channel of the pixel
                int red = Color.red(pixel);

                // get the LSB bit of the red channel
                int lsbRed = red & 1;

                // add the new bit to the string msg
                message += Integer.toString(lsbRed);
                bitCounter++;

                Log.d("POC", String.format("x:%d ,y:%d, bit:%d, red:%d ",x,y,lsbRed,red));

                if (bitCounter/8 >= SOM.length()) {
                    String extractedPartOfMessage = utils.convertBinaryToString(message);
                    Log.d("POC", "extractedPartOfMessage: "+extractedPartOfMessage);

                    // if there we didn't find SOM header - there is no message embedded
                    if(!SOMFound && !checkIfStartMsg(extractedPartOfMessage))
                    {
                        SOMFound = true;
                        return null;
                    }

                    // if we've reached the EOM footer - exit the loop cause we extracted the message
                    if(bitCounter%8 == 0 && checkIfEnd(extractedPartOfMessage))
                    {
                        break scanImageLoop;
                    }
                }
            }
        }

        String extractedMessage = utils.convertBinaryToString(message);
        Log.d("POC", "TheMessage: "+message);
        Log.d("POC", "TheExtractedMessage: "+extractedMessage);

        String extractedMessageWithoutMarkingCode = extractMessageBody(extractedMessage);
        Log.d("POC", "TheExtractedMessageWithoutEnding: "+extractedMessageWithoutMarkingCode);

        return extractedMessageWithoutMarkingCode;
    }

    public boolean checkIfEnd(String partOfMsg){
        return partOfMsg.endsWith(EOM);
    }

    public String removeEndingCode(String msgWithEndingCode)
    {
        //find the last index of the ending code in the message
        int lastIndexOfRealMsg = msgWithEndingCode.length() - EOM.length();

        //extract the sub string from the beginning of the message up to the last index
        return msgWithEndingCode.substring(0, lastIndexOfRealMsg);
    }


    public boolean checkIfStartMsg(String partOfMsg){
        return partOfMsg.startsWith(SOM);
    }

    public String extractMessageBody(String messageFromImage)
    {
        // find the start index of the message body
        int firstIndexOfRealMsg = SOM.length();

        // find the last index of the message body
        int lastIndexOfRealMsg = messageFromImage.length() - EOM.length();

        //extract the sub string from the beginning of the message up to the last index
        return messageFromImage.substring(firstIndexOfRealMsg, lastIndexOfRealMsg);
    }

    public Bitmap deleteMessageFromLSB() {

        //get the msg with the prefix length from the image
        String msg = getMessage();
        String fullMsgBlock = SOM + msg + EOM;

        //set a random text by the length we need to run over
        String randomText = generateRandomString(fullMsgBlock.length());

        //convert the random text to binary
        String binaryMsg = utils.convertStringToBinary(randomText);

        //embed the random text in the image
        return hideMessageInLSB(bmpWithMessage, binaryMsg);
    }

    private String generateRandomString(int length) {

        // ASCII values for printable characters range from 32 to 126
        int minAsciiValue = 32;
        int maxAsciiValue = 126;

        // Create a StringBuilder to store the random string
        StringBuilder sb = new StringBuilder();

        // Create a Random object
        Random random = new Random();

        // Generate random characters and append them to the StringBuilder until it reaches the specified length
        for (int i = 0; i < length; i++) {
            int randomAsciiValue = random.nextInt(maxAsciiValue - minAsciiValue + 1) + minAsciiValue;
            char randomChar = (char) randomAsciiValue;
            sb.append(randomChar);
        }

        // Convert the StringBuilder to a String and return
        return sb.toString();
    }

}
