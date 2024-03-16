package com.example.pentimento;

import java.util.List;

public interface DBActionResult<T> {
    void onSuccess(T data);
    void onError(Exception e);
}
