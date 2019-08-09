package com.patel.gomap.ui;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;
import com.patel.gomap.model.PinData;
import com.patel.gomap.network.GoTennaAPI;
import com.patel.gomap.room.Repository;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import java.util.List;

public class MainViewModel extends ViewModel {

    private GoTennaAPI goTennaAPI;
    private Repository repository;

    @Inject
    public MainViewModel(GoTennaAPI goTennaAPI, Repository repository){
        this.goTennaAPI = goTennaAPI;
        this.repository = repository;
    }

    public LiveData<List<PinData>> PinDataAPICall(){
        return LiveDataReactiveStreams.fromPublisher(
            goTennaAPI.getPinData().subscribeOn(Schedulers.io())
        );
    }

    public Flowable<Resource<Integer>> addPinData(PinData pinData){
        return repository.insertPinData(pinData);
    }

    public Flowable<Resource<Integer>> deleteAllPinData(){
        return repository.deleteAllPinData();
    }

    public LiveData<List<PinData>> getAllPinData(){
        return repository.getAllPinData();
    }

}
