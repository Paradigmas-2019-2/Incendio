package incendio;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.ClassSelectionDialog.ClassesTableModel;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;


public class Bombeiro extends Agent {
	
	private int meuLocal; 
	@Override
	protected void setup() {
		Random aux = new Random();
		// gerar local do posto de bombeiro
		meuLocal = (int)(aux.nextInt(45)+1);

		//Registrando
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd =new ServiceDescription();
		sd.setType("bombeiro");
		sd.setName("Jade-bombeiro");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		//adicionando comportamento EsperaChamada		
//		addBehaviour(new EsperaChamada());
		addBehaviour(new EnviaLocal());
		

	}
	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("trabalho do "+getAID().getName()+" acabou.");
	}

	
	private class EnviaLocal extends CyclicBehaviour{
		@Override
		public void action() {
			// Recebe mensagem pedindo local do bombeiro
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				ACLMessage reply = msg.createReply();
				String auxiliar = String.valueOf(meuLocal);
				System.out.println(msg.getContent());
				
				if (auxiliar != null) {
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println("Enviando local");
					reply.setContent(auxiliar);
				}
				else {
					// The requested book has been sold to another buyer in the meanwhile .
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("Erro no local");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
			
		}
			
	}
	
}
