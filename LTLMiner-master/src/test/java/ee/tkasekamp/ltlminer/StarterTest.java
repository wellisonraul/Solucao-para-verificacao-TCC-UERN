package ee.tkasekamp.ltlminer;


import java.util.Properties;
import org.processmining.plugins.ltlchecker.model.*;

import org.junit.Test;

public class StarterTest {
	
	// MODIFICADO
	@Test
	public double test(String Log, int idConsulta, String nomeServico) throws Exception {
		LTLMinerStarter starter = new LTLMinerStarter(getProps(Log, idConsulta, nomeServico));
		
		return starter.minePorcetagem();
	}

	
	/*@Test
	public void test2() throws Exception {
		Properties props = getProps();
		props.setProperty("outputFormat", "text");
		props.setProperty("outputPath", "rules.txt");
		LTLMinerStarter starter = new LTLMinerStarter(props);
		starter.mine();
		assertTrue(new File("rules.txt").exists());
	}

	@Test
	public void test3() throws Exception {
		Properties props = getProps();
		String queries = "\"[](( (?x{A,B})  ->  <>(?y{E,A})))\"; \"<>(?x{C,sad})\"";
		props.setProperty("queries", queries);
		props.setProperty("considerEventTypes", "false");
		props.setProperty("minSupport", "0.0");
		LTLMinerStarter starter = new LTLMinerStarter(props);
		starter.mine();
	}*/

	private Properties getProps(String caminhoLog, int idConsulta, String nomeServico) {
		Properties props = new Properties();
		props.setProperty("considerEventTypes", "true");
		props.setProperty("logPath", caminhoLog);
		props.setProperty("minSupport", "0.1");
		props.setProperty("outputFormat", "console");
		props.setProperty("outputPath", "/home/wellisonraul/saidaPROM.txt");
		String queries = "";
		if (idConsulta==1) queries = "\"[](( (?x)  ->  <>(?y)))\"; \"<>(?x)\"";
		if (idConsulta==2) queries = "\"<>(?x{"+nomeServico+"})\"";
		if (idConsulta==3) queries = "Consulta2";
		props.setProperty("queries", queries);
		return props;
	}

}
