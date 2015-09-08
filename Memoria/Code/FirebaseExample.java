//...
//Importar libreias de firebase.client ChildEventListener
//DataSnapshot, Firebase, FirebaseError, Query;
//...
public class FirebaseExample extends Activity {
  Firebase ref;
  //...
  public void onCreate (Bundle savedInstanceBundle) {
    super.onCreate(savedInstanceBundle);
    Firebase.setAndroidContext(this);
    ref = new Firebase ("URL de la tabla en la BBDD");
    //...
  }
  public void preparingData () {
    //Ejemplo de Consulta
    Query query = ref.orderByChild(colegio).equalTo(email);
    query.addChildEventListener( new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {}
      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
      @Override
      public void onCancelled(FirebaseError firebaseError) {}
    });
  }}