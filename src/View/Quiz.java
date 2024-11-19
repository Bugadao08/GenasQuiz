/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Model.ProvedorConexao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.beans.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;

/**
 *
 * @author genil
 */
public class Quiz extends javax.swing.JFrame {
    private ArrayList<String[]> perguntas;
    public String questaoId = "1";
    public String correcta;
    public int min = 0;
    public int sec = 0;
    public int marcada = 0;
    
    public void correctaCheck(){
        String estudanteCorrecta="";
        if(jRadioButton1.isSelected()){
            estudanteCorrecta=jRadioButton1.getText();
        }
        else if(jRadioButton2.isSelected()){
            estudanteCorrecta=jRadioButton2.getText();
        }
        else if(jRadioButton3.isSelected()){
            estudanteCorrecta=jRadioButton3.getText();
        }
        else{
            estudanteCorrecta=jRadioButton4.getText();
        }
            
        if(estudanteCorrecta.equals(correcta)){
            marcada=marcada+1;
            String marcada1=String.valueOf(marcada);
            jLabel14.setText(marcada1);
        }
        
        //proxima questao
        int questaoId1=Integer.parseInt(questaoId);
        questaoId1=questaoId1+1;
        questaoId=String.valueOf(questaoId1);
        
        //limpar JRadioButton
        
        jRadioButton1.setSelected(false);
        jRadioButton2.setSelected(false);
        jRadioButton3.setSelected(false);
        jRadioButton4.setSelected(false);
        
        //ultima questao
        
        if(questaoId.equals("10")){
            jButton2.setVisible(false);
        }
        
    }
     public void questao(){
    try {
        Connection con = ProvedorConexao.getCon();
        Statement st = con.createStatement();
        ResultSet rsl = st.executeQuery("select *from questao where id='" + questaoId + "'");
        if (rsl.next()) {
            jLabel12.setText(rsl.getString(1));  // Número da questão
            jLabel20.setText(rsl.getString(2));  // Pergunta

            // Recupera as alternativas da consulta SQL
            String alternativa1 = rsl.getString(3);
            String alternativa2 = rsl.getString(4);
            String alternativa3 = rsl.getString(5);
            String alternativa4 = rsl.getString(6);
            correcta = rsl.getString(7); // Resposta correta

            // Cria uma lista com as alternativas e embaralha
            List<String> alternativas = Arrays.asList(alternativa1, alternativa2, alternativa3, alternativa4);
            Collections.shuffle(alternativas);

            // Define as alternativas embaralhadas nos botões
            jRadioButton1.setText(alternativas.get(0));
            jRadioButton2.setText(alternativas.get(1));
            jRadioButton3.setText(alternativas.get(2));
            jRadioButton4.setText(alternativas.get(3));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}


    // Método para carregar e embaralhar as perguntas do banco de dados
    public void carregarQuestoes() {
        perguntas = new ArrayList<>();
        try {
            Connection con = ProvedorConexao.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from questao");

            while (rs.next()) {
                String[] questao = new String[5];
                questao[0] = rs.getString(2); // Pergunta
                questao[1] = rs.getString(3); // Resposta 1
                questao[2] = rs.getString(4); // Resposta 2
                questao[3] = rs.getString(5); // Resposta 3
                questao[4] = rs.getString(6); // Resposta 4
                perguntas.add(questao);
            }
            Collections.shuffle(perguntas); // Embaralha as perguntas

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public void submit(){
        String id=jLabel4.getText();
        correctaCheck();
        try{
            Connection con=ProvedorConexao.getCon();
            Statement st=con.createStatement();
            st.executeUpdate("update estudante set marcada='"+marcada+"' where id='"+id+"' ");
            String marcada1=String.valueOf(marcada);
            setVisible(false);
            new CertoQuiz(marcada1).setVisible(true);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
        
    }

    /**
     * Creates new form Quiz
     */
    public Quiz() {
        initComponents();
        setSize(1050, 530);
        setLocationRelativeTo(null); // Centraliza a janela na tela

    }
    Timer time;

    public Quiz(String id) {
        initComponents();
        jLabel4.setText(id);

        //data
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date date = new Date();
        jLabel2.setText(dFormat.format(date));

        //primeira questao
        try {
            Connection con = ProvedorConexao.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select *from estudante where id='" + id + "'");

            while (rs.next()) {
                jLabel6.setText(rs.getString(2));
            }
            ResultSet rsl=st.executeQuery("select *from questao where id='"+questaoId+"'");
            while(rsl.next()){
                jLabel12.setText(rsl.getString(1));
                jLabel20.setText(rsl.getString(2));
                jRadioButton1.setText(rsl.getString(3));
                jRadioButton2.setText(rsl.getString(4));
                jRadioButton3.setText(rsl.getString(5));
                jRadioButton4.setText(rsl.getString(6));
                correcta=rsl.getString(7);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }
        //time
        setLocationRelativeTo(this);
        time=new Timer(1000,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                jLabel19.setText(String.valueOf(sec));
                jLabel18.setText(String.valueOf(min));
                if(sec==60){
                    sec=0;
                    min++;
                    if(min==1){
                        time.stop();
                        correctaCheck();
                        submit();
                    }
                }
                sec++;
            }
        });
        time.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Algerian", 1, 40)); // NOI18N
        jLabel9.setText("Bem-vindo");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 960, 20));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Imagem/add new question.png"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel1.setText("Data");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel3.setText("id");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setText("200000");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, -1, 20));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setText("Nome");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel6.setText("Daniel G");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel7.setText("Total de Questões");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel8.setText("10");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, 40, 20));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel11.setText("Nº Questão");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel12.setText("00");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel13.setText("Marcadas");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel14.setText("00");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, -1, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel15.setText("Tempo Total");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel16.setText("10min");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 20, -1, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel17.setText("Tempo");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 50, -1, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel18.setText("00");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 50, -1, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel19.setText("00");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 50, -1, -1));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel20.setText("Diz a pergunta?");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 130, -1, -1));

        jRadioButton1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jRadioButton1.setText("jRadioButton1");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, -1, -1));

        jRadioButton2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jRadioButton2.setText("jRadioButton2");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 210, -1, -1));

        jRadioButton3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jRadioButton3.setText("jRadioButton3");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 250, -1, -1));

        jRadioButton4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jRadioButton4.setText("jRadioButton4");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 290, -1, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jButton2.setText("Next");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 330, -1, -1));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jButton3.setText("Submeter");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 330, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       correctaCheck();
       questao();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int a=JOptionPane.showConfirmDialog(null, "Deseja Submeter?","Select",JOptionPane.YES_NO_OPTION);
        if(a==0){
            correctaCheck();
            submit();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        if(jRadioButton1.isSelected()){
            jRadioButton2.setSelected(false);
            jRadioButton3.setSelected(false);
            jRadioButton4.setSelected(false);
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        if(jRadioButton2.isSelected()){
            jRadioButton1.setSelected(false);
            jRadioButton3.setSelected(false);
            jRadioButton4.setSelected(false);
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        if(jRadioButton3.isSelected()){
            jRadioButton2.setSelected(false);
            jRadioButton1.setSelected(false);
            jRadioButton4.setSelected(false);
        }
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        if(jRadioButton4.isSelected()){
            jRadioButton2.setSelected(false);
            jRadioButton3.setSelected(false);
            jRadioButton1.setSelected(false);
        }
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Quiz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Quiz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Quiz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Quiz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Quiz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
