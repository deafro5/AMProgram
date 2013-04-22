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
		DefaultListModel<String> tempList = null;
		String recommend = null;
		ArrayList<String> recommendList = new ArrayList<String>();
		reqs = databaseReader.fetchReqs("TestReq");
		
		
		
		for(int i = 0; i<reqs.length; i++){
			if(reqs[i][0].contains("@")){
				//we have found an @ which represents select any class > indicated value
				if(tempList != null){
					for(int x = 0; x<tempList.getSize();x++){
						transcript.addElement(tempList.get(x));
					}
					tempList = null;
				}
				if(reqs[i][1].substring(0,1) == "*"){
					if(reqs[i][1].substring(3,4) == "*"){
						//in the requirement file, these sorts of reqs (*** ***) represent a 
						// "choose any class, free elective" and must be LAST in the file
						if(transcript.get(0)!=null){
							//any class meets this requirement, so just take one and run with it
							matchFound = true;
							transcript.remove(0);
						}
					}else{
						//in the requirement file, these sorts of reqs (*** 300) represent a 
						// "choose any elective of a certain level"
						// in file must go BEFORE free electives, but AFTER professional electives
						for(int k = 0; k < transcript.getSize(); k++){
							if(reqs[i][1].substring(3,6).compareTo(transcript.get(k).substring(3,6)) > 0){
								//we found a class taken that matches criteria for a req
								matchFound = true;
								transcript.remove(k);
							}
						}
					}
				}else{
					//if the first 3 charcaters are not ***, they will be the a field of study (mth,csc, etc)
					// and the line will represent "choose a ___ class of level ___ or higher"
					// "proffesional electives" follow this format
					for(int k = 0; k < transcript.getSize(); k++){
						if(reqs[i][1].substring(0,3).compareTo(transcript.get(k).substring(0,3)) == 0){
							if(reqs[i][1].substring(3,6).compareTo(transcript.get(k).substring(3,6)) > 0){
								//we found a class taken that matches criteria for a req
								matchFound = true;
								transcript.remove(k);
							}
						}
					}
					
				}	
			}else if(reqs[i][0].contains("*")){
				//we have found a * which represents that a class fulfilling it may also
				// be used to fulfill other requirements
				//The code in this section is identical to the "@" functionality, except is does not delete
				// the requirement from the transcript, so one class may fulffill multiple req's
				// in the requirements file these types of req's MUST GO FIRST
				if(reqs[i][1].substring(0,1) == "*"){
					if(reqs[i][1].substring(3,4) == "*"){
						if(transcript.get(0)!=null){
							matchFound = true;
							tempList.addElement(transcript.get(0));
							transcript.remove(0);
						}
					}else{
						for(int k = 0; k < transcript.getSize(); k++){
							if(reqs[i][1].substring(3,6).compareTo(transcript.get(k).substring(3,6)) > 0){
								matchFound = true;
								tempList.addElement(transcript.get(k));
								transcript.remove(k);
							}
						}
					}
				}else{
					for(int k = 0; k < transcript.getSize(); k++){
						if(reqs[i][1].substring(0,3).compareTo(transcript.get(k).substring(0,3)) == 0){
							if(reqs[i][1].substring(3,6).compareTo(transcript.get(k).substring(3,6)) > 0){
								matchFound = true;
								tempList.addElement(transcript.get(k));
								transcript.remove(k);
							}
						}
					}
					
				}	
				
				
			}else{
				//with no special rules, see if any transcript values match any
				// values in reqs[i]
				if(tempList != null){
					for(int x = 0; x<tempList.getSize();x++){
						transcript.addElement(tempList.get(x));
					}
					tempList = null;
				}
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
