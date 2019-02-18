package uk.co.syski.client.android.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static Date LongToDate(Long Long) {
        return Long == null ? null : new Date(Long);
    }

    @TypeConverter
    public static Long DateToLong(Date Date) {
        return Date == null ? null : Date.getTime();
    }

    @TypeConverter
    public static String UUIDToString(UUID Id) {
        return Id == null ? null : Id.toString();
    }

    @TypeConverter
    public static UUID StringToUUID(String String) {
        return String == null ? null : UUID.fromString(String);
    }
}
