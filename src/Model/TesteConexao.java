/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.sql.Connection;

/**
 *
 * @author genil
 */

    

public class TesteConexao {
    public static void main(String[] args) {
        Connection con = ProvedorConexao.getCon();

        if (con != null) {
            System.out.println("Conexão estabelecida com sucesso!");
        } else {
            System.out.println("Falha na conexão.");
        }
    }
}

    

