package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import Utils.FileIO;

public class Config {

	public boolean autoScroll;
	
	public String encryptionKey;
	
	public boolean LOG;
	
	public String logPath;
	public Config(String path){
		encryptionKey = "";
		String config = FileIO.readFile(path);
	    try {
	    	//new fangled lambda iteration, works better than buffered reader
				try(Stream<String> stream = Files.lines(Paths.get(path))){
					stream.forEach(line->{
					    if(line.startsWith("scroll")){
					    	if (line.contains("true")) autoScroll = true;
					    	else autoScroll = false;
					    }else if (line.contains("log =")){
					    	if (line.contains("true")) LOG = true;
					    }else if (line.contains("logPath")){
					    	System.out.println(line.substring(10));
					    	logPath = line.substring(10);
					    }
					});
				}				    
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
