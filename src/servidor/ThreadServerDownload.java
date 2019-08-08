package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import javax.swing.JLabel;
import utilitario.arquivo.Diretorio;
import utilitario.comunicacao.Download;
import utilitario.arquivo.FileHeader;
import utilitario.gui.Mensagem;
import utilitario.comunicacao.Requisicao;

public class ThreadServerDownload implements Runnable {

    private final String diretorio;
    private final int porta;
    private ServerSocket servidor;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final JLabel label;

    public ThreadServerDownload(String diretorio, int porta, JLabel label) {
        this.diretorio = diretorio;
        this.porta = porta;
        this.label = label;
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
        Mensagem.exibir(label,"aguardando novas conexões...");
        socket = servidor.accept();

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        Requisicao requisicao = new Requisicao(ois, oos);
        //server recebe a lista que o usuario deseja enviar
        Collection<FileHeader> arquivosRequeridos = requisicao.receber();
        Collection<FileHeader> arquivosAntigos = Diretorio.obterArquivos(diretorio);

        arquivosRequeridos = Comparador.obterMaisRecentes(arquivosRequeridos, arquivosAntigos);
        //server envia a lista que o usuario precisa enviar
        requisicao.enviar(arquivosRequeridos);
        //prepara o server para baixar os arquivos do cliente
        Download download = new Download(ois, diretorio, label);
        download.iniciar();
    }

    @Override
    public void run() {
        try {
            servidor = new ServerSocket(porta);
            
            while (true) {
                try {
                    main();
                }finally{
                    fecharRecursos();
                    Mensagem.exibir(label,"---");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
