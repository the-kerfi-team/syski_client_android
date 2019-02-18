package uk.co.syski.client.android.data;

import android.arch.persistence.room.Room;
import android.content.Context;

public class SyskiCache {

    private SyskiCache() {
    }

    private static CacheDatabase Database;

    /**
     * Only works if the Database does not yet exist.
     */
    public static void BuildDatabase(Context Context) {
        if (Database == null)
            Database = Room.databaseBuilder(Context, CacheDatabase.class, "SyskiCache").build();
    }

    public static CacheDatabase GetDatabase() {
        return Database;
    }
}
