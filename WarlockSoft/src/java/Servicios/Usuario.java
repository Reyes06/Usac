
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
@WebService(serviceName = "Usuario")
public class Usuario {

    @WebMethod(operationName = "validarUsuario")
    public boolean validarUsuario(@WebParam(name = "nickname") String nickname,
                                  @WebParam(name = "password") String password) {
        Conexion conexion = new Conexion();
        try {
            return conexion.validarUsuario(nickname, password);
        } catch (SQLException ex) {
            System.err.println("Error en el servicio");
            System.err.println(ex.getMessage());
            System.err.println(ex.getLocalizedMessage());
            return false;
        }
    }
    
    @WebMethod(operationName="agregarUsuario")
    public boolean agregarUsuario(@WebParam(name = "nombre") String nombre,
                                  @WebParam(name = "correo") String correo,
                                  @WebParam(name = "dia") int dia,
                                  @WebParam(name = "mes") int mes,
                                  @WebParam(name = "ano") int ano,
                                  @WebParam(name = "password") String password,
                                  @WebParam(name = "nickname") String nickname){
    
    Conexion conexion = new Conexion();
        try {
            return conexion.registrarUsuario(nombre, correo, dia, mes, ano, password, nickname);
        } catch (SQLException ex) {
            System.err.println("Error en el servicio");
            System.err.println(ex.getMessage());
            System.err.println(ex.getMessage());
            return false;
        }
    }
    
    @WebMethod(operationName="obtenerUsuario")
    public String[] obtenerUsuario(@WebParam(name="id_usuario") int id_usuario){
        Conexion con = new Conexion();
        try {
            return con.obtenerUsuario(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @WebMethod(operationName="obtenerDatosUsuario")
    public String[] obtenerDatosUsuario(@WebParam(name="nickname") String nickname,
                                   @WebParam(name="password") String password){
        Conexion con = new Conexion();
        try {
            return con.obtenerUsuario(nickname, password);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @WebMethod(operationName="agregarConocimiento")
    public boolean agregarConocimiento(@WebParam(name="id_usuario") int id_usuario,
                                       @WebParam(name="conocimiento") String conocimiento)
    {
        Conexion con = new Conexion();
        try {
            boolean agregado = con.agregarConocimientoUsuario(conocimiento, id_usuario);
            if(agregado){
                return con.agregarKarmaExtra(id_usuario, id_usuario, conocimiento, 0);
            } else{
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName="agregarKarmaExtra")
    public boolean agregarKarmaExtra(@WebParam(name="id_usuarioRecibidor") int id_usuarioRecibidor, 
                                     @WebParam(name="id_usuarioDador") int id_usuarioDador, 
                                     @WebParam(name="conocimiento") String conocimiento, 
                                     @WebParam(name="karmaExtra") int karmaExtra){
        Conexion con = new Conexion();
        try {
            return con.agregarKarmaExtra(id_usuarioDador, id_usuarioRecibidor, conocimiento, karmaExtra);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    @WebMethod(operationName="enviarMensaje")
    public boolean enviarMensaje(@WebParam(name="id_usuario") int id_usuario,
                                 @WebParam(name="nicknameUsuario") String nicknameUsuario,
                                 @WebParam(name="contenido") String contenido)
    {
        Conexion con = new Conexion();
        try {
            return con.agregarMensaje(nicknameUsuario, id_usuario, contenido);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName="mensajesRecibidos")
    public String[] mensajesRecibidos(@WebParam(name="id_usuario") int id_usuario)
    {
        Conexion con = new Conexion();
        try {
            return con.obtenerMensajesRecibidos(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="mensajesEnviados")
    public String[] mensajesEnviados(@WebParam(name="id_usuario") int id_usuario)
    {
        Conexion con = new Conexion();
        try {
            return con.obtenerMensajesEnviados(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="agregarContacto")
    public boolean agregarContacto(@WebParam(name="nicknameOtro")String nicknameOtro, 
                                   @WebParam(name="id_usuario")int id_usuario){
        Conexion con = new Conexion();
        try {
            return con.agregarContacto(nicknameOtro, id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }   
    
    @WebMethod(operationName="eliminarContacto")
    public boolean eliminarContacto(@WebParam(name="nicknameOtro")String nicknameOtro, 
                                   @WebParam(name="id_usuario")int id_usuario){
        Conexion con = new Conexion();
        try {
            return con.eliminarContacto(nicknameOtro, id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }   
    
    @WebMethod(operationName="obtenerContactos")
    public String[] obtenerContactos(@WebParam(name="id_usuario")int id_usuario){
        Conexion con = new Conexion();
        try {
            return con.obtenerContactos(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
