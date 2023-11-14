package chatclient;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends javax.swing.JFrame {

    private JTextField messageInput;
    private JTextArea chatArea;
    private PrintWriter writer;
    private Socket socket;
    
    public ChatClient(String username) {
        super("Chat Client");

        // Configurações da interface gráfica
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Componentes da interface
        messageInput = new JTextField();
        JButton sendButton = new JButton("Enviar");
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        // Layout
        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageInput, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Ação do botão de enviar
        sendButton.addActionListener(e -> sendMessage());

        // Conexão com o servidor
        try {
            socket = new Socket("localhost", 1515);
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(username);

            // Thread para receber mensagens
            Thread receiver = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        appendMessage(message);
                    }
                } catch (IOException e1) {
                    handleDisconnect();
                }
            });
            receiver.start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            writer.println(message);
            messageInput.setText("");
            if(message.equalsIgnoreCase("/sair")) {
                handleDisconnect();
            }
        }
    }

    private void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }

    private void handleDisconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                writer.println("/sair");
                System.exit(0);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setVisible(false);
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        ChatClientGUI ccf = new ChatClientGUI();
        ccf.setVisible(true);
        ccf.setLocationRelativeTo(ccf);
    }
}
