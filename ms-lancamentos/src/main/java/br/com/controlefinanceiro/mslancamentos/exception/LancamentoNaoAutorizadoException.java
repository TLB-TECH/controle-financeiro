package br.com.controlefinanceiro.mslancamentos.exception;

public class LancamentoNaoAutorizadoException extends RuntimeException {
    public LancamentoNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
