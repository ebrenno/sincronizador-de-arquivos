/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitario.arquivo;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

public class Diretorio {

    /**
     * obtém uma relação de arquivos em um diretório
     *
     * @param caminho o diretório de interesse
     * @return uma lista de arquivos ou uma lista vazia
     */
    public static Collection<FileHeader> obterArquivos(String caminho) {
        
        File file = new File(caminho);
        File[] lista = filtrarArquivosEmTransferencia(file);

        return convertFrom(lista);
    }

    /**
     * recupera apenas os arquivos que foram completamente movidos para o
     * diretório. Os arquivos que estão sendo movidos no mesmo instante da busca
     * serão ignorados.
     *
     * @param caminho o caminho do diretório de arquivos
     * @return um vetor com os arquivos filtrados ou um vetor vazio.
     */
    private static File[] filtrarArquivosEmTransferencia(File caminho) {
        return caminho.listFiles(file -> {
            long tamanho = file.length();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return tamanho == file.length();
        });
    }

    /**
     * converte um vetor de arquivos do disco para uma lista LinkedHashSet
     *
     * @param file o vetor de arquivos
     */
    private static Collection<FileHeader> convertFrom(File[] file) {
        Collection<FileHeader> listaArquivo = new LinkedHashSet<>();
        for (File i : file) {
            FileHeader j = new FileHeader(i.getName(), i.lastModified(),i.length());
            listaArquivo.add(j);
        }
        return listaArquivo;
    }

}
