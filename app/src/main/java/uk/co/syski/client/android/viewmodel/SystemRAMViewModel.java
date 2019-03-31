package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.repository.RAMRepository;
import uk.co.syski.client.android.data.repository.Repository;

public class SystemRAMViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();

    private RAMRepository mRAMRepository;
    private MutableLiveData<List<RAMEntity>> mRAMList;
    private UUID systemId;

    public SystemRAMViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mRAMRepository = Repository.getInstance().getRAMRepository();
        mRAMList = mRAMRepository.get(systemId);
    }

    public LiveData<List<RAMEntity>> get() {
        return mRAMList;
    }

    public void update() {
        mRAMList = mRAMRepository.get();
    }

}
