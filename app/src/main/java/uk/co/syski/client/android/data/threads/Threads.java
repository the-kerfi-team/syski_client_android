package uk.co.syski.client.android.data.threads;

import uk.co.syski.client.android.data.threads.system.SystemThreads;

public class Threads {
    private static final Threads ourInstance = new Threads();

    public static Threads getInstance() {
        return ourInstance;
    }

    private Threads() {
    }

    public final SystemThreads SystemThreads = uk.co.syski.client.android.data.threads.system.SystemThreads.getInstance();
}
