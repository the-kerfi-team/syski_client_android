package uk.co.syski.client.android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.data.repository.SystemRepository;

public class SystemListViewModel extends ViewModel {

    private String TAG = this.getClass().getSimpleName();

    private SystemRepository mSystemRepository;
    private MutableLiveData<List<SystemEntity>> mSystemList;


    public SystemListViewModel()
    {
        mSystemRepository = Repository.getSystemRepository();
        mSystemList = mSystemRepository.get();
    }

    public LiveData<List<SystemEntity>> get() { return mSystemList; }

    public void update()
    {
        mSystemList = mSystemRepository.get();
    }

}
