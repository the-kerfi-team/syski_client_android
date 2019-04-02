package uk.co.syski.client.android.model.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.MotherboardDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemMotherboardDao;
import uk.co.syski.client.android.model.database.entity.MotherboardEntity;
import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemMotherboardEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.model.viewmodel.OperatingSystemModel;
import uk.co.syski.client.android.model.viewmodel.SystemMotherboardModel;

public enum MOBORepository {
    INSTANCE;

    // Database DAO's
    private MotherboardDao mMOBODao;
    private SystemMotherboardDao mSystemMOBODao;

    // MOBO Cache in Memory
    private HashMap<UUID, MotherboardEntity> mMOBOEntities;
    private HashMap<UUID, SystemMotherboardEntity> mSystemMOBOEntities;
    private HashMap<UUID, SystemMotherboardModel> mSystemMOBOModels;

    // LiveData
    private MutableLiveData<HashMap<UUID, SystemMotherboardModel>> mLiveDataSystemMOBOModels;

    MOBORepository() {
        mMOBODao = SyskiCache.GetDatabase().MotherboardDao();
        mSystemMOBODao = SyskiCache.GetDatabase().SystemMotherboardDao();

        mMOBOEntities = new HashMap<>();
        mSystemMOBOEntities = new HashMap<>();
        mSystemMOBOModels = new HashMap<>();

        mLiveDataSystemMOBOModels = new MutableLiveData();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for OS's
            mMOBOEntities = new loadMOBOEntitiesAsyncTask(mMOBODao, mMOBOEntities).execute().get();

            // Load data from Database for System OS's
            mSystemMOBOEntities = new loadSystemMOBOEntitiesAsyncTask(mSystemMOBODao, mSystemMOBOEntities).execute().get();

            // Set Data in LiveData
            for (Map.Entry<UUID, SystemMotherboardEntity> entry : mSystemMOBOEntities.entrySet())
            {
                SystemMotherboardEntity systemMOBOEntity = entry.getValue();
                MotherboardEntity moboEntity = mMOBOEntities.get(systemMOBOEntity.MotherboardId);
                mSystemMOBOModels.put(entry.getKey(), new SystemMotherboardModel(moboEntity.ModelName, moboEntity.ManufacturerName, moboEntity.Version));
            }
            mLiveDataSystemMOBOModels.postValue(mSystemMOBOModels);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
