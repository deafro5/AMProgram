import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class CompareEngine {
	FileReader databaseReader = new FileReader();;
	String catalogues;
	String genEds;
	String[][] reqs;
	boolean matchFound = false;
	
	CompareEngine(String degree) throws IOException{
		reqs = databaseReader.fetchReqs(degree);
	}
	
	public String[][] getReqs(){
		return reqs;
	}
	
	public void resetReqs(String relation) throws IOException{
		reqs = databaseReader.fetchReqs(relation);
	}
	
	public ArrayList compare(DefaultListModel classlist) throws IOException{
		DefaultListModel<String> transcript = classlist;
		DefaultListModel<String> buffertranscript = new DefaultListModel();
		String recommend = null;
		ArrayList<String> recommendList = new ArrayList<String>();

		for(int i = 0; i<transcript.getSize();i++){
			buffertranscript.addElement(transcript.get(i));
		}
		
		
		for(int i = 0; i<reqs.length; i++){
			if(reqs[i][0].contains("@")){
				//we have found an @ which represents select any class > indicated value
				if(reqs[i][1].substring(0,2).contains("*")){
					if(reqs[i][1].substring(4,6).contains("*")){
						//in the requirement file, these sorts of reqs (*** ***) represent a 
						// "choose any class, free elective" and must be LAST in the file
						if(transcript.getSize() != 0){
							//any class meets this requirement, so just take one and run with it
							if(matchFound == false) transcript.remove(0);
							matchFound = true;
						}
					}else{
						//in the requirement file, these sorts of reqs (*** 300) represent a 
						// "choose any elective of a certain level"
						// in file must go BEFORE free electives, but AFTER professional electives
						for(int k = 0; k < transcript.getSize(); k++){
							if(reqs[i][1].substring(4,7).compareTo(transcript.get(k).substring(4,7)) < 0){
								//we found a class taken that matches criteria for a req
								if(matchFound == false){
									transcript.remove(k);
									k--;
								}
								matchFound = true;
							}
						}
					}
				}else{
					//if the first 3 charcaters are not ***, they will be the a field of study (mth,csc, etc)
					// and the line will represent "choose a ___ class of level ___ or higher"
					// "proffesional electives" follow this format
					for(int k = 0; k < transcript.getSize(); k++){
						if(reqs[i][1].substring(0,3).compareTo(transcript.get(k).substring(0,3)) == 0){
							if(reqs[i][1].substring(4,7).compareTo(transcript.get(k).substring(4,7)) < 0){
								//we found a class taken that matches criteria for a req
								if(matchFound == false){
									transcript.remove(k);
									k--;
								}
								matchFound = true;
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
				if(reqs[i][1].substring(0,2).contains("*")){
					if(reqs[i][1].substring(4,6).contains("*")){
						if(transcript.getSize()!=0){
							if(matchFound == false){
								buffertranscript.remove(0);
							}
							matchFound = true;
						}
					}else{
						for(int k = 0; k < buffertranscript.getSize(); k++){
							if(reqs[i][1].substring(4,7).compareTo(buffertranscript.get(k).substring(4,7)) < 0){
								if(matchFound == false){
									buffertranscript.remove(k);
									k--;
								}
								matchFound = true;
							}
						}
					}
				}else{
					for(int k = 0; k < transcript.getSize(); k++){
						if(reqs[i][1].substring(0,3).compareTo(transcript.get(k).substring(0,3)) == 0){
							if(reqs[i][1].substring(4,7).compareTo(transcript.get(k).substring(4,7)) < 0){
								if(matchFound == false){
									buffertranscript.remove(k);
									k--;
								}
								matchFound = true;
							}
						}
					}
					
				}	
				
				
			}else{
				//with no special rules, see if any transcript values match any
				// values in reqs[i]
				for(int j = 1; j< reqs[i].length; j++){
					for(int k = 0; k < transcript.getSize(); k++){
						if(reqs[i][j].equals(transcript.get(k))){
							//we found a class taken that matches criteria for a req
							if(matchFound == false)transcript.remove(k);
							matchFound = true;
						}
					}
				}
			}
			if(!matchFound){		//if we did not find a match
				recommend = "You need a class to fit:";	
				for(int j = 1; j < reqs[i].length; j++){	//provide the user a list of selections
					recommend = recommend + " " + reqs[i][j];
				}
				recommendList.add(recommend);
			}else{
				matchFound = false;
			}
			//requirement precedence. one class may not satisfy 2 requirements, but can.
			// required classes should take precedence over proffesional electives, which
			// take precedence over free electives. precedence is determined by order 
			// listed in requirements file
		}
		return recommendList;
	}
	
	//this method takes 2 recommendation lists, each generated by comparing the users
	// transcript to the requirements for each major, and the comparing the
	// two recommendations to each other to eliminate overlap and excess free electives
	public ArrayList DoubleMajor(ArrayList primary, ArrayList secondary){
		ArrayList<String> majorOne = primary;
		ArrayList<String> majorTwo = secondary;
		boolean matchFound = false;
		
		for(int i = 0; i<majorOne.size();i++){
			for(int j = 0; j<majorTwo.size();j++){
				if(majorOne.get(i).substring(majorOne.get(i).substring(24).indexOf(" ")).equals(
						majorTwo.get(j).substring(majorTwo.get(j).substring(24).indexOf(" ")))){
					if(matchFound == false){
						majorTwo.remove(j);
						j--;
						//whenever we find a shared element remove it from the second list
					}
					matchFound = true;
				}else if(majorTwo.get(j).substring(majorTwo.get(j).substring(24).indexOf(" ")).contains("*** ***")){
					majorTwo.remove(j);
					j--;
				}
			}
			if(!majorTwo.isEmpty()){
				if(majorOne.get(i).substring(majorOne.get(i).substring(24).indexOf(" ")).contains("*** ***")){
					majorOne.remove(i);
					i--;
				}
			}
			matchFound = false;
		}
		//after removing shared items + excess free electives, what remains
		// are classes needed. Append what's for major 2 onto major 1 list
		for(int k = 0; k<majorTwo.size();k++){
			majorOne.add(majorTwo.get(k));
		}
		
		//return shared list
		
		return majorOne;
		
		//a "bug" here is that it DOES NOT combine requirements. if the user needs to take
		// PHY 312 for one degree, and a 300+ level PHY course for the other, they are not
		// currently combined when they could be. This is due to under developed code 
		// based on time restraints for deployment.
	}
	
}
