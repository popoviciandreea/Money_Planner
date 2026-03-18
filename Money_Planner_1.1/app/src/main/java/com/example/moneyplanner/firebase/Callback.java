package com.example.moneyplanner.firebase;

public interface Callback<R> {
    void runResultOnUiThread(R result);
}
