package br.uern.wellisonraul.engine;

import java.io.File;
import java.util.List;

import br.uern.wellisonraul.drogaria.dao.ExecucaoDAO;
import br.uern.wellisonraul.drogaria.dao.XlogDAO;
import br.uern.wellisonraul.drogaria.dominio.Execucao;
import br.uern.wellisonraul.drogaria.dominio.Xlog;
import br.uern.wellisonraul.threads.ExecucaoThread;
import br.uern.wellisonraul.threads.MonitorThread;

public class ProdutoEngine {
	
	public static void main(String[] args) throws Exception{
		File n = new File("/home/wellisonraul/saida_teste1_prom.txt");
		n.delete();
		// TRATA QUESTÕES DO BANCO PARA O XLOG
		ProdutoEngine pe = new ProdutoEngine();
		pe.inicializacao();
		try{
			// CHAMA THREAD DE EXECUÇÃO
			ExecucaoThread execucao = new ExecucaoThread();
			Thread threadExecucao = new Thread(execucao);
			threadExecucao.start();
			
			// CHAMA THREAD DE VERIFICAÇÃO
			MonitorThread monitoramento = new MonitorThread(); 
			Thread threadMonitoramento = new Thread(monitoramento);
			threadMonitoramento.start();
			int contadora = 0;
			
			while(true){
				Thread.sleep(1000);
				contadora++;
				System.out.println("Relôgio: "+contadora);
			}
		}catch(Exception e){
			
		}
		
		
	}
	
	public void inicializacao() throws InterruptedException{
		ExecucaoDAO execucaoDAO = new ExecucaoDAO();
		List<Execucao> execucoes = null;
		execucoes = execucaoDAO.listar();
		
		Xlog xlog = new Xlog();
		XlogDAO xlogDAO = new XlogDAO();
		xlog.setCodigo((long) 1);
		
		// NÃO EXISTE NENHUMA INSTÂNCIA!
		if(!execucoes.isEmpty()){
			xlog.setInicio(execucoes.get(execucoes.size()-1).getCodigo());
			xlog.setFim((long) 0 );
			
		// O BANCO JÁ EXISTE!
		}else{ 
			xlog.setInicio((long) 0); 
			xlog.setFim((long)0);
		}
		
		xlogDAO.editar(xlog);
	}
}
