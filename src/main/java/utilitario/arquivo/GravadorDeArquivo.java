package utilitario.arquivo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class GravadorDeArquivo {

    private FileChannel fc;
    public int gravar(Path caminho, ByteBuffer buffer, long posicao) throws IOException {
        fc = this.abrirFileChannel(caminho);
        int bytesEscritos;
        bytesEscritos = fc.write(buffer,posicao);
        return bytesEscritos;
    }
    private FileChannel abrirFileChannel(Path caminho) throws IOException {
        if(Objects.isNull(fc) || !fc.isOpen()) return FileChannel.open(caminho, StandardOpenOption.CREATE,StandardOpenOption.WRITE);
        return fc;
    }
    public void fecharFileChannel() throws IOException {
        fc.close();
    }
}
