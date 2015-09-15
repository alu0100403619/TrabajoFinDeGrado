//...
public class MessageSQLHelper extends SQLiteOpenHelper {/*...*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMessagesTable = "CREATE TABLE messages ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idConversation TEXT, " + "dniRemitter TEXT, " +
                "remitter TEXT, " + "day TEXT, " +
                "month TEXT, " + "year TEXT, " +
                "hour TEXT, " + "minutes TEXT, " +
                "message TEXT )";
        String createConversationsTable = "CREATE TABLE conversations ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dniAddressee TEXT, " + "dniRemitter TEXT )";
        //Creando BBDD
        db.execSQL(createMessagesTable);
        db.execSQL(createConversationsTable);}
    /*Operaciones para obtener mensajes, conversaciones, ...*/
    //...
}//class