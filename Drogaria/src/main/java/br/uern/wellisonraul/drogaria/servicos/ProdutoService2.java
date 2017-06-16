package br.uern.wellisonraul.drogaria.servicos;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.Gson;

import br.uern.wellisonraul.drogaria.dao.ProdutoDAO;
import br.uern.wellisonraul.drogaria.dominio.Produto;

@Path("produto2")
public class ProdutoService2 {
	@GET
	@Path("/buscar/{id}")
	public String buscarProduto(@PathParam("id") Long id){
		List<Produto> produtos;
		// CLASSE PARA CONSULTA	
		ProdutoDAO produtoDAO = new ProdutoDAO();
		// CLASSE PARA GERAR JSON
		Gson gson = new Gson();
		// CLASSE DO PRODUTO
		
		// BUSCANDO UM PRODUTO COM ID RECEBIDO
		produtos = produtoDAO.listarChaveEstrageira("fabricante.codigo",id);
		// TRANSFORMANDO DE CLASSE PARA JSON
		String json = gson.toJson(produtos);
		// RETORNANDO
		return json;
	}
	
	@POST
	public String quantidadeProdutos(String json){
		Gson gson = new Gson();
		List<Produto> produtos;
		int qtdProdutos = 0;
		Produto[] vetor = gson.fromJson(json, Produto[].class);
		String qtd = "";
		// Tava com um erro.
		try{
			produtos = Arrays.asList(vetor);
			
			for(Produto p: produtos){
				qtdProdutos += p.getQuantidade();
			}
			
			qtd = "A quantidade total de produtos que a empresa dispoẽ para drogaria é de: "+qtdProdutos;
		}catch(Exception e){
			System.out.println(e);
		}
	
		
		
		return qtd;
	}
}
