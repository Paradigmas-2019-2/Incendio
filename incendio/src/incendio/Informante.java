package incendio;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Informante extends Agent{

	protected void setup() {
		addBehaviour(new EnviaInformacao());
	}
	
	private class  EnviaInformacao extends OneShotBehaviour{
		public void action() {
			ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
			
			mensagem.setContent("FOGO FOGO");
			mensagem.addReceiver(new AID("bombeiro",AID.ISLOCALNAME));
			myAgent.send(mensagem);
		}
	}
}
