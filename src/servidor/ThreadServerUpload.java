package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import javax.swing.JLabel;
import utilitario.arquivo.Diretorio;
import utilitario.arquivo.FileHeader;
import utilitario.gui.Mensagem;
import utilitario.comunicacao.Requisicao;
import utilitario.comunicacao.Upload;

public class ThreadServerUpload implements Runnable {

    public ThreadServerUpload(String diretorio, int porta, JLabel label) {
        this.diretorio = diretorio;
        this.porta = porta;
        this.label = label;
    }
    private final String diretorio;
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

        socket = servidor.accept();

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        Requisicao requisicao = new Requisicao(ois, oos);
        //obtém os arquivos que o usuario tem
        Collection<FileHeader> arquivosAntigos = requisicao.receber();
        //obtem os arquivos que o server tem
        Collection<FileHeader> arquivosRequeridos = Diretorio.obterArquivos(diretorio);
        //este metodo indica os arquivos que o usuario nao tem
        arquivosRequeridos = Comparador.obterMaisRecentes(arquivosRequeridos, arquivosAntigos);
        //envia para o usuario os arquivos disponiveis
        requisicao.enviar(arquivosRequeridos);
        //recebe do usuario os arquivos que ele quer
        arquivosRequeridos = requisicao.receber();
        //sobe os arquivos que o usuario espera
        Upload upload = new Upload(oos, diretorio, label);
        upload.iniciar(arquivosRequeridos);
    }

    @Override
    public void run() {
        try {
            servidor = new ServerSocket(porta);

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
