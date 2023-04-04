/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitario.arquivo;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author brenno
 */
public class Comparador {
    /**
    * Compara duas listas de arquivos e exclui os arquivos mais antigos
    * @param arquivosRequeridos os supostos arquivos mais novos
    * @param arquivosAntigos os supostos arquivos mais antigos
    * @return uma lista com apenas os arquivos mais novos ou uma lista vazia
    */
    public static Collection<FileHeader> obterMaisRecentes(Collection<FileHeader> arquivosRequeridos, Collection<FileHeader> arquivosAntigos) {
        //remove os arquivos iguais de nome e data de modificação
        arquivosRequeridos.removeAll(arquivosAntigos);
        //remove os arquivos antigos
        for(Iterator<FileHeader> iter = arquivosRequeridos.iterator();iter.hasNext();){
            FileHeader i = iter.next();
            for (FileHeader j : arquivosAntigos) {
                if(i.getNome().equals(j.getNome()) && i.getDataDeModificacao() < j.getDataDeModificacao() ){
                    iter.remove();
                    break;
                }
            }
        }
       
        return arquivosRequeridos;

    }
}
