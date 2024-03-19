package com.example.pentimento;

public interface StorageActionResult<T> {
    void onSuccess(T data);

    void onError(Exception e);
}
