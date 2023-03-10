package agents;


import java.util.Random;

import Containers.ContainerVendeur;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class AgentVendeur extends GuiAgent {
	
	protected ContainerVendeur gui;
	@Override
	protected void setup() {
		
		if(getArguments().length==1) {
			gui=(ContainerVendeur) getArguments()[0];
			gui.setAgentVendeur(this);
		}
		
		ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
		parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				DFAgentDescription agentDescription =new DFAgentDescription();
				agentDescription.setName(getAID());
				ServiceDescription serviceDescription = new ServiceDescription();
				serviceDescription.setType("Book-selling");
				serviceDescription.setName("Vente-livres");
				agentDescription.addServices(serviceDescription);
				try {
					DFService.register(myAgent, agentDescription);
				} catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
			}
		});
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage aclMessage=receive();
				if(aclMessage!=null) {
					gui.logMessage(aclMessage);
					/*switch (aclMessage.getPerformative()) {
					case ACLMessage.CFP:
						ACLMessage reply= aclMessage.createReply();
						reply.setPerformative(ACLMessage.PROPOSE);
						reply.setContent(String.valueOf(500+new Random().nextInt(1000)));
						send(reply);
						break;
                    case ACLMessage.ACCEPT_PROPOSAL:
						ACLMessage aclMessage2=aclMessage.createReply();
						aclMessage2.setPerformative(ACLMessage.AGREE);
						aclMessage2.setContent("BestPrice");
						send(aclMessage2);
						break;
					default:
						break;
					}*/
					 
				}
				
				else block();
				
			}
		});
	}
	@Override
	protected void onGuiEvent(GuiEvent event) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    protected void takeDown() {
    	
    	try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
