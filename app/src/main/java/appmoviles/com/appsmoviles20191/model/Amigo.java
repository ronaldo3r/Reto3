package appmoviles.com.appsmoviles20191.model;

public class Amigo {

    private String id;
    private String nombre;
    private String edad;
    private String telefono;
    private String email;
    private String userID;

    //Serializar
    public Amigo() {
    }

    public Amigo(String id, String nombre, String edad, String telefono, String email, String userID) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.email = email;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Amigo){
            return this.id.equals(((Amigo) obj).id);
        }
        return false;
    }
}
