package utilitario.comunicacao;

import utilitario.arquivo.Arquivo;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JLabel;
import utilitario.gui.Mensagem;

public class Download {

    private final String diretorio;
    private Arquivo arquivo;
    private final ObjectInputStream ois;
    private final JLabel label;

    public Download(ObjectInputStream ois,String diretorio, JLabel label) {
        this.ois = ois;
        this.diretorio = diretorio;
        this.label = label;
    }

    /**
     * invoca uma sequência de downloads enquanto houver arquivos para receber e
     * ordena a gravação dos arquivos em disco.
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public void iniciar() throws IOException, ClassNotFoundException {
        //executa o laço enquanto houver instancia de arquivo para gravar
        while (hasNext()) {
            arquivo.gravarNoDisco(diretorio);
            Mensagem.exibir(label, "\"" + arquivo.getFh().getNome() + "\" gravado com sucesso!");
        }

    }

    /**
     * obtém o arquivo enviado por outro sistema. Se não houver arquivo para
     * receber, o valor <strong>null</strong> será esperado.
     *
     * @return true se um arquivo foi recebido ou false se nenhum arquivo foi
     * recebido
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    private boolean hasNext() throws IOException, ClassNotFoundException {
        arquivo = (Arquivo) ois.readObject();
        return arquivo != null;
    }

}
