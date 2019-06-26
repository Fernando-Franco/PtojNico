package com.almost.peruibemelhor.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessageViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<String> messagens;

    public MutableLiveData<String> getCurrentName() {
        if (messagens == null) {
            messagens = new MutableLiveData<String>();
        }
        return messagens;
    }

// Rest of the ViewModel...
}
