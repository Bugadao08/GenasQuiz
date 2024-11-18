/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.*;

/**
 *
 * @author genil
 */
public class ProvedorConexao {

     public static Connection getCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/geni", "root", "Toretto40");
            return con;
        } catch (Exception e) {
            e.printStackTrace();  // Adicione esta linha para ver a mensagem de erro no console
            return null;
        }
    }

}
