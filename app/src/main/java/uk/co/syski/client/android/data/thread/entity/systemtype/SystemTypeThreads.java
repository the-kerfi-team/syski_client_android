package uk.co.syski.client.android.data.thread.entity.systemtype;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.SystemType;
import uk.co.syski.client.android.data.thread.entity.systemtype.statement.DeleteAll;
import uk.co.syski.client.android.data.thread.entity.systemtype.statement.GetTypeIds;
import uk.co.syski.client.android.data.thread.entity.systemtype.statement.InsertAll;

/**
 * Created by t7014146 on 19/02/19.
 */

public class SystemTypeThreads {
    private static final SystemTypeThreads ourInstance = new SystemTypeThreads();

    public static SystemTypeThreads getInstance() {
        return ourInstance;
    }

    private SystemTypeThreads() {
    }

    public List<UUID> GetTypeIds(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetTypeIds().execute(uuids).get();
    }

    public Void InsertAll(SystemType... SystemTypes) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(SystemTypes).get();
    }

    public Void DeleteAll(SystemType... SystemTypes) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(SystemTypes).get();
    }
}
