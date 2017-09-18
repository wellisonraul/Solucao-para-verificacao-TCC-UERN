package br.uern.wellisonraul.threads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import br.uern.wellisonraul.util.UtilitarioConfiguracao;

public class Teste {

	public static void main(String[] args) throws IOException {
		
		File arquivoDeConversao = new File(UtilitarioConfiguracao.MCL); // CRIA O ARQUIVO
		arquivoDeConversao.delete();
		
		// CLASSE PARA CRIAÇÃO DO ARQUIVO MCL
		FileWriter escreveArquivo = new FileWriter(arquivoDeConversao);
		BufferedWriter buferizadorArquivo = new BufferedWriter(escreveArquivo);
		
		// TEM UM ERRO AQUI
		buferizadorArquivo.write("macro M_A1 (P) = [");
		int qtdVezes = 5;
		for(int i=0; i<qtdVezes; i++){
			if(1==qtdVezes){
				buferizadorArquivo.write("(not P)*. P");
			}else{
				if((qtdVezes-1)!=i){
					System.out.println("PRIMEIRO ?");
					buferizadorArquivo.write("(not P)*. P. ");
				}else{
					System.out.println("SEGUNDO?");
					buferizadorArquivo.write("(not P)*. P");
				}
			}
			
		}
		buferizadorArquivo.write("] false end_macro M_A1('ERROR_SERVICE_TESTE')");
		
		buferizadorArquivo.close();
		escreveArquivo.close();
		

	}

}
