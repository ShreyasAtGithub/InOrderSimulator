
public class Execute extends InOrderSimulator {
	
	public void performExecute(){

		if(!stallEX && !stallMEM && !stallWB){
			nextExecute = actualExecute;		
			actualExecute = nextDecode;
				if(actualExecute != null && actualExecute.instrType != null){
					System.err.print(" EX "+actualExecute);
					switch (actualExecute.instrType) {
						case MOVC: 
						case MOV: 
						case NOP:
						case LOAD:
						case STORE:
									break;
						case HALT: y = incycles;
						break;
						
						case ADD: int operand2=0;
								  operand2 =  actualExecute.thReg != -1 ? actualExecute.thirdReg : actualExecute.literal;
								  actualExecute.destValue= actualExecute.secondReg + operand2;
						          if(actualExecute.destValue == 0) status=0;
						          else
						        	  status = -1;
						break;
						
						case MUL:
									 operand2=  actualExecute.thReg!= -1 ? actualExecute.thirdReg : actualExecute.literal;
								     actualExecute.destValue= actualExecute.secondReg*operand2;
						break;
						
						case SUB: 
									operand2 =  actualExecute.thReg != -1 ? actualExecute.thirdReg : actualExecute.literal;
									actualExecute.destValue= actualExecute.secondReg-operand2;
									if(actualExecute.destValue == 0)status=0;
									else
										status = -1;
						break;
						
						case AND: 
									operand2 =  actualExecute.thReg != -1 ? actualExecute.thirdReg : actualExecute.literal;
									actualExecute.destValue = actualExecute.secondReg & operand2;
									if(actualExecute.destValue == 0)status = 0;
									else
										status = -1;
						break;
						
						case OR: 
									operand2=  actualExecute.thReg != -1 ? actualExecute.thirdReg : actualExecute.literal;
									actualExecute.destValue = actualExecute.secondReg | operand2;
									if(actualExecute.destValue==0) status = 0;
									else
										status = -1;
						break;
						
						case XOR: 
									operand2 =  actualExecute.thReg!= -1 ? actualExecute.thirdReg : actualExecute.literal;
									actualExecute.destValue = actualExecute.secondReg ^ operand2;
									if(actualExecute.destValue==0) status= 0;
									else
										status = -1;
							break;
						
						case BNZ: 
									if(status != 0){ 
										for(int i=0;i<8;i++){
											if(registerInvalid[i] == actualDecode.address || registerInvalid[i] == actualExecute.address){
												registerInvalid[i] = -1;
												registerValid[i]=true;
											}
										}
										actualDecode=null; actualFetch=null; nextFetch=null; nextDecode=null;
										programCounter = actualExecute.address+actualExecute.literal;
									}
									break;
						
						case BZ: 
								 	if(status == 0){
								 		for(int i=0;i<8;i++){
											if(registerInvalid[i] == actualDecode.address || registerInvalid[i] == actualExecute.address){
												registerInvalid[i] = -1;
												registerValid[i]=true;
											}
										}
										actualDecode=null; actualFetch=null; nextFetch=null; nextDecode=null;
								 		programCounter = actualExecute.address + actualExecute.literal;
								 	}
									break;
						
						case BAL: 	for(int i=0;i<8;i++){
										if(registerInvalid[i] == actualDecode.address || registerInvalid[i] == actualExecute.address){
											registerInvalid[i] = -1;
											registerValid[i]=true;
										}
									}
									actualDecode=null; actualFetch=null; nextFetch=null; nextDecode=null;
									stalled=false; 
									register[8]= actualExecute.address+1;
									programCounter = actualExecute.literal;
									break;
						
						case JUMP:	for(int i=0;i<8;i++){
									if(registerInvalid[i] == actualDecode.address || registerInvalid[i] == actualExecute.address){
										registerInvalid[i] = -1;
										registerValid[i]=true;
										}
									}
									stalled=false; 
									programCounter= register[actualExecute.destination] + actualExecute.literal;
									break;
					}
				}
		    }
		else{
			nextExecute=null;
		}
	}
}
