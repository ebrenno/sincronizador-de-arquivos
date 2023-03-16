package utilitario.arquivo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LeitorDeArquivo {
    public int ler(Path caminho, ByteBuffer buffer, long posicao) throws IOException {

        int bytesLidos;
        try (FileChannel fc = FileChannel.open(caminho, StandardOpenOption.READ)) {
            bytesLidos = fc.read(buffer, posicao);
        }
        return bytesLidos;
    }

}
