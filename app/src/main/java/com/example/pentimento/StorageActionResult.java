package com.example.pentimento;

public interface StorageActionResult {
    void onSuccess(String data);
    void onError(Exception e);
}
