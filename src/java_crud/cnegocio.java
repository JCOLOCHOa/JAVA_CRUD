package java_crud;

import java.awt.TextField;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class cnegocio {
    int codigo;
    String nombre;
    int stock;
    float precio;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public static void InsertarDato(JTextField paramNombre, JTextField paramStock, JTextField paramPrecio) {
        cnegocio negocio = new cnegocio(); 
        negocio.setNombre(paramNombre.getText());
        negocio.setStock(Integer.parseInt(paramStock.getText())); 
        negocio.setPrecio(Float.parseFloat(paramPrecio.getText().trim())); 

        conexion objetoConexion = new conexion();
        
        String consulta = "INSERT INTO Productos(nombre, stock, precio_unitario) VALUES (?, ?, ?);";

        try {
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setString(1, negocio.getNombre());
            cs.setInt(2, negocio.getStock());
            cs.setFloat(3, negocio.getPrecio());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Se insertó correctamente el producto");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: No se insertó correctamente el producto. " + e.getMessage());
        }
    }

    public void MostrarDatos(JTable paramTablaProductos) {
        conexion objetoConexion = new conexion();
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> OrdenarTabla = new TableRowSorter<TableModel>(modelo);
        paramTablaProductos.setRowSorter(OrdenarTabla);

        String sql = "";
        
        modelo.addColumn("id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Stock");
        modelo.addColumn("Precio");
        
        paramTablaProductos.setModel(modelo);
        
        sql = "SELECT * FROM Productos;";
        
        String[] datos = new String[4];
        
        try (Statement st = objetoConexion.estableceConexion().createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                
                modelo.addRow(datos);
            }
            
            paramTablaProductos.setModel(modelo);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudieron mostrar los Productos. " + e.getMessage());
        }
    }

    public void SeleccionarDato(JTable paramTablaDatos, JTextField paramId, JTextField paramNombres, JTextField paramStock, JTextField paramPrecio) {
        
        try {
            int fila = paramTablaDatos.getSelectedRow();
            
            if (fila >= 0) {
                paramId.setText((paramTablaDatos.getValueAt(fila, 0).toString()));
                paramNombres.setText((paramTablaDatos.getValueAt(fila, 1).toString()));
                paramStock.setText((paramTablaDatos.getValueAt(fila, 2).toString()));
                paramPrecio.setText((paramTablaDatos.getValueAt(fila, 3).toString()));
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de selección: " + e.toString());
        }
    }

    public void ModificarDato(JTextField paramCodigo, JTextField paramNombres, JTextField paramStock, JTextField paramPrecio) {
        setCodigo(Integer.parseInt(paramCodigo.getText().trim()));
        
        setNombre(paramNombres.getText());
        setStock(Integer.parseInt(paramStock.getText().trim()));
        setPrecio(Float.parseFloat(paramPrecio.getText().trim())); 
        
        conexion objetoConexion = new conexion();
        
        String consulta = "UPDATE Productos SET nombre=?, stock=?, precio_unitario=? WHERE id=?;";
        
        try {
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            
            cs.setString(1, getNombre());
            cs.setInt(2, getStock());
            cs.setFloat(3, getPrecio());
            cs.setInt(4, getCodigo()); 
            
            cs.execute();
            JOptionPane.showMessageDialog(null, "MODIFICACIÓN EXITOSA");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: NO SE MODIFICÓ EL PRODUCTO. " + e.toString());
        }
    }

    public void EliminarDato(JTextField paramCodigo) {
        setCodigo(Integer.parseInt(paramCodigo.getText().trim()));
        
        conexion objetoConexion = new conexion();
        
        String consulta ="DELETE FROM Productos WHERE id=?;";
        
        try {
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            
            cs.setInt(1, getCodigo());
            cs.execute();
            
            JOptionPane.showMessageDialog(null,"Se eliminó correctamente el producto");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"ERROR: NO SE ELIMINÓ EL PRODUCTO. " + e.toString());
        }
    }
 public void ConsultarDato(JTable paramTablaProductos, String criterio) {
    DefaultTableModel modelo = (DefaultTableModel) paramTablaProductos.getModel();
    
    
    paramTablaProductos.clearSelection();

    boolean encontrado = false;

    for (int i = 0; i < modelo.getRowCount(); i++) {
        String idProducto = modelo.getValueAt(i, 0).toString();
        
        
        if (idProducto.equals(criterio)) {
            
            paramTablaProductos.addRowSelectionInterval(i, i);
            encontrado = true;
            break; 
        }
    }

    
    
    if (!encontrado) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombreProducto = modelo.getValueAt(i, 1).toString();
            
            
            if (nombreProducto.equalsIgnoreCase(criterio)) {
               
                paramTablaProductos.addRowSelectionInterval(i, i);
                encontrado = true;
                break; 
            }
        }
    }

    if (!encontrado) {
        JOptionPane.showMessageDialog(null, "No se encontró ningún producto con ese criterio.");
    }
}
}