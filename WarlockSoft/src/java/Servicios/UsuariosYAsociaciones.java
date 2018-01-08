
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
@WebService(serviceName = "UsuariosYAsociaciones")
public class UsuariosYAsociaciones {

    String texto;
    String temp;
    int contador;

    String id_usuario = "";
    String nombre = "";
    String apellido = "";
    String nickname = "";
    String password = "";

    String nombreAsociacion = "";
    ArrayList<String> miembros;
    int id_asociacion = 0;

    public UsuariosYAsociaciones() {
        this.texto = "";
        this.contador = 0;
        this.temp = "";
    }

    @WebMethod(operationName = "leerUsuariosYAsociaciones")
    public void leer(@WebParam(name="ruta") String ruta) {
        try {
            Archivo a = new Archivo();
            this.texto = a.leer(ruta);
            S1();
        } catch (NullPointerException e) {
            System.out.println("Que magnifico error xD");
        }
        
    }

    public void asignarValorUsuario(String identificador, String dato) {
        switch (identificador) {
            case "idusuario":
                this.id_usuario = dato;
                break;
            case "nombre":
                this.nombre = dato;
                break;
            case "apellido":
                this.apellido = dato;
                break;
            case "nickname":
                this.nickname = dato;
                break;
            case "password":
                this.password = dato;
                break;
        }
    }

    public void asignarValorAsociacion(String identificador, String dato) {
        switch (identificador) {
            case "nombre":
                this.nombreAsociacion = dato;
                break;
            case "idasociacion":
                this.id_asociacion = Integer.parseInt(dato);
        }
    }

    public void S1() {
        System.out.println("S1");
        String letra = texto.charAt(contador) + "";
        if (letra.equals(" ") || letra.equals("\n")) {
            contador++;
        } else {
            temp += letra;
            contador++;
            if (temp.equals("{")) {
                temp = "";
                S2();
            }
        }
    }

    public void S2() {
        System.out.println("S2");
        boolean esIdentificador = false;
        while (!esIdentificador) {
            String letra = texto.charAt(contador) + "";
            if (" ".equals(letra) || "\n".equals(letra) || ",".equals(letra)) {
                //Pasar al siguiente
                contador++;
            } else {
                temp += texto.charAt(contador);
                contador++;
                switch (temp) {
                    case "\"Usuarios\":[":
                        esIdentificador = true;
                        temp = "";
                        S3();
                        break;
                    case "\"Asociaciones\":[":
                        esIdentificador = true;
                        temp = "";
                        S6();
                        break;
                    case "}":
                        //Estado de aceptacion
                        esIdentificador = true;
                        break;
                }
            }
        }
    }

