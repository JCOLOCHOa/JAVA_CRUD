package java_crud;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class conexion {
    
    
    Connection conectar= null;
    
    String usuario="root";
    String contrasenia="JC0L0CH0_q";
    String bd="DJstore";
    String ip="127.0.0.1";
    String puerto="3306";
    
    
    String cadena="jdbc:mysql://"+ip+":"+puerto+"/"+bd;
    
    public Connection estableceConexion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar= DriverManager.getConnection(cadena,usuario,contrasenia);
            JOptionPane.showMessageDialog(null, "La conexion se reaalizo con exito");
            
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos"+e.toString());
        }
        return conectar;
    }
}
