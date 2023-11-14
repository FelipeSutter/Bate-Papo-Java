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
import java.util.List;
import java.util.Set;

/**
 *
 * @author FACC
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private String username;
    private PrintWriter escritor;
    private List<ClientHandler> listaDeEscritores;

    ClientHandler(Socket socket, List<ClientHandler> escritores) {
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

            username = leitor.readLine();
            transmitir(username + " entrou no chat!");
            System.out.println(username + " conectou-se ao servidor.");

            String clientMessage;
            do {
                clientMessage = leitor.readLine();

                if (clientMessage.startsWith("/pv")) {
                    processarComandoPrivado(clientMessage);
                } else {
                    transmitir(username + ": " + clientMessage);
                }

            } while (!clientMessage.equalsIgnoreCase("/sair"));

            System.out.println(username + " desconectou.");
            transmitir(username + " saiu do chat :( ");

            listaDeEscritores.remove(this);

            leitor.close();
            escritor.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void enviarMensagem(String mensagem) {
        escritor.println(mensagem);
    }

    private void transmitir(String mensagem) {
        synchronized (listaDeEscritores) {
            listaDeEscritores.forEach(client -> {
                client.enviarMensagem(mensagem);
            });
        }
    }

    private void processarComandoPrivado(String mensagem) {
        // Formato esperado: /pv username mensagem
        String[] partes = mensagem.split(" ", 3);
        if (partes.length == 3) {
            String destinatario = partes[1];
            String mensagemPrivada = partes[2];
            enviarMensagemPrivada(destinatario, mensagemPrivada);
        } else {
            escritor.println("Formato inválido para comando privado. Use /pv username mensagem");
        }
    }

    private void enviarMensagemPrivada(String destinatario, String mensagem) {
        synchronized (listaDeEscritores) {
            listaDeEscritores.forEach(client -> {
                if (client.getUsername().equalsIgnoreCase(destinatario)) {
                    client.enviarMensagem(username + " (mensagem privada): " + mensagem);
                }
            });
        }
    }
}
