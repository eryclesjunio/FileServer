/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Erycles Junio
 */

public class TrataCliente implements Runnable {
    //Determina qual arquivo deverá ser enviado quando requisitado e onde ele está
    public final static String FILE_TO_SEND = "C:\\Users\\user\\Desktop\\arquivos\\code.txt";
    private Socket cliente;
    private Servidor servidor;
    
    /**
     * @param cliente define o Socket do cliente que realizou a conexão
     * @param servidor objeto que controla a conexão
     */

    public TrataCliente(Socket cliente, Servidor servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }

    @Override
    public synchronized void run() {
        
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        
        // quando chegar uma msg, distribui pra todos
        /*Scanner s = null;
        try {
            s = new Scanner(this.cliente.getInputStream());
        } catch (IOException ex) {
           System.err.println("Deu problema no Scanner");
        }
        while (s.hasNextLine()) {
            servidor.distribuiMensagem(s.nextLine());
        }*/
        
        //tratando arquivos
        try{
            File arquivo = new File (FILE_TO_SEND);
            byte [] bytearray  = new byte [(int)arquivo.length()]; //Prepara o arquivo
            
           /*As classes abstratas InputStream e OutputStream que definem a forma como, respectivamente,
            *você lê e grava sequências de bytes sem se importar com a fonte ou destino dos dados.
            */
           
           /*FileInputStream e FileOutputStream: como os nomes sugerem, são para tratar de arquivos,
            *o primeiro possui métodos para ler um arquivo e o segundo possui métodos para escrever.
            */
           
            fis = new FileInputStream(arquivo);
            bis = new BufferedInputStream(fis); //BufferedInputStream possui melhor performance (Leitura mais rápida)
            bis.read(bytearray,0,bytearray.length); //Lê o conteúdo do arquivo para o array de Bytes
            
            os = this.cliente.getOutputStream(); //Maneira de comunicar com o cliente através de streams
            System.out.println("Enviando " + FILE_TO_SEND + "(" + bytearray.length + " bytes)");
           //enviar um dado para um dispositivo de saída 
            os.write(bytearray,0,bytearray.length);
            os.flush(); //descarrega
            this.servidor.distribuiMensagem("O arquivo foi enviado!");
            System.out.println("Enviado com sucesso!.");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
         finally {
            try {
                if (bis != null) bis.close();
                if (os != null) os.close();
                //if (s != null) s.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        
        
        
    }
}