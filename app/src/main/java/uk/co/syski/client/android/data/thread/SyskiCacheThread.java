package uk.co.syski.client.android.data.thread;

import uk.co.syski.client.android.data.thread.system.SystemThreads;
import uk.co.syski.client.android.data.thread.type.TypeThreads;

public class SyskiCacheThread {
    private static final SyskiCacheThread ourInstance = new SyskiCacheThread();

    public static SyskiCacheThread getInstance() {
        return ourInstance;
    }

    private SyskiCacheThread() {
    }

    public final SystemThreads SystemThreads = uk.co.syski.client.android.data.thread.system.SystemThreads.getInstance();

    public final TypeThreads TypeThreads = uk.co.syski.client.android.data.thread.type.TypeThreads.getInstance();
}
