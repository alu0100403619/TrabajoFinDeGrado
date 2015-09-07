package com.example.gonzalo.schoolapp.database;
//...
public class MessageSQLHelper extends SQLiteOpenHelper {
//...
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

    public void addMessage(Message message, String idConversation){/*...*/}
    public  void addConversation (String dniSender, String dniRemitter) {/*...*/}
    public String getIdConversation (String dniRemitter) {/*...*/}
    public List<Message> getAllMessagesConversation(String idConversation) {/*...*/}
    public void deleteAllMessageConversation(String idConversation) {/*...*/}
}//class
