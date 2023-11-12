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
        try {
            // cria o socket para o client, conectando na porta 1515
            Socket socket = new Socket("localhost", 1515);
            
            // leitor e escritor p/ client enviar pro server e receber msg do server
            // TODO: Fazer o Scanner para ler o input do teclado.
            BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
            System.out.print("Escreva algo: ");
            Scanner mensagemCliente = new Scanner(System.in);
            escritor.println(mensagemCliente);
            
            
            String mensagemRecebida = leitor.readLine();
            System.out.println(mensagemRecebida);
            
            socket.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
            
    }
    
}
