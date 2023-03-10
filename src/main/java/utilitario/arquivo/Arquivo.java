package utilitario.arquivo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Arquivo implements Serializable {

    private final FileHeader fh;
    private byte[] conteudo;

    private static final long serialVersionUID = 1L;

    public Arquivo(FileHeader fh) {
        this.fh = fh;
    }

    public FileHeader getFh() {
        return fh;
    }

    /**
     * grava os bytes alocados para o disco
     *
     * @param caminho o caminho do diretório de destino
     * @throws java.io.IOException
     */
    public void gravarNoDisco(String caminho) throws IOException {

        Path path = Paths.get(caminho, fh.getNome());
        File file = new File(path.toString());
        FileOutputStream foi = new FileOutputStream(file);
        try (BufferedOutputStream bos = new BufferedOutputStream(foi)) {
            bos.write(conteudo);
            bos.flush();
        }
        //mantem data original do arquivo
        file.setLastModified(fh.getDataDeModificacao());
    }

    /**
     * carrega os bytes do arquivo do disco
     *
     * @param file o arquivo requerido
     * @throws java.io.IOException
     */
    public void carregarDoDisco(File file) throws IOException {

        conteudo = new byte[(int) file.length()];
        FileInputStream input = new FileInputStream(file);
        try (BufferedInputStream bis = new BufferedInputStream(input)) {
           
            bis.read(conteudo);
        }

    }

}
