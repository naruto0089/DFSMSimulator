import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
public class SimDFSM {
	/**
	 * Course Name:CS-5313 Formal Language Theory
	 * Submitted By:Asim Neupane
	 * Programming Assignment Number: 1
	 * 
	 * This program is the simulation of DFSM. It takes two inputs namely the name of file containing the transition table
	 * for DFSM and a string to test whether it is accepted by the given DFSM or not.
	 * 
	 */
	//global Variables
	public static SimDFSM objSimDFSM = new SimDFSM();	
	ArrayList<String> fileContent =new ArrayList<String>();
	static char [] inputString;
	static String fileName;
	String[] alphabets= null;
	String[] finalStates =null;
	String[][] transitionTable = new String[1000][1000];
	
	//handling inputs 
	public void SimDFSMInputs(String fileName, String input){	
		fileName = fileName.replaceAll("\\s", "");
		if(fileName.isEmpty()){
			System.err.println("Error: Invalid filename or filename cannot be empty");
			System.exit(-1);
		}
		//converting input string to character array
		inputString  = input.toCharArray();
		//reading data from file
		ReadAFile(fileName);	
	}
	//Reads a file and returns its content in ArrayList
	public  ArrayList<String> ReadAFile(String fileName){
		try {
		File file = new File(fileName);
		if(!file.exists()){
			System.err.println("Error: File Not Found");
			System.exit(-1);
		}
		else{
		 FileReader in = new FileReader(fileName);
		    BufferedReader br = new BufferedReader(in);
		    String line;
		    while ((line = br.readLine()) != null) {
		        fileContent.add(line);
		    }
		    in.close();
		}
		}
		catch (Exception ex){
			System.out.println(ex);
		}
		return fileContent;
	}
	public boolean isAlpha(String str){
		char[] chars = str.toCharArray();
		for(char c : chars){
			if(!Character.isLetter(c)){
				return false;
			}
		}
		return true;
	}
	//get position of alphabets
	public int GetPositon(String presentInput){
		int index = -1;
		for (int i=0;i<alphabets.length;i++) {
		    if (alphabets[i].equals(presentInput)) {
		        index = i;  //it starts with 0
		        break;
		    }
		}
		//if index -1 then input is not present in alphabet
		if(index == -1){
			System.err.println("Error: Input read does not belong to alphabets");
			System.exit(-1);
		}
		return index;  
	}
	public static boolean IsCurrentStateFinalState(String state) {
		for(String s: objSimDFSM.finalStates){
			if(s.equals(state))
				return true;
		}
		return false;
	}
	public static ArrayList<String> spaceremover(ArrayList<String> fileContent){
		
		ArrayList<String> filedata = new ArrayList<String>();
		for(String st : fileContent){
			filedata.add(st.replaceAll("^\\s+", ""));
		}
		return filedata;
	}
	//Splitting the content of file as alphabets, transition table and final state
	public void ArrangeInputs(){
		ArrayList<String> contentOfFile = new ArrayList<String>();
		contentOfFile = spaceremover(objSimDFSM.fileContent);
		//to check if there is empty spaces instead of state
		TransitionTableChecker(contentOfFile);	
		for(String st : contentOfFile){
			//Checking empty lines
			if(st.isEmpty()){
				System.err.println("Eror: Invalid Specification of DFSM");
				System.exit(-1);
			}
		}
		//checking if file contains more than three line or not
		//if less than 3 then throw data insufficient message
		if(contentOfFile.size() ==2){
			if(IsFinalStateInitialState(contentOfFile.get(contentOfFile.size()-1))){
				System.err.println("Empty Machine/Language");
				System.exit(-1);
			}
			System.err.println("Error: Missing transition table");
			System.exit(-1);
		}
		else if(contentOfFile.size() == 1){
			System.err.println("Error:Insufficient data in file. It contains only alphabets.");
			System.exit(-1);
		}
		else if(contentOfFile.size() < 1){
			System.err.println("Error: Empty File");
			System.exit(-1);
		}
		else{
		alphabets= (contentOfFile.get(0).split("\\s+"));
		//checking if alphabets contains dublicate alphabet or not
		if(AlphabetChecker(alphabets)){
			System.err.println("Error: Invalid alphabets. Contains one or more dublicate alphabets");
			System.exit(-1);
		}
			int row =0 , column;
		for (int i= 1; i<contentOfFile.size()-1;i++ ){
			String [] values =  (contentOfFile.get(i).split("\\s+"));
			for(String st : values){
				st = st.replaceAll("\\s+", "");
				//checking if state is represented by string of length more than one
			if(isAlpha(st)){
				System.err.println("Error: Invalid state '"+ st + "' detected in transition table");
				System.exit(-1);
				}	
			}
			column =0;
			for (String str : values)
        	{
				transitionTable[row][column]= str;
				column=column+1;
        	}
        	row=row+1;
		}
				
		finalStates = contentOfFile.get(contentOfFile.size()-1).split("\\s+");
		for(String st : finalStates){
		if(isAlpha(st)){
			System.err.println("Error: Invalid State '"+ st +"' detected in final state");
			System.exit(-1);
		}
		}
	}
	}
	//check if alphabets are repeated or not
	public boolean AlphabetChecker(String[] alphabets){
		 for (int i=0; i<alphabets.length; i++){
        for (int j=i+1; j<alphabets.length; j++){
            if (alphabets[j].equals(alphabets[i])){
            	return true;
            }
        }
    }
    return false;
	}
	//checking if final state is initial state
	public boolean IsFinalStateInitialState(String finalState){
		String [] finalStates=null;
		finalStates = finalState.split("\\s+");
		for(String st : finalStates){
		if(st.equals("1")){
			return true;
		}
		}
		return false;
	}
	//counts the number of spaces in transition table and compares it with number of spaces in alphabets to check if transiton 
	//table provides is valid
	public static void TransitionTableChecker(ArrayList<String> fileContent){
		String alphabets = fileContent.get(0).trim().replaceAll("\\s{2,}"," ");
		int numberofSpacesInAlphabets = alphabets.length() - alphabets.replace(" ", "").length();
		//System.out.println(alphabets);
		//System.out.println("Number of Spaces "+ numberofSpacesInAlphabets);
		//counting number of spaces
		for(int i= 1; i< fileContent.size() -1; i++){
			
			//System.out.println(fileContent.get(i));
			String transitonTableData = fileContent.get(i).trim().replaceAll("\\s{2,}"," ");
			int numberofSpacesInTransitionTableData = transitonTableData.length() - transitonTableData.replace(" ", "").length();
			if(numberofSpacesInAlphabets!=numberofSpacesInTransitionTableData){
				System.out.println("Error: Invalid transition table");
				System.exit(-1);
			}
		}
	}
	//Checking of input string 
	public void Processing(){
		//row is the state and column is position of char
		char presentInput;
		String currentState ="";
		int row = 1, column;
		for(int i =0; i< inputString.length;i++){
			//get present input
			presentInput = inputString[i];
			//get the position of present input from alphabets
			column = objSimDFSM.GetPositon(Character.toString(presentInput));
			row = Integer.valueOf(objSimDFSM.transitionTable[row-1][column]);
			currentState = Integer.toString(row);
		}
		if(IsCurrentStateFinalState(currentState)){
			//System.out.println("Yes, "+String.valueOf(inputString)+" is accepted in given DFSM");
			System.out.println("Yes");
		}
		else{
			//System.out.println("No, "+String.valueOf(inputString)+" is not accepted in given DFSM");
			System.out.println("No");
		}
	}	
	//Checking for case if initial state is the final state
	 public static void InitialStateIsFinalState(String input, String [] finalStates){
		 	if(input.equals(""))
		{
			for(String st : finalStates){
			if(st.equals("1")){
			System.out.println("Yes");
			System.exit(0);
			}
			else{
			System.out.println("No");
			System.exit(0);	
			}
		}
		}
	 }
	//main	
	public static void main(String args [] ) {
		
		if(args.length <2){
			System.err.println("Error: Insufficient Inputs");
			System.exit(-1);
		}
		if(args.length>2){
			System.err.println("Error: Too many input passed");
		}
		objSimDFSM.fileName = args[0];
		String input = args[1];
	
		
		objSimDFSM.SimDFSMInputs(objSimDFSM.fileName,input);
		objSimDFSM.ArrangeInputs();
		InitialStateIsFinalState(input,objSimDFSM.finalStates);
		objSimDFSM.Processing();
	}
}
