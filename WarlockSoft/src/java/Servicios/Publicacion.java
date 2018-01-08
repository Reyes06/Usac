
package Servicios;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "Publicacion")
public class Publicacion {

    @WebMethod(operationName = "agregarPublicacion")
    public boolean agregarPublicacion(@WebParam(name = "id_usuario")int id_usuario,
                                     @WebParam(name="contenido")String contenido) {
        Conexion c = new Conexion();
        try {
            return c.agregarPublicacion(id_usuario, contenido);
        } catch (SQLException ex) {
            Logger.getLogger(Publicacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName="comentarPublicacion")
    public boolean comentarPublicacion(@WebParam(name="id_usuario") int id_usuario,
                                       @WebParam(name="id_publicacion") int id_publicacion,
                                       @WebParam(name="contenido") String contenido){
        Conexion c = new Conexion();
        try {
            return c.agregarComentario(id_usuario, id_publicacion, contenido);
        } catch (SQLException ex) {
            Logger.getLogger(Publicacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName="comentarPub")
    public boolean comentarPub(@WebParam(name="nickname") String nickname,
                                       @WebParam(name="id_publicacion") int id_publicacion,
                                       @WebParam(name="contenido") String contenido){
        Conexion c = new Conexion();
        try {
            return c.agregarComentario(nickname, id_publicacion, contenido);
        } catch (SQLException ex) {
            Logger.getLogger(Publicacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName="obtenerComentarios")
    public String[] obtenerComentariosPublicacion(@WebParam(name="id_publicacion") int id_publicacion){
        Conexion c = new Conexion();
        try {
            return c.obtenerComentarios(id_publicacion);
        } catch (SQLException ex) {
            Logger.getLogger(Publicacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="obtenerPublicacionesContactos")
    public String[] obtenerPublicacionesContactos(@WebParam(name="id_usuario") int id_usuario){
        Conexion c = new Conexion();
        try {
            return c.obtenerPublicacionesContactos(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Publicacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="obtenerPublicacionesIndividuales")
    public String[] obtenerPublicacionesIndividuales(@WebParam(name="id_usuario") int id_usuario){
        Conexion c = new Conexion();
        try {
            return c.obtenerPublicacionesIndividuales(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Publicacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
}
