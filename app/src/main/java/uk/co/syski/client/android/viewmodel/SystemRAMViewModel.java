package uk.co.syski.client.android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.repository.RAMRepository;
import uk.co.syski.client.android.data.repository.Repository;

public class SystemRAMViewModel extends ViewModel {

    private String TAG = this.getClass().getSimpleName();

    private RAMRepository mRAMRepository;
    private MutableLiveData<List<RAMEntity>> mRAMList;


    public SystemRAMViewModel() {
        mRAMRepository = Repository.getInstance().getRAMRepository();
        mRAMList = mRAMRepository.get();
    }

    public LiveData<List<RAMEntity>> get() {
        return mRAMList;
    }

    public void update() {
        mRAMList = mRAMRepository.get();
    }

}
