package incendio;

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
	protected void setup() {
		addBehaviour(new Comunicacao());
		
	}
	
	private class Comunicacao extends CyclicBehaviour{
		private int step = 0;
		public void action() {
			switch (step) {
			case 0:
				System.out.println("Iniciando comunicação com o Informante(incendio - Informante)....");
				try { Thread.sleep (500); } catch (InterruptedException ex) {}
				ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
				mensagem.setContent("Começou tudo");
				mensagem.addReceiver(new AID("info",AID.ISLOCALNAME));
				myAgent.send(mensagem);
				step = 1;
				
				break;

			case 1:
				
				ACLMessage mensagemBombeiro = receive();
				if(mensagemBombeiro != null) {
					System.out.println("Bombeiro->"+ mensagemBombeiro.getContent());
					System.out.println("FIMM");
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
