
package Servicios;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Eddy
 */
@WebService(serviceName = "Conocimiento")
public class Conocimiento {

    @WebMethod(operationName = "obtenerConocimientos")
    public String[] obtenerCOnocimientos() {
        Conexion conexion = new Conexion();
        try {
            return conexion.obtenerConocimientos();
        } catch (SQLException ex) {
            Logger.getLogger(Conocimiento.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="obtenerConocimientosUsuario")
    public String[] obtenerConocimientosUsuario(@WebParam(name= "id_usuario") int id_usuario){
        Conexion conexion = new Conexion();
        try {
            return conexion.obtenerConocimientosUsuario(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Conocimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /*
    public static void main(String[] args){
        Conocimiento c = new Conocimiento();
        String[] con = c.obtenerConocimientosUsuario(1);
        for (int i = 0; i < con.length; i++) {
            System.out.println(con[i]);
        }
    }
    */
}



