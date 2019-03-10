package uk.co.syski.client.android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.repository.CPURepository;
import uk.co.syski.client.android.data.repository.Repository;

public class CPUViewModel extends ViewModel{

    private String TAG = this.getClass().getSimpleName();

    private CPURepository mCPURepository;
    private MutableLiveData<List<CPUEntity>> mCPUList;

    public CPUViewModel() {
        mCPURepository = Repository.getCPURepository();
        mCPUList = mCPURepository.get();
    }

    public LiveData<List<CPUEntity>> get() {
        return mCPUList;
    }

    public void update() {

        mCPUList = mCPURepository.get();
    }

}
