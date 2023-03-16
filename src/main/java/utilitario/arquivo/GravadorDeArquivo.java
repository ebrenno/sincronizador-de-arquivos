package utilitario.arquivo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class GravadorDeArquivo {
    public int gravar(Path caminho, ByteBuffer buffer, long posicao) throws IOException {
        int bytesEscritos;
        try (FileChannel fc = FileChannel.open(caminho, StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.APPEND)) {
            bytesEscritos = fc.write(buffer,posicao);
        }
        return bytesEscritos;
    }
}
