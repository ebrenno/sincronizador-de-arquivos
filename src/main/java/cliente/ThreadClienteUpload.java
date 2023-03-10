package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import utilitario.arquivo.Diretorio;
import utilitario.arquivo.FileHeader;
import utilitario.gui.Mensagem;
import utilitario.comunicacao.Requisicao;
import utilitario.comunicacao.Upload;

public class ThreadClienteUpload implements Runnable {

    public ThreadClienteUpload(String diretorio, int porta, String serverIp, JLabel label) {
        this.serverIp = serverIp;
        this.porta = porta;
        this.diretorio = diretorio;
        this.label = label;
    }
    private final JLabel label;
    private final String diretorio;
    private final String serverIp;
    private final int porta;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Collection<FileHeader> escolherArquivos(Collection<FileHeader> lista) throws IOException {
       
        for(Iterator<FileHeader> i = lista.iterator() ; i.hasNext();){
            FileHeader fh = i.next();
            int decisao = JOptionPane.showConfirmDialog(null, "enviar " + fh.getNome() + " ?", "decisão", JOptionPane.YES_NO_OPTION);
            if (decisao == JOptionPane.NO_OPTION) {
                i.remove();
            }
        }
        return lista;
    }

    public void main() throws IOException, ClassNotFoundException {
       
        Mensagem.exibir(label,"conectando...");
        socket = new Socket(serverIp, porta);

        Collection<FileHeader> listaFinal = Diretorio.obterArquivos(diretorio);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        Requisicao requisicao = new Requisicao(ois, oos);
        //envia a lista de arquivos para o server
        requisicao.enviar(listaFinal);
        //recebe a lista necessaria para envio
        listaFinal = requisicao.receber();
        //escolhe os arquivos que serao enviados
        listaFinal = escolherArquivos(listaFinal);
        //envia definitivamente os arquivos
        Upload upload = new Upload(oos,diretorio, label);
        upload.iniciar(listaFinal);
    }

    public void fecharRecursos() {
       
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            main();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            fecharRecursos();
            Mensagem.exibir(label,"---");
        }
        
    }

}
