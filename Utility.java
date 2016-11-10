
import java.util.StringTokenizer;

public class Utility extends InOrderSimulator {
	
	 public void processInstr(Instruction instruction){
		 
		 	String temp;
			String string = instruction.string;
			
			string = string.trim().replaceAll(",", " ");
			string = string.replaceAll("#", " ");
			string = string.replaceAll(" +", " ");
			
			StringTokenizer tokenizer = new StringTokenizer(string," ");
			instruction.instrName= tokenizer.nextToken();	
			instruction.instrType= InstructionType.valueOf(instruction.instrName);
			
			if(instruction.instrType == InstructionType.HALT) return;
			if(instruction.instrType == InstructionType.NOP) return;
			
			if(tokenizer.hasMoreTokens()){
				temp = tokenizer.nextToken();
				if(temp.equalsIgnoreCase("X")){
					instruction.destination = 8;
				}
				else if (temp.charAt(0)!= 'R') {
					instruction.literal=Integer.parseInt(temp);
					return;
				}
				else{
					instruction.destination=Integer.parseInt(temp.substring(1) );
				}
			}

			if(tokenizer.hasMoreTokens()){
				temp = tokenizer.nextToken();
				 if(temp.equalsIgnoreCase("X")){
						instruction.secReg = 8;
				 }
				 else if(temp.charAt(0)!= 'R'){
					instruction.literal=Integer.parseInt(temp);
					return;
				 }
				else{
					instruction.secReg= Integer.parseInt(temp.substring(1));
				}
			}

			if(tokenizer.hasMoreTokens()){
				temp = tokenizer.nextToken();
				if(temp.charAt(0)!= 'R'){
					instruction.literal=Integer.parseInt(temp);
					return;
				}
				else{
					instruction.thReg = Integer.parseInt(temp.substring(1) );
				}
			}
	   }
	 
	 public void initializeSimulator(){
		 
		 programCounter = 20000;
			instructions = new Instruction[2000];
			
			for (int i=0; i<2000; i++) {
				instructions[i]=new Instruction();
			}
			register = new int[9];
			registerValid= new boolean[9];
			registerInvalid= new int[9];
			
			for(int i=0;i<9;i++) 
				registerValid[i]=true;
			
			memory = new int[10000];
			
			for(int i=0;i<9;i++) 
				registerInvalid[i]=-1;
	 }
}
