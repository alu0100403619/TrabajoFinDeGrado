//Importar Librerias de Firebase: FindCallback, Parse
//ParseException, ParseObject, ParseQuery, ParseUser
public class MainActivity extends ListActivity {
    public static final String APPLICATION_ID = "APP_ID";
    public static final String CLIENT_KEY = "ClientKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //...
        //Inicializando Parse
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        //Para autenticacion de Usuarios
        ParseUser currentUser = ParseUser.getCurrentUser(); }
    private void refreshPostList() {
        Object[] authors = {null, ParseUser.getCurrentUser()};
        //Ejemplo de consulta a Parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("author", null); //Notas sin autor
        query.whereContainedIn("author", Arrays.asList(authors));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    posts.clear();
                    for (ParseObject post : postList) {
                        Note note = new Note(post.getObjectId(),
                                post.getString("title"), post.getString("content"));
                        posts.add(note);}//for
                    ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged(); } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }}});//query.findInBackground
    }}//class