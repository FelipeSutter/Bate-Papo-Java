/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

/**
 *
 * @author FACC
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private String username;
    private PrintWriter escritor;
    private Set<PrintWriter> listaDeEscritores;

    ClientHandler(Socket socket, Set<PrintWriter> escritores) {
        this.socket = socket;
        listaDeEscritores = escritores;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        try {
            BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            escritor = new PrintWriter(socket.getOutputStream(), true);

            synchronized (listaDeEscritores) {
                listaDeEscritores.add(escritor);
            }

            username = leitor.readLine();
            transmitir(username + " entrou no chat!");
            System.out.println(username + " conectou-se ao servidor.");

            String clientMessage;
            do {

                clientMessage = leitor.readLine();
                transmitir(username + ": " + clientMessage);

            } while ((clientMessage != null) || (!clientMessage.equalsIgnoreCase("/sair")));

            synchronized (listaDeEscritores) {
                listaDeEscritores.remove(escritor);
            }

            transmitir(username + " saiu do chat :( ");
            System.out.println(username + " desconectou-se.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transmitir(String mensagem) {
        synchronized (listaDeEscritores) {
            listaDeEscritores.forEach(escritor -> {
                escritor.println(mensagem);
            });
        }
    }

}
