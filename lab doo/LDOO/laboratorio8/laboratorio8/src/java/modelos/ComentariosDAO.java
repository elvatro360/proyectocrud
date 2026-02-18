/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Fernando Martinez Bautista 1702824
 */
public class ComentariosDAO {
    private Connection conexion;
    private void abrirConexion() throws SQLException{
          String dbURI = "jdbc:derby://localhost:1527/Comentarios";
         String user = "fcfm";
         String password = "lsti01";
         conexion = DriverManager.getConnection(dbURI, user, password);
     }
    private void cerrarConexion() throws SQLException{
        conexion.close();
    }
    public boolean insertar(ComentariosPOJO cm){
       try{
            abrirConexion();
            String insert = "insert into COMENTARIOS values ('" + cm.getNombre() + "', '" + cm.getComentario() + "')";
            Statement stmt = conexion.createStatement();
            int filas = stmt.executeUpdate(insert);
            cerrarConexion();
            return filas >0;
       } catch(Exception e){
           return false;
       }
    }
    public ArrayList<ComentariosPOJO> buscar(ComentariosPOJO compojo) {
    
         ResultSet result = null;
          ArrayList<ComentariosPOJO> beans = new ArrayList();
        try{ 
            abrirConexion();
                String insert =  "select * from COMENTARIOS where NOMBRE = '" + compojo.getNombre() + "' and COMENTARIO like '%" + compojo.getComentario()+ "%'";
                Statement stmt = conexion.createStatement();
                result = stmt.executeQuery(insert);
                while(result.next()){ 
                    ComentariosPOJO dto = new ComentariosPOJO();
                    dto.setNombre(result.getString("NOMBRE"));
                    dto.setComentario(result.getString("COMENTARIO"));
                    beans.add(dto);  
                }
                 return beans;
            } 
            catch(Exception e){
                return null;
            }
            finally {
                try {
                    cerrarConexion();
                }
                catch(Exception e) { 
                    return null;
                }
            }  
       
    }
}

