package SentenceSplitting;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/*
 * This script splits all sentences of a text file.
 * Input:
 * - Text file.
 * - Sentence splitting model.
 * Output: 
 * - All sentences of the text file splitted, one sentence per line.
 * 
 * This script only works with sentence splitting models created with Apache OpenNLP.
 */

public class SentenceSplitter {

	private String file;
	private String model;
	
	private List<String> fullText;
	private SentenceDetectorME sentenceDetectorME;
	
	public SentenceSplitter(String file, String model)
	{
		this.file = file;
		this.model = model;
		
		fullText = new ArrayList<String>();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try
		{
			if (args.length != 2)
			{
				System.out.println("USAGE:\t" + "java -jar SentenceSplitter.jar TXT_FILE MODEL_FILE");
			}
			else
			{
				String file = args[0];
				String model = args[1];

				SentenceSplitter sentenceSplitter = new SentenceSplitter(file, model);
				sentenceSplitter.start();
			}			
		}
		catch (Exception e)
		{
			System.out.println("USAGE:\t" + "java -jar SentenceSplitter.jar TXT_FILE MODEL_FILE");
			e.printStackTrace();
		}		
	}

	public void start() throws IOException
	{
		loadModel();
		readTextFile();
		splitSentences();
	}
	
	public void loadModel() throws FileNotFoundException, IOException
	{
		SentenceModel sentenceModel = new SentenceModel(new FileInputStream(model));
        sentenceDetectorME = new SentenceDetectorME(sentenceModel);
	}
	
	public void readTextFile() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
	    String line = "";
	    while ((line = reader.readLine()) != null)
	    {
	    	fullText.add(line);
	    }
	    reader.close();
	}
	
	public void splitSentences()
	{
		for (int j = 0; j < fullText.size(); j++)
	    {
	    	String origLine = fullText.get(j);
	    	String[] sentences = sentenceDetectorME.sentDetect(origLine);

	    	for(int k = 0; k < sentences.length; k++)
	    	{
	            System.out.println(sentences[k]);
	        }
	    }
	}
}
