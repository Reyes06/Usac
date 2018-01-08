package Servicios;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Eddy
 */
@WebService(serviceName = "Proyectos")
public class Proyectos {

    //Variables de lectura
    String texto;
    int contador;
    String temp;

    //Variables de la tarea
    int id = 0;
    String nombre = "";
    String descripcion = "";
    int diaIni = 0;
    int mesIni = 0;
    int anoIni = 0;
    int diaFin = 0;
    int mesFin = 0;
    int anoFin = 0;
    String estadoactual = "";

    ArrayList<String> nicknames;

    //Algunas Variables del proyecto
    int id_p = 0;
    double pago = 0.0;
    String creador = "";

    public Proyectos() {
        this.contador = 0;
        this.temp = "";
    }

    @WebMethod(operationName = "leerProyectos")
    public void leer(@WebParam(name="ruta") String ruta) {
        Archivo ar = new Archivo();
        this.texto = ar.leer(ruta);
        S01();
    }

    public boolean asignarValor(String identificador, String valor) {
        switch (identificador) {
            case "\"id\"":
                this.id = Integer.parseInt(valor);
                break;
            case "\"creador\"":
                valor = valor.substring(1, valor.length() - 1);
                this.creador = valor;
                break;
            case "\"nombre\"":
                valor = valor.substring(1, valor.length() - 1);
                this.nombre = valor;
                break;
            case "\"descripcion\"":
                valor = valor.substring(1, valor.length() - 1);
                this.descripcion = valor;
                break;
            case "\"fechainicio\"":
                valor = valor.substring(1, valor.length() - 1);
                String[] fechaLarga = valor.split(" ");
                String[] fecha = fechaLarga[0].split("/");
                this.diaIni = Integer.parseInt(fecha[0]);
                this.mesIni = Integer.parseInt(fecha[1]);
                this.anoIni = Integer.parseInt(fecha[2]);
                break;
            case "\"fechacierre\"":
                valor = valor.substring(1, valor.length() - 1);
                String[] fechaLarga2 = valor.split(" ");
                String[] fecha2 = fechaLarga2[0].split("/");
                this.diaFin = Integer.parseInt(fecha2[0]);
                this.mesFin = Integer.parseInt(fecha2[1]);
                this.anoFin = Integer.parseInt(fecha2[2]);
                break;
            case "\"estadoactual\"":
                valor = valor.substring(1, valor.length() - 1);
                this.estadoactual = valor;
                break;
            case "\"estado\"":
                valor = valor.substring(1, valor.length() - 1);
                this.estadoactual = valor;
                break;
            case "\"sueldo\"":
                valor = valor.substring(1, valor.length() - 1);
                this.pago = Double.valueOf(valor);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean esUnEntero(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void S01() {
        System.out.println("S01");
        if (contador >= texto.length()) {
            System.out.println("Felicidades campeon: Proyecto registrado");
        } else {
            String letra = texto.charAt(contador) + "";
            if (letra.equals("{")) {
                temp = "";
                contador++;
                S02();
            }
        }
    }

    public void S02() {
        System.out.println("S02");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case "}":
                        temp = "";
                        salir = true;
                        S01();
                        break;
                    case "\"Proyectos\":[":
                        temp = "";
                        salir = true;
                        S03();
                        break;
                }
            }
        }
    }

