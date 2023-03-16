package utilitario.comunicacao;

import utilitario.arquivo.GravadorDeArquivo;
import utilitario.arquivo.Pacote;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.logging.Logger;

public class Download {
    private static final Logger log = Logger.getLogger(Download.class.getName());
    private final String diretorio;
    private Pacote pacote;
    private final ObjectInputStream ois;

    public Download(ObjectInputStream ois,String diretorio) {
        this.ois = ois;
        this.diretorio = diretorio;
    }

    /**
     * Invoca uma sequência de downloads enquanto houver pacotes para receber e
     * ordena a gravação destes pacotes em disco.
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public void iniciar() throws IOException, ClassNotFoundException {
        //executa o laço enquanto houver instancia de pacote para gravar
        log.info("iniciando downloads.");

        while (hasNext()) {

            GravadorDeArquivo gravador = new GravadorDeArquivo();
            Path caminho = Path.of(diretorio.concat(pacote.getFh().getNome()));
            ByteBuffer conteudo = ByteBuffer.wrap(pacote.getConteudo(),
                    pacote.getConteudo().length - pacote.getTamanhoDoConteudo()
                    ,pacote.getTamanhoDoConteudo());
            log.info(String.format("status do buffer: %s",conteudo));
            long posicao = pacote.getPosicao();
            int bytesEscritos = gravador.gravar(caminho, conteudo, posicao);
            log.info(String.format("bytes escritos: %d/%d, na posição: %d",bytesEscritos,conteudo.capacity(),posicao));

            Files.setLastModifiedTime(caminho, FileTime.fromMillis(pacote.getFh().getDataDeModificacao()));

        }
            log.info("fim dos downloads.");
    }

    /**
     * Obtém partes do pacote enviado por outro sistema. Quando não houver pacote para
     * receber, o valor <strong>null</strong> será esperado.
     *
     * @return true se um pacote foi recebido ou false se nenhum pacote foi
     * recebido
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    private boolean hasNext() throws IOException, ClassNotFoundException {
        pacote = (Pacote) ois.readObject();
        return pacote != null;
    }

}
