
public class Fetch extends InOrderSimulator{
	
	public void performFetch() {
		
		if(InOrderSimulator.stop && stalled) 
			return;
		if(stalled)
			return;
		nextFetch= actualFetch;
		if(stop) actualFetch = null;

		actualFetch=InOrderSimulator.instructions[programCounter-20000];
	
		if(instructions[programCounter-20000+1].isPresent){
				programCounter++;
		}
		else{
			if(!stop){
				programCounter++;
				stop = true;} 
		 }
	}
}
