import java.io.IOException;

import javax.swing.DefaultListModel;

public class CompareEngine {
	FileReader databaseReader;
	String catalogues;
	String genEds;
	String[][] reqs;
	
	CompareEngine(){
		databaseReader = new FileReader();
	}
	
	public void compare(DefaultListModel transcript) throws IOException{
		reqs = databaseReader.fetchReqs("TestReq");
		
		
		
		for(int i = 0; i<reqs.length; i++){
			for(int j = 0; j<reqs[i].length; j++){
				System.out.print(reqs[i][j]);
			}
		}
	}
	
}
