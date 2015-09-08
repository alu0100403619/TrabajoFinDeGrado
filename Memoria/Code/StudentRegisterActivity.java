//...
public class StudentRegisterActivity extends Activity {
    //...
    public boolean haveEmptyFields() {/*...*/}
    public void submit (View view) {
        //...
        if (!haveEmptyFields()) {
            rootRef.createUser(mail, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    Map<String, Object> aluMap = new HashMap<>();
                    aluMap.put(getString(R.string.bbdd_name), name);
                    aluMap.put(getString(R.string.bbdd_lastname), lastname);
                    //...
                    String uuid = UUID.randomUUID().toString();
                      studentRef.child(uuid).setValue(aluMap);
                    //Lanzando StudentTabActivity
                    rootRef.authWithPassword(mail, password, new Firebase.AuthResultHandler() {/*...*/}
                @Override
                public void onError(FirebaseError firebaseError) {/*..*/} });//rootRef
        }//if
    }
    //...
}
