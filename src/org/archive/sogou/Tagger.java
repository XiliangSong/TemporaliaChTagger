package org.archive.sogou;

import java.io.PrintStream;

import de.unihd.dbs.sogou.TemSogouTagger;

public class Tagger {
	
	private static void testUsage(){
		//1
		String NoTagFileDir = "collectionTest/NoTagVersion/";
		String TagFileDir = "collectionTest/TagVersion/";
		
		try {
			PrintStream logPrinter = new PrintStream("tag_log.txt");
			System.setErr(logPrinter);
			
			TemSogouTagger temSogouTagger = new TemSogouTagger(NoTagFileDir, TagFileDir);
			temSogouTagger.run();
			
			logPrinter.flush();
			logPrinter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		
	}
	
	
	
	/**
	 * This class performs Temporalia-Style tagging given the files generated by calling Preprocesor
	 * 
	 * @param NoTagFileDir the directory of the non-taggered files generated by calling Preprocesor
	 * @param TagFileDir:	the output directory, i.e., the directory to store the tagged files
	 * 
	 * **/
	
	public static void main(String []args){
		
		////example usage
		
		String NoTagFileDir = "collectionTest/NoTagVersion/";
		String TagFileDir = "collectionTest/TagVersion/";
		
		try {

			PrintStream logPrinter = new PrintStream("tag_log.txt");
			System.setErr(logPrinter);
			
			TemSogouTagger temSogouTagger = new TemSogouTagger(NoTagFileDir, TagFileDir);
			temSogouTagger.run();
			
			logPrinter.flush();
			logPrinter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}				
		
	}

}
