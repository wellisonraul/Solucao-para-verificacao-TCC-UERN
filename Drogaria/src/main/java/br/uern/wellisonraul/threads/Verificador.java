package br.uern.wellisonraul.threads;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.uern.wellisonraul.adaptacoes.tipo.ConversaoArquivos;
import br.uern.wellisonraul.adaptacoes.tipo.InvocaFerramentas;
import br.uern.wellisonraul.drogaria.dao.ExecucaoDAO;
import br.uern.wellisonraul.drogaria.dao.ServicoDAO;
import br.uern.wellisonraul.drogaria.dominio.Execucao;
import br.uern.wellisonraul.drogaria.dominio.Servico;
import br.uern.wellisonraul.engine.SeletordeServicos;
import br.uern.wellisonraul.util.UtilitarioConfiguracao;

public class Verificador{
	
	// CLASSES EM COMUM DOS ARQUIVOS
	ConversaoArquivos ca = new ConversaoArquivos();
	InvocaFerramentas ife = new InvocaFerramentas();
		
	// MÉTODO RESPOSÁVEL POR PESQUISAR DO INICIO DO LOG AO FINAL
	public List<Execucao> ConsultaInstâncias(long inicio_banco, long final_banco){
		
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
	
	// MÉTODO RESPOSÁVEL PRO CHAMAR O PROM. 
	public void VerificadorFerramentas(List<Execucao> execucoes) throws Exception{
		// PARA CONTER TODOS OS SERVIÇOS
		// LIKEDHASHSET POIS GUARDA A SEQUÊNCIA DE ENTRDA.
		Set<String> colecaoSet = new LinkedHashSet<String>();
		
		// INSERI EM UM SET
		// NÃO PERMITE DUPLICATAS
		for (Execucao listadeServicos : execucoes) {
			colecaoSet.add(listadeServicos.getNome_servico());
		}	
		
		if(UtilitarioConfiguracao.FERRAMENTA==0){
			String log = ca.conversaoPROM(execucoes);
			int idConsulta = 2;
			for (String nomeServico : colecaoSet) {
				double resultado = ife.invocaPROM(log, idConsulta, nomeServico);
				System.out.println("O serviço " + nomeServico + " tem o valor:" + resultado);
				
				
				// PODE ENCONTRAR NO BANCO
				String servico = nomeServico.substring(0, nomeServico.length()-1);
				String codigo  = nomeServico.substring(nomeServico.length()-1, nomeServico.length());
				
				// Transofrmando o id do processo!
				long cod = Integer.parseInt(codigo);
				
				// BUSCANDO O IDA ACIMA
				ServicoDAO servicoDAO = new ServicoDAO();
				Servico s = new Servico();
				s = servicoDAO.buscar(cod);
				
				// TESTE PARA SABER SE REALIZA OU NÃO ADAPTAÇÃO
				// 0.8 É A CONFIBILIDADE ESPERADA PELO SERVIÇO
				if((resultado < 0.9)){
					s.setConfiabilidade_real(resultado);
					servicoDAO.editar(s);
					
					System.out.println("Confiabilidade real: "+resultado);
					SeletordeServicos ss = new SeletordeServicos();
					
					// REORDENA OS SERVIÇOS
					// TRATAR ERRO DO BANCO 
					ss.reordenarServicos(null, servico,s.getPosicao(), 2);
					
					System.out.println("Adaptação realizada!");
				}else{
					System.out.println("Confiabilidade real: "+resultado);
					s.setConfiabilidade_real(resultado);
					servicoDAO.editar(s);
				}

			}
		}else{
			File arquivo = ca.conversaoCADP(execucoes);
			
			// TRATAMENTO PARA O CADP FUNCIONAR PARECIDO COM O PROM
			Map<String, Integer> mapaNomes = new HashMap<String, Integer>();
			
			try {
			      FileReader arq = new FileReader(arquivo);
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
				
				System.out.println("Nome serviço: "+nomeServico+" valor "+mapaNomes.get(nomeServico.toUpperCase()));
				// POSSO REMOVER ISSO AQUI
				if(mapaNomes.get(nomeServico.toUpperCase())==null){
					qtdMacro = 1;
					System.out.println("id:null");
				}else{
					qtdMacro = mapaNomes.get(nomeServico.toUpperCase());	
				}
				
				qtdMinimo = qtdMacro * 0.9;
				qtdVezes = (int) Math.ceil(qtdMinimo)-1;
				qtdVezes = qtdMacro - qtdVezes;
				
				System.out.println("Variável quantidade de vezes: "+qtdVezes);
				
				// TEM UM ERRO AQUI
				buferizadorArquivo.write("macro M_A1 (P) = [");
				for(int i=0; i<qtdVezes; i++){
					buferizadorArquivo.write("(not P)*. P");
					if((qtdVezes-1)!=i) buferizadorArquivo.write("");
				}
				buferizadorArquivo.write("] false end_macro M_A1('ERROR_SERVICE_"+nomeServico.toUpperCase()+"')");
				
				buferizadorArquivo.close();
				escreveArquivo.close();
				
				// CHAMA O TESTE COM O MCL MODIFICADO
				boolean resultado = ife.chamarCADP(arquivo.toString());
				System.out.println("O serviço " + nomeServico + " tem o valor:" + resultado);
				
				
				if(resultado==false){
					// PODE ENCONTRAR NO BANCO
					String servico = nomeServico.substring(0, nomeServico.length()-1);
					String codigo  = nomeServico.substring(nomeServico.length()-1, nomeServico.length());
					
					// Transofrmando o id do processo!
					long cod = Integer.parseInt(codigo);
					
					// BUSCANDO O IDA ACIMA
					ServicoDAO servicoDAO = new ServicoDAO();
					Servico s = new Servico();
					s = servicoDAO.buscar(cod);
					
					SeletordeServicos ss = new SeletordeServicos();
					
					// REORDENA OS SERVIÇOS
					// TRATAR ERRO DO BANCO 
					ss.reordenarServicos(null, servico,s.getPosicao(), 2);
					System.out.println("Adaptação realizada no serviço "+nomeServico);
				}else{
					System.out.println("Não foi necessário realizar adaptação");
				}
			}
			
		System.out.println("Encerrou os testes!\n\n\n\n\n\n\n\n\n\n");
		}
	}
}
