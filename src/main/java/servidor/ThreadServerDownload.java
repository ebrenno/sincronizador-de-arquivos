package servidor;

import utilitario.arquivo.Comparador;
import utilitario.arquivo.Diretorio;
import utilitario.arquivo.FileHeader;
import utilitario.comunicacao.Download;
import utilitario.comunicacao.Requisicao;
import utilitario.gui.Mensagem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Logger;

public class ThreadServerDownload implements Runnable {
    private static final Logger log = Logger.getLogger(ThreadServerDownload.class.getName());
    private final String diretorio;
    private final String serverIp;
    private final int porta;
    private ServerSocket servidor;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ThreadServerDownload(String diretorio, String serverIp,int porta) throws UnsupportedEncodingException {
        this.diretorio = diretorio;
        this.serverIp = serverIp;
        this.porta = porta;
    }
    
    public void fecharRecursos() {
        try {
            oos.close();
            ois.close();
            socket.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void main() throws ClassNotFoundException, IOException {
        Mensagem.updateDownload("aguardando novas conexões...");
        socket = servidor.accept();

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        Requisicao requisicao = new Requisicao(ois, oos);
        //server recebe a lista que o usuario deseja enviar
        log.info("obtendo relação de arquivos do usuário.");
        Collection<FileHeader> arquivosRequeridos = requisicao.receber();
        log.info("obtendo relação de arquivos internos.");
        Collection<FileHeader> arquivosAntigos = Diretorio.obterArquivos(diretorio);
        log.info("checando por arquivos novos ou atualizados.");
        arquivosRequeridos = Comparador.obterMaisRecentes(arquivosRequeridos, arquivosAntigos);
        //server envia a lista que o usuario precisa enviar
        requisicao.enviar(arquivosRequeridos);
        if(arquivosRequeridos.isEmpty()){
            log.info("não há arquivos candidatos à download.");
            return;
        }
        log.info("aquardar por confirmação do usuário.");
        arquivosRequeridos = requisicao.receber();
        if(arquivosRequeridos.isEmpty()){
            log.info("não há arquivos candidatos à download.");
            return;
        }

        //prepara o server para baixar os arquivos do cliente
        Download download = new Download(ois, diretorio);
        download.iniciar();
    }

    @Override
    public void run() {
        try {
            log.info(String.format("abrindo serviço de download com o ip:porta %s:%s.",serverIp,porta));
            servidor = new ServerSocket(porta,0, InetAddress.getByName(serverIp));

            while (true) {
                try {
                    main();
                }finally{
                    fecharRecursos();
                    Mensagem.updateDownload("---");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
