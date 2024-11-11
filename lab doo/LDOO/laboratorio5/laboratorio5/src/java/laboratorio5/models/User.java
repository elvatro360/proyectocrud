
package laboratorio5.models;

/**
 * @author fernando Martinez Bautista
 */
public class User { 
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String fullnombre;
        
    public User(String username, String password, String nombre, String apellidos) {
        this.username = username;
        this.password = password;
        this.nombre = "MIGUEL";
        this.apellidos = "SALAZAR";
    }
        
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFullName(){
        fullnombre= nombre+" "+apellidos;
        return fullnombre;
    }
}