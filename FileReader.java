

import java.io.*;
import java.util.*;

public class FileReader 
{
	FileReader(){
		
	}
	
	/*updateDatabase is an administrative accessed method. The administrator will use a file browser from the GUI
	 * to locate a CSV file likely generated from eCampus. When the user finishes selecting a file
	 * the file is sent here, where the reader will take the input, and then open an output
	 * to overwrite a current file in the database*/
	private void updateDatabase(String aFileName){
		//open reader
		//cut extraneous data
		//output to database file
	}
	
	
	/*fetchCatalogues will be used to populate a list of classes for the user to choose from
	 * while they are assembling their transcript. The results will also be used
	 * by the CompareEngine when it makes recommendations for the user
	 * The method is given a list of catalogs needed, and returns an array of lists*/
	private String[][] fetchCatalog(String[] fields) throws IOException{
		
		String[][] catalogs = new String[fields.length][]; 
	    Scanner scanner;
	    String relation;
	    int sizeOfFile;
		
		for(int i=0; i<fields.length; i++){
			relation = fields[i]; //relation = C:\pathToDatabaseFolder\File, File determined by the value at fields[i]
			scanner = new Scanner(new FileInputStream(relation)); //create a file reader targeting the ith catalog we need
			
			sizeOfFile = lengthOfTask(relation);
			catalogs[i] = new String[sizeOfFile];
			for(int j = 0; j<sizeOfFile; j++){
				catalogs[i][j] = (new StringBuilder("test:")).append(scanner.nextLine()).append(NL).toString();
				/*the above line will need tweaking once the data format is decided. as of now each line of the
				 * array will store the data: "test: <original data from database>" */
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
	private int[][] fetchReqs(String degree){
		//access database file for degree
		//for each line of file generate an array of class ID's
	}

	
	/*lengthOftask is used by the other methods to assess how many lines are in the file they will be
	 * fetching data from, so that they will be able to properly initialize array sizes where
	 * they will place that data*/
    public int lengthOfTask(String relation)
            throws FileNotFoundException
        {
            int tasklength = 0;
            Scanner lengthTest;
            for(lengthTest = new Scanner(new FileInputStream(relation)); lengthTest.hasNextLine();)
            {
                lengthTest.nextLine();
                tasklength++;
            }

            lengthTest.close();
            return tasklength;
        }

	private static final String NL = System.getProperty("line.separator");
}