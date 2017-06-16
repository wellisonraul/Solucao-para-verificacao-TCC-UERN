package br.uern.wellisonraul.drogaria.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.uern.wellisonraul.drogaria.dao.ExecucaoDAO;
import br.uern.wellisonraul.drogaria.dominio.Execucao;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ExecucaoBean implements Serializable{
	private Execucao Execucao;
	private List<Execucao> Execucoes;
	
	public Execucao getExecucao() {
		return Execucao;
	}
	
	public void setExecucao(Execucao execucao) {
		Execucao = execucao;
	}	
	public List<Execucao> getExecucoes() {
		return Execucoes;
	}

	public void setExecucoes(List<Execucao> execucoes) {
		Execucoes = execucoes;
	}

	@PostConstruct
	public void listar() {
		try {
			ExecucaoDAO execucaoDAO = new ExecucaoDAO();
			Execucoes = execucaoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os estados");
			erro.printStackTrace();
		}
	}

	public void novo() {
		Execucao = new Execucao();
	}

	public void salvar() {
		try {
			ExecucaoDAO execucaoDAO = new ExecucaoDAO();
			execucaoDAO.merge(Execucao);

			Execucao = new Execucao();
			Execucoes = execucaoDAO.listar();

			Messages.addGlobalInfo("Estado salvo com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o estado");
			erro.printStackTrace();
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			Execucao = (Execucao) evento.getComponent().getAttributes().get("execucaoselecionada");

			ExecucaoDAO execucaoDAO = new ExecucaoDAO();
			execucaoDAO.excluir(Execucao);
			
			Execucoes = execucaoDAO.listar();

			Messages.addGlobalInfo("Estado removido com sucesso");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover o estado");
			erro.printStackTrace();
		}
	}
	
	public void editar(ActionEvent evento){
		Execucao = (Execucao) evento.getComponent().getAttributes().get("ExecucaoSelecionada");
	}
	
}
