package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.MotherboardEntity;
import uk.co.syski.client.android.model.repository.MOBORepository;
import uk.co.syski.client.android.model.repository.Repository;

public class MotherboardViewModel extends AndroidViewModel{

    private MOBORepository mMOBORepository;
    private MutableLiveData<List<MotherboardEntity>> mSystemMOBOList;
    private UUID systemId;

    public MotherboardViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mMOBORepository = Repository.getInstance().getMOBORepository();
        mSystemMOBOList = mMOBORepository.get(systemId);
    }

    public MutableLiveData<List<MotherboardEntity>> get() {
        return mSystemMOBOList;
    }

}
