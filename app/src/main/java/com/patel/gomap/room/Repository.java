package com.patel.gomap.room;

import androidx.lifecycle.LiveData;
import com.patel.gomap.model.PinData;
import com.patel.gomap.ui.Resource;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Repository {

    private PinDAO pinDAO;

    @Inject
    public Repository(PinDAO pinDAO){
        this.pinDAO = pinDAO;
    }

    public LiveData<List<PinData>> getAllPinData(){
        return pinDAO.getAllPinData();
    }

    public Flowable<Resource<Integer>> insertPinData(PinData pinData){
        return pinDAO.addPinData(pinData)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong){
                        long l = aLong;
                        return (int)l;
                    }
                })
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable){
                        return -1;
                    }
                })
                .map(new Function<Integer, Resource<Integer>>() {
                    @Override
                    public Resource<Integer> apply(Integer integer) {
                        if(integer>0){
                            return Resource.success(integer, "success");
                        }
                        return Resource.error(null, "failure");
                    }
                })
                .subscribeOn(Schedulers.io()).toFlowable();
    }

    public Flowable<Resource<Integer>> deleteAllPinData(){
        return pinDAO.removeAllPinData()
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        return -1;
                    }
                })
                .map(new Function<Integer, Resource<Integer>>() {
                    @Override
                    public Resource<Integer> apply(Integer integer) throws Exception {
                        if(integer>0){
                            return Resource.success(integer, "success");
                        }
                        return Resource.error(null, "failure");
                    }
                })
                .subscribeOn(Schedulers.io()).toFlowable();
    }

}
