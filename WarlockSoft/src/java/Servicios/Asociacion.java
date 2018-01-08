
package Servicios;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "Asociacion")
public class Asociacion {

    @WebMethod(operationName = "crearAsociacion")
    public int crearAsociacion(@WebParam(name = "nombre") String nombre, 
                                   @WebParam(name="id_creador") int id_usuario) {
        Conexion c = new Conexion();
        try {
            int id_asociacion = c.crearAsociacion(nombre);
            c.agregarUsuariosPorAsociacion(id_usuario, id_asociacion, "Administrador");
            return id_asociacion;
        } catch (SQLException ex) {
            Logger.getLogger(Asociacion.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    @WebMethod(operationName = "nuevoMiembro")
    public boolean agregarMiembro(@WebParam(name = "nickname") String nickname, 
                                  @WebParam(name = "id_asociacion") int id_asociacion){
        Conexion c = new Conexion();
        try {
            return c.agregarUsuariosPorAsociacion(nickname, id_asociacion, "Miembro");
        } catch (SQLException ex) {
            Logger.getLogger(Asociacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName = "eliminarMiembro")
    public boolean eliminarMiembro(@WebParam(name = "nickname") String nickname, 
                                  @WebParam(name = "id_asociacion") int id_asociacion){
        Conexion c = new Conexion();
        try {
            return c.eliminarUsuariosPorAsociacion(nickname, id_asociacion);
        } catch (SQLException ex) {
            Logger.getLogger(Asociacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName = "cambiarTipoMembresia")
    public boolean cambiarTipoMembresia(@WebParam(name = "nickname") String nickname, 
                                  @WebParam(name = "id_asociacion") int id_asociacion,
                                  @WebParam(name = "membresia") String membresia){
        Conexion c = new Conexion();
        try {
            return c.cambiarTipoUsuariosPorAsociacion(nickname, id_asociacion, membresia);
        } catch (SQLException ex) {
            Logger.getLogger(Asociacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName = "obtenerUsuariosPorAsociacion")
    public String[] obtenerUsuariosPorAsociacion(@WebParam(name = "id_asociacion")int id_asociacion){
        Conexion c = new Conexion();
        try {
            return c.obtenerUsuariosPorAsociacion(id_asociacion);
        } catch (SQLException ex) {
            Logger.getLogger(Asociacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "obtenerPublicacionesAsociacion")
    public String[] obtenerPublicacionesAsociacion(@WebParam(name = "id_asociacion")int id_asociacion){
        Conexion c = new Conexion();
        try {
            return c.obtenerPublicacionesAsociacion(id_asociacion);
        } catch (SQLException ex) {
            Logger.getLogger(Asociacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "obtenerAsociacionesPorUsuario")
    public String[] obtenerAsociacionesPorUsuario(@WebParam(name = "id_usuario")int id_usuario,
                                                  @WebParam(name="tipo") String tipo){
        Conexion c = new Conexion();
        try {
            return c.obtenerAsociacionesPorUsuario(id_usuario, tipo);
        } catch (SQLException ex) {
            Logger.getLogger(Asociacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
