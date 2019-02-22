package uk.co.syski.client.android.data.thread.entity.linking.systemram;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;
import uk.co.syski.client.android.data.thread.entity.linking.systemram.statement.InsertAll;

public class SystemRAMThreads {
    private static final SystemRAMThreads ourInstance = new SystemRAMThreads();

    public static SystemRAMThreads getInstance() {
        return ourInstance;
    }

    private SystemRAMThreads() {}

    public Void InsertAll(SystemRAMEntity... systemRAMEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systemRAMEntities).get();
    }
}
