package utilitario.arquivo;

import java.io.Serializable;
import java.util.Objects;

public class FileHeader implements Serializable {

    private final String nome;
    private final long dataDeModificacao;

    public FileHeader(String nome, long dataDeModificacao) {
        this.nome = nome;
        this.dataDeModificacao = dataDeModificacao;
    }

    public String getNome() {
        return nome;
    }

    public long getDataDeModificacao() {
        return dataDeModificacao;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(dataDeModificacao);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileHeader other = (FileHeader) obj;
        return Objects.equals(this.nome, other.nome);
    }

}
