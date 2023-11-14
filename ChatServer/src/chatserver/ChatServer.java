/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author FACC
 */
public class ChatServer {

    private static final int PORTA = 1515;

    private static List<ClientHandler> escritores = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORTA)) {
            // cria o server na porta que eu disser
            System.out.println("Servidor rodando na porta: " + PORTA);

            while (true) {
                Socket socket = server.accept();
                ClientHandler clientHandler = new ClientHandler(socket, escritores);
                escritores.add(clientHandler);
                clientHandler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
