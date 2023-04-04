package utilitario.comunicacao;

public class ProgressoDeTransferencia {
    public static String porPorcentagem(long volumeTotal,long posicaoAtual){
        if(volumeTotal == 0)volumeTotal = 1;
        return String.valueOf((posicaoAtual*100)/volumeTotal);
    }
}
