
package Servicios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    public Connection conectar() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/warlocksoft?user=root&password=12326");
            return conexion;
        } catch (ClassNotFoundException | SQLException e) {
             System.err.println("Lo siento... algo ha fallado");
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }

    public boolean validarUsuario(String nickname, String password) throws SQLException {
        String consulta = "SELECT nickname, password FROM usuario "
                + "WHERE nickname = ? AND  password = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nickname);
            sentencia.setString(2, password);
            ResultSet tabla = sentencia.executeQuery();
            conexion.close();
            return tabla.next();
        } catch (SQLException ex) {
            System.err.println("Lo siento... algo ha fallado");
            System.err.println(ex.getMessage());
            System.err.println(ex.getLocalizedMessage());
            return false;
        } finally {
            if (null != conexion) {
                conexion.close();
            }
            if (null != sentencia) {
                sentencia.close();
            }
        }
    }
    
    public boolean registrarUsuario(String nombre, String correo, int dia, int mes, int ano, String password, String nickname) throws SQLException{
        String consulta = "INSERT INTO Usuario VALUES(null,?,?,?,?,?)";
        PreparedStatement sentencia = null;
        Connection conexion = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nombre);
            sentencia.setString(2, correo);
            sentencia.setDate(3, new Date(ano, mes, dia));
            sentencia.setString(4, password);
            sentencia.setString(5, nickname);
            sentencia.executeUpdate();
            sentencia.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Lo siento... algo fallo");
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean registrarUsuario(int id_usuario, String nombre, String correo, int dia, int mes, int ano, String password, String nickname) throws SQLException{
        String consulta = "INSERT INTO Usuario VALUES(?,?,?,?,?,?)";
        PreparedStatement sentencia = null;
        Connection conexion = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            sentencia.setString(2, nombre);
            sentencia.setString(3, correo);
            sentencia.setDate(4, new Date(ano, mes, dia));
            sentencia.setString(5, password);
            sentencia.setString(6, nickname);
            sentencia.executeUpdate();
            sentencia.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Lo siento... algo fallo");
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarConcimientoNecesario(String[] conocimientos, int id_tarea) throws SQLException {
        for (String conocimiento : conocimientos) {
            String consulta = "INSERT INTO ConocimientoNecesario (id_conocimiento, id_tarea) "
                    + "SELECT C.id_conocimiento, T.id_tarea FROM Tarea T, Conocimiento C "
                    + "WHERE T.id_tarea = ? AND C.nombre = ?";
            Connection conexion = null;
            PreparedStatement sentencia = null;
            try {
                conexion = conectar();
                sentencia = conexion.prepareStatement(consulta);
                sentencia.setInt(1, id_tarea);
                sentencia.setString(2, conocimiento);
                sentencia.executeUpdate();
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Lo siento... algo ha fallado");
                System.err.println(e.getMessage());
                System.err.println(e.getLocalizedMessage());
                return false;
            } finally {
                if(conexion != null){
                    conexion.close();
                }
                if(sentencia != null){
                    sentencia.close();
                }
            }
        }
        return true;
    }

    public boolean agregarTarea(String nombre, String descripcion, int diaIni, int mesIni, int anoIni, int diaFin,int mesFin,int anoFin, double precio, int cantMax, String estado, int id_usuario, String[] conocimientos) throws SQLException {
        String consulta = "INSERT INTO Tarea VALUES (null,?,?,?,?,?,?,?,?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            //Guardar datos en la tabla Tarea
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nombre);
            sentencia.setString(2, descripcion);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String f = anoIni+"-"+mesIni+"-"+diaIni;
            java.util.Date fechaIni = sdf.parse(f);
            
            sentencia.setDate(3, new Date(fechaIni.getTime()));
            
            f = anoFin+"-"+mesFin+"-"+diaFin;
            java.util.Date fechaFin = sdf.parse(f);
            
            sentencia.setDate(4, new Date(fechaFin.getTime()));
            sentencia.setDouble(5, precio);
            sentencia.setInt(6, cantMax);
            sentencia.setString(7, estado);
            sentencia.setInt(8, id_usuario);
            sentencia.executeUpdate();
            conexion.close();
            //Guardar datos en la tabla conocimientoNecesario
            consulta = "SELECT Max(id_tarea) FROM Tarea";
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet id_tarea = sentencia.executeQuery();
            conexion.close();
            if(id_tarea.next()){
                agregarConcimientoNecesario(conocimientos, id_tarea.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            System.err.println("Lo siento... algo ha fallado");
            System.err.println(ex.getMessage());
            System.err.println(ex.getLocalizedMessage());
            return false;
        } catch (ParseException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
            if (sentencia != null) {
                sentencia.close();
            }
        }
    }
    
    public boolean agregarTarea(int id_tarea, String nombre, String descripcion, int diaIni, int mesIni, int anoIni, int diaFin,int mesFin,int anoFin, double precio, int cantMax, String estado, String nickname) throws SQLException {
        String consulta = "INSERT INTO Tarea "
                + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, id_usuario "
                + "FROM usuario WHERE nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            //Guardar datos en la tabla Tarea
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_tarea);
            sentencia.setString(2, nombre);
            sentencia.setString(3, descripcion);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String f = anoIni+"-"+mesIni+"-"+diaIni;
            java.util.Date fechaIni = sdf.parse(f);
            
            sentencia.setDate(4, new Date(fechaIni.getTime()));
            
            f = anoFin+"-"+mesFin+"-"+diaFin;
            java.util.Date fechaFin = sdf.parse(f);
            
            sentencia.setDate(5, new Date(fechaFin.getTime()));
            sentencia.setDouble(6, precio);
            sentencia.setInt(7, cantMax);
            sentencia.setString(8, estado);
            sentencia.setString(9, nickname);
            sentencia.executeUpdate();
            conexion.close();
            return true;
        } catch (SQLException ex) {
            System.err.println("Lo siento... algo ha fallado");
            System.err.println(ex.getMessage());
            System.err.println(ex.getLocalizedMessage());
            return false;
        } catch (ParseException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
            if (sentencia != null) {
                sentencia.close();
            }
        }
    }
    
    public boolean agregarProyecto(String nombre, int diaIni, int mesIni, int anoIni, int diaFin, int mesFin, int anoFin, String estado, double salario, String modoPago, int id_usuario) throws SQLException{
        String consulta = "INSERT INTO Proyecto VALUES (null,?,?,?,?,?,?,?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            //Guardar los datos en 'proyecto'
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nombre);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String f = anoIni+"-"+mesIni+"-"+diaIni;
            java.util.Date fechaIni = sdf.parse(f);
            
            sentencia.setDate(2, new Date(fechaIni.getTime()));
            
            f = anoFin+"-"+mesFin+"-"+diaFin;
            java.util.Date fechaFin = sdf.parse(f);
            
            sentencia.setDate(3, new Date(fechaFin.getTime()));
            sentencia.setString(4, estado);
            sentencia.setDouble(5, salario);
            sentencia.setString(6, modoPago);
            sentencia.setInt(7, id_usuario);
            sentencia.executeUpdate();
            sentencia.close();
            return true;
        } catch (SQLException ex) {
            System.err.println("Lo siento... algo ha fallado");
            System.err.println(ex.getMessage());
            System.err.println(ex.getLocalizedMessage());
            return false;
        } catch (ParseException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarProyecto(int id_proyecto, String nombre, int diaIni, int mesIni, int anoIni, int diaFin, int mesFin, int anoFin, String estado, double salario, String modoPago, String nickname) throws SQLException{
        String consulta = "INSERT INTO Proyecto "
                + "SELECT ?, ?, ?, ?, ?, ?, ?, id_usuario "
                + "FROM usuario WHERE nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            //Guardar los datos en 'proyecto'
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_proyecto);
            sentencia.setString(2, nombre);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String f = anoIni+"-"+mesIni+"-"+diaIni;
            java.util.Date fechaIni = sdf.parse(f);
            
            sentencia.setDate(3, new Date(fechaIni.getTime()));
            
            f = anoFin+"-"+mesFin+"-"+diaFin;
            java.util.Date fechaFin = sdf.parse(f);
            
            sentencia.setDate(4, new Date(fechaFin.getTime()));
            
            sentencia.setString(5, estado);
            sentencia.setDouble(6, salario);
            if(modoPago == null){
                sentencia.setString(7, "Quincenal");
            } else {
                sentencia.setString(7, modoPago);
            }
            sentencia.setString(8, nickname);
            sentencia.executeUpdate();
            sentencia.close();
            return true;
        } catch (SQLException ex) {
            System.err.println("Lo siento... algo ha fallado");
            System.err.println(ex.getMessage());
            System.err.println(ex.getLocalizedMessage());
            return false;
        } catch (ParseException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarTareaAlProyecto(int id_proyecto) throws  SQLException{
        //Obtener el id de la ultima tareas ingresadas
        String consulta = "SELECT Max(id_tarea) FROM Tarea";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            conexion.close();
            if(rs.next()){
                //Guardar la tarea dentro del proyecto
                consulta = "INSERT INTO TareasPorProyecto "
                         + "VALUES (null,?,?)";
                conexion = conectar();
                sentencia = conexion.prepareStatement(consulta);
                sentencia.setInt(1, rs.getInt(1));
                sentencia.setInt(2, id_proyecto);
                sentencia.executeUpdate();
                conexion.close();
            }
            return true;
        } catch (SQLException ex) {
            System.err.println("Lo siento... algo ha fallado");
            System.err.println(ex.getMessage());
            System.err.println(ex.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerConocimientos() throws SQLException{
        String consulta = "SELECT nombre FROM Conocimiento";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> conocimientos = new ArrayList();
            while(rs.next()){
                conocimientos.add(rs.getString(1));
            }
            conexion.close();
            return conocimientos.toArray(new String[conocimientos.size()]);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally{
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerTareasPorProyecto(int id_proyecto) throws SQLException{
        String consulta = "SELECT t.id_tarea, t.nombre  FROM Tarea t, TareasPorProyecto tpp "
                        + "WHERE tpp.id_tarea = t.id_tarea "
                        + "AND tpp.id_proyecto = ? ";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_proyecto);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> temp = new ArrayList();
            while(rs.next()){
                int id_tarea = rs.getInt(1);
                String nombre = rs.getString(2);
                String tarea = Integer.toString(id_tarea)+";"+ nombre;
                temp.add(tarea);
            }
            conexion.close();
            return temp.toArray(new String[temp.size()]);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerTareasIndividuales() throws SQLException{
        String consulta = "SELECT T.* FROM Tarea T "
                        + "WHERE T.estado = 'Activa' "
                        + "AND T.id_tarea NOT IN "
                        + "(SELECT id_tarea FROM TareasPorProyecto)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            ArrayList<String> array = new ArrayList();
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            while(rs.next()){
                String id_tarea = Integer.toString(rs.getInt(1));
                String nombre = rs.getString(2);
                String descripcion = rs.getString(3);
                String fecha_ini = rs.getDate(4).toString();
                String precio = Double.toString(rs.getDouble(5));
                String tiempoEstimado = Integer.toString(rs.getInt(6));
                String cantParticipantes = Integer.toString(rs.getInt(7));
                String estado = rs.getString(8);
                String id_usuario = Integer.toString(rs.getInt(9));
                String t = id_tarea+";"+nombre+";"+descripcion+";"+fecha_ini+";"+precio+";"+tiempoEstimado+";"+cantParticipantes+";"+estado+";"+id_usuario;
                array.add(t);
            }
            conexion.close();
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerProyectos() throws SQLException{
        String consulta= "SELECT * FROM PROYECTO WHERE estado = 'Activo'";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            ArrayList<String> array = new ArrayList();
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            while(rs.next()){
                String id_proyecto = Integer.toString(rs.getInt(1));
                String nombre = rs.getString(2);
                String fecha_ini = rs.getDate(3).toString();
                String fecha_fin = rs.getDate(4).toString();
                String estado = rs.getString(5);
                String salario = Double.toString(rs.getDouble(6));
                String modoPago = rs.getString(7);
                String id_usuario = Integer.toString(rs.getInt(8));
                String t = id_proyecto+";"+nombre+";"+fecha_ini+";"+fecha_fin+";"+estado+";"+salario+";"+modoPago+";"+id_usuario;
                array.add(t);
            }
            conexion.close();
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        }finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    
    public String[] obtenerUsuario(int id_usuario) throws SQLException{
        String consulta = "SELECT * FROM Usuario WHERE id_usuario = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1,id_usuario);
            ResultSet rs = sentencia.executeQuery();
            String[] usuario = new String[6];
            if(rs.next()){
                usuario[0] = Integer.toString(rs.getInt(1));
                usuario[1] = rs.getString(2);
                usuario[2] = rs.getString(3);
                usuario[3] = rs.getDate(4).toString();
                usuario[4] = rs.getString(5);
                usuario[5] = rs.getString(6);
            }
            conexion.close();
            return usuario;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerUsuario(String nick, String pass) throws SQLException{
        String consulta = "SELECT * FROM Usuario WHERE nickname = ? AND password = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1,nick);
            sentencia.setString(2,pass);
            ResultSet rs = sentencia.executeQuery();
            String[] usuario = new String[6];
            if(rs.next()){
                usuario[0] = Integer.toString(rs.getInt(1));
                usuario[1] = rs.getString(2);
                usuario[2] = rs.getString(3);
                usuario[3] = rs.getDate(4).toString();
                usuario[4] = rs.getString(5);
                usuario[5] = rs.getString(6);
            }
            conexion.close();
            return usuario;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerMisTareasIndividuales(int id_usuario) throws SQLException{
        String consulta = "SELECT T.* FROM Tarea T "
                        + "WHERE T.id_usuario = ? "
                        + "AND T.id_tarea NOT IN "
                        + "(SELECT tpp.id_tarea FROM tareasporproyecto tpp)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            ArrayList<String> array = new ArrayList();
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1,id_usuario);
            ResultSet rs = sentencia.executeQuery();
            while(rs.next()){
                String id_tarea = Integer.toString(rs.getInt(1));
                String nombre = rs.getString(2);
                String descripcion = rs.getString(3);
                String fecha_ini = rs.getDate(4).toString();
                String fecha_fin = rs.getDate(5).toString();
                String precio = Double.toString(rs.getDouble(6));
                String cantParticipantes = Integer.toString(rs.getInt(7));
                String estado = rs.getString(8);
                String id_user = Integer.toString(rs.getInt(9));
                String t = id_tarea+";"+nombre+";"+descripcion+";"+fecha_ini+";"+fecha_fin+";"+precio+";"+cantParticipantes+";"+estado+";"+id_user;
                array.add(t);
            }
            conexion.close();
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerMisProyectos(int id_usuario) throws SQLException{
        String consulta= "SELECT * FROM PROYECTO WHERE id_usuario = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            ArrayList<String> array = new ArrayList();
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1,id_usuario);
            ResultSet rs = sentencia.executeQuery();
            while(rs.next()){
                String id_proyecto = Integer.toString(rs.getInt(1));
                String nombre = rs.getString(2);
                String fecha_ini = rs.getDate(3).toString();
                String fecha_fin = rs.getDate(4).toString();
                String estado = rs.getString(5);
                String salario = Double.toString(rs.getDouble(6));
                String modoPago = rs.getString(7);
                String id_user = Integer.toString(rs.getInt(8));
                String t = id_proyecto+";"+nombre+";"+fecha_ini+";"+fecha_fin+";"+estado+";"+salario+";"+modoPago+";"+id_user;
                array.add(t);
            }
            conexion.close();
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        }finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerProyecto(int id_proyecto) throws SQLException{
        String consulta = "SELECT * FROM proyecto WHERE id_proyecto = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1,id_proyecto);
            ResultSet rs = sentencia.executeQuery();
            String[] p = new String[8];
            if(rs.next()){
                String id_proyect = Integer.toString(rs.getInt(1));
                String nombre = rs.getString(2);
                String fecha_ini = rs.getDate(3).toString();
                String fecha_fin = rs.getDate(4).toString();
                String estado = rs.getString(5);
                String salario = Double.toString(rs.getDouble(6));
                String modoPago = rs.getString(7);
                String id_user = Integer.toString(rs.getInt(8));
                p[0] = id_proyect;
                p[1] = nombre;
                p[2] = fecha_ini;
                p[3] = fecha_fin;
                p[4] = estado;
                p[5] = salario;
                p[6] = modoPago;
                p[7] = id_user;                
            }
            conexion.close();
            return p;
        } catch (SQLException e) {
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerTarea(int id_tarea) throws SQLException{
        String consulta = "SELECT * FROM Tarea WHERE id_tarea = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1,id_tarea);
            ResultSet rs = sentencia.executeQuery();
            String[] p = new String[9];
            if(rs.next()){
                String id_t = Integer.toString(rs.getInt(1));
                String nombre = rs.getString(2);
                String descripcion= rs.getString(3);
                String fecha_ini = rs.getDate(4).toString();
                String precio = Double.toString(rs.getDouble(5));
                String tiempoEstimado = Integer.toString(rs.getInt(6));
                String cantParti = Integer.toString(rs.getInt(7));
                String estado = rs.getString(8);
                String id_user = Integer.toString(rs.getInt(9));
                p[0] = id_t;
                p[1] = nombre;
                p[2] = descripcion;
                p[3] = fecha_ini;
                p[4] = precio;
                p[5] = tiempoEstimado;
                p[6] = cantParti;
                p[7] = estado; 
                p[8] = id_user;
            }
            conexion.close();
            return p;
        } catch (SQLException e) {
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean cambiarEstadoProyecto(int id_proyecto, String estado) throws SQLException{
        String consulta = "UPDATE Proyecto SET estado = ? WHERE id_proyecto = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, estado);
            sentencia.setInt(2, id_proyecto);
            sentencia.executeUpdate();
            conexion.close();
            return true;
        }catch(SQLException e){
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
     
    public boolean cambiarEstadoTarea(int id_tarea, String estado) throws SQLException{
        String consulta = "UPDATE Tarea SET estado = ? WHERE id_tarea = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, estado);
            sentencia.setInt(2, id_tarea);
            sentencia.executeUpdate();
            conexion.close();
            return true;
        }catch(SQLException e){
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    
    //Pues de aqui en adelante no se ha probado nada
    
    public boolean agregarMensaje(String nicknameOtro, int id_usuario, String contenido) throws SQLException{
        String consulta = "INSERT INTO Mensaje "
                        + "SELECT null, ?, ? , u.id_usuario "
                        + "FROM usuario u "
                        + "WHERE u.nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(2, id_usuario);
            sentencia.setString(1, contenido);
            sentencia.setString(3, nicknameOtro);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerMensajesEnviados(int id_usuario) throws SQLException{
        String consulta = "SELECT m.id_mensaje, m.contenido, u.nickname "
                        + "FROM mensaje m, usuario u "
                        + "WHERE m.id_receptor = u.id_usuario "
                        + "AND m.id_emisor = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2) + ";";
                temp += rs.getString(3);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerMensajesRecibidos(int id_usuario) throws SQLException{
        String consulta = "SELECT m.id_mensaje, m.contenido, u.nickname "
                        + "FROM mensaje m, usuario u "
                        + "WHERE m.id_emisor = u.id_usuario "
                        + "AND m.id_receptor = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2) + ";";
                temp += rs.getString(3);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarConocimientoUsuario(String conocimiento, int id_usuario) throws SQLException{
        String consulta = "INSERT INTO ConocimientosPorUsuario "
                        + "SELECT null, 0, ?, c.id_conocimiento "
                        + "FROM Conocimiento c "
                        + "WHERE c.nombre = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            sentencia.setString(2, conocimiento);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarKarmaExtra(int id_usuarioDador, int id_usuarioRecibidor, String conocimiento, int karmaExtra) throws SQLException{
        String consulta = "INSERT INTO KarmaExtra "
                        + "SELECT null, ?, cpu.id_conPorU, ? "
                        + "FROM conocimientosporusuario cpu, conocimiento c "
                        + "WHERE cpu.id_conocimiento = c.id_conocimiento "
                        + "AND cpu.id_usuario = ?"
                        + "AND c.nombre = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, karmaExtra);
            sentencia.setInt(2, id_usuarioDador);
            sentencia.setInt(3, id_usuarioRecibidor);
            sentencia.setString(4, conocimiento);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    
    public boolean agregarContacto(String nickContacto, int id_usuario) throws SQLException{
        String consulta = "INSERT INTO ContactosPorUsuario "
                        + "SELECT null, ?, u.id_usuario "
                        + "FROM usuario u "
                        + "WHERE u.nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            sentencia.setString(2, nickContacto);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean eliminarContacto(String nickContacto, int id_usuario) throws SQLException{
        String consulta = "SELECT id_usuario FROM usuario WHERE nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nickContacto);
            ResultSet rs = sentencia.executeQuery();
            int id_contacto = 0;
            if(rs.next()){
                id_contacto = rs.getInt(1);
            }
            conexion.close();
            consulta = "DELETE FROM contactosPorUsuario "
                     + "WHERE id_usuario = ? "
                     + "AND id_contacto = ?";
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            sentencia.setInt(2, id_contacto);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerContactos(int id_usuario) throws SQLException{
        String consulta = "SELECT cpu.id_cpu, u.nombre "
                        + "FROM contactosporusuario cpu, usuario u "
                        + "WHERE u.id_usuario = cpu.id_contacto "
                        + "AND cpu.id_usuario = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public int crearAsociacion(String nombre) throws SQLException{
        String consulta = "INSERT INTO Asociacion "
                        + "VALUES( null, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nombre);
            sentencia.executeUpdate();
            conexion.close();
            
            //Obtener la ultima asociacion creada
            consulta = "SELECT Max(id_asociacion) FROM Asociacion";
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return 0;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public int crearAsociacion(int id_usuario, String nombre) throws SQLException{
        String consulta = "INSERT INTO Asociacion "
                        + "VALUES( ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            sentencia.setString(2, nombre);
            sentencia.executeUpdate();
            conexion.close();
            
            //Obtener la ultima asociacion creada
            consulta = "SELECT Max(id_asociacion) FROM Asociacion";
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return 0;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    
    public String[] obtenerAsociaciones() throws SQLException{
        String consulta = "SELECT * FROM  Asociacion";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public String[] obtenerAsociacionesPorUsuario(int id_usuario, String tipo) throws SQLException{
        String consulta = "SELECT a.id_asociacion, a.nombre, upa.tipo "
                        + "FROM asociacion a, usuariosporasociacion upa "
                        + "WHERE upa.id_usuario = ? "
                        + "AND  upa.id_asociacion = a.id_asociacion "
                        + "AND upa.tipo = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            sentencia.setString(2, tipo);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2) + ";";
                temp += rs.getString(3);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public String[] obtenerUsuariosPorAsociacion(int id_asociacion) throws SQLException{
        String consulta = "SELECT u.id_usuario, u.nombre, upa.tipo "
                        + "FROM usuariosporasociacion upa, usuario u "
                        + "WHERE upa.id_usuario = u.id_usuario "
                        + "AND upa.id_asociacion = ?" ;
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_asociacion);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2) + ";";
                temp += rs.getString(3);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }  
    
    public boolean agregarUsuariosPorAsociacion(int id_usuario, int id_asociacion, String membresia) throws SQLException{
        String consulta = "INSERT INTO usuariosPorAsociacion "
                        + "VALUES(null,?,?,?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, membresia);
            sentencia.setInt(2, id_usuario);
            sentencia.setInt(3, id_asociacion);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarUsuariosPorAsociacion(String nickname, int id_asociacion, String membresia) throws SQLException{
        String consulta = "INSERT INTO usuariosPorAsociacion "
                        + "SELECT null, ?, u.id_usuario, ? FROM usuario u "
                        + "WHERE u.nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, membresia);
            sentencia.setString(3, nickname);
            sentencia.setInt(2, id_asociacion);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean eliminarUsuariosPorAsociacion(String nickname, int id_asociacion) throws SQLException{
        String consulta = "SELECT id_usuario FROM usuario WHERE nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nickname);
            ResultSet rs = sentencia.executeQuery();
            int id_usuario = 0;
            if(rs.next()){
                id_usuario = rs.getInt(1);
            }
            conexion.close();
            consulta = "DELETE FROM usuariosporasociacion WHERE id_usuario = ? AND id_asociacion = ?";
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            sentencia.setInt(2, id_asociacion);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean cambiarTipoUsuariosPorAsociacion(String nickname, int id_asociacion, String membresia) throws SQLException{
        String consulta = "SELECT id_usuario FROM usuario WHERE nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, nickname);
            ResultSet rs = sentencia.executeQuery();
            int id_usuario = 0;
            if(rs.next()){
                id_usuario = rs.getInt(1);
            }
            conexion.close();
            consulta = "UPDATE usuariosporasociacion "
                    + "SET tipo = ? "
                    + "WHERE id_usuario = ? AND id_asociacion = ?";
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(2, id_usuario);
            sentencia.setString(1, membresia);
            sentencia.setInt(3, id_asociacion);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarPublicacion(int id_usuario, String contenido) throws SQLException{
        String consulta = "INSERT INTO Publicacion "
                        + "VALUES (null, ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(2, id_usuario);
            sentencia.setString(1, contenido);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarPublicacion(int id_publicacion, String nickname, String contenido) throws SQLException{
        String consulta = "INSERT INTO Publicacion "
                        + "SELECT ?, ?, u.id_usuario "
                        + "FROM usuario u "
                        + "WHERE u.nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_publicacion);
            sentencia.setString(2, contenido);
            sentencia.setString(3, nickname);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarComentario(int id_usuario, int id_publicacion, String contenido) throws SQLException{
        String consulta = "INSERT INTO Comentario "
                        + "VALUES (null, ? , ? , ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(3, id_publicacion);
            sentencia.setInt(2, id_usuario);
            sentencia.setString(1, contenido);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarComentario(String nickname, int id_publicacion, int id_respuesta, String contenido) throws SQLException{
        String consulta = "INSERT INTO Comentario "
                        + "SELECT ?, ?, u.id_usuario, ? "
                        + "FROM usuario u "
                        + "WHERE u.nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_respuesta);
            sentencia.setString(2, contenido);
            sentencia.setInt(3, id_publicacion);
            sentencia.setString(4, nickname);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarComentario(String nickname, int id_publicacion, String contenido) throws SQLException{
        String consulta = "INSERT INTO Comentario "
                        + "SELECT null, ?, u.id_usuario, ? "
                        + "FROM usuario u "
                        + "WHERE u.nickname = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, contenido);
            sentencia.setInt(2, id_publicacion);
            sentencia.setString(3, nickname);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerComentarios(int id_publicacion) throws SQLException{
        String consulta = "SELECT c.id_comentario, c.contenido, u.nombre "
                        + "FROM comentario c, usuario u "
                        + "WHERE c.id_publicacion = ? "
                        + "AND u.id_usuario = c.id_usuario "
                        + "ORDER BY c.id_comentario DESC";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_publicacion);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2) + ";";
                temp += rs.getString(3);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public String[] obtenerPublicacionesContactos(int id_usuario) throws SQLException{
        String consulta = "SELECT p.id_publicacion, p.contenido, u.nombre "
                        + "FROM publicacion p, usuario u, contactosporusuario cpu "
                        + "WHERE cpu.id_usuario = ? "
                        + "AND cpu.id_contacto = p.id_publicacion "
                        + "AND cpu.id_contacto = u.id_usuario "
                        + "ORDER BY p.id_publicacion DESC "
                        + "LIMIT 10";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2) + ";";
                temp += rs.getString(3);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public String[] obtenerPublicacionesIndividuales(int id_usuario) throws SQLException{
        String consulta = "SELECT p.id_publicacion, p.contenido, u.nombre "
                        + "FROM publicacion p, usuario u "
                        + "WHERE u.id_usuario = ? "
                        + "AND u.id_usuario = p.id_usuario "
                        + "ORDER BY p.id_publicacion DESC "
                        + "LIMIT 10";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = "";
                temp += rs.getInt(1) + ";";
                temp += rs.getString(2) + ";";
                temp += rs.getString(3);
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public boolean agregarMiembrosPorProyecto(int id_usuario, int id_proyecto) throws SQLException{
        String consulta = "INSERT INTO MiembrosPorProyecto "
                        + "VALUES(null,?, ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, "Inscrito");
            sentencia.setInt(2, id_proyecto);
            sentencia.setInt(3, id_usuario);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerMiembrosParticipantesPorProyecto(int id_proyecto) throws SQLException{
        String consulta = "SELECT u.nickname "
                        + "FROM miembrosporproyecto mpp, usuario u "
                        + "WHERE u.id_usuario = mpp.id_usuario "
                        + "AND mpp.id_proyecto =  ? "
                        + "AND  mpp.membresia = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_proyecto);
            sentencia.setString(2, "Participante");
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = rs.getInt(1) + "";
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public String[] obtenerMiembrosInscritosPorProyecto(int id_proyecto) throws SQLException{
        String consulta = "SELECT u.nickname "
                        + "FROM miembrosporproyecto mpp, usuario u "
                        + "WHERE u.id_usuario = mpp.id_usuario "
                        + "AND mpp.id_proyecto =  ? "
                        + "AND  mpp.membresia = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_proyecto);
            sentencia.setString(2, "Inscrito");
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = rs.getInt(1) + "";
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public boolean elegirParticipanteProyecto(int id_usuario, int id_proyecto) throws SQLException{
        String consulta = "UPDATE miembrosporproyecto "
                + "SET membresia = ? "
                + "WHERE id_usuario = ? AND id_proyecto = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, "Participante");
            sentencia.setInt(2, id_usuario);
            sentencia.setInt(3, id_proyecto);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarMiembrosPorTarea(int id_usuario, int id_tarea) throws SQLException{
        String consulta = "INSERT INTO MiembrosPorTarea "
                        + "VALUES(null,?, ?, ?)";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, "Inscrito");
            sentencia.setInt(2, id_tarea);
            sentencia.setInt(3, id_usuario);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public boolean agregarMiembrosPorTarea(String nickname, int id_tarea) throws SQLException{
        String consulta = "INSERT INTO MiembrosPorTarea "
                        + "SELECT null, ?, ?, id_usuario "
                + "FROM  usuario WHERE nickname = ? ";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, "Participante");
            sentencia.setInt(2, id_tarea);
            sentencia.setString(3, nickname);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerMiembrosParticipantesPorTarea(int id_tarea) throws SQLException{
        String consulta = "SELECT u.id_usuario, u.nickname "
                        + "FROM miembrosportarea mpt, usuario u "
                        + "WHERE u.id_usuario = mpt.id_usuario "
                        + "AND mpt.id_tarea =  ? "
                        + "AND mpt.membresia = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_tarea);
            sentencia.setString(2, "Participante");
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = rs.getInt(1) + "";
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public String[] obtenerMiembrosInscritosPorTarea(int id_tarea) throws SQLException{
        String consulta = "SELECT u.id_usuario, u.nickname "
                        + "FROM miembrosportarea mpt, usuario u "
                        + "WHERE u.id_usuario = mpt.id_usuario "
                        + "AND mpt.id_tarea =  ? "
                        + "AND mpt.membresia = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_tarea);
            sentencia.setString(2, "Inscrito");
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> array = new ArrayList();
            while(rs.next()){
                String temp = rs.getInt(1) + "";
                array.add(temp);
            }
            return array.toArray(new String[array.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }   
    
    public boolean elegirParticipanteTarea(int id_usuario, int id_tarea) throws SQLException{
        String consulta = "UPDATE miembrosportarea "
                + "SET membresia = ? "
                + "WHERE id_usuario = ? AND id_tarea = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, "Participante");
            sentencia.setInt(2, id_usuario);
            sentencia.setInt(3, id_tarea);
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return false;
        } finally {
            if(conexion!= null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerConocimientosUsuario(int id_usuario) throws SQLException{
        String consulta = "SELECT h.nombre, c.nombre, cpp.karma, ke.karma " +
                          "FROM conocimiento c, habilidad h, conocimientosporusuario cpp, karmaextra ke " +
                          "WHERE c.id_conocimiento = cpp.id_conocimiento " +
                          "AND h.id_habilidad = c.id_habilidad "
                        + "AND ke.id_conPorU = cpp.id_conPorU "+
                          "AND cpp.id_usuario = ? "+
                          "ORDER BY h.nombre";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_usuario);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> hab_con= new ArrayList();
            if(rs.next()){
                String habilidad = rs.getString(1);
                String temp = habilidad;
                do {                    
                    if(habilidad.equals(rs.getString(1))){
                        //La misma habilidad que la iteracion anterior
                        temp += ";"+rs.getString(2);
                        temp += ";"+rs.getInt(3);
                        temp += ";"+rs.getInt(4);
                    } else {
                        //Cambio la habilidad
                        hab_con.add(temp);
                        temp = rs.getString(1);
                    }
                    habilidad = rs.getString(1);
                } while (rs.next());
                hab_con.add(temp);
            }
            conexion.close();
            return hab_con.toArray(new String[hab_con.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerPublicacionesAsociacion(int id_asociacion) throws SQLException{
        String consulta = "SELECT u.id_usuario, u.nickname, p.id_publicacion, p.contenido "
                + "FROM publicacion p, usuariosporasociacion upa, usuario u "
                + "WHERE p.id_usuario = upa.id_usuario "
                + "AND p.id_usuario = u.id_usuario "
                + "AND upa.id_asociacion = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, id_asociacion);
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> publicacion = new ArrayList();
            while(rs.next()){
                int id_usuario = rs.getInt(1);
                String nickname = rs.getString(2);
                int id_pub = rs.getInt(3);
                String contenido = rs.getString(4);
                publicacion.add(id_usuario+";"+nickname+";"+id_pub+";"+contenido);
            }
            conexion.close();
            return publicacion.toArray(new String[publicacion.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerKarmaInscritosProyecto(int id_proyecto, String conocimiento) throws SQLException{
        String consulta = "SELECT u.nickname, cpu.karma, ke.karma "
                + "FROM conocimientosporusuario cpu, usuario u, conocimiento c, "
                + "miembrosporproyecto mpp, karmaextra ke "
                + "WHERE u.id_usuario = mpp.id_usuario "
                + "AND u.id_usuario = cpu.id_usuario "
                + "AND cpu.id_conocimiento = c.id_conocimiento "
                + "AND ke.id_usuario = u.id_usuario "
                + "AND ke.id_conPorU = cpu.id_conPorU "
                + "AND c.nombre = ? "
                + "AND mpp.id_proyecto = ? "
                + "AND mpp.membresia = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, conocimiento);
            sentencia.setInt(2, id_proyecto);
            sentencia.setString(3, "Inscrito");
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> publicacion = new ArrayList();
            while(rs.next()){
                int id_usuario = rs.getInt(1);
                String nickname = rs.getString(2);
                int id_pub = rs.getInt(3);
                String contenido = rs.getString(4);
                publicacion.add(id_usuario+";"+nickname+";"+id_pub+";"+contenido);
            }
            conexion.close();
            return publicacion.toArray(new String[publicacion.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    public String[] obtenerKarmaInscritosTarea(int id_tarea, String conocimiento) throws SQLException{
        String consulta = "SELECT u.nickname, cpu.karma, ke.karma "
                + "FROM conocimientosporusuario cpu, usuario u, conocimiento c, "
                + "miembrosportarea mpt, karmaextra ke "
                + "WHERE u.id_usuario = mpt.id_usuario "
                + "AND u.id_usuario = cpu.id_usuario "
                + "AND cpu.id_conocimiento = c.id_conocimiento "
                + "AND ke.id_usuario = u.id_usuario "
                + "AND ke.id_conPorU = cpu.id_conPorU "
                + "AND c.nombre = ? "
                + "AND mpt.id_proyecto = ? "
                + "AND mpt.membresia = ?";
        Connection conexion = null;
        PreparedStatement sentencia = null;
        try {
            conexion = conectar();
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, conocimiento);
            sentencia.setInt(2, id_tarea);
            sentencia.setString(3, "Inscrito");
            ResultSet rs = sentencia.executeQuery();
            ArrayList<String> publicacion = new ArrayList();
            while(rs.next()){
                int id_usuario = rs.getInt(1);
                String nickname = rs.getString(2);
                int id_pub = rs.getInt(3);
                String contenido = rs.getString(4);
                publicacion.add(id_usuario+";"+nickname+";"+id_pub+";"+contenido);
            }
            conexion.close();
            return publicacion.toArray(new String[publicacion.size()]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            return null;
        } finally{
            if(conexion != null){
                conexion.close();
            }
            if(sentencia != null){
                sentencia.close();
            }
        }
    }
    
    /*
    public static void main(String args[]){
        Conexion c = new Conexion();
        try {
            String[] v = c.obtenerMisTareasIndividuales(1);
            for (int i = 0; i < v.length; i++) {
                System.out.println(v[i]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    */
}
