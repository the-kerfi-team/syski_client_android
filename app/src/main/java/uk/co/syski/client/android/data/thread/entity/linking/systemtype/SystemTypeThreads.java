package uk.co.syski.client.android.data.thread.entity.linking.systemtype;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.linking.SystemTypeEntity;
import uk.co.syski.client.android.data.thread.entity.linking.systemtype.statement.InsertAll;

public class SystemTypeThreads {
    private static final SystemTypeThreads ourInstance = new SystemTypeThreads();

    public static SystemTypeThreads getInstance() {
        return ourInstance;
    }

    private SystemTypeThreads() {}

    public Void InsertAll(SystemTypeEntity... systemTypeEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systemTypeEntities).get();
    }
}
