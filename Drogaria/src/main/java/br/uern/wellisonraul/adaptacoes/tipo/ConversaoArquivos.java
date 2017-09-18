package br.uern.wellisonraul.adaptacoes.tipo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.uern.wellisonraul.drogaria.dominio.Execucao;
import br.uern.wellisonraul.util.*;



/* CLASSE RESPOSÁVEL POR REALIZAR A CONVERSÃO DE ARQUIVOS DO BANCO DADOS PARA A FERRAMENTA CADP OU PROM */

public class ConversaoArquivos {
	
	// MÉTODO DE CONVERSÃO DO CADP
	public File conversaoCADP(List<Execucao> entradaBDCADP) throws IOException{
		File arquivoDeConversao = new File(UtilitarioConfiguracao.CADP_ENTRADA); // CRIA O ARQUIVO
		
		// CLASSE PARA LEITURA NO ARQUIVO SEQ.
		FileWriter escreveArquivo = new FileWriter(arquivoDeConversao);
		BufferedWriter buferizadorArquivo = new BufferedWriter(escreveArquivo);
		
		/* For each para rodar o array que veio do banco 
		   Ele escreve em um arquivo.seq as propriedades vindas do banco
		   Transformando esse arquivo para entrada no CADP*/
		
		/*for(String valoresArray: entradaBDCADP){
			buferizadorArquivo.write(valoresArray.toString()); // ESCREVE PROPRIEDADE
			buferizadorArquivo.newLine(); // QUEBRA LINHA
		}*/
		
		int qtdStart = 0;
		String nome_servico_auxiliar = "null";
		// FOR PARA CRIAR OS TRACES
		
		
		for (Execucao valoresArray: entradaBDCADP) {
			if(valoresArray.getEvento().equals("start")){
				if(qtdStart==3){
					if(valoresArray.getEvento().equals("start")){
						buferizadorArquivo.write("\"INVOKE_SERVICE_"+nome_servico_auxiliar.toUpperCase()+"\""); // ESCREVE PROPRIEDADE
						buferizadorArquivo.newLine(); // QUEBRA LINHA
						buferizadorArquivo.write("\"ERROR_SERVICE_"+nome_servico_auxiliar.toUpperCase()+"\""); // ESCREVE PROPRIEDADE
						buferizadorArquivo.newLine(); // QUEBRA LINHA
						
						qtdStart = 1;
						continue;
					}
				}else{
					qtdStart++;
					if(qtdStart==3) nome_servico_auxiliar = valoresArray.getNome_servico();
					continue;
				}
			}
			
			if(valoresArray.getEvento().equals("complete")){
				buferizadorArquivo.write("\"INVOKE_SERVICE_"+valoresArray.getNome_servico().toUpperCase()+"\""); // ESCREVE PROPRIEDADE
				buferizadorArquivo.newLine(); // QUEBRA LINHA
				buferizadorArquivo.write("\"RESPONSE_SERVICE_"+valoresArray.getNome_servico().toUpperCase()+"\""); // ESCREVE PROPRIEDADE
				buferizadorArquivo.newLine(); // QUEBRA LINHA
				qtdStart = 0;
			}
		
		
		}
		buferizadorArquivo.close();
		escreveArquivo.close();
		
		return arquivoDeConversao;
	}
	
	
	public String conversaoPROM(List<Execucao> entradaPROM) throws Exception{
		File arquivoDeConversao = new File(UtilitarioConfiguracao.PROM_ENTRADA);
		
		
		// CLASSE PARA LEITURA NO ARQUIVO XES.
		FileWriter escreveArquivo = new FileWriter(arquivoDeConversao);
		BufferedWriter buferizadorArquivo = new BufferedWriter(escreveArquivo);
			
		// CRIANDO ARQUIVO .XES
		// CABEÇALHO
		buferizadorArquivo.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("<log xes.version=\"1.0\" xes.features=\"nested-attributes\" openxes.version=\"1.0RC7\">");
		buferizadorArquivo.newLine();
		
		buferizadorArquivo.write("\t<extension name=\"Lifecycle\" prefix=\"lifecycle\" uri=\"http://www.xes-standard.org/lifecycle.xesext\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t<extension name=\"Organizational\" prefix=\"org\" uri=\"http://www.xes-standard.org/org.xesext\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t<extension name=\"Time\" prefix=\"time\" uri=\"http://www.xes-standard.org/time.xesext\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t<extension name=\"Concept\" prefix=\"concept\" uri=\"http://www.xes-standard.org/concept.xesext\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t<extension name=\"Semantic\" prefix=\"semantic\" uri=\"http://www.xes-standard.org/semantic.xesext\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.newLine();
		
		buferizadorArquivo.write("\t<global scope=\"trace\">");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t\t<string key=\"concept:name\" value=\"UNKNOWN\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t</global>");
		buferizadorArquivo.newLine();
		
		
		buferizadorArquivo.write("\t<global scope=\"event\">");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t\t<date key=\"time:timestamp\" value=\"1970-01-01T00:00:00\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t\t<string key=\"lifecycle:transition\" value=\"UNKNOWN\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t\t<string key=\"concept:name\" value=\"UNKNOWN\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t</global>");
		buferizadorArquivo.newLine();
		
