package com.example.gonzalo.schoolapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gonzalo.schoolapp.clases.Date;
import com.example.gonzalo.schoolapp.clases.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gonzalo on 27/05/2015.
 */
public class MessageSQLHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "MessagesDB";

    public MessageSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMessagesTable = "CREATE TABLE messages ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idConversation TEXT, " +
                "dniRemitter TEXT, " +
                "remitter TEXT, " +
                "day TEXT, " +
                "month TEXT, " +
                "year TEXT, " +
                "hour TEXT, " +
                "minutes TEXT, " +
                "message TEXT )";

        String createConversationsTable = "CREATE TABLE conversations ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dniSender TEXT, " +
                "dniRemitter TEXT )";

        //Creando BBDD
        db.execSQL(createMessagesTable);
        db.execSQL(createConversationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS messages");
        db.execSQL("DROP TABLE IF EXISTS conversations");

        // create fresh books table
        this.onCreate(db);
    }

    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Table name
    private static final String TABLE_MESSAGES = "messages";
    private static final String TABLE_CONVERSATIONS = "conversations";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IDCONVERSATION = "idConversation";
    private static final String KEY_DNIREMITTER = "dniRemitter";
    private static final String KEY_REMITTER = "remitter";
    private static final String KEY_DNISENDER = "dniSender";
    private static final String KEY_DAY= "day";
    private static final String KEY_MONTH= "month";
    private static final String KEY_YEAR= "year";
    private static final String KEY_HOUR= "hour";
    private static final String KEY_MINUTES= "minutes";
    private static final String KEY_MESSAGE = "message";

    private static final String[] COLUMNS_MESSAGE = {KEY_ID, KEY_IDCONVERSATION, KEY_DNIREMITTER,
            KEY_REMITTER, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_HOUR, KEY_MINUTES, KEY_MESSAGE};

    private static final String[] COLUMNS_CONVERSATION = {KEY_ID};

    public void addMessage(Message message, String idConversation){
        Log.d("addMessage", message.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_IDCONVERSATION, idConversation);
        values.put(KEY_DNIREMITTER, message.getDniRemitter());
        values.put(KEY_REMITTER, message.getRemitter());
        values.put(KEY_DAY, message.getDate().getDay());
        values.put(KEY_MONTH, message.getDate().getMonth());
        values.put(KEY_YEAR, message.getDate().getYear());
        values.put(KEY_HOUR, message.getDate().getHour());
        values.put(KEY_MINUTES, message.getDate().getMinutes());
        values.put(KEY_MESSAGE, message.getMessage());

        // 3. insert
        db.insert(TABLE_MESSAGES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values


        // 4. close
        db.close();
    }

    public  void addConversation (String dniSender, String dniRemitter) {
        Log.d("addConversation", dniSender + "->" + dniRemitter);

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_DNISENDER, dniSender);
        values.put(KEY_DNIREMITTER, dniRemitter);

        // 3. insert
        db.insert(TABLE_CONVERSATIONS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public String getIdConversation (String dniRemitter) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONVERSATIONS,
                COLUMNS_CONVERSATION,
                KEY_DNIREMITTER + " = ?",
                new String[] {dniRemitter}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        else {
            return null;
        }
    }

    public List<Message> getAllMessagesConversation(String idConversation) {
        List<Message> messages = new LinkedList<Message>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_MESSAGES, // a. tabla a consultar (FROM)
                        COLUMNS_MESSAGE, // b. columnas a devolver (SELECT)
                        " idConversation = ?", // c. WHERE
                        new String[]{idConversation}, // d. reemplaza a ?
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. go over each row, build book and add it to list
        Message message = null;
        if (cursor.moveToFirst()) {
            do {
                message = new Message();
                message.setDniRemitter(cursor.getString(2));
                message.setRemitter(cursor.getString(3));
                Date date = new Date();
                date.setDay(Long.valueOf(cursor.getString(4)));
                date.setMonth(Long.valueOf(cursor.getString(5)));
                date.setYear(Long.valueOf(cursor.getString(6)));
                date.setHour(Long.valueOf(cursor.getString(7)));
                date.setMinutes(Long.valueOf(cursor.getString(8)));
                message.setDate(date);
                message.setMessage(cursor.getString(9));

                // Add book to books
                messages.add(message);
            } while (cursor.moveToNext());
        }

        Log.d("getAllRemMessages()", messages.toString());

        // return messages
        return messages;
    }

    public void deleteAllMessageConversation(String idConversation) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //2. delete
        db.delete(TABLE_MESSAGES,
                KEY_IDCONVERSATION + " = ?",
                new String[]{idConversation});

        //3.close
        db.close();
    }

    public void deleteConversation(String idConversation) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //2. delete
        db.delete(TABLE_CONVERSATIONS,
                KEY_ID + " = ?",
                new String[]{idConversation});

        //3.close
        db.close();
    }
}//class
