package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.data.RAMDataEntity;
import uk.co.syski.client.android.model.repository.SystemRAMDataRepository;

public class SystemRAMDataViewModel extends AndroidViewModel {

    private SystemRAMDataRepository mRAMDataRepository;
    private MutableLiveData<List<RAMDataEntity>> mSystemDataRAMList;
    private UUID systemId;

    public SystemRAMDataViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mRAMDataRepository = new SystemRAMDataRepository(application, systemId);
        mSystemDataRAMList = mRAMDataRepository.get();
        mRAMDataRepository.start();
    }

    public MutableLiveData<List<RAMDataEntity>> get() {
        return mSystemDataRAMList;
    }

    @Override
    public void onCleared()
    {
        mRAMDataRepository.stop();
    }


}
