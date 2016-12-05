/**
 * @author Shreyas Mahanthappa Nagaraj
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.StringTokenizer;

public class InOrderSimulator{

	public boolean stalled = false;
	boolean stallFetch=false, stallDecode=false, stallEX=false, stallMEM=false, stallWB=false;

	public Instruction actualFetch = null,  nextFetch = null;
	public Instruction actualDecode = null,  nextDecode = null;
	public Instruction actualExecute = null,  nextExecute = null;
	public Instruction actualMemory = null,  nextMemory = null;
	public Instruction actualWB = null;
	
	public boolean registerValid[];
	public int registerInvalid[];
	public int programCounter;
	public int status = -1;
	Instruction[] instructions;
	public int register[];
	public int memory[];
	
	boolean stop=false; 
	static int incycles=0;
	int y=0;
	
	public InOrderSimulator(){
		
		programCounter = 20000;
		instructions = new Instruction[2000];
		
		for (int i=0; i<2000; i++) {
			instructions[i]=new Instruction();
		}
		register = new int[9];
		registerValid= new boolean[9];
		registerInvalid= new int[9];
		
		for(int i=0;i<9;i++) 
			registerInvalid[i]=-1;
		
		for(int i=0;i<9;i++) 
			registerValid[i]=true;
		
		   memory = new int[10000];
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		try{
			PrintStream stream = new PrintStream("logFile");
			System.setErr(stream);
		}
		catch (Exception e) {
			System.out.println("Log file doesn't exist");
		}

		InOrderSimulator simulator = new InOrderSimulator();
		String INPUT_FILE ="";
		
		while(true){
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("");
			System.out.println("Enter command (load <filename>, Initialize, Simulate <n>, Display): ");
			
			String command = sc.nextLine();
			command = command.toLowerCase();
			System.out.println("Command Entered : "+command);
			
			if(command.contains("loadfile")){
				try{
					INPUT_FILE = (command.split(" ")[1].trim());
					System.out.println("INPUT FILE NAME :: "+INPUT_FILE);
				} catch (Exception e){
					System.out.println("Please provide the Input File");
				}
					simulator.loadfile(INPUT_FILE);
			} else if(command.contains("initialize")){
					simulator.initialize();
			} else if(command.contains("simulate")){
				try{
					incycles = Integer.parseInt(command.split(" ")[1].trim());
				} catch (NumberFormatException e){
					System.out.println("Please provide the number of Cycles");
				}
					simulator.simulate(incycles);
			} else if(command.contains("display")){
					simulator.displayAll();
			} else {
				System.out.println("");
				System.out.println(" You have entered the Wrong Input. Please run again (type : make)");
				System.out.println("");
				break;
			}
		}
	}
	
	
	public void initialize(){
		System.out.println("=================== INORDER SIMULATOR INITIALIZED ===================");
		
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

	
	public void loadfile(String inFile){
		BufferedReader br;
		int i=0;
		String line;
		
		try{ 
			br = new BufferedReader(new FileReader(inFile));
			while((line = br.readLine()) != null) {
				System.out.println(i+"  "+line);
				  instructions[i].string =line;
				  instructions[i].address = 20000+i;
				  instructions[i].isPresent = true;
			      processOperands(this.instructions[i]);
				  i++;
			}
		}
		catch (Exception e) {
			System.out.println("Problem with instructions in the Input File \n"+e);
			System.exit(0);
		}
	}

	
	public void processOperands(Instruction instruction) throws Exception{
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

	
	public void simulate(int cycles){

		for(y=0;y<cycles;y++){
			
		 if(programCounter<20000) {
				programCounter= (programCounter+20000);
		 }
			
			fetchStage();
			decodeStage();
			executeStage();
			memoryStage();
			writebackStage();
			
		}
	}
	

	public void fetchStage(){
		
		if(stop && stalled) 
			return;
		if(stalled)
			return;
		nextFetch= actualFetch;
		if(stop) actualFetch = null;

		actualFetch=this.instructions[programCounter-20000];
	
		if(instructions[programCounter-20000+1].isPresent){
				programCounter++;
		}
		else{
			if(!stop){
				programCounter++;
				stop = true;} 
		 }
	 }


	public void decodeStage(){
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

	
	public void executeStage(){
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


	public void memoryStage(){
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

	
	public void writebackStage(){
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
	
	
	public  void printStages(){
		
		System.out.println();
		System.out.print("   Fetch Stage:      ");   System.out.print(actualFetch+"\n");
		System.out.print("   Decode Stage:     ");   System.out.print(actualDecode+"\n");
		System.out.print("   Execute Stage:    ");   System.out.print(actualExecute+"\n");
		System.out.print("   Memory Stage:     ");   System.out.print(actualMemory+"\n");
		System.out.print("   Writeback Stage:  ");   System.out.print(actualWB+"\n");
	}
	
	
	public void displayAll(){
		
		printStages();
		
		System.out.println("");
		System.out.println("\n ===============  Printing Register's Contents  =============== ");
		
		System.out.println("    R0:"+register[0]+    "    R1:"+register[1]+"    R2:"+register[2]+ "    R3:"+register[3]);
		System.out.println("    R4:"+register[4]+    "    R5:"+register[5]+"    R6:"+register[6]+ "    R7:"+register[7]);
		System.out.println("    X:"+register[8]);
		System.out.println("");
		
		System.out.println("=================  PRINTING MEMORY LOCATIONS  ================= ");

		for(int i=0;i<=100;i++){
			System.out.print("   Memory"+"["+i+"]" +": "+memory[i]);
			if((i+1)%10==0)System.out.println();
		}
		System.out.println("");
	}
}
