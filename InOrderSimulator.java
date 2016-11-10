import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;

public class InOrderSimulator{

	public static boolean stalled = false;
	static boolean stallFetch=false, stallDecode=false, stallEX=false, stallMEM=false, stallWB=false;

	public static Instruction actualFetch = null,  nextFetch = null;
	public static Instruction actualDecode = null,  nextDecode = null;
	public static Instruction actualExecute = null,  nextExecute = null;
	public static Instruction actualMemory = null,  nextMemory = null;
	public static Instruction actualWB = null;
	
	public static boolean registerValid[];
	public static int programCounter;
	public static int status = -1;
	static Instruction[] instructions;
	public static int registerInvalid[];
	public static int register[];
	public static int memory[];
	
	static boolean stop=false; 
	static int incycles=0;
	static int y=0;
	
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
		
		Fetch fetch = new Fetch();
		Decode decode = new Decode();
		Execute execute= new Execute();
		Memory mem = new Memory();
		WriteBack wb = new WriteBack();
		
		Utility utility = new Utility();
		
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
					simulator.loadfile(utility, INPUT_FILE);
			} else if(command.contains("initialize")){
					simulator.initialize(utility);
			} else if(command.contains("simulate")){
				try{
					incycles = Integer.parseInt(command.split(" ")[1].trim());
				} catch (NumberFormatException e){
					System.out.println("Please provide the number of Cycles");
				}
					simulator.simulate(fetch, decode, execute, mem, wb ,incycles);
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
	
	
	public void initialize(Utility utility){
		utility.initializeSimulator();
		System.out.println("=================== INORDER SIMULATOR INITIALIZED ===================");
	}

	
	public void loadfile(Utility utility, String inFile){
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
				  utility.processInstr(InOrderSimulator.instructions[i]);
				  i++;
			}
		}
		catch (Exception e) {
			System.out.println("Problem with instructions in the Input File \n"+e);
			System.exit(0);
		}
	}

	
	public void simulate(Fetch fetch,Decode decode, Execute execute, Memory mem, WriteBack wb, int cycles){

		for(y=0;y<cycles;y++){
			
		 if(programCounter<20000) {
				programCounter= (programCounter+20000);
		 }
		 
			fetch.performFetch();
			decode.performDecode();
			execute.performExecute();
			mem.performMemory();
			wb.performWb();
		}
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
