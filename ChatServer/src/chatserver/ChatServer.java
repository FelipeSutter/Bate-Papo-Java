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

/**
 *
 * @author FACC
 */
public class ChatServer {

    private static final int PORTA = 1515;

    public static void main(String[] args) {
        try {
            // cria o server na porta que eu disser
            ServerSocket server = new ServerSocket(PORTA);
            System.out.println("Servidor rodando na porta: " + PORTA);

            while (true) {
                // cria o socket e fica aguardando o client se conectar a ele.
                System.out.println("Aguardando conexao...");
                Socket client = server.accept();
                System.out.println("Cliente" + client.getInetAddress().getHostAddress() + "conectou!");

                // cria um leitor e escritor para poder ler e escrever mensagens do cliente.
                BufferedReader leitor = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter escritor = new PrintWriter(client.getOutputStream());

                // lê a mensagem que o client enviou
                String mensagem = leitor.readLine();
                System.out.println("Mensagem enviada do cliente: " + mensagem);

                // enviar mensagem pro client
                escritor.println("Sua mensagem foi recebida. Aqui está ela: " + mensagem);

                client.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
