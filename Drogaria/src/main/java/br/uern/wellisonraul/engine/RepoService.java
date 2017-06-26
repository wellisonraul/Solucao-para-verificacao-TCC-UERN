package br.uern.wellisonraul.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import br.uern.wellisonraul.drogaria.dao.ExecucaoDAO;
import br.uern.wellisonraul.drogaria.dao.ServicoDAO;
import br.uern.wellisonraul.drogaria.dominio.Execucao;
import br.uern.wellisonraul.drogaria.dominio.Fabricante;
import br.uern.wellisonraul.drogaria.dominio.Produto;
import br.uern.wellisonraul.drogaria.dominio.Servico;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class RepoService implements Serializable{
	private Servico servico;
	private List<Servico> servicos;
	private LineNumberReader lineRead;
	
	public Servico getServico() {
		return servico;
	}
	
	public void setServico(Servico servico) {
		this.servico = servico;
	}
	
	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}
	
	public List<Servico> getServicos() {
		return servicos;
	}
	
	public String chamarService(){
		List<Produto> produtos;
		Gson gson = new Gson();
		
		Fabricante fabricante = new Fabricante();
		String json = "";
		
		try{
			json = buscarFabricante("1");
			fabricante = gson.fromJson(json, Fabricante.class);
			fabricante.getCodigo();
		}catch(Exception e){
			System.out.println("O serviço buscaFabricante está indisponível!");
			System.out.println("Administrador, resolva o problema");
			System.out.println("Composição de serviços encerrada inesperadamente");
			System.out.println("Erro: "+e);
		}
		
		
		try{
			json = buscarProduto(fabricante.getCodigo());
			Produto[] vetor = gson.fromJson(json, Produto[].class);
			produtos= Arrays.asList(vetor);
			String jsonEnvio = gson.toJson(produtos);
			json = quantidadeProdutos(jsonEnvio);
			
		}catch(Exception e){
			System.out.println("O serviço buscaProduto está indisponível!");
			System.out.println("Administrador, resolva o problema");
			System.out.println("Composição de serviços encerrada inesperadamente");
			System.out.println("Erro: "+e);
		}
		
		return json;
	}
	
	// BUSCA UM FABRICANTE PELO ID
	public String buscarFabricante(String id){
		String json = null;
		return tratarRequisições(id,"buscarFabricante","GET",json);
	}
	
	// LISTA TODOS OS PRODUTOS DE UMA DETERMINA FABRICANTE
	public String buscarProduto(Long id){
		String ide = id.toString();
		String json = "";
		return tratarRequisições(ide, "buscarProduto","GET",json);
	}
	
	// QUANTIDADE DE PRODUTOS DE UM DETERMINADA FABRICANTE NA DROGARIA
	public String quantidadeProdutos(String j){
		String retorno = tratarRequisições("","quantidadeProdutos","POST",j);
		if(retorno.equals("")) {
			System.out.println("O serviço quantidadeProdutos está indisponível!");
			System.out.println("Administrador, resolva o problema");
			System.out.println("Composição de serviços encerrada inesperadamente");
			
		}
		return retorno;
	}
	
	public String tratarRequisições(String id,String metodo, String protocolo, String json){
		Response resposta = null;
		int contador;
		Client cliente = ClientBuilder.newClient();
		ServicoDAO servicoDAO = new ServicoDAO();
		ExecucaoDAO execucaoDAO = new ExecucaoDAO();
		Execucao execucao = new Execucao();
		Date data = new Date();
		SimpleDateFormat formatadorData = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatadorHora = new SimpleDateFormat("HH:mm:ss");
		StringBuilder stringBuilder = new StringBuilder();
		String tempo = "";
		
		
		try {
			
			
			// TRATAMENTO DE CONSULTA AO BANCO DIRETAMENTO | CRIAR SERVIÇO.
			servicos = servicoDAO.listarPeloTipo(metodo,"posicao");
			
			// RODA OS SERVIÇOS
			for(Servico s: servicos){
					// CRIA O CLIENTE E O CONT
					contador = 0;
					
					// VARIÁVEL DO CLIENTE HTTP
					WebTarget caminho;
					
					// GERADOR DE ERROS
					Random gerador = new Random();
					// MODIFICAÇÕES
					int r = gerador.nextInt(10);
					
					
					if(r>=0 && r<=4){
						caminho = cliente.target(s.getUri()+"vaidarerro").path(id);
					}else{
						caminho = cliente.target(s.getUri()).path(id);
					}
					
					
					
					// TESTA SE O MÉTODO VAI SER GET OU POST
					if(protocolo.equals("GET"))resposta = caminho.request().get(); // MÉTODO GET
					else if (protocolo.equals("POST")) resposta = caminho.request().post(Entity.json(json)); // MÉTODO POST
					
					// BANCO DE DADOS
					stringBuilder = new StringBuilder();
					
					stringBuilder.append(formatadorData.format(data));
					stringBuilder.append("T"+formatadorHora.format(data));
					tempo =  stringBuilder.toString();
					
					execucao.setEvento("start");
					execucao.setNome_servico(s.getNome()+s.getCodigo());
					execucao.setTempo(tempo);
					execucaoDAO.salvar(execucao);
					
					// INFORMA A EXECUÇÃO
					System.out.println("Inicializando serviço:"+s.getNome());
					System.out.println("Tentativa número 1");
					
					// CASO DÊ ERRADO TENTE MAIS DUAS VEZES.
					while(contador!=2 && resposta.getStatus()==404){
						
						// CRIAR OU REVOGAR ERROS EM TODAS AS INSTÂNCIAS. 
						r = gerador.nextInt(10);
						if(r>=0 && r<=4){
							caminho = cliente.target(s.getUri()+"vaidarerro").path(id);
						}else{
							caminho = cliente.target(s.getUri()).path(id);
						}
						
						// TRATA O TIPO DE PROTOCOLO
						if(protocolo.equals("GET"))resposta = caminho.request().get(); // MÉTODO GET
						else if (protocolo.equals("POST")) resposta = caminho.request().post(Entity.json(json)); // MÉTODO POST
						contador++;
						
						// INFORMA AO USUÁRIO
						System.out.println("Tentativa "+(contador+1));
						
						// INSERI TENTATIVA NO BANCO
						stringBuilder = new StringBuilder();
						
						stringBuilder.append(formatadorData.format(data));
						stringBuilder.append("T"+formatadorHora.format(data));
						tempo =  stringBuilder.toString();
						
						execucao.setEvento("start");
						execucao.setNome_servico(s.getNome()+s.getCodigo());
						execucao.setTempo(tempo);
						execucaoDAO.salvar(execucao);
					}
					
					// SERVIDOR RESPONDE CORRETAMENTE
					if(resposta.getStatus()==200){
						
						if(s.getNome().equals("buscarFabricante")){
							try {
								
								File arquivoDeConversao = new File("/home/wellisonraul/saida_teste1_prom.txt"); // CRIA O ARQUIVO
								FileWriter escreveArquivo = null;
								BufferedWriter buferizadorArquivo = null;
								try {
									escreveArquivo = new FileWriter(arquivoDeConversao,true);
									buferizadorArquivo = new BufferedWriter(escreveArquivo);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								// TESTE 1
								int resultado = (int) s.getTempo_execucao();
								buferizadorArquivo.write(String.valueOf(resultado));
								buferizadorArquivo.newLine();
								
								buferizadorArquivo.close();
								escreveArquivo.close();
								/*
								long tamanhoArquivo = arquivoDeConversao.length();
								int numLinhas = 0;
								try {
									// PARAMÊTROS PARA LEITURA NECESSÁRIOS
									FileInputStream fs = new FileInputStream(arquivoDeConversao);
									DataInputStream in = new DataInputStream(fs);	
									lineRead = new LineNumberReader(new InputStreamReader(in));
									lineRead.skip(tamanhoArquivo);
									numLinhas = lineRead.getLineNumber() + 1;
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								if(numLinhas==100){
									System.out.println("\n\n\n\nCOMPLETO!\n\n\n\n");
								}else{
									System.out.println("Número de linhas igual a: "+numLinhas);
								}*/
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						}
						
						stringBuilder = new StringBuilder();
						stringBuilder.append(formatadorData.format(data));
						stringBuilder.append("T"+formatadorHora.format(data));
						tempo =  stringBuilder.toString();
						
						execucao.setEvento("complete");
						execucao.setNome_servico(s.getNome()+s.getCodigo());
						execucao.setTempo(tempo);
						execucaoDAO.salvar(execucao);
						
						System.out.println("Serviço "+s.getNome()+" funcionando na porta :"+s.getPosicao());
						try{
							json = resposta.readEntity(String.class);
						}catch(Exception e){
							System.out.println(e);
						}
						break;
					}
					
					// URI INVALIDO
					if(resposta.getStatus()==404){
						if(s.getNome().equals("buscarFabricante")){
							try {
								File arquivoDeConversao = new File("/home/wellisonraul/saida_teste1_prom.txt"); // CRIA O ARQUIVO
								FileWriter escreveArquivo = null;
								BufferedWriter buferizadorArquivo = null;
								try {
									escreveArquivo = new FileWriter(arquivoDeConversao,true);
									buferizadorArquivo = new BufferedWriter(escreveArquivo);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								int resultado = (int) s.getTempo_execucao();
								buferizadorArquivo.write(String.valueOf(resultado));
								buferizadorArquivo.newLine();
								buferizadorArquivo.close();
								escreveArquivo.close();
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
						System.out.println("Serviço "+s.getNome()+" na posição "+(contador+1)+" não funcionando erro: "+resposta.getStatus()); // RESOLVER AQUI DEPOIS
						json = "";
						for(Servico ordernacao: servicos){
							if(ordernacao.equals(s)){
								ordernacao.setPosicao((short)servicos.size());
							}else{
								ordernacao.setPosicao((short)(ordernacao.getPosicao()-1));
							}
						}
					}
			}
 		} catch (RuntimeException erro) {
			erro.printStackTrace();
		}finally {
				SeletordeServicos ss = new SeletordeServicos();
				ss.reordenarServicos(servicos, null, null, 1);
		}
		return json;
	}
}
