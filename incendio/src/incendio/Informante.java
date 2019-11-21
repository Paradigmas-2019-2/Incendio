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

public class Informante extends Agent{
	public String localIncendio;
	public AID[] agentesBombeiro;
	
	private MessageTemplate mt; // The template to receive replies
	protected void setup() {
//		addBehaviour(new InformacaoIncendio());
		addBehaviour(new EnviaInformacao());
		
	}
	
	//Espera informação do incendio
	private class InformacaoIncendio extends CyclicBehaviour{
		public void action() {
			
			ACLMessage fogo = receive();
			
			if(fogo !=null) {
				localIncendio = fogo.getContent();
			}
			else {
				block();
			}
			
		}
		
	}
	
//	private class  EnviaInform extends OneShotBehaviour{
//		public void action() {
//			ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
//			
//			mensagem.setContent("blalalal");
//			mensagem.addReceiver(new AID("bombeiro",AID.ISLOCALNAME));
//			myAgent.send(mensagem);
//		}
//	}
	private class EnviaInformacao extends CyclicBehaviour{
		private int step = 0;
		private MessageTemplate mt; // The template to receive replies

		public void action() {

			switch (step) {
			case 0:
				// Send the purchase order to the seller that provided the best offer
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(new AID("bombeiro",AID.ISLOCALNAME));
				order.setContent("Qual o seu local?");
				order.setConversationId("bombeiro");
				order.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(order);
				// Prepare the template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("bombeiro"),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 1;
				break;
			case 1:      
				// Receive the purchase order reply
				
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						System.out.println("Bombeiro local: "+reply.getContent());
						myAgent.doDelete();
					}
					else {
						System.out.println("Attempt failed: requested book already sold.");
					}

				}
				else {
					block();
				}
				break;
			
			
			}
		
		}
	}
}
