//import ChildEventListener, DataSnapshot, Firebase, FirebaseError y Query;
public class StudentActivity extends ListActivity {
    /*...*/ Firebase aluRef;
    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);
        aluRef = new Firebase (getString(R.string.studentRef));/*...*/}
    public void preparingData(){ //Obtener los Student de la misma clase
        Query getClassmates = aluRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s){}
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,String s){}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot){}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s){}
            @Override
            public void onCancelled(FirebaseError firebaseError){} });
        }}//class