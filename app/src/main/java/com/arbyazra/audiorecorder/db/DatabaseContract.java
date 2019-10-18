package com.arbyazra.audiorecorder.db;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {}
    public static class AudioBase implements BaseColumns {
        public static final String TABLE_NAME = "audio";
        public static final String COLUMN_NAME_FILE = "name";
        public static final String COLUMN_NAME_OUTPUTFILE = "title";
        public static final String COLUMN_NAME_ONCREATED = "oncreated";
    }
}
