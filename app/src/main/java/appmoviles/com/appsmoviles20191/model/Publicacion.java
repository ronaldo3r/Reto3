package appmoviles.com.appsmoviles20191.model;

public class Publicacion {
    private String nombre;
    private String fecha;
    private String mensaje;

    public Publicacion(){}

    public Publicacion(String nombre, String fecha, String mensaje) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
