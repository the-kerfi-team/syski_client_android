package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.repository.MOBORepository;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.model.viewmodel.SystemMotherboardModel;

public class MotherboardViewModel extends AndroidViewModel{

    private MOBORepository mMOBORepository;
    private LiveData<SystemMotherboardModel> mSystemMOBO;
    private UUID systemId;

    public MotherboardViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mMOBORepository = Repository.getInstance().getMOBORepository();
        mSystemMOBO = mMOBORepository.getSystemMotherboardLiveData(systemId, getApplication().getBaseContext());
    }

    public LiveData<SystemMotherboardModel> get() {
        return mSystemMOBO;
    }

}
