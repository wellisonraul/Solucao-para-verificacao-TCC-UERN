package br.uern.wellisonraul.drogaria.dominio;
import javax.persistence.Column;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
public class Servico extends GenericDomain{
	@Column(length = 80, nullable = false)
	private String nome;
	
	@Column(length = 255, nullable = false)
	private String uri;
	
	@Column(nullable = false)
	private Short posicao;
	
	@Column(nullable = false)
	private double confiabilidade;
	
	@Column(nullable = false)
	private double confiabilidade_real;
	
	@Column(nullable = false)
	private double tempo_execucao;
	
	
	public double getTempo_execucao() {
		return tempo_execucao;
	}

	public void setTempo_execucao(double tempo_execucao) {
		this.tempo_execucao = tempo_execucao;
	}

	public double getConfiabilidade_real() {
		return confiabilidade_real;
	}

	public void setConfiabilidade_real(double confiabilidade_real) {
		this.confiabilidade_real = confiabilidade_real;
	}

	public double getConfiabilidade() {
		return confiabilidade;
	}

	public void setConfiabilidade(double confiabilidade) {
		this.confiabilidade = confiabilidade;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setPosicao(Short posicao) {
		this.posicao = posicao;
	}
	
	public Short getPosicao() {
		return posicao;
	}
}
