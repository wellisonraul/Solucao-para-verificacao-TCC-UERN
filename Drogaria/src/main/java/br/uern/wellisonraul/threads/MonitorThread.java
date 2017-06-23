package br.uern.wellisonraul.threads;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.uern.wellisonraul.drogaria.dao.ExecucaoDAO;
import br.uern.wellisonraul.drogaria.dao.XlogDAO;
import br.uern.wellisonraul.drogaria.dominio.Execucao;
import br.uern.wellisonraul.drogaria.dominio.Xlog;

public class MonitorThread implements Runnable{
	Long inicio, ultimo = null;
	Set<String> colecaoSet = new LinkedHashSet<String>();
	List<Execucao> execucoes = null;
	String log;
	
	
	/*@Override
	public void run() {
		while(true){
			try {
				// ZERA AS VARIAVEIS
				colecaoSet = null;
				execucoes = null;
				log = null;
				verificador = null;
				// ESPERA 10 SEGUNDOS
				Thread.sleep(10000);
				// INICIO DO BANCO PARA XES
				inicio = inicioBanco();
				// FINAL DO BANCO PARA XES
				ultimo = finalBanco(inicio);
				// VERIFICADOR PARA TRANSFORMAR O XES
				Verificador v = new Verificador();
				
				// TRANSFORMAR O XES
				// Consulta quais valores devem ser utilizados
				execucoes = v.ConsultaInstâncias(inicio, ultimo);
				// Cria o XES
				log = v.criaXES(execucoes);
				// Recebe os serviços que foram usados nessa composição.
				colecaoSet = v.Colecao(execucoes);
				
				verificador = new VerificadorThread(colecaoSet,log);
				Thread threadVerificador = new Thread(verificador);
				threadVerificador.start();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}*/
	
	@Override
	public void run() {
		try{
			while(true){
				Thread.sleep(13000);
				// INICIO DO BANCO PARA XES
				inicio = inicioBanco();
				// FINAL DO BANCO PARA XES
				ultimo = finalBanco(inicio);
				// VERIFICADOR PARA TRANSFORMAR O XES
				Verificador v = new Verificador();
				List <Execucao> execucoes = v.ConsultaInstâncias(inicio, ultimo);
				v.VerificadorFerramentas(execucoes);
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
