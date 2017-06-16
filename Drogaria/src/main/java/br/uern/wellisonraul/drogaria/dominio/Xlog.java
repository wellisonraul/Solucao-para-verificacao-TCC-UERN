package br.uern.wellisonraul.drogaria.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
public class Xlog extends GenericDomain {
	@Column(nullable=false)
	private Long inicio;
	
	@Column(nullable=false)
	private Long fim;

	public Long getInicio() {
		return inicio;
	}

	public void setInicio(Long inicio) {
		this.inicio = inicio;
	}

	public Long getFim() {
		return fim;
	}

	public void setFim(Long fim) {
		this.fim = fim;
	}
	
}
