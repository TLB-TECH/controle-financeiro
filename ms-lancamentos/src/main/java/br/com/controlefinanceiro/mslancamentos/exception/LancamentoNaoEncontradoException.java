package br.com.controlefinanceiro.mslancamentos.exception;

public class LancamentoNaoEncontradoException extends RuntimeException  {
    public LancamentoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

}
