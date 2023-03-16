package utilitario.comunicacao;

import utilitario.arquivo.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Logger;

public class Upload {

    private static final Logger log = Logger.getLogger(Upload.class.getName());
    private final String diretorio;
    private final ObjectOutputStream oos;

    public Upload(ObjectOutputStream oos,String diretorio) {
        this.oos = oos;
        this.diretorio = diretorio;
    }

    /**
     * organiza sequencialmente os arquivos e ordena seu envio
     *
     * @param lista uma lista com as relações de arquivos a serem enviados ou
     * uma lista vazia
     * @throws java.io.IOException
     */
    public void iniciar(Collection<FileHeader> lista) throws IOException {

        log.info(String.format("subindo %d arquivos.",lista.size()));
        for (FileHeader fh : lista) {

            uploadArquivo(fh);
        }
        //o parâmetro abaixo é o que faz a outra aplicação saber que a operação terminou
        log.info("enviando sinal de término do upload.");
        oos.writeObject(null);
        log.info("uploads concluído.");

    }

    /**
     * envia um arquivo para outra aplicação
     */
    private void uploadArquivo(FileHeader fh) throws IOException {

        Path caminho = Paths.get(diretorio.concat(fh.getNome()));
        log.info(String.format("preparando %s.",caminho));
        LeitorDeArquivo leitor = new LeitorDeArquivo();

        long posicao = 0;
        long tamanho = fh.getTamanho();
        log.info(String.format("enviando %s",fh.getNome()));
        ByteBuffer buffer = ByteBuffer.allocate(TamanhoDoBuffer.PADRAO);
        byte[] conteudo = new byte[buffer.capacity()];

        do{
            log.info(String.format("posição de leitura: %d",posicao));
            int bytesLidos = leitor.ler(caminho,buffer,posicao);
            log.info(String.format("bytes lidos: %d | estado do buffer após leitura: %s",bytesLidos,buffer));
            buffer.get(0,conteudo,conteudo.length - bytesLidos, bytesLidos);
            Pacote pacote = new Pacote(fh,conteudo,posicao,bytesLidos);
            oos.writeObject(pacote);
            oos.flush();
            oos.reset();
            posicao = posicao+bytesLidos;
            buffer.clear();

            log.info(String.format("Posição: %d/%d, Progresso: %s%%", posicao, tamanho, ProgressoDeTransferencia.porPorcentagem(tamanho, posicao)));
        }while(posicao < tamanho);

        log.info(String.format("%s enviado.",fh.getNome()));
    }

}
