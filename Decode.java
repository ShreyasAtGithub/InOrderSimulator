
public class Decode extends InOrderSimulator {
	
	public void performDecode(){

		if(actualDecode != null){
			if(stalled){
				nextDecode = null;
				if(checkIfStalled(actualDecode))
					 return;							
				stalled = false;
		
				if(actualDecode.secReg != -1){
					actualDecode.secondReg = register[actualDecode.secReg];
					registerValid[actualDecode.secReg] = false;
					registerInvalid[actualDecode.secReg]= actualDecode.address;
				}
				if(actualDecode.thReg != -1 ){
					actualDecode.thirdReg = register[actualDecode.thReg];
					registerValid[actualDecode.thReg] = false;
					registerInvalid[actualDecode.thReg] = actualDecode.address;
				}
				if(actualDecode.destination != -1 ){
					registerValid[actualDecode.destination] = false;
					registerInvalid[actualDecode.destination] = actualDecode.address;
				}
			}
			
		 else {
				nextDecode = actualDecode;
				actualDecode = nextFetch;
				if(checkIfStalled(actualDecode)){
					stalled = true;
				}
				else{
					stalled=false;
					if(actualDecode.secReg != -1){
						actualDecode.secondReg = register[actualDecode.secReg];
						registerValid[actualDecode.secReg] = false;
						registerInvalid[actualDecode.secReg] = actualDecode.address;
				}
					
				if(actualDecode.thReg != -1 ){
					actualDecode.thirdReg = register[actualDecode.thReg];
					registerValid[actualDecode.thReg] = false;
					registerInvalid[actualDecode.thReg] = actualDecode.address;
				}

				if(actualDecode.destination != -1 ){
					registerValid[actualDecode.destination] = false;
					registerInvalid[actualDecode.destination] = actualDecode.address;
				}
			   }
			}
		}
		
	   else {
			 nextDecode = actualDecode;
			 actualDecode = nextFetch;
			  if(actualDecode != null){
				if(checkIfStalled(actualDecode)){
					stalled = true;
				}
				  else{
					stalled = false;
					if(actualDecode.secReg != -1){
						actualDecode.secondReg = register[actualDecode.secReg];
						registerValid[actualDecode.secReg] = false;
						registerInvalid[actualDecode.secReg] = actualDecode.address;
					}
					
					if(actualDecode.thReg != -1 ){
						actualDecode.thirdReg = register[actualDecode.thReg];
						registerValid[actualDecode.thReg] = false;
						registerInvalid[actualDecode.thReg] = actualDecode.address;
					}
					
					if(actualDecode.destination != -1 ){
						registerValid[actualDecode.destination] = false;
						registerInvalid[actualDecode.destination] = actualDecode.address;
					}
				 }
			  }	
	    	}
	}
	
	public boolean checkIfStalled(Instruction inDecode){
		if(inDecode==null )
				return false;
		if(inDecode.destination != -1 && !registerValid[inDecode.destination] ){
				return true;
		}
		if(inDecode.secReg != -1 && !registerValid[inDecode.secReg]){
				return true;
		}
		if(inDecode.thReg != -1 && !registerValid[inDecode.thReg]){
				return true;
		}
		return false;
	}
}
