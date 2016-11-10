
enum InstructionType {
	
			     		ADD, 
			     		SUB, 
			     		MUL, 
			     		AND, 
			     		OR, 
			     		XOR, 
			     		MOVC, 
			     		MOV, 
			     		LOAD, 
			     		STORE, 
			     		BZ, 
			     		BNZ, 
			     		JUMP, 
			     		BAL, 
			     		HALT, 
			     		NOP
                  }


public class Instruction{

		public Instruction(){
			isPresent = false;
			instrType = null;
		}
		
		public String toString(){
			return (string);
		 }
	
		public void print(){
		StringBuilder sbuilder= new StringBuilder();
		if(instrType != null){
			sbuilder.append("Instruction:"+instrType.toString());
		}
		else{
			System.out.println("Empty");return;
		}
		sbuilder.append("Dest:R "+destination+"\tSrc1:R "+secReg+"\tSrc2:R "+thReg+"\tliteral:"+literal+"\tAddress(PC):"+address);
		System.out.println(sbuilder);
	}
	
		public InstructionType instrType=null;
		public String instrName="";
		
		public int address;
		int noOfOperands;
		public boolean isPresent;
		
		public int secReg = -1,thReg = -1;
		public int secondReg=-1,thirdReg=-1;

		public int destination=-1;
		public int destValue=-1;

		public int literal=-1;
		public String string;
		
 }
