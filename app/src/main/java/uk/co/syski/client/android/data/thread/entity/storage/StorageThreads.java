package uk.co.syski.client.android.data.thread.entity.storage;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.StorageEntity;
import uk.co.syski.client.android.data.thread.entity.storage.statement.*;

public class StorageThreads {
    private static final StorageThreads ourInstance = new StorageThreads();

    public static StorageThreads getInstance() {
        return ourInstance;
    }

    private StorageThreads() {}

    public List<StorageEntity> GetStorages(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetStorages().execute(uuids).get();
    }

    public Void InsertAll(StorageEntity... storageEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(storageEntities).get();
    }

    public Void DeleteAll(StorageEntity... storageEntities) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(storageEntities).get();
    }

}
