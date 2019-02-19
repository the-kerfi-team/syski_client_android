package uk.co.syski.client.android.data.threads;

import uk.co.syski.client.android.data.threads.system.SystemThreads;

public class SyskiCacheThread {
    private static final SyskiCacheThread ourInstance = new SyskiCacheThread();

    public static SyskiCacheThread getInstance() {
        return ourInstance;
    }

    private SyskiCacheThread() {
    }

    public final SystemThreads SystemThreads = uk.co.syski.client.android.data.threads.system.SystemThreads.getInstance();
}
