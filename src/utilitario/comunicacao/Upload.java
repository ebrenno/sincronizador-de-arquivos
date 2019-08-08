package utilitario.comunicacao;

import utilitario.arquivo.FileHeader;
import utilitario.arquivo.Arquivo;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import javax.swing.JLabel;
import utilitario.gui.Mensagem;

public class Upload {

    private final JLabel label;
    private final String diretorio;
    private Arquivo arquivo;
    private final ObjectOutputStream oos;

    public Upload(ObjectOutputStream oos,String diretorio, JLabel label) {
        this.oos = oos;
        this.diretorio = diretorio;
        this.label = label;
    }

    /**
     * organiza sequencialmente os arquivos e ordena seu envio
     *
     * @param lista uma lista com as relações de arquivos a serem enviados ou
     * uma lista vazia
     * @throws java.io.IOException
     */
    public void iniciar(Collection<FileHeader> lista) throws IOException {

        for (FileHeader fh : lista) {
            prepararArquivo(fh);
            uploadArquivo();
        }
        //o parâmetro abaixo é o que faz a outra aplicação saber que a operação terminou
        oos.writeObject(null);

    }

    private void prepararArquivo(FileHeader fh) throws IOException {
        arquivo = new Arquivo(fh);
        File file = new File(diretorio, fh.getNome());
        arquivo.carregarDoDisco(file);
    }

    /**
     * envia um arquivo para outra aplicação
     */
    private void uploadArquivo() throws IOException {
        oos.writeObject(arquivo);
        oos.flush();
        
        Mensagem.exibir(label, "arquivo enviado!");
    }

}
