package br.uern.wellisonraul.drogaria.mapek;

import java.util.Map;

import br.uern.wellisonraul.drogaria.dao.ServicoDAO;
import br.uern.wellisonraul.drogaria.dominio.Servico;
import br.uern.wellisonraul.engine.SeletordeServicos;

public class Executador {
	public void executar(Map<String,Double> adaptacoes){
		for (Map.Entry<String,Double> adaptado : adaptacoes.entrySet()) {
			if(adaptado.getValue()==0.0){
				// PODE ENCONTRAR NO BANCO
				String servico = adaptado.getKey().substring(0, adaptado.getKey().length()-1);
				String codigo  = adaptado.getKey().substring(adaptado.getKey().length()-1, adaptado.getKey().length());
				
				// Transofrmando o id do processo!
				long cod = Integer.parseInt(codigo);
				
				// BUSCANDO O IDA ACIMA
				ServicoDAO servicoDAO = new ServicoDAO();
				Servico s = new Servico();
				s = servicoDAO.buscar(cod);
				
				SeletordeServicos ss = new SeletordeServicos();
				
				// REORDENA OS SERVIÇOS
				// TRATAR ERRO DO BANCO 
				ss.reordenarServicos(null, servico,s.getPosicao(), 2);
				System.out.println("Adaptação realizada no serviço "+adaptado.getKey());
			}else{
				System.out.println("Não foi realizado adaptação no serviço: "+adaptado.getKey());
			}
		}
	}
}
