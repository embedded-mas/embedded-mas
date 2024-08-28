package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;

import embedded.mas.exception.PerceivingException;
import jason.architecture.AgArch;
import jason.asSyntax.Literal;
import static jason.asSyntax.ASSyntax.createLiteral;


public class DefaultEmbeddedAgArch extends AgArch{


	protected Collection <DefaultDevice> devices = null;


	public DefaultEmbeddedAgArch() {		
		super();

	}

	@Override
	public Collection<Literal> perceive() {
		Collection<Literal> p = super.perceive(); //get the default perceptions
		Collection<Literal> sensorData = null;
		if(devices!=null)
			sensorData = updateSensor(); //get the sensor data	
		
		if(p!=null&&sensorData!=null) //attach the sensor data in the default perception list
			p.addAll(sensorData);
		else
			if(sensorData!=null)
				p = sensorData;
			else
				 return null;
				
		return p;		
	}


	public void setDevices(Collection<DefaultDevice> devices) {
		this.devices = devices;
	}


	public Collection<DefaultDevice> getDevices(){
		return this.devices;

	}


	private final Collection<Literal> updateSensor() {
		/* Same comment as in EmbeddeAgent.checkSensor
		 * The architecture requres a list of devices to handle the perceptions. 
		   In some point after the agent creation, an architecture other than DefaultEmbeddedAgArch is set and the list of sensor is lost.
		   This method update the list of devices if it is null.
		   TODO: improve this */
		ArrayList<Literal> percepts = new ArrayList<Literal>();
		synchronized (this.devices) {

			if(this.devices==null) return null;
			//*******************
			
			for(IDevice s:this.devices) { //for each sensor
				try {
					Collection<Literal> p = s.getPercepts();
					if(p!=null) {
						for(Literal l:p) {
							l.addAnnot(createLiteral("device", s.getId()));
						}

						percepts.addAll(p);//get all the sensor data
					}
				} catch (PerceivingException e) {} //if it fails, do nothing 			
			}
		}
		if(percepts.size()==0) return null;
		return percepts;
	}

}
