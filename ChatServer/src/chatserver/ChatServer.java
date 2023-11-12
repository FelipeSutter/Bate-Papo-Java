/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author FACC
 */
public class ChatServer {

    private static final int PORTA = 1515;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORTA);
            System.out.println("Servidor rodando na porta: " + PORTA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
