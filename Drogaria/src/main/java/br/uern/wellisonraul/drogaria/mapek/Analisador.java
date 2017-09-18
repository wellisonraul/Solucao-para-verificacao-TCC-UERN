package br.uern.wellisonraul.drogaria.mapek;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.uern.wellisonraul.adaptacoes.tipo.InvocaFerramentas;
import br.uern.wellisonraul.util.UtilitarioConfiguracao;

public class Analisador {
	InvocaFerramentas ife = new InvocaFerramentas();

	public Map<String,Double> analisar(Set<String> colecaoSet) throws Exception{
		Map<String,Double> adaptacoes = new LinkedHashMap<String,Double>();
		
		System.out.println("A lista de serviços é: ");
		
		for (String string : colecaoSet) {
			System.out.println("ServiçosSet: "+string);
		}
		
		if((UtilitarioConfiguracao.FERRAMENTA)==0){
			int idConsulta = 2;
			for (String nomeServico : colecaoSet) {
				double resultado = 0;
				resultado = ife.invocaPROM(UtilitarioConfiguracao.PROM_ENTRADA, idConsulta, nomeServico);
				adaptacoes.put(nomeServico, resultado);
				System.out.println("O serviço " + nomeServico + " tem o valor:" + resultado);
			}
			
		}else if((UtilitarioConfiguracao.FERRAMENTA)==1){
			
			// TRATAMENTO PARA O CADP FUNCIONAR PARECIDO COM O PROM
			Map<String, Integer> mapaNomes = new HashMap<String, Integer>();
			
			try {
			      FileReader arq = new FileReader(UtilitarioConfiguracao.CADP_ENTRADA);
			      BufferedReader lerArq = new BufferedReader(arq);
			 
			      String linha = lerArq.readLine();
			      
			      while (linha != null) {
			        if(linha.contains("INVOKE_SERVICE_")){
			        	String sub_string = linha.substring(16, linha.length()-1);
			        	if(mapaNomes.containsKey(sub_string)){
			        		int quantidade = mapaNomes.get(sub_string);
			        		mapaNomes.put(sub_string, quantidade++);
			        	}else{
			        		mapaNomes.put(sub_string, 1);
			        	}
			        }
			        linha = lerArq.readLine(); // lê da segunda até a última linha
			      }
			 
			      arq.close();
			    } catch (IOException e) {
			        System.err.printf("Erro na abertura do arquivo: %s.\n",
			          e.getMessage());
			    }
			
			for (String nomeServico : colecaoSet) {
				
				// ESCREVE O ARQUIVO
				File arquivoDeConversao = new File(UtilitarioConfiguracao.MCL); // CRIA O ARQUIVO
				arquivoDeConversao.delete();
				
				// CLASSE PARA CRIAÇÃO DO ARQUIVO MCL
				FileWriter escreveArquivo = new FileWriter(arquivoDeConversao);
				BufferedWriter buferizadorArquivo = new BufferedWriter(escreveArquivo);
				
				/*if(mapaNomes.containsKey(nomeServico.toUpperCase())){
					System.out.println("O serviço "+nomeServico.toUpperCase()+" contém: "+mapaNomes.get(nomeServico.toUpperCase()));
				}*/
				
				/* 0 - 9 nenhum vez*/
				/* 10 - 19 máximo 1 */
				/* 11-20 - no máximo */
				
				int qtdMacro = 0, qtdVezes = 0;
				double qtdMinimo = 0;
				
				qtdMacro = mapaNomes.get(nomeServico.toUpperCase());	
				
				qtdMinimo = qtdMacro * 0.9;
				qtdVezes = (int) Math.ceil(qtdMinimo)-1;
				qtdVezes = qtdMacro - qtdVezes;
				
				
				
				// TEM UM ERRO AQUI
				
				buferizadorArquivo.write("macro M_A1 (P) = [");
				for(int i=0; i<qtdVezes; i++){
					// TEM APENAS UM, NÃO CABE PONTO
					if(1==qtdVezes){
						buferizadorArquivo.write("(not P)*. P");
					}else{
						// SE NÃO FOR O ÚLTIMO NOT P.
						if((qtdVezes-1)!=i){
							buferizadorArquivo.write("(not P)*. P. ");
						// SE FOR O ÚLTIMO SEM PONTO
						}else{
							buferizadorArquivo.write("(not P)*. P");
						}
					}
				}
				buferizadorArquivo.write("] false end_macro M_A1('ERROR_SERVICE_"+nomeServico.toUpperCase()+"')");
				
				buferizadorArquivo.close();
				escreveArquivo.close();
				
				
				// CHAMA O TESTE COM O MCL MODIFICADO
				boolean resultado = ife.chamarCADP(UtilitarioConfiguracao.CADP_ENTRADA);
				System.out.println("O serviço " + nomeServico + " tem o valor:" + resultado);
				if(resultado==false) adaptacoes.put(nomeServico, 0.8);
				else adaptacoes.put(nomeServico, 1.0);
				
		}
	}
		return adaptacoes;
}
}
	
