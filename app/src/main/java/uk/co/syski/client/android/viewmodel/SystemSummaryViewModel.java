package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.VolleySingleton;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.api.requests.system.APISystemRestartRequest;
import uk.co.syski.client.android.api.requests.system.APISystemShutdownRequest;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.UserEntity;
import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.data.repository.SystemRepository;

public class SystemSummaryViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();

    private SystemRepository mSystemRepository;
    private MutableLiveData<SystemEntity> mSystem;
    private UUID systemId;

    public SystemSummaryViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mSystemRepository = Repository.getInstance().getSystemRepository();
        mSystem = mSystemRepository.get(systemId);
    }

    public void shutdownOnClick() {
        UserEntity user = null;
        user = Repository.getInstance().getUserRepository().getUser();
        if (user == null || user.TokenExpiry == null || Calendar.getInstance().getTime().after(user.TokenExpiry))
        {
            VolleySingleton.getInstance(getApplication()).addToRequestQueue(new APITokenRequest(getApplication(), user.Id));
        }
        VolleySingleton.getInstance(this.getApplication()).addToRequestQueue(new APISystemShutdownRequest(getApplication(), systemId));
    }

    public void restartOnClick() {
        UserEntity user = null;
        user = Repository.getInstance().getUserRepository().getUser();
        if (user == null || user.TokenExpiry == null || Calendar.getInstance().getTime().after(user.TokenExpiry))
        {
            VolleySingleton.getInstance(getApplication()).addToRequestQueue(new APITokenRequest(getApplication(), user.Id));
        }
        VolleySingleton.getInstance(getApplication()).addToRequestQueue(new APISystemRestartRequest(getApplication(), systemId));
    }

    public LiveData<SystemEntity> get() { return mSystem; }

}
