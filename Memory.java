
public class Memory extends InOrderSimulator {
	
	public void performMemory(){
		

		nextMemory = actualMemory;
		actualMemory = nextExecute;
		
		if(actualMemory != null && actualMemory.instrType != null){
			
			switch (actualMemory.instrType) {

				case LOAD: 
				case STORE: 
							if(actualMemory.instrType == InstructionType.LOAD){
								if(actualMemory.literal == -1){
									actualMemory.destValue = memory[actualMemory.secondReg+actualMemory.thirdReg];
								}
								else {
									actualMemory.destValue = memory[actualMemory.secondReg+actualMemory.literal];
								}
							}	

							else if(actualMemory.instrType==InstructionType.STORE){
								if(actualMemory.literal == -1){
									memory[actualMemory.secondReg+actualMemory.thirdReg] = register[actualMemory.destination];
								}
								else {
									memory[actualMemory.secondReg+actualMemory.literal] = register[actualMemory.destination];
								}
							}

							break;
				case MOVC: 	actualMemory.destValue = actualMemory.literal;
				break;
				
				case MOV: 	actualMemory.destValue = register[actualMemory.secReg];
				break;
				default:
			}
		}
	}
}
