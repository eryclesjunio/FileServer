/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Erycles Junio
 */

public class Servidor {

    public static void main(String[] args) throws IOException {
        // inicia o servidor
        new Servidor(12345).executa(); //Porta 12345
    }
    
    //Atributos
    
    //Determina qual arquivo deverá ser enviado quando requisitado e onde ele está
    public final static String FILE_TO_SEND = "C:\\Users\\user\\Desktop\\arquivos\\code.txt"; 
    private int porta;
    public ServerSocket servidor;
    //Salva stream dos clientes para comunicar mais tarde
    private List<PrintStream> clientes; 

    //Inicializa os atributos
    public Servidor (int porta) throws IOException {
        this.porta = porta;
        this.servidor = new ServerSocket(this.porta);
        this.clientes = new ArrayList<>();
    }

    public void executa () throws IOException {
        System.out.println("Porta 12345 aberta!");
        //Enquanto true, cria uma Thread para cada cliente
        while (true) {
            System.out.println("Esperando...");
            // aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente " +     
                cliente.getInetAddress().getHostAddress()
            );

            // adiciona saida do cliente à lista
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            this.clientes.add(ps);

            // cria tratador de cliente numa nova thread
            TrataCliente tc = new TrataCliente(cliente, this);
            new Thread(tc).start();
            
        }

    }
    
    //Ainda não finalizado
    public void distribuiMensagem(String msg) {
        // envia msg para todo mundo
        this.clientes.forEach((cliente) -> {
            cliente.println("Repassando mensagem:  "+ msg);
        });
    }
}