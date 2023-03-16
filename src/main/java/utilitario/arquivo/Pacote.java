package utilitario.arquivo;

import java.io.Serializable;

public class Pacote implements Serializable {

    private final FileHeader fh;
    private final byte[] conteudo;
    private final long posicao;
    private final int tamanhoDoConteudo;

    private static final long serialVersionUID = 1L;

    public Pacote(FileHeader fh, byte[] conteudo,long posicao,int tamanhoDoConteudo) {

        this.fh = fh;
        this.conteudo = conteudo;
        this.posicao = posicao;
        this.tamanhoDoConteudo = tamanhoDoConteudo;
    }

    public FileHeader getFh() {
        return fh;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public long getPosicao() {
        return posicao;
    }

    public int getTamanhoDoConteudo() {
        return tamanhoDoConteudo;
    }
}
