package br.uern.wellisonraul.drogaria.mapek;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.uern.wellisonraul.adaptacoes.tipo.ConversaoArquivos;
import br.uern.wellisonraul.drogaria.dao.ExecucaoDAO;
import br.uern.wellisonraul.drogaria.dao.XlogDAO;
import br.uern.wellisonraul.drogaria.dominio.Execucao;
import br.uern.wellisonraul.drogaria.dominio.Xlog;
import br.uern.wellisonraul.threads.Verificador;
import br.uern.wellisonraul.util.UtilitarioConfiguracao;

public class Monitor {
	Long inicio, ultimo = null;
	List<Execucao> execucoes = null;
	Verificador v = new Verificador();
	String log;
	// CLASSES EM COMUM DOS ARQUIVOS
	ConversaoArquivos ca = new ConversaoArquivos();
		
		
	// MÉTODO RESPOSÁVEL POR PESQUISAR DO INICIO DO LOG AO FINAL
	public Set<String> retornaArquivo(){
		// INICIO DO BANCO PARA XES
		inicio = inicioBanco();
		// FINAL DO BANCO PARA XES
		ultimo = finalBanco(inicio);
		// VERIFICADOR PARA TRANSFORMAR O XES
		execucoes = consulta(inicio, ultimo);
		Set<String> colecaoSet = conversorArquivos(execucoes);
		return colecaoSet;
	}
	
	public Set<String> conversorArquivos(List<Execucao> execucoes){
		
		// PARA CONTER TODOS OS SERVIÇOS
		// LIKEDHASHSET POIS GUARDA A SEQUÊNCIA DE ENTRDA.
		Set<String> colecaoSet = new LinkedHashSet<String>();
		// INSERI EM UM SET
		// NÃO PERMITE DUPLICATAS

		for (Execucao listadeServicos : execucoes) {
			colecaoSet.add(listadeServicos.getNome_servico());	
		}	
		
		if(UtilitarioConfiguracao.FERRAMENTA==0){
			try {
				ca.conversaoPROM(execucoes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(UtilitarioConfiguracao.FERRAMENTA==1){
			try {
				ca.conversaoCADP(execucoes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return colecaoSet;
	}
		
	public List<Execucao> consulta(long inicio_banco, long final_banco){
		List<Execucao> execucoes = null;
		
		ExecucaoDAO execucaoDAO = new ExecucaoDAO();
		execucoes = execucaoDAO.listar();
			
		// SIGNIFICA QUE NÃO HÁ NADA NO BANCO.
		if(inicio_banco==0){
			inicio_banco = execucoes.get(0).getCodigo();
			execucoes = execucaoDAO.listarPeloTipoCodigo(inicio_banco, final_banco);
		// SIGNIFICA QUE O BANCO JÁ CONTÉM ALGO
		}else{
			execucoes = execucaoDAO.listarPeloTipoCodigo(inicio_banco, final_banco);
		}
			
		return execucoes;
	}
		
	// MÉTODO PARA TRATAR O INICIO DA ITERAÇÃO
	public Long inicioBanco(){
		XlogDAO xlogDAO = new XlogDAO();
		List<Xlog> xlogs= null;
			
		List<Execucao> execucoes = null;
		ExecucaoDAO execucaoDAO = new ExecucaoDAO();
		execucoes = execucaoDAO.listar();
			
		Long inicio_banco;
			
		xlogs = xlogDAO.listar();
			
		// SE O INICIO FOR 0 O BANCO ESTÁ SECO.
		if(xlogs.get(0).getInicio()==0){
			inicio_banco = execucoes.get(0).getCodigo();
				
		// NÃO ESTÁ ZERADO ENTÃO PEGUE O VALOR NA VÁRIAVEL XLOG.
		}else{
			inicio_banco = xlogs.get(0).getInicio() + 1;
		}
			
		return inicio_banco;
	}
		
		// METODO PARA TRATAR O FIM DA ITERAÇÃO.
		public long finalBanco(Long inicio_banco){
			XlogDAO xlogDAO = new XlogDAO();
			Xlog xlog = new Xlog();
			
			List<Execucao> execucoes = null;
			ExecucaoDAO execucaoDAO = new ExecucaoDAO();
			
			
			execucoes = execucaoDAO.buscaFinalXES(inicio_banco);
			
			Long ultimo_banco = (Long) execucoes.get(execucoes.size()-1).getCodigo();
			
			xlog.setCodigo((long) 1);
			xlog.setInicio(ultimo_banco);
			xlog.setFim(inicio_banco);
			xlogDAO.editar(xlog);
			
			
			return ultimo_banco;
		}
		
		// GET E SET!
		public Long getInicio() {
			return inicio;
		}

		public void setInicio(Long inicio) {
			this.inicio = inicio;
		}

		public Long getUltimo() {
			return ultimo;
		}

		public void setUltimo(Long ultimo) {
			this.ultimo = ultimo;
		}
}
