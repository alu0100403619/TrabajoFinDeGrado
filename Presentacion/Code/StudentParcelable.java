//...
public class Student implements Parcelable { //...
    //*****Parte de la interfaz Parcelable*****//
    public Student(Parcel in) { readFromParcel(in); }
    @Override
    public int describeContents() { return 0; }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lastname);
        /*...*/ }
    public void readFromParcel(Parcel in) {
        name = in.readString();
        lastname = in.readString();
        school = in.readString(); /*...En el mismo orden que writeToParcel*/}
    public static final Parcelable.Creator CREATOR=new Parcelable.Creator(){
        @Override
        public Student createFromParcel(Parcel in){return new Student(in);}
        @Override
        public Student[] newArray(int size){return new Student[size];}};/*Parcelable.creator*/}