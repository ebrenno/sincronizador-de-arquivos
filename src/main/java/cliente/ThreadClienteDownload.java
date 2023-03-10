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
import utilitario.comunicacao.Download;
import utilitario.arquivo.FileHeader;
import utilitario.gui.Mensagem;
import utilitario.comunicacao.Requisicao;

public class ThreadClienteDownload implements Runnable {

    private final String diretorio;
    private final int porta;
    private final String serverIp;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final JLabel label; 
    
    public ThreadClienteDownload(String diretorio,int porta,String serverIp, JLabel label) {
        this.diretorio = diretorio;
        this.porta = porta;
        this.serverIp = serverIp;
        this.label = label;

    }

    public Collection<FileHeader> escolherArquivos(Collection<FileHeader> lista) throws IOException {
        
        for(Iterator<FileHeader> i = lista.iterator() ; i.hasNext();){
            FileHeader fh = i.next();
            int decisao = JOptionPane.showConfirmDialog(null, "baixar \"" + fh.getNome()+ "\" ?", "decisão", JOptionPane.YES_NO_OPTION);
            if (decisao == JOptionPane.NO_OPTION) {
                i.remove();
            }
        }
        return lista;
    }

    public void main() throws ClassNotFoundException, IOException {

            Mensagem.exibir(label,"conectando...");
            socket = new Socket(serverIp, porta);

            Collection<FileHeader> lista = Diretorio.obterArquivos(diretorio);

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            Requisicao requisicao = new Requisicao(ois, oos);
            //envia para o server a lista atual de arquivos
            requisicao.enviar(lista);
            //recebe a lista necessaria para download
            lista = requisicao.receber();
            //usuario decide os arquivos que ira baixar
            lista = escolherArquivos(lista);
            //usuario envia a relação de arquivos para o server
            requisicao.enviar(lista);
            //prossegue o download dos arquivos necessarios
            Download download = new Download(ois, diretorio, label);
            download.iniciar();

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

    @Override
    public void run() {
        try {
            main();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            fecharRecursos();
            Mensagem.exibir(label,"---");
        }
        
    }

}
