package Servicios;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "EstadosYComentarios")
public class EstadosYComentarios {

    String texto;
    String temp;
    int contador;

    String usuario_estado = "";
    int id_estado = 0;
    String texto_estado = "";

    int id_respuesta = 0;
    String usuario_respuesta = "";
    String texto_respuesta = "";

    public EstadosYComentarios() {
        this.texto = "";
        this.temp = "";
        this.contador = 0;
    }
    

    @WebMethod(operationName = "leerEstadosYComentarios")
    public void leer(@WebParam(name="ruta") String ruta) {
        Archivo a = new Archivo();
        this.texto = a.leer(ruta);
        S1();
    }

    public boolean esUnNumero(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void S1() {
        System.out.println("S1");
        boolean salir = false;
        if (contador>= texto.length()) {
            System.out.println("Mision cumplida campeon");
            salir = true;
        }
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //Continuar
                contador++;
            } else if (letra.equals("{")) {
                contador++;
                salir = true;
                S2();
            }
        }
    }

    public void S2() {
        System.out.println("S2");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n")) {
                //COntinuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case "}":
                        temp = "";
                        salir = true;
                        S1();
                        break;
                    case "\"Comentarios\":[":
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
                contador++;
                switch (letra) {
                    case "]":
                        salir = true;
                        temp = "";
                        S2();
                        break;
                    case "{":
                        salir = true;
                        temp = "";
                        S4();
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
                switch (temp) {
                    case "\"usuario\":":
                        temp = "";
                        salir = true;
                        S5();
                        break;
                    case "}":
                        temp = "";
                        salir = true;
                        S3();
                        break;
                    case "\"Estados\":[":
                        temp = "";
                        salir = true;
                        S6();
                        break;
                }
            }
        }
    }

    public void S5() {
        System.out.println("S5");
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
                    usuario_estado = temp;
                    temp = "";
                } else if (temp.equals(",")) {
                    temp = "";
                    salir = true;
                    S4();
                }
            }
        }
    }

    public void S6() {
        System.out.println("S6");
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
                        S4();
                        break;
                    case "{":
                        temp = "";
                        salir = true;
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
            if (letra.equals(" ") || letra.equals("\n") || letra.equals(":")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (temp.equals("\"Respuestas\"")) {
                    temp = "";
                    salir = true;
                    Conexion con = new Conexion();
                    try {
                        con.agregarPublicacion(id_estado, usuario_estado, texto_estado);
                        System.out.println("Estado agregado");
                    } catch (SQLException ex) {
                        Logger.getLogger(EstadosYComentarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    S9();
                } else if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    String aux = temp.substring(1, temp.length() - 1);
                    temp = "";
                    salir = true;
                    S8(aux);
                } else if (temp.equals(",")) {
                    temp = "";
                } else if (temp.equals("}")) {
                    temp = "";
                    salir = true;
                    S6();
                }
            }
        }
    }

    public void S8(String identificador) {
        System.out.println("S8");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n") || letra.equals(":")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (identificador.equals("id") && esUnNumero(letra)) {
                    //Es un id
                    String otraLetra = texto.charAt(contador) + "";
                    if (esUnNumero(otraLetra)) {
                        this.id_estado = Integer.parseInt(letra + otraLetra);
                        contador++;
                    } else {
                        this.id_estado = Integer.parseInt(letra);
                    }
                    temp = "";
                } else if (identificador.equals("texto") && temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    //Es un texto
                    temp = temp.substring(1, temp.length() - 1).toLowerCase();
                    this.texto_estado = temp;
                    temp = "";
                } else if (temp.equals(",")) {
                    //Sale del estado
                    temp = "";
                    salir = true;
                    S7();
                }
            }
        }
    }

    public void S9() {
        System.out.println("S9");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n") || letra.equals(":")) {
                //Continuar;
                contador++;
            } else {
                contador++;
                if (letra.equals("[")) {
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
            if (letra.equals(" ") || letra.equals("\n") || letra.equals(":")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                switch (temp) {
                    case "]":
                        salir = true;
                        temp = "";
                        S7();
                        break;
                    case ",":
                        salir = true;
                        temp = "";
                        S10();
                        break;
                    case "{":
                        salir = true;
                        temp = "";
                        S11();
                        break;
                }
            }
        }
    }

    public void S11() {
        System.out.println("S11");
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
                    String aux = temp.substring(1, temp.length() - 1);
                    temp = "";
                    salir = true;
                    S12(aux);
                }
            }
        }
    }

    public void S12(String identificador) {
        System.out.println("S12");
        boolean salir = false;
        while (!salir) {
            String letra = texto.charAt(contador) + "";
            if (letra.equals(" ") || letra.equals("\n") || letra.equals(":")) {
                //Continuar
                contador++;
            } else {
                temp += letra;
                contador++;
                if (identificador.equals("id") && esUnNumero(letra)) {
                    //Es el id de la respuesta
                    String otraLetra = texto.charAt(contador) + "";
                    if (esUnNumero(otraLetra)) {
                        id_respuesta = Integer.parseInt(letra + otraLetra);
                        contador++;
                    } else {
                        id_respuesta = Integer.parseInt(letra);
                    }
                    temp = "";
                } else if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() > 2) {
                    //Es un usuario_Respuesta o un texto_respuesta
                    switch (identificador) {
                        case "usuario":
                            usuario_respuesta = temp.substring(1, temp.length() - 1);
                            temp = "";
                            break;
                        case "texto":
                            texto_respuesta = temp.substring(1, temp.length() - 1);
                            temp = "";
                            break;
                    }
                } else if (temp.equals(",")) {
                    temp = "";
                    salir = true;
                    S11();
                } else if (temp.equals("}")) {
                    temp = "";
                    salir = true;
                    Conexion con = new Conexion();
                    try {
                        con.agregarComentario(usuario_respuesta, id_estado, id_respuesta, texto_respuesta);
                        System.out.println("Respuesta agregada");
                    } catch (SQLException ex) {
                        Logger.getLogger(EstadosYComentarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    S10();
                }
            }
        }
    }

    /*
    public static void main(String args[]) {
        String prueba = ""
                + "{"
                + "\"Comentarios\": ["
                + "{"
                + "\"usuario\": \"Reyes06\","
                + "\"Estados\": ["
                + "{"
                + "\"id\": 25,"
                + "\"texto\": \"Este es un texto\","
                + "\"Respuestas\": ["
                + "{"
                + "\"id\": 50,"
                + "\"usuario\": \"Maruco-chan\","
                + "\"texto\": \"first comment\""
                + "}"
                + "{"
                + "\"usuario\": \"Chanaxx\","
                + "\"id\": 51,"
                + "\"texto\": \"x2 lol\""
                + "}"
                + "]"
                + "}"
                + "]"
                + "}"
                + "]"
                + "}";
        EstadosYComentarios p = new EstadosYComentarios();
        p.texto = prueba;
        p.S1();
    }
    */
}
