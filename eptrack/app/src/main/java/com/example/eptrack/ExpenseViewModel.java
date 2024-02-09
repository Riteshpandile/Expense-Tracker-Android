package com.example.eptrack;

// ExpenseViewModel.java
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExpenseViewModel extends ViewModel {
    private final MutableLiveData<Boolean> refreshTrigger = new MutableLiveData<>();

    public void triggerRefresh() {
        refreshTrigger.setValue(true);
    }

    public LiveData<Boolean> getRefreshTrigger() {
        return refreshTrigger;
    }
}
