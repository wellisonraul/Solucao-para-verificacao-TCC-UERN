package br.uern.wellisonraul.threads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import br.uern.wellisonraul.engine.RepoService;
import br.uern.wellisonraul.util.UtilitarioConfiguracao;

public class ExecucaoThread implements Runnable{

	// THREAD RESPOSÁVEL PELA EXECUÇÃO
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// INVOCAÇÃO DE SERVIÇOS
		int x = 0;
		
		
		while(true){
			// CRIA A INSTÂNCIA DOS SERVIÇOS E OS CHAMA. 
			// INVOCA
			RepoService reposervice = new RepoService();
			System.out.println(reposervice.chamarService());
			System.out.println("Foram executas "+(++x)+" instâncias com sucesso!");	
			if(x==100){
				System.out.println("COMPLETOU O TESTE");
				try{
					Thread.sleep(100000);
				}catch(Exception e){
					System.out.println(e);
				}
				
			}
			
			// ESPERA 3 SEGUNDOS
			try{
				Thread.sleep(1000);
			}catch(Exception e){
				System.out.println(e);
			}
		}
	}
	
}
