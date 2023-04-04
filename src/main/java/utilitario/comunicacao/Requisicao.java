package utilitario.comunicacao;

import utilitario.arquivo.FileHeader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

public class Requisicao {

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    public Requisicao(ObjectInputStream ois, ObjectOutputStream oos) {
        super();
        this.ois = ois;
        this.oos = oos;
    }

    /**
     * recebe de outra aplicação uma lista de cabeçalhos de arquivos
     *
     * @return uma lista com elementos ou uma lista vazia
     */
    public Collection<FileHeader> receber() throws IOException, ClassNotFoundException {
        Collection<FileHeader> listaArquivo = (Collection<FileHeader>) ois.readObject();
        return listaArquivo;
    }

    /**
     * envia para outra aplicação uma lista de cabeçalhos de arquivos.
     *
     * @param lista uma lista com elementos ou uma lista vazia
     */
    public void enviar(Collection<FileHeader> lista) throws IOException {
        oos.writeObject(lista);
        oos.flush();

    }

}
