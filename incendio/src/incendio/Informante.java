package incendio;

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

public class Informante extends Agent{
	public String localIncendio;
	public AID[] agentesBombeiro;
	
	private MessageTemplate mt; // The template to receive replies
	protected void setup() {
//		addBehaviour(new InformacaoIncendio());
		addBehaviour(new TrocaInformacao());
		
	}
	
	private class TrocaInformacao extends CyclicBehaviour{
		private int step = 0;
		private MessageTemplate mt; // The template to receive replies

		public void action() {

			switch (step) {
			case 0:
				// Espera a mensagem  com o local do incendio
				ACLMessage mensagemIncendio = receive();
				if(mensagemIncendio !=null) {
					System.out.print("Incendio localizado no local: ");
					localIncendio = mensagemIncendio.getContent();
					System.out.println(localIncendio);
				  step = 1;
				}
				else {
					block();	
				}
				
				break;
			
			
			case 1:
				// Envia uma mensagem perguntando o local do bombeiro
				ACLMessage msgBomb = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				msgBomb.addReceiver(new AID("bombeiro",AID.ISLOCALNAME));
				System.out.println("");
				msgBomb.setContent("Qual sua distância até aqui? ");
				System.out.println("");
				msgBomb.setConversationId("bombeiro");
				msgBomb.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(msgBomb);
				// Prepare o modelo para obter a resposta do pedido
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("bombeiro"),
						MessageTemplate.MatchInReplyTo(msgBomb.getReplyWith()));
				step = 2;
				break;
			case 2:      
				//Esperando resposta do bombeiro
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						System.out.println("Bombeiro: Estamos à " +reply.getContent()+ " KM do incêndio\n" );
						myAgent.doDelete();
					}
					else {
						System.out.println("Falha na comunicação.");
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
