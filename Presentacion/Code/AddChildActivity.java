//...
public class AddChildActivity extends Activity {
    Firebase childRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        childRef=new Firebase(getString(R.string.studentRef));/*...*/}
    public void addChild (View vew) {
        if (!haveEmptyFields()) { //Obtencion de datos del XML
            Query existStudent = childRef.//...;
            existStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Student not Exist
                    if (dataSnapshot.getValue() == null) {
                        Map<String, Object> studentMap = new HashMap<>();
                        studentMap.put(getString(R.string.bbdd_name), name);
                        uuid = UUID.randomUUID().toString();
                        childRef.child(uuid).setValue(studentMap); }
                }/*...*/ }); }} }//class
