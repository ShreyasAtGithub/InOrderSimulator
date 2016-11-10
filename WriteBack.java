
public class WriteBack extends InOrderSimulator {
	
	public void performWb(){
		

		actualWB=nextMemory;
		if(actualWB != null && actualWB.instrType != null){
	
			switch (actualWB.instrType) {
						case STORE:
						case BNZ:
						case BZ:
						case BAL:
						case HALT:
						case JUMP: 
								break;
						case MOVC: 
						case MOV:
						case LOAD:
						case ADD: 
						case MUL:				
						case SUB: 
						case AND: 
						case XOR:
						case OR: OR(actualWB);
								break;
						case NOP:
								break;
			            default:
				                break; 
				}
				checkWriteBack(actualWB);
     	    }
   	}
	
	public void OR(Instruction actualWB){
		register[actualWB.destination] = actualWB.destValue;
	}
	
	
	public void checkWriteBack(Instruction actualWB){
		if(actualWB.secReg != -1){
			registerValid[actualWB.secReg] =true;
			}
		if(actualWB.thReg != -1 ){
			registerValid[actualWB.thReg] =true;
		}
		if(actualWB.destination != -1 ){
			registerValid[actualWB.destination] = true;
		}
	}
}
