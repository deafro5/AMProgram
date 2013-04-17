import java.io.*;
import java.util.*;

public class FileReader 
{
	FileReader(){
		//relation = "C:\\Users\\Jimmy\\Documents\\GitHub\\AMProgram\\Database";
		relation = System.getProperty("user.dir") + "\\database";
	}

	/*updateDatabase is an administrative accessed method. The administrator will use a file browser from the GUI
	 * to locate a CSV file likely generated from eCampus. When the user finishes selecting a file
	 * the file is sent here, where the reader will take the input, and then open an output
	 * to overwrite a current file in the database*/
	public void updateDatabase(String aFileName){
		//open reader
		//cut extraneous data
		//output to database file
	}

	/*ftechClassLists looks for all files in the catalog folder and returns a 2 dimensional array
	 * each "i" item of the array represents a catalog, and every "j" item a class in that catalog.
	 * a dropdown menu can be populated using the "i"s and when an option is selected the "j"s
	 * can be used to generate a second dropdown.
	 */
	public String[][] fetchClassLists() throws IOException{
		String[] catalogs = null;
		String[][] lists = null;

		File f = new File(relation + "\\catalogs");
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("txt");
		    }
		});
		catalogs = new String[matchingFiles.length];
		for(int i=0; i < matchingFiles.length; i++){
			catalogs[i] = matchingFiles[i].getName();
		}


		lists = fetchCatalog(catalogs);
		return lists;
	}


	/*fetchCatalogues will be used to populate a list of classes for the user to choose from
	 * while they are assembling their transcript. The results will also be used
	 * by the CompareEngine when it makes recommendations for the user
	 * The method is given a list of catalogs needed, and returns an array of lists*/
	public String[][] fetchCatalog(String[] fields) throws IOException{

		String[][] catalogs = new String[fields.length][]; 
	    Scanner scanner;
	    int sizeOfFile;
	    String workingRelation;

		for(int i=0; i<fields.length; i++){
			workingRelation = (new StringBuilder(relation)).append("\\catalogs\\").append(fields[i]).toString(); //relation = C:\pathToDatabaseFolder\File, File determined by the value at fields[i]
			scanner = new Scanner(new FileInputStream(workingRelation)); //create a file reader targeting the ith catalog we need

			sizeOfFile = lengthOfTask(workingRelation);
			catalogs[i] = new String[sizeOfFile + 1];
			catalogs[i][0] = new StringBuilder(fields[i]).delete(fields[i].lastIndexOf(".txt"), fields[i].length()).toString();
			for(int j = 1; j<=sizeOfFile; j++){
				catalogs[i][j] = (new StringBuilder("")).append(scanner.nextLine()).toString();
				/*the above line will need tweaking once the data format is decided. as of now each line of the
				 * array will store the data: "<original data from database>" */
				//each "i" represents a catalog, and each "j" represents a line in the ith catalog
			}
			scanner.close(); //unlink the scanner for this catalog before moving onto the next
		}		

		return catalogs;
	}


	/*fetchReqs is used to find out what requirements are needed for graduation for a specific degree
	 * the method will be given the degree to be targeted and the reader will access that database file
	 * in order to generate a list of requirements and how to satisfy those requirement. 
	 * In the returned value, an array represents a requirement, and each element will contain a class
	 * ID which satisfies it*/
	public String[][] fetchReqs(String degree)throws IOException{
		Scanner scanner;
		int numOfReqs;
		String workingRelation;

		workingRelation = (new StringBuilder(relation)).append("\\requirements\\").append(degree).append(".txt").toString(); //relation = C:\pathToDatabaseFolder\File
		scanner = new Scanner(new FileInputStream(workingRelation)); //create a file reader 
		scanner.useDelimiter(",|\\\n");

		numOfReqs = lengthOfTask(relation);
		String[][] reqs = new String[numOfReqs][];

		for(int i = 0; i<numOfReqs; i++){
			reqs[i] = new String[LengthOfLine(workingRelation, i)];

			for(int j = 0; j < reqs[i].length; j++){
				StringBuilder newLine = new StringBuilder("");
				newLine.append(scanner.next());

				reqs[i][j] = newLine.toString();
				/*This reader is similar to the "fetchCatalogs" reader, but differs in that
				 * instead of a catalog, the outer loop represents a requirement. each item of the inner
				 * loop represents a class that fulfills that requirement.
				 * This will need to be tweaked when database format is finalized, as currently
				 * the first element is always the requirement ID, which may go unused later
				 */
			}

		}
		scanner.close(); //unlink the scanner for this file

		return reqs;
	}


	/*lengthOftask is used by the other methods to assess how many lines are in the file they will be
	 * fetching data from, so that they will be able to properly initialize array sizes where
	 * they will place that data*/
    private int lengthOfTask(String relation)
            throws FileNotFoundException
        {
            int taskLength = 0;
            Scanner lengthTest;
            for(lengthTest = new Scanner(new FileInputStream(relation)); lengthTest.hasNextLine();)
            {
                lengthTest.nextLine();
                taskLength++;
            }

            lengthTest.close();
            return taskLength;
        }
    
    private int LengthOfLine(String Relation, int lineNum)
    		throws FileNotFoundException
    {
    	int taskLength = 1;
        Scanner lengthTest;
       
        lengthTest = new Scanner(new FileInputStream(relation));
        for(int i = 0; i < lineNum; i++){
        	lengthTest.nextLine();
        }
        while(lengthTest.findInLine(",") != null){ 
        	taskLength++;
        }


        lengthTest.close();
        return taskLength;
    }
    
    public void writeNewUser(String hashedName, String hashedPass) throws IOException{
    	String fileWriteName = relation + "\\Saves\\Login.txt";
    	
    	FileWriter fw = new FileWriter(fileWriteName, true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		bw.append(hashedName + ", " + hashedPass);
		bw.append(NL);

		bw.close();
    }
    
    /*
     * This method fetches a 2D array of the login database information.
     * Each 'i' is a new student.
     * The first element of 'j' is the hashed-username.
     * The second element of 'j' is the hashed-password.
     */
	public String[][] fetchLoginInfo() throws IOException{

		String relation2 = relation + "\\Saves\\Login.txt"; //current directory
		Scanner scanner = new Scanner(new FileInputStream(relation2)); //create a file reader targeting the login database
		String[][] loginCatalog = new String[lengthOfTask(relation2)][2];
	    int sizeOfFile2 = lengthOfTask(relation2);
	    loginInfoSize = sizeOfFile2; //Size of login info database
	    	    //use space and page break for delimiters
	    	    scanner.useDelimiter(",|\\\n");
	     		for(int i=0; i<sizeOfFile2; i++){

	     			//loginCatalog[i] = new String[sizeOfFile2];
	     			for(int j = 0; j<2; j++){
	     				loginCatalog[i][j] = scanner.next().trim(); //(new StringBuilder()).append(scanner.next(",")).toString();
	     			}
				/*the above line will take all the string info up to the next ',' */
				//each "i" is a new student, the first "j" element is username, the second "j" element is the password
			}

		scanner.close(); //unlink the scanner for this catalog before moving onto the next
		return loginCatalog;
	}

	/*
	 * Returns the size of the login database file
	 */
	public int fetchLoginInfoSize(){
		return loginInfoSize;
	}
	
	/**
	 * Saves user's classes into database so he/she can load them next time they log on.
	 * Takes user's hashed name, object array of classes taken and the # of classes taken.
	 */
	public void writeUserSave(String someHashedUser, Object[] takenClassArray, int numberOfClasses) throws IOException{
		String fileWriteName2 = relation + "\\Saves\\" + someHashedUser + ".txt";
	    
    	FileWriter fw = new FileWriter(fileWriteName2, false);
		BufferedWriter bw = new BufferedWriter(fw);
		String[] classesTakenString = new String[numberOfClasses];

		//Copies array of objects into array of strings
		for(int i=0; i<numberOfClasses; i++){
			classesTakenString[i] = takenClassArray[i].toString();
		}
		
		bw.write("");//Clears file
		
		//Appends classes to file
		for(int j=0; j<numberOfClasses; j++){
			bw.append(classesTakenString[j]); //Appends class
			bw.append(NL);//Skips down to next line for next class
		}
		bw.close(); //Closes writer
	}
	
	/**
	 * This method reads a list of classes from a .txt file and saves it to an array of Strings
	 * @param someHashedUser: The user's hashed name
	 * @return String[]: A String array of all the classes the student has taken
	 * @throws FileNotFoundException
	 */
	public String[] readLoadClasses(String someHashedUser) throws FileNotFoundException{
		String fileReadLoadName2 = relation + "\\Saves\\" + someHashedUser + ".txt";
		File f = new File(fileReadLoadName2);
		
		//Checks to see if a save file exists first
		//If it does, read the file and store it in an array
		if(f.exists()) {
			String[] loadClass = new String[lengthOfTask(fileReadLoadName2)];
			Scanner scanner = new Scanner(new FileInputStream(fileReadLoadName2));
			
			for(int i=0; i<lengthOfTask(fileReadLoadName2); i++){
				loadClass[i] = (new StringBuilder("")).append(scanner.nextLine()).toString();
			}
			
			return loadClass;
		}else{ //If the file does not exist, return a blank array
			String[] loadClass = new String[0];
			return loadClass;
		}
	}
    
    private int loginInfoSize = 0; //Size of the login database
	private static final String NL = System.getProperty("line.separator");
	private String relation;
}
