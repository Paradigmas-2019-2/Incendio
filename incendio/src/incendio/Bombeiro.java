package incendio;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;



public class Bombeiro extends Agent {
	
	@Override
	protected void setup() {
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
		
		//adcionando comportamento EsperaChamada
		addBehaviour(new EsperaChamada());
		
		

	}
	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message
		System.out.println("trabalho do "+getAID().getName()+" acabou.");
	}
	
	//comportamento que espera a mensagem do informante
	private class EsperaChamada extends CyclicBehaviour{
		@Override
		public void action() {
			
			//recebe mensagem do informante
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				System.out.println("mensagem: "+msg.getContent());
				//doDelete();
			}
			else {
				block();
			}
			
		}
		
	}
}
