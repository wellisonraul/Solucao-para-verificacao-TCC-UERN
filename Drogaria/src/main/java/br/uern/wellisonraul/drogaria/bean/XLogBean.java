package br.uern.wellisonraul.drogaria.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import br.uern.wellisonraul.drogaria.dao.XlogDAO;
import br.uern.wellisonraul.drogaria.dominio.Xlog;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class XLogBean implements Serializable{
	private Xlog Xlog;
	private List<Xlog> Xlogs;

	public Xlog getXlog() {
		return Xlog;
	}

	public void setXlog(Xlog xlog) {
		Xlog = xlog;
	}

	public List<Xlog> getXlogs() {
		return Xlogs;
	}

	public void setXlogs(List<Xlog> xlogs) {
		Xlogs = xlogs;
	}

	@PostConstruct
	public void listar() {
		try {
			XlogDAO xlogDAO = new XlogDAO();
			Xlogs = xlogDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os estados");
			erro.printStackTrace();
		}
	}

	public void novo() {
		Xlog = new Xlog();
	}

	public void salvar() {
		try {
			XlogDAO xlogDAO = new XlogDAO();
			xlogDAO.merge(Xlog);

			Xlog = new Xlog();
			Xlogs = xlogDAO.listar();

			Messages.addGlobalInfo("Estado salvo com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o estado");
			erro.printStackTrace();
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			
			XlogDAO xlogDAO = new XlogDAO();
			xlogDAO.excluir(Xlog);
			
			Xlogs = xlogDAO.listar();

			Messages.addGlobalInfo("Estado removido com sucesso");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover o estado");
			erro.printStackTrace();
		}
	}
	
	public void editar(ActionEvent evento){
		//Execucao = (Execucao) evento.getComponent().getAttributes().get("ExecucaoSelecionada");
	}
	
}
