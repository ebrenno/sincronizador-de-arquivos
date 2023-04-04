package cliente;

import utilitario.arquivo.Diretorio;
import utilitario.arquivo.FileHeader;
import utilitario.comunicacao.Requisicao;
import utilitario.comunicacao.Upload;
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

public class ThreadClienteUpload implements Runnable {
private static final Logger log = Logger.getLogger(ThreadClienteUpload.class.getName());
    public ThreadClienteUpload(String diretorio, int porta, String serverIp) throws UnsupportedEncodingException {
        this.serverIp = serverIp;
        this.porta = porta;
        this.diretorio = diretorio;
    }
    
    private final String diretorio;
    private final String serverIp;
    private final int porta;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Collection<FileHeader> escolherArquivos(Collection<FileHeader> lista) {
       
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
       
        Mensagem.updateUpload("conectando...");
        socket = new Socket(serverIp, porta);
        log.info("obtendo relação de arquivos internos.");
        Collection<FileHeader> relacaoDeArquivos = Diretorio.obterArquivos(diretorio);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        Requisicao requisicao = new Requisicao(ois, oos);
        //envia a lista de arquivos para o server
        log.info("enviando relação de arquivos para o serviço.");
        requisicao.enviar(relacaoDeArquivos);
        //recebe a lista necessaria para envio
        log.info("aguardando requisição do serviço para upload de novos arquivos.");
        relacaoDeArquivos = requisicao.receber();

        if(relacaoDeArquivos.isEmpty()){
            log.info("não há arquivos candidatos à envio.");
            return;
        }
        //escolhe os arquivos que serao enviados
        log.info("aguardando relação de arquivos que o usuário quer subir.");
        relacaoDeArquivos = escolherArquivos(relacaoDeArquivos);
        //avisa o serviço se o processo irá continuar.
        requisicao.enviar(relacaoDeArquivos);
        if(relacaoDeArquivos.isEmpty()){
            log.info("não há arquivos para envio.");
            return;
        }
        //envia definitivamente os arquivos
        Upload upload = new Upload(oos,diretorio);
        upload.iniciar(relacaoDeArquivos);
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
            Mensagem.updateUpload("---");
        }
        
    }

}
