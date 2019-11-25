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
	
	private int meuLocal, aux = 0, aux2 =0; 
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
		addBehaviour(new ComunicacaoInformante());
		addBehaviour(new ComunicacaoIncendio());
		
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
	
	
	
	private class ComunicacaoIncendio extends CyclicBehaviour{
		@Override
		public void action() {
			if(aux == 1) {
				switch(aux2){

					case 0:
						System.out.println("Averiguando o incêndio...");
						ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
						mensagem.setContent("Qual a intensidade?");
						mensagem.addReceiver(new AID("incend",AID.ISLOCALNAME));
						myAgent.send(mensagem);
						aux2 = 1;
						break;
					
					case 1:
						//Espera informação sobre a intencidade do incendio
		
						ACLMessage msg = receive();
						if(msg != null ){
							System.out.println("Intencidade do incêndio é: " + msg.getContent());
							System.out.println("Deslocando viaturas...");
							try { Thread.sleep (2000); } catch (InterruptedException ex) {}
							
							ACLMessage mensa = new ACLMessage(ACLMessage.INFORM);
							mensa.setContent("E lá vai água...");
							mensa.addReceiver(new AID("incend",AID.ISLOCALNAME));
							myAgent.send(mensa);
							
							aux = 2;
						}
						else{
							block();
						}
						break;
				}
				
			}
			else if (aux == 2) {
				
				try { Thread.sleep (20000); } catch (InterruptedException ex) {}
				doDelete();
			}
			else{
				block();
			}
		}
	}

	
	private class ComunicacaoInformante extends CyclicBehaviour{
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
					System.out.println("Enviando local...");
					reply.setContent(auxiliar);
				}
				else {
					// Outro corpo de bombeiro foi escolhido.
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("Erro no local \n");
				}
				myAgent.send(reply);
				try { Thread.sleep (500); } catch (InterruptedException ex) {}
				aux = 1;

			}
			else {
				block();
			}
			
		}
			
	}
	
}
