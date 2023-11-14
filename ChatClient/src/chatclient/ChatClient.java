/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author FACC
 */
public class ChatClient {

    public static void main(String[] args) {

        System.out.print("Digite o seu nome: ");
        Scanner scanner = new Scanner(System.in);
        // pega o nome do usuario
        String username = scanner.nextLine();

        // cria o socket onde o server está rodando, o leitor e escritor.
        try (Socket socket = new Socket("localhost", 1515); 
                BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);) 
        {
            escritor.println(username);

            // thread criada para receber mensagem dos usuários
            Thread receptor = new Thread(() -> {
                try {
                    String mensagemRecebida;
                    while((mensagemRecebida = leitor.readLine()) != null) {
                        System.out.println(mensagemRecebida);
                    }
                } catch (IOException e2) {
                    System.out.println(username + " saiu do chat.");
                }
            });
            
            receptor.start();
            
            // cliente escrever a msg
            // continua mandando msg se for diferente de /sair
            String mensagem;
            while(true){
                Thread.sleep(300);
                System.out.println("Escreva uma mensagem: ");
                mensagem = scanner.nextLine();
                if(!mensagem.isEmpty()) {
                    escritor.println(mensagem);
                    if(mensagem.equalsIgnoreCase("/sair")) break;
                }
            }
            
            System.out.println("Saindo do chat...");
            receptor.interrupt();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
