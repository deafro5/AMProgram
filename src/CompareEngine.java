import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class CompareEngine {
	FileReader databaseReader;
	String catalogues;
	String genEds;
	String[][] reqs;
	boolean matchFound = false;
	
	CompareEngine(){
		databaseReader = new FileReader();
	}
	
	public void compare(DefaultListModel classlist) throws IOException{
		DefaultListModel<String> transcript = classlist;
		String recommend = null;
		ArrayList<String> recommendList = new ArrayList<String>();
		reqs = databaseReader.fetchReqs("TestReq");
		
		
		
		for(int i = 0; i<reqs.length; i++){
			if(reqs[i][0].contains("@")){
				//we have found an @ which represents select any class > indicated value
			}else{
				//with no special rules, see if any transcript values match any
				// values in reqs[i]
				for(int j = 1; j< reqs[i].length; j++){
					for(int k = 0; k < transcript.getSize(); k++){
						if(reqs[i][j].equals(transcript.get(k))){
							//we found a class taken that matches criteria for a req
							matchFound = true;
							transcript.remove(k);
						}
					}
				}
				if(!matchFound){		//if we did not find a match
					recommend = "You need a class to fit:";	
					for(int j = 0; j < reqs[i].length; j++){	//provide the user a list of selections
						recommend = recommend + " " + reqs[i][j];
					}
					recommendList.add(recommend);
				}else{
					matchFound = false;
				}
			}
			//requirement precedence. one class may not satisfy 2 requirements, but can.
			// required classes should take precedence over proffesional electives, which
			// take precedence over free electives. precedence is determined by order 
			// listed in requirements file
		}
		for(int i =0; i<recommendList.size();i++){
			System.out.println(recommendList.get(i));
		}
	}
	
}
