package embedded.mas.bridges.javard;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import arduino.Arduino;
import com.fazecast.jSerialComm.*;

import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IPhysicalInterface;

public class SerialReader extends Arduino implements IPhysicalInterface{

	boolean needHistory = false;
	
	private String leitura=""; //variavel sincronizada (startReader && reader)
	private String leituraBuffer = "";
 
	private Queue<String> jsonList = new LinkedList<String>(); //variavel sincronizada (makeJSON && makeBeliefs)
	private Map<String,Stack<String>> historico = new HashMap<String,Stack<String>>();
	private Map<String,String> mapPercepts = new HashMap<String,String>();
	private CopyOnWriteArrayList<String> percepts = new CopyOnWriteArrayList<String>(); //TODO: Mudar de <String> para <Literal>
	
	private String msgSpliter = "\n";
	private String lengthSpliter = "::";
	private String prefix = "==";
	private String suffix = "--";

	public SerialReader(String portDescription, int baud_rate) {
		super(portDescription, baud_rate);
		this.openConnection();
		this.startReader();
	}

	private String internalRead() {
		return this.serialReadAll();
	}

	@Override
	public String read() {
		String s = "";
		for (String belief : percepts) {
			s += belief;
			s += "/";
		}
		if(s=="")
			return null;
		return s;
	}
	
//	public ArrayList<String> read() {
//		return percepts;
//	}
	
	@Override
	public boolean write(String s) {
		try {
			serialWrite(s);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	private void startReader() {		
		new Thread() {
			@Override
			public void run() {
				while(true) {
					int i=0;
					while(comPort.bytesAvailable()==0 && i++ < 30) {try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
					synchronized (leitura){
						leitura += internalRead();	
						System.out.println("[SerialReader] leitura " + leitura);
					}
					BufferReader();
					makeJSON();
					//printTotal();
				}
			}
		}.start();
	}

	private void BufferReader() {	
		synchronized (leitura) {
			//System.out.println("#0 - LEITURA");
			//System.out.println(leitura);
			leituraBuffer += leitura;
			leitura = "";
		}
	}
	private void makeJSON() {
		String leituraSplit[];
		if(leituraBuffer!="") {
			leituraSplit = leituraBuffer.split(msgSpliter);
			if(leituraSplit.length>0) {
				if(isMASMessage(leituraSplit[leituraSplit.length-1])) 
					leituraBuffer = "";
				else
					leituraBuffer = leituraSplit[leituraSplit.length-1];

				for(int i = leituraSplit.length-1; i>-1 ;i--) { //percorre o vetor de trás para frente
					if(isMASMessage(leituraSplit[i]))
						synchronized(jsonList) {
							jsonList.add(verifyAndDecode(leituraSplit[i]));
						}
				}
				
				// print a lista de jason
				//System.out.println("\n## - Lista de JSON");
//				for(int i = 0; i<jsonList.size() ;i++) {
//						//System.out.println(jsonList.get(i));
//				}
			}
			
		}
		makeBeliefs();
	}

	private boolean isMASMessage(String msg) {
		if (msg.endsWith(suffix + (char)13) && //(char)13 é um caracter que aparece
				msg.startsWith(prefix) &&
				msg.contains(lengthSpliter)) 
			return true;
		return false;
	}

	private String verifyAndDecode(String msg) {
		String m[] = msg.split(lengthSpliter);
		m[0] = m[0].replace(prefix, "");
		m[1] = m[1].replace(suffix, "");

		int length = Integer.parseInt(m[0]);
		String message = m[1];
		message = message.replace(""+(char)13, "");

		if(length == message.length()) {
			return message;
		}

		return "Erro verificação da mensagem";
	}
	
	
	//----------------- Organiza Json------------
	
	private void makeBeliefs() {	
		synchronized (jsonList) {
			if(!jsonList.isEmpty())
				jsonToBelief(jsonList.poll());
		}
		getMapPercepts();
		internalGetPercepts();
	}
	
	// TODO: Mudar return para <Literal>
	private void jsonToBelief(String json) { 
		if(!json.equals("Erro verificação da mensagem")) {
			JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
//			System.out.println("## : ");
//			System.out.println(json);
			JsonObject jsonObject = reader.readObject(); //transformar a string em um "objeto JSON"
			
			for(String key: jsonObject.keySet()) { //iterar sobre todos os elementos do JsonObject - a variável "key" armazena cada chave do objeto json    		
				Object value = jsonObject.get(key); //obtém o valor associado à chave "key"
				String stringValue = "";
				String belief = key +"(";
				if(!(value instanceof JsonArray)) //se o valor não for um vetor (ou seja, se for uma belief com apenas um valor)
					stringValue = (String) value;
				else { //se for um vetor [v1,v2,...,vn], monta uma belief key(v1,v2,...,vn) 
					stringValue = value.toString().replace("[","").replace("]", "");	 	
				}
				belief = belief + stringValue + ")";
				
				if(historico.containsKey(key))
					historico.get(key).push(stringValue);
				else {
					historico.put(key, new Stack<String>());
					historico.get(key).push(stringValue);
				}
			}
		}
	}
	
	private void getMapPercepts() {
		for (String key : historico.keySet()) {
			if(mapPercepts.containsKey(key)) {
				mapPercepts.replace(key, historico.get(key).peek());
			}
			else {
				mapPercepts.put(key, historico.get(key).peek());
			}	
		}
		if(!needHistory)
			historico.clear();
	}
	
	private  void internalGetPercepts() {
		percepts.removeAll(percepts);
		for (String key : mapPercepts.keySet()) {
			percepts.add(key+"("+mapPercepts.get(key)+")");
		}	
	
	}
		

	public void printTotal() {
		System.out.println("\n\n#################################################################################");
		System.out.println("\n# ---------  Leitura da serial ---------");
		System.out.println(leitura);
		
		System.out.println("\n# ---------  Buffer de leitura ---------");
		System.out.println(leituraBuffer);
		
		System.out.println("\n# ---------  Lista de JSON ---------");
		System.out.println(jsonList.toString());
		
		System.out.println("\n# --------- Mapa do Histórico ---------");
		for (String key : historico.keySet()) {
            Stack<String> value = historico.get(key);
            System.out.println(key + " = " + value.toString());
		}
		System.out.println("\n # --------- MapPercepts ---------");
		for (String key : mapPercepts.keySet()) {
		 	String value = mapPercepts.get(key);
            System.out.println(key + " = " + value);
		}		
		System.out.println("\n# --------- Lista de Percepções ---------");
		for(int i = 0; i<percepts.size() ;i++) {
				System.out.println(percepts.get(i));
		}
	}

	@Override
	public void execEmbeddedAction(EmbeddedAction action) {
		// TODO implementar
		
	}
}
