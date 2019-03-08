package uk.co.syski.client.android.data.thread.entity.linking.systemstorage;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.linking.SystemStorageEntity;
import uk.co.syski.client.android.data.thread.entity.linking.systemstorage.statement.InsertAll;

public class SystemStorageThreads {
    private static final SystemStorageThreads ourInstance = new SystemStorageThreads();

    public static SystemStorageThreads getInstance() {
        return ourInstance;
    }

    private SystemStorageThreads() {}

    public Void InsertAll(SystemStorageEntity... systemStorageEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systemStorageEntities).get();
    }

}