    public void S3() {
        System.out.println("S3");
        boolean letraHallada = false;

        while (!letraHallada) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                contador++;
            } else {
                temp += letra;
                contador++;
                letraHallada = true;
                switch (temp) {
                    case "{":
                        temp = "";
                        S4();
                        break;
                    case "]":
                        temp = "";
                        S2();
                        break;
                }
            }
        }
    }

    public void S4() {
        System.out.println("S4");
        boolean esIdentificador = false;

        while (!esIdentificador) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Pasar al siguiente
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    esIdentificador = true;
                    String aux = temp;
                    temp = "";
                    S5(aux);
                } else if (temp.equals("}")) {
                    esIdentificador = true;
                    temp = "";
                    S3();
                }
            }
        }
    }

    public void S5(String identificador) {

        boolean salir = false;
        System.out.println("S5");
        while (!salir) {

            String letra = texto.charAt(contador) + "";
            if (letra.equals(":") || letra.equals(" ") || letra.equals("\n")) {
                //Avanzar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {

                    identificador = identificador.substring(1, identificador.length() - 1);
                    temp = temp.substring(1, temp.length() - 1);
                    asignarValorUsuario(identificador, temp);
                    temp = "";
                } else if (temp.equals(",")) {

                    temp = "";
                    S4();
                    salir = true;
                } else if (temp.equals("}")) {
                    salir = true;
                    temp = "";
                    Conexion con = new Conexion();
                    try {
                        con.registrarUsuario(Integer.parseInt(id_usuario), nombre + " " + apellido, "sin correo", 12, 12, 12, password, nickname);
                    } catch (SQLException ex) {
                        Logger.getLogger(UsuariosYAsociaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    S3();
                }
            }
        }
    }

    public void S6() {
        System.out.println("S6");
        boolean esUnaLetra = false;
        while (!esUnaLetra) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Seguir adelante
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case "]":
                        esUnaLetra = true;
                        temp = "";
                        S2();
                        break;
                    case "{":
                        esUnaLetra = true;
                        temp = "";
                        S7();
                        break;
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
                //Continuar;
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.equals("}")) {
                    temp = "";
                    S6();
                    salir = true;
                } else if (temp.equals("\"Miembros\"")) {
                    Conexion con = new Conexion();
                    try {
                        con.crearAsociacion(id_asociacion, nombreAsociacion);
                    } catch (SQLException ex) {
                        Logger.getLogger(UsuariosYAsociaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    temp = "";
                    miembros = new ArrayList();
                    S9();
                    salir = true;
                } else if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    String aux = temp;
                    temp = "";
                    S8(aux);
                    salir = true;
                }
            }
        }
    }

    public void S8(String identificador) {
        boolean salir = false;
        System.out.println("S8");
        while (!salir) {

            String letra = texto.charAt(contador) + "";
            if (letra.equals(":") || letra.equals(" ") || letra.equals("\n")) {
                //Avanzar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {

                    identificador = identificador.substring(1, identificador.length() - 1);
                    temp = temp.substring(1, temp.length() - 1);
                    asignarValorAsociacion(identificador, temp);
                    temp = "";
                } else if (temp.equals(",")) {
                    temp = "";
                    S7();
                    salir = true;
                } else if (temp.equals("}")) {
                    salir = true;
                    temp = "";
                    S7();
                }
            }
        }
    }
    
    public void S9(){
        System.out.println("S9");
        boolean salir = false;
        while(!salir){
            String letra = texto.charAt(contador) + "";
            if(letra.equals(" ") || letra.equals("\n") || letra.equals(":")){
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if(temp.equals("[")){
                    temp = "";
                    salir = true;
                    S10();
                }
            }
        }
    }

    public void S10() {
        System.out.println("S10");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    temp = temp.substring(1, temp.length() - 1);
                    miembros.add(temp);
                    temp = "";
                    S11();
                    salir = true;
                }
            }
        }
    }

    public void S11() {
        System.out.println("S11");
        boolean letraEncontrada = false;
        while (!letraEncontrada) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case ",":
                        letraEncontrada = true;
                        temp = "";
                        S10();
                        break;
                    case "]":
                        letraEncontrada = true;
                        temp = "";
                        Conexion con = new Conexion();
                        String[] nicknames = miembros.toArray(new String[miembros.size()]);
                        for (String nick : nicknames) {
                            try {
                                con.agregarUsuariosPorAsociacion(nick, id_asociacion, "Miembro");
                            } catch (SQLException ex) {
                                Logger.getLogger(UsuariosYAsociaciones.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        S7();
                }
            }
        }
    }

    /*
    public static void main(String args[]) {
        String texto = ""
                + "{"
                + "\"Usuarios\": ["
                + "{"
                + "\"id usuario\": \"50\","
                + "\"nombre\": \"John\","
                + "\"apellido\": \"Lennon\","
                + "\"nickname\": \"imagen09\","
                + "\"password\": \"1234\""
                + "}"
                + "]"
                + ","
                + "\"Asociaciones\": ["
                + "{"
                + "\"id asociacion\": \"51\","
                + "\"nombre\": \"Wings\","
                + "\"Miembros\": ["
                + "\"Reyes06\","
                + "\"lv31tdle\""
                + "]"
                + "}"
                + "]"
                + "}";
        UsuariosYAsociaciones a = new UsuariosYAsociaciones();
        a.texto = texto;
        a.S1();
    }
    */
}
