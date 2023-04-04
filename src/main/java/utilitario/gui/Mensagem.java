/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitario.gui;

import javax.swing.JLabel;

public class Mensagem {
    
    private static JLabel statusUpload;
    private static JLabel statusDownload;
    
    public static void atribuir(JLabel upload, JLabel download){
        statusUpload = upload;
        statusDownload = download;
    }
    
    public static void updateUpload(String mensagem){
        statusUpload.setText(mensagem);
        Mensagem.esperar();
    }
    public static void updateDownload(String mensagem){
        statusDownload.setText(mensagem);
        Mensagem.esperar();
    }
    
    private static void esperar() {
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
