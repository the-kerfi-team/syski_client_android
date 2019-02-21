package uk.co.syski.client.android.data.thread.entity.system;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.thread.entity.system.statement.*;

public class SystemThreads {
    private static final SystemThreads ourInstance = new SystemThreads();

    public static SystemThreads getInstance() {
        return ourInstance;
    }

    private SystemThreads() {}

    public List<SystemEntity> IndexSystems() throws ExecutionException, InterruptedException {
        return new GetAllSystems().execute().get();
    }

    public List<SystemEntity> GetSystems(UUID... Ids) throws ExecutionException, InterruptedException {
        return new GetSystems().execute(Ids).get();
    }

    public Void InsertAll(SystemEntity... systemEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systemEntities).get();
    }

    public Void DeleteAll(SystemEntity... systemEntities) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(systemEntities).get();
    }
}