    public void S03() {
        System.out.println("S03");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case "]":
                        temp = "";
                        salir = true;
                        S02();
                        break;
                    case "{":
                        temp = "";
                        salir = true;
                        S04();
                        break;
                    case ",":
                        temp = "";
                        break;
                }
            }
        }
    }

    public void S04() {
        System.out.println("S04");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.equals("\"Tareas\"")) {
                    temp = "";
                    salir = true;
                    Conexion c = new Conexion();
                    try {
                        boolean r = c.agregarProyecto(id, nombre, diaIni, mesIni, anoIni, diaFin, mesFin, anoFin, estadoactual, pago, null, creador);
                        if(r){
                            System.out.println("Proyecto Creado y Registrado: "+nombre);
                        } else {
                            System.out.println("Proyecto no creado: "+nombre);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Proyectos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    S06();
                } else if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    String aux = temp;
                    temp = "";
                    salir = true;
                    S05(aux);
                } else if (temp.equals("}")){
                    temp = "";
                    salir = true;
                    S03();
                }
            }
        }
    }

    public void S05(String identificador) {
        System.out.println("S05");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals("\n") || letra.equals(":")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                String aux = temp.trim();
                if(aux.equals(",")){
                    temp = "";
                    salir = true;
                    S04();
                } else if (identificador.equals("\"id\"")) {
                    if (!esUnEntero(aux) && aux.length() >= 1) {
                        aux = aux.substring(0, aux.length() - 1);
                        if (asignarValor(identificador, aux)) {
                            contador--;
                            temp = "";
                        } else {
                            System.out.println("Sorry bro. Algo ha fallado con tu logica (Proyecto)");
                            System.out.println("Error en identificar un ENTERO");
                        }
                    }
                } else if (aux.startsWith("\"") && aux.endsWith("\"") && aux.length() > 2) {
                    if (asignarValor(identificador, aux)) {
                        temp = "";
                    } else {
                        System.out.println("Sorry bro. Algo ha fallado con tu logica (Proyecto)");
                        System.out.println("Error en identificar un VARCHAR");
                    }
                }
            }
        }
    }
    
    public void S06(){
        boolean salir = false;
        while(!salir){
            String letra = texto.charAt(contador) + "";
            if(letra.equals(" ") || letra.equals("\n") || letra.equals(":")){
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch(temp){
                    case "[":
                        temp = "";
                        salir = true;
                        S3();
                        break;
                }
            }
        }
    }

    public void S3() {
        System.out.println("S3");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case "{":
                        salir = true;
                        temp = "";
                        S4();
                        break;
                    case "]":
                        salir = true;
                        temp = "";
                        S04();
                        break;
                    case ",":
                        temp = "";
                        break;
                }
            }
        }
    }

    public void S4() {
        System.out.println("S4");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.equals("}")) {
                    salir = true;
                    temp = "";
                    S3();
                } else if (temp.equals("\"Asignados\"")) {
                    temp = "";
                    salir = true;
                    Conexion c = new Conexion();
                    try {
                        boolean r = c.agregarTarea(id, nombre, descripcion, diaIni, mesIni, anoIni, diaFin, mesFin, anoFin, pago, 0, estadoactual, creador);
                        if(r){
                            System.out.println("Tarea creada y guardada: "+nombre);
                        } else {
                            System.out.println("Tarea no creada: "+nombre);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    S6();
                } else if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    salir = true;
                    String aux = temp;
                    temp = "";
                    S5(aux);
                }
            }
        }
    }

    public void S5(String identificador) {
        System.out.println("S5");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals("\n") || letra.equals(":")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                String aux = temp.trim();
                if (aux.equals(",")) {
                    //Unica forma de salir del estado
                    salir = true;
                    temp = "";
                    S4();
                } else if (identificador.equals("\"pago\"")) {
                    if (!esUnEntero(aux) && aux.length() >= 1) {
                        aux += texto.charAt(contador);
                        contador++;
                        aux += texto.charAt(contador);
                        contador++;
                        if (asignarValor(identificador, temp)) {
                            temp = "";
                        } else {
                            System.out.println("Sorry bro. Algo ha fallado con tu logica (Tarea)");
                            System.out.println("Error en identificar un DECIMAL");
                        }
                    }
                } else if (identificador.equals("\"id\"")) {
                    if (!esUnEntero(aux) && aux.length() >= 1) {
                        aux = aux.substring(0, aux.length() - 1);
                        if (asignarValor(identificador, aux)) {
                            contador--;
                            temp = "";
                        } else {
                            System.out.println("Sorry bro. Algo ha fallado con tu logica (Tarea)");
                            System.out.println("Error en identificar un ENTERO");
                        }
                    }
                } else if (aux.startsWith("\"") && aux.endsWith("\"") && aux.length() > 2) {
                    if (asignarValor(identificador, aux)) {
                        temp = "";
                    } else {
                        System.out.println("Sorry bro. Algo ha fallado con tu logica (Tarea)");
                        System.out.println("Error en identificar un VARCHAR");
                    }
                }
            }
        }
    }

    public void S6() {
        System.out.println("S6");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n") || letra.equals(":")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.equals("[")) {
                    temp = "";
                    salir = true;
                    nicknames = new ArrayList();
                    S7();
                }
            }
        }
    }

    public void S7() {
        System.out.println("S7");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.equals("]")) {
                    temp = "";
                    salir = true;
                    S4();
                } else if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    String aux = temp;
                    temp = "";
                    salir = true;
                    S8(aux);
                }
            }
        }
    }

    public void S8(String valor) {
        System.out.println("S8");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals("\n") || letra.equals(" ")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case ",":
                        nicknames.add(valor);
                        temp = "";
                        salir = true;
                        S7();
                        break;
                    case "]":
                        nicknames.add(valor);
                        temp = "";
                        salir = true;
                        Conexion c = new Conexion();
                        String[] nicks = nicknames.toArray(new String[nicknames.size()]);
                        for (String nick : nicks) {
                            nick = nick.substring(1, nick.length() - 1);
                            try {
                                boolean r = c.agregarMiembrosPorTarea(nick, id);
                                if(r){
                                    System.out.println("Miembro agregado: " + nick);
                                } else {
                                    System.out.println("Miembro no agregado: "+nick);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Tareas.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        S4();
                        break;
                }
            }
        }
    }
    
    /*
    public static void main(String args[]) {
        String texto = ""
                + "{"
                + "\"Proyectos\": ["
                + "{"
                + "\"id\": 20,"
                + "\"creador\": \"Reyes06\","
                + "\"fechainicio\": \"1/12/2017 20:30\","
                + "\"fechacierre\": \"5/1/2018 00:30\","
                + "\"sueldo\": \"10000.00\","
                + "\"estado\": \"activo\","
                + "\"Tareas\": ["
                + "{"
                + "\"id\": 30,"
                + "\"creador\": \"Chanaxx\","
                + "\"nombre\": \"Tarea Individual 1\","
                + "\"descripcion\": \"Descripcion de la tarea\","
                + "\"fechainicio\": \"12/12/2017 20:30\","
                + "\"fechacierre\": \"15/12/2017 00:30\","
                + "\"estadoactual\": \"iniciado\","
                + "\"Asignados\": ["
                + "\"Reyes06\","
                + "\"Maruco-chan\""
                + "]"
                + "}"
                + "]"
                + "}"
                + "]"
                + "}";
        Proyectos t = new Proyectos();
        t.texto = texto;
        t.S01();
    }
    */
}
