package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.SystemRAMModel;
import uk.co.syski.client.android.model.repository.RAMRepository;
import uk.co.syski.client.android.model.repository.Repository;

public class SystemRAMViewModel extends AndroidViewModel {

    private RAMRepository mRAMRepository;
    private LiveData<List<SystemRAMModel>> mRAMList;
    private UUID systemId;

    public SystemRAMViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mRAMRepository = Repository.getInstance().getRAMRepository();
        mRAMList = mRAMRepository.getSystemRAMsLiveData(systemId, application.getBaseContext());
    }

    public LiveData<List<SystemRAMModel>> get() {
        return mRAMList;
    }

}
