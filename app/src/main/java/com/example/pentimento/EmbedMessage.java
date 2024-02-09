package com.example.pentimento;

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

}
