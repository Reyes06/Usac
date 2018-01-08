
package Servicios;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "Proyecto")
public class Proyecto{


    @WebMethod(operationName = "agregarProyecto")
    public boolean agregarProyecto(@WebParam(name = "nombre") String nombre,
                                   @WebParam(name="diaIni") int diaIni,
                                   @WebParam(name="mesIni") int mesIni,
                                   @WebParam(name="anoIni") int anoIni,
                                   @WebParam(name="diaFin") int diaFin,
                                   @WebParam(name="mesFin") int mesFin,
                                   @WebParam(name="anoFin") int anoFin,
                                   @WebParam(name="estado") String estado,
                                   @WebParam(name="salario") double salario,
                                   @WebParam(name="modoPago") String modoPago,
                                   @WebParam(name="id_usuario") int id_usuario) {
        Conexion conexion = new Conexion();
        try {
            return conexion.agregarProyecto(nombre, diaIni, mesIni, anoIni, diaFin, mesFin, anoFin, estado, salario, modoPago, id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName="agregarTareaAlProyecto")
    public boolean agregarTareaAlProyecto(@WebParam(name = "nombre") String nombre,
                                          @WebParam(name="descripcion") String descripcion,
                                          @WebParam(name="diaIni") int diaIni,
                                          @WebParam(name="mesIni") int mesIni,
                                          @WebParam(name="anoIni") int anoIni,
                                          @WebParam(name="diaFin") int diaFin,
                                          @WebParam(name="mesFin") int mesFin,
                                          @WebParam(name="anoFin") int anoFin,
                                          @WebParam(name="precio") double precio,
                                          @WebParam(name="cantidadParticipantes") int cantidadParticipantes,
                                          @WebParam(name="estado") String estado,
                                          @WebParam(name="id_usuario") int id_usuario,
                                          @WebParam(name="conocimientos") String[] conocimientos,
                                          @WebParam(name="id_proyecto") int id_proyecto){
            //Agregar la tarea
            Tarea t = new Tarea();
            t.agregarTarea(nombre, descripcion, diaIni, mesIni, anoIni, diaFin, mesFin, anoFin, precio, cantidadParticipantes, estado, id_usuario, conocimientos);
            //Asingar la tarea al proyecto
            Conexion conexion = new Conexion();
            try {
                return conexion.agregarTareaAlProyecto(id_proyecto);
            } catch (SQLException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
    }
    
    @WebMethod(operationName="obtenerTareas")
    public String[] obtenerTareas(@WebParam(name="id_proyecto")int id_proyecto){
        Conexion conexion = new Conexion();
        try {
            return conexion.obtenerTareasPorProyecto(id_proyecto);
        } catch (SQLException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @WebMethod(operationName="obtenerProyectos")
    public String[] obtenerProyectos(){
        Conexion conexion = new Conexion();
        try {
            return conexion.obtenerProyectos();
        } catch (SQLException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="obtenerMisProyectos")
    public String[] obtenerMisProyectos(@WebParam(name= "id_usuario") int id_usuario){
        Conexion conexion = new Conexion();
        try {
            return conexion.obtenerMisProyectos(id_usuario);
        } catch (SQLException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="obtenerDatosProyecto")
    public String[] obtenerDatosProyecto(@WebParam(name= "id_proyecto") int id_proyecto){
        Conexion conexion = new Conexion();
        try {
            return conexion.obtenerProyecto(id_proyecto);
        } catch (SQLException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName="cambiarEstado")
    public boolean cambiarEstadoProyecto(@WebParam(name= "id_proyecto") int id_proyecto,
                                         @WebParam(name= "estado") String estado){
        Conexion conexion = new Conexion();
        try {
            return conexion.cambiarEstadoProyecto(id_proyecto, estado);
        } catch (SQLException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    
    public static void main(String args[]){
        Proyecto p = new Proyecto();
        String[] tareas = p.obtenerTareas(2);
        System.out.println(tareas[1]);
        System.out.println(tareas[0]);
    }
}
