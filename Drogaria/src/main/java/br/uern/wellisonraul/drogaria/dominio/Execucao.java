package br.uern.wellisonraul.drogaria.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;


@SuppressWarnings("serial")
@Entity
public class Execucao extends GenericDomain {
	@Column(length = 255, nullable = false)
	private String nome_servico;
	
	@Column(length = 255, nullable = false)
	private String tempo;
	
	@Column(length = 255, nullable = false)
	private String evento;
	
	public String getNome_servico() {
		return nome_servico;
	}

	public void setNome_servico(String nome_servico) {
		this.nome_servico = nome_servico;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}	
	
}
