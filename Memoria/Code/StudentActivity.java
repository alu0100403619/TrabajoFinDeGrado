//...
public class StudentActivity extends ListActivity {
    List<Student> alus;
    //...
    public void onCreate (Bundle savedInstanceBundle) {/*...*/}
    public void preparingData(){
        //Obtener los Student de la misma clase
        Query getClassmates = aluRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Student alu = new Student(values);
                String dni = values.get(getString(R.string.bbdd_dni)).toString();
                if ((values.get(getString(R.string.bbdd_class)).equals(clase)) && (!alu.getMail().isEmpty()) &&
                        (!dni.equals(myDNI))) {
                    alus.add(alu); }//if
                //Seteamos el ArrayAdapter
                ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(StudentActivity.this,
                        R.layout.list_item_layout, alus);
                setListAdapter(adapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });}//function
/*...*/}//class