package br.uern.wellisonraul.threads;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.uern.wellisonraul.drogaria.dao.ExecucaoDAO;
import br.uern.wellisonraul.drogaria.dao.XlogDAO;
import br.uern.wellisonraul.drogaria.dominio.Execucao;
import br.uern.wellisonraul.drogaria.dominio.Xlog;
import br.uern.wellisonraul.drogaria.mapek.Analisador;
import br.uern.wellisonraul.drogaria.mapek.Executador;
import br.uern.wellisonraul.drogaria.mapek.Monitor;
import br.uern.wellisonraul.drogaria.mapek.Planejador;

public class MonitorThread implements Runnable{
	Long inicio, ultimo = null;
	Set<String> colecaoSet = new LinkedHashSet<String>();
	List<Execucao> execucoes = null;
	String log;
	
	@Override
	public void run() {
		Monitor monitor = new Monitor();
		Analisador analisador = new Analisador();
		Planejador planejador = new Planejador();
		Executador executador = new Executador();
		try{
			while(true){
				Thread.sleep(13000);
				Set<String> colecao = monitor.retornaArquivo();
				Map<String,Double> adaptacoes = analisador.analisar(colecao);
				Map<String,Double> adaptacoes2 = planejador.planejar(adaptacoes);
				executador.executar(adaptacoes2);
			}
			
		}catch(Exception e){
			
		}
		
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
