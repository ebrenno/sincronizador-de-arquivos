/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitario.gui;

import javax.swing.JLabel;

public class Mensagem {

    /**
     * exibe na interface gráfica da aplicação uma mensagem
     *
     * @param label a jlabel que irá mostrar a mensagem
     * @param mensagem a mensagem que será mostrada
     */
    public static void exibir(JLabel label, String mensagem) {
        label.setText(mensagem);
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
