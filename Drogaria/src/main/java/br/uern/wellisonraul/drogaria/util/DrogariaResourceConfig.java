package br.uern.wellisonraul.drogaria.util;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

// http://localhost:8080/Drogaria/rest
@ApplicationPath("rest")
public class DrogariaResourceConfig extends ResourceConfig{
	// Construtor resposável por chamar o pacote de serviços
	public DrogariaResourceConfig(){
		packages("br.uern.wellisonraul.drogaria.servicos");
	}
}
