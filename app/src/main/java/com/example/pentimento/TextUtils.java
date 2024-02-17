package com.example.pentimento;

import android.util.Log;

public class TextUtils {

    //
    //add msg
    //

    public String convertStringToBinary(String text) {

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
    //get msg
    //

    public String convertBinaryToString(String binaryMsg) {

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
