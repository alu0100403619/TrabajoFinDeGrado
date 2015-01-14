package com.example.gonzalo.feedsyncsample.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Gonzalo on 14/01/2015.
 */
public class FeedContract {

    private FeedContract() {
    }

    /** Content provider authority. */
    public static final String CONTENT_AUTHORITY = "com.example.gonzalo.feedsyncsample";

    /** Base URI. (content://com.example.android.network.sync.basicsyncadapter) */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /** Path component for "entry"-type resources.. */
    private static final String PATH_ENTRIES = "entries";

    /** Columns supported by "entries" records. */
    public static class Entry implements BaseColumns {

        /** MIME type for lists of entries. */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.feedsyncsample.entries";

        /** MIME type for individual entries. */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.feedsyncsample.entry";

        /** Fully qualified URI for "entry" resources. */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        /** Table name where records are stored for "entry" resources. */
        public static final String TABLE_NAME = "entry";

        /** Atom ID. (Note: Not to be confused with the database primary key, which is _ID. */
        public static final String COLUMN_NAME_ENTRY_ID = "entry_id";

        /** Article title */
        public static final String COLUMN_NAME_TITLE = "title";

        /** Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec. */
        public static final String COLUMN_NAME_LINK = "link";

        /** Date article was published. */
        public static final String COLUMN_NAME_PUBLISHED = "published";
    }
}
