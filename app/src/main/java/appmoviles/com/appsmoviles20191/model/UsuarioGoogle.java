package appmoviles.com.appsmoviles20191.model;

public class UsuarioGoogle {

    private String id;
    private String telefono;
    private String correo;

    public UsuarioGoogle() {
    }

    public UsuarioGoogle(String id, String telefono, String correo) {
        this.id = id;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
