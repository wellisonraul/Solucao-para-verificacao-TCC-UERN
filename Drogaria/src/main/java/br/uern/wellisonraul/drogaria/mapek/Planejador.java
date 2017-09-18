package br.uern.wellisonraul.drogaria.mapek;

import java.util.Map;

public class Planejador {
	
	public Map<String,Double>  planejar(Map<String,Double> adaptacoes){
		for (Map.Entry<String,Double> adaptado : adaptacoes.entrySet()) {
			if(adaptado.getValue()<0.9){
				adaptado.setValue(0.0);
			}else{
				adaptado.setValue(1.0);
			}
		}
		
		return adaptacoes;
	}
}
