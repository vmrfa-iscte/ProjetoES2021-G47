package G47.Grupo47;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DirExplorer {
	
    private File fileToSearch; //Directory chosen to search for .java Files
    private ArrayList<MethodMetrics> accumulateMetrics; //List where all metrics will be accumulated
    private int currentMethod_id; //Last method's method_id to keep track of current method_id
    private static int START_LEVEL = 0,FIRST_METHOD_ID = 1;
    private static String FILE_ENDING = ".java";

    public DirExplorer(File fileToSearch) {
        this.fileToSearch = fileToSearch;
        this.accumulateMetrics = new ArrayList<>();
        this.currentMethod_id = FIRST_METHOD_ID;
    }
           
    public ArrayList<MethodMetrics> exploreAndExtract() throws FileNotFoundException {
        return exploreAndExtract(START_LEVEL, "",fileToSearch);
    }
    
    private ArrayList<MethodMetrics> exploreAndExtract(int level, String currentPath,File currentFile) throws FileNotFoundException {
    	if (currentFile.isDirectory()) goThroughFiles(currentFile,level,currentPath); //If currentFile is a directory keep searching for files
    	else verifyAndExtractMetrics(currentFile,currentPath); //If current file is not a directory verify and extract metrics for that file
    	return accumulateMetrics;
    }
    
    private void verifyAndExtractMetrics(File currentFile,String filePath) throws FileNotFoundException {
    	if(filePath.endsWith(FILE_ENDING)) { //Verify if the currentFile is a java file
    		ExtractMetrics extractMetricsFromFile = new ExtractMetrics(currentFile); //Creating ExtractMetrics object with currentFile
    		extractMetricsFromFile.doExtractMetrics(accumulateMetrics,currentMethod_id); //Extract metrics for given file (currentFile) given the current method_id and metrics list
    		currentMethod_id = extractMetricsFromFile.getCurrentMethodID(); //Update current method_id
    	}
    }
    
    private void goThroughFiles(File currentFile,int level,String currentPath) throws FileNotFoundException {
    	for (File child : currentFile.listFiles()) {
			exploreAndExtract(level + 1, currentPath + "/" + child.getName(), child); //Recursive, going through every file inside given file (currentFile) when its a Directory
		}
    }
}
