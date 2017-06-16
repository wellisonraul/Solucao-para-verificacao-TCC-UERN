package br.uern.wellisonraul.engine;

import java.util.List;

import br.uern.wellisonraul.drogaria.dao.ServicoDAO;
import br.uern.wellisonraul.drogaria.dominio.Servico;

public class SeletordeServicos {
	private List<Servico> servicos;
	ServicoDAO servicoDAO = new ServicoDAO();
			
	public void reordenarServicos(List<Servico> s,String metodo, Short codigo, int op){
		
		if(op==2){
			// TRATAMENTO DE CONSULTA AO BANCO DIRETAMENTO | CRIAR SERVIÇO.
			servicos = servicoDAO.listarPeloTipo(metodo,"posicao");
			
			// SE FOR IGUAL AO SERVIÇO RECEBA O ULTIMO, SE NÃO FOR ANDE UMA POSIÇÃO
			// TEM QUE TRATAR ISSO AINDA.
			
			for(Servico ordernacao: servicos){
				if(!(codigo==servicos.size())){
					if(ordernacao.getPosicao()==codigo){
						ordernacao.setPosicao((short)servicos.size());
					}else{
						if(!(ordernacao.getPosicao()==1)){
							ordernacao.setPosicao((short)(ordernacao.getPosicao()-1));
						}
					}
				}
			}
			
			// SALVE A NOVA LISTA
			for(Servico ser : servicos){
				servicoDAO.editar(ser);
			}
		}else{
			// SALVE A NOVA LISTA
			for(Servico se : s){
				servicoDAO.editar(se);
			}
		}
		
	}
}
