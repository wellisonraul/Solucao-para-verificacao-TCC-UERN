package br.uern.wellisonraul.threads;

import br.uern.wellisonraul.engine.RepoService;

public class ExecucaoThread implements Runnable{

	// THREAD RESPOSÁVEL PELA EXECUÇÃO
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// INVOCAÇÃO DE SERVIÇOS
		while(true){
			// CRIA A INSTÂNCIA DOS SERVIÇOS E OS CHAMA. 
			RepoService reposervice = new RepoService();
			System.out.println(reposervice.chamarService());
		}
	}
	
}
