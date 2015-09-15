//...
public class Student implements Parcelable {
    private String name;
    private String lastname;
    private String school;
    private String classroom;
    private String mail;
    private String telephone;
    private String dni;
    private String rol = "Alumno";
    public Student(Map<String, Object> values) {
        setName((String) values.get("nombre"));
        setLastname((String) values.get("apellido"));
        /*...*/ } }