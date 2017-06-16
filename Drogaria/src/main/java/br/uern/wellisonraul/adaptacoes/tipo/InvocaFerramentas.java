package br.uern.wellisonraul.adaptacoes.tipo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ee.tkasekamp.ltlminer.StarterTest;

public class InvocaFerramentas {
	
	public void chamarCADP(File arquivo) throws IOException{		
		// COMANDO PARA EXECUTAR O TERMINAL
		Runtime r = Runtime.getRuntime();
		System.out.println();
		Process p = r.exec(new String[]{"/bin/bash", "-c", "cd /home/wellisonraul/cadp/com && ./seq.open "+arquivo+" evaluator -verbose -bfs -diag evuluator.bcg ./M_A1.mcl"});
		
		// Impressão no arquivo.txt
		saidaCADP(p);
		
	}
	
	public void saidaCADP(Process p) throws IOException{
		// LEITOR DO RETORNO DO DASH
		BufferedReader br = null;
		
		// ARQUIVO PARA LEITURA DA RESPOSTA
	    final InputStream is = p.getInputStream();
		final InputStreamReader isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
			    
		// CRIA O ARQUIVO
		File saida = new File( "/home/wellisonraul/saida.txt" );
			    
		//verifica se o arquivo ou diretório existe
		boolean existe = saida.exists();
			    
		if(!existe) saida.createNewFile();
			    
		//construtor que recebe o objeto do tipo arquivo
		FileWriter fw = new FileWriter( saida);
			   
		//construtor recebe como argumento o objeto do tipo FileWriter
		BufferedWriter bw = new BufferedWriter( fw );
			    
		String resultado;
		while((resultado = br.readLine()) != null) {
			if(resultado.contains("FALSE") || resultado.contains("TRUE")){
				bw.write(resultado);
				bw.newLine();
			}
		}    
		
		//fecha os recursos
		bw.close();
		fw.close();
	}
	
	// MODIFICAÇÃO
	public double invocaPROM(String Log, int idConsulta, String nomeServico) throws Exception{
		StarterTest starter = new StarterTest();
		return starter.test(Log,idConsulta,nomeServico);
	}
}
