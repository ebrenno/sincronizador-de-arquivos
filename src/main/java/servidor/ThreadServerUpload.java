package servidor;

import utilitario.arquivo.Diretorio;
import utilitario.arquivo.FileHeader;
import utilitario.comunicacao.Requisicao;
import utilitario.comunicacao.Upload;
import utilitario.gui.Mensagem;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Logger;

public class ThreadServerUpload implements Runnable {
    private static final Logger log = Logger.getLogger(ThreadServerUpload.class.getName());
    public ThreadServerUpload(String diretorio, String serverIp,int porta, JLabel label) {
        this.diretorio = diretorio;
        this.serverIp = serverIp;
        this.porta = porta;
        this.label = label;
    }
    private final String diretorio;
    private final String serverIp;
    private final int porta;
    private ServerSocket servidor;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private final JLabel label;
    
    public void fecharRecursos(){
 
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void main() throws IOException, ClassNotFoundException {

        Mensagem.exibir(label,"aguardando novas conexões...");
        log.info("aguardando novas conexões.");
        socket = servidor.accept();
        log.info("iniciando atendimento.");
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        Requisicao requisicao = new Requisicao(ois, oos);
        //obtém os arquivos que o usuario tem
        log.info("obtendo relação de arquivos do usuário.");
        Collection<FileHeader> arquivosAntigos = requisicao.receber();
        //obtem os arquivos que o server tem
        log.info("obtendo relação de arquivos internos.");
        Collection<FileHeader> arquivosRequeridos = Diretorio.obterArquivos(diretorio);
        //este metodo indica os arquivos que o usuario nao tem
        log.info("checando por arquivos novos ou atualizados.");
        arquivosRequeridos = Comparador.obterMaisRecentes(arquivosRequeridos, arquivosAntigos);
        //envia para o usuario os arquivos disponiveis
        requisicao.enviar(arquivosRequeridos);

        if(arquivosRequeridos.isEmpty()){
            log.info("não há arquivos candidatos à envio.");
            return;
        }
        //recebe do usuario os arquivos que ele quer
        log.info("recebendo relação de arquivos que o usuário deseja obter");
        arquivosRequeridos = requisicao.receber();

        if(arquivosRequeridos.isEmpty()){
            log.info("nada a ser enviado para o usuário.");
            return;
        }
        //sobe os arquivos que o usuario espera
        Upload upload = new Upload(oos, diretorio);
        upload.iniciar(arquivosRequeridos);
    }

    @Override
    public void run() {
        try {
            log.info(String.format("abrindo serviço de upload com o ip:porta %s:%s.",serverIp,porta));
            servidor = new ServerSocket(porta,0, InetAddress.getByName(serverIp));

            while (true) {
                try {
                    main();
                } finally {
                    fecharRecursos();
                    Mensagem.exibir(label,"---");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
