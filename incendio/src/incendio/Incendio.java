package incendio;

import javax.swing.JOptionPane;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Incendio extends Agent{
	private String local = "";
	
	protected void setup() { 
		
		addBehaviour(new Comunicacao());
		
	}
	
	private class Comunicacao extends CyclicBehaviour{
		private int step = 0;
		public void action() {
			switch (step) {
			case 0:
				String entrada = JOptionPane.showInputDialog("Local do incêndio: ");
				System.out.println("O incêndio iniciou em: " + entrada + "\n");
				// System.out.println("");

				local = entrada;

				System.out.println("Iniciando comunicação com o Informante(Incendio->Informante)...");
				try { Thread.sleep (500); } catch (InterruptedException ex) {}
				ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
				mensagem.setContent(local);
				mensagem.addReceiver(new AID("info",AID.ISLOCALNAME));
				myAgent.send(mensagem);
				step = 1;
				break;

			case 1:
				
				ACLMessage mensagemBombeiro = receive();
				if(mensagemBombeiro != null) {
					System.out.println("Bombeiro->"+ mensagemBombeiro.getContent()+ "\n");
					
					System.out.println("FIMM"+ "\n");
					doDelete();
				}
				else {
					block();
				}
				break;
			}
		}
	}

}