		buferizadorArquivo.write("\t<classifier name=\"classifier\" keys=\"&apos;concept:name time:timestamp&apos;\"/>");
		buferizadorArquivo.newLine();
		buferizadorArquivo.write("\t<string key=\"concept:name\" value=\"logExecucao\"/>");
		buferizadorArquivo.newLine();
		
		// FIM DO CABECALHO
		
		
		int controle = 0;
		
		Execucao ant = null ,atual,prox = null;
		
		// FOR PARA CRIAR OS TRACES
		int n = entradaPROM.size();
		for (int i=0;i < n; i++) {
			
			// TRATAMENTOS PARA TRACES
			if(i!=0) ant = entradaPROM.get(i-1);
			else{
				controle = 0;
			}
			atual = entradaPROM.get(i);
			if(i!=entradaPROM.size()-1) prox = entradaPROM.get(i+1);
			else controle = 2;
			
			if( (controle==0) || ((atual.getNome_servico().contains("buscarFabricante")) && (ant.getNome_servico().contains("quantidadeProdutos"))) ){
				controle = 1;
				buferizadorArquivo.write("\t<trace>");
				buferizadorArquivo.newLine();
			}
			
			if(entradaPROM.get(i).getEvento().equals("start")){
				buferizadorArquivo.write("\t\t<event>");
				buferizadorArquivo.newLine();
				
				buferizadorArquivo.write("\t\t\t<string key=\"concept:name\" value=\""+atual.getNome_servico()+"\"/>");
				buferizadorArquivo.newLine();
				buferizadorArquivo.write("\t\t\t<date key=\"time:timestamp\" value=\""+atual.getTempo()+"\"/>");
				buferizadorArquivo.newLine();
				
				buferizadorArquivo.write("\t\t\t<string key=\"lifecycle:transition\" value=\""+atual.getEvento()+"\"/>");
				
				buferizadorArquivo.newLine();
				
				buferizadorArquivo.write("\t\t</event>");
				buferizadorArquivo.newLine();
			}else{
				buferizadorArquivo.write("\t\t<event>");
				buferizadorArquivo.newLine();
				
				buferizadorArquivo.write("\t\t\t<string key=\"concept:name\" value=\""+atual.getNome_servico()+"\"/>");
				buferizadorArquivo.newLine();
				buferizadorArquivo.write("\t\t\t<date key=\"time:timestamp\" value=\""+atual.getTempo()+"\"/>");
				buferizadorArquivo.newLine();
				
				buferizadorArquivo.write("\t\t\t<string key=\"lifecycle:transition\" value=\""+atual.getEvento()+"\"/>");
				buferizadorArquivo.newLine();
				
				buferizadorArquivo.write("\t\t</event>");
				buferizadorArquivo.newLine();
			}
			
			
			if((controle == 2) || atual.getNome_servico().contains("quantidadeProdutos") && prox.getNome_servico().contains("buscarFabricante")){
				buferizadorArquivo.write("\t</trace>");
				buferizadorArquivo.newLine();
			}
		}
		
		buferizadorArquivo.write("</log>");
	
		// FECHA O ARQUIVO
		buferizadorArquivo.close();
		escreveArquivo.close();
		
		String retornaCaminho = arquivoDeConversao.toString();
		
		return retornaCaminho;
	}
}
