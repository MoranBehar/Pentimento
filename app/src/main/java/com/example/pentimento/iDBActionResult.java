package com.example.pentimento;

public interface iDBActionResult {
    void onSuccess(String data);
    void onError(Exception e);
}
