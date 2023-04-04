package cliente;

import utilitario.arquivo.Diretorio;
import utilitario.arquivo.FileHeader;
import utilitario.comunicacao.Download;
import utilitario.comunicacao.Requisicao;
import utilitario.gui.Mensagem;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public class ThreadClienteDownload implements Runnable {

    private static final Logger log = Logger.getLogger(ThreadClienteDownload.class.getName());
    private final String diretorio;
    private final int porta;
    private final String serverIp;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    public ThreadClienteDownload(String diretorio,int porta,String serverIp) throws UnsupportedEncodingException {
        this.diretorio = diretorio;
        this.porta = porta;
        this.serverIp = serverIp;
    }

    public Collection<FileHeader> escolherArquivos(Collection<FileHeader> lista) {
        
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

            Mensagem.updateDownload("conectando...");
            log.info("conectando ao servidor.");
            socket = new Socket(serverIp, porta);
            log.info("obtendo relação de arquivos internos.");
            Collection<FileHeader> lista = Diretorio.obterArquivos(diretorio);

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            Requisicao requisicao = new Requisicao(ois, oos);
            //envia para o server a lista atual de arquivos
            log.info("enviando relação de arquivos do usuário");
            requisicao.enviar(lista);
            //recebe a lista necessaria para download
            log.info("aguardando relação de arquivos candidatos à download");
            lista = requisicao.receber();
            if(lista.isEmpty()){
                log.info("não há arquivos candidatos à download.");
                return;
            }
            //usuario decide os arquivos que ira baixar
            log.info("aguardando relação de arquivos que o usuário quer baixar.");
            lista = escolherArquivos(lista);
            //usuario envia a relação de arquivos para o server
            log.info("enviando a relação de arquivos para o serviço.");
            requisicao.enviar(lista);

            if(lista.isEmpty()){
                log.info("nenhum arquivo foi escolhido.");
                return;
            }
            //prossegue o download dos arquivos necessarios
            Download download = new Download(ois, diretorio);
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
            log.info("iniciando serviço de download.");
            main();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            fecharRecursos();
            Mensagem.updateDownload("---");
        }
        
    }

}
