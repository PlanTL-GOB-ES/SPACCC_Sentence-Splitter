package SentenceSplitting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/*
 * This class evaluates the performance of the sentence splitting model created with the class CreateModelSS.
 * Input:
 * - Test set folder (one file per document).
 * - Gold standard folder, one file per document and one sentence per line.
 * - Folder to print the results of the sentence splitting process with the created model, one sentence per line.
 * - Model file.
 * Output:
 * - Files with sentences splitted.
 * - Evaluation of the sentence splitter, showing the number of documents that match with the gold standard, with exactly the same output,
 * and the number of sentences per document that match with the gold sentences.
 */

public class EvaluateModelSS {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String testDir = args[0];
		String gsDir = args[1];
		String out = args[2];
		String modelFile = args[3];
		
		// Load the model
		SentenceModel model = new SentenceModel(new File(modelFile));
		SentenceDetectorME sentenceDetectorME = new SentenceDetectorME(model);
		
		// Variables for statistics.
		// Document level evaluation.
		double totalFiles = 0;
		double totalCorrect = 0;
		double totalCorrectSentencesIncluded = 0;
		double totalWrong = 0;
		double more = 0;
		double less = 0;
		
		// Sentence-level evaluation, used to get Precision, Recall and F-Measure.
		double totalSentencesGS = 0;
		double totalSentencesTest = 0;
		double totalSentencesCorrect = 0;
		double totalSentencesWrong = 0;
		
		/*
		 * Load each test file, split the sentences, and evaluate the output against the gold standard.
		 */
		File testFiles[] = new File(testDir).listFiles();
		for (int i = 0; i < testFiles.length; i++)
		{
			String name = testFiles[i].getName();
			
			// Load the test file.
			List<String> fullText = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(testDir + File.separator + name));
		    String line = "";
		    while ((line = reader.readLine()) != null)
		    {
		    	fullText.add(line);
		    }
		    reader.close();
			
		    // Load the Gold Standard file.
		    String gsName = name;
			List<String> gsSentenceList = new ArrayList<String>();
			BufferedReader reader2 = new BufferedReader(new FileReader(gsDir + File.separator + gsName));
		    String line2 = "";
		    while ((line2 = reader2.readLine()) != null)
		    {
		    	gsSentenceList.add(line2);
		    }
		    reader2.close();
		    String[] gsSentences = new String[gsSentenceList.size()];
		    gsSentences = gsSentenceList.toArray(gsSentences);	
		    
		 // Total sentences in the Gold Standard.
		    totalSentencesGS = totalSentencesGS + gsSentenceList.size();
	         
		    // Split sentences of the testing set and write the results in a new file.
		    int testSentences = 0;
		    List<String> testSentenceList = new ArrayList<String>();
		    BufferedWriter writer = new BufferedWriter(new FileWriter(out + File.separator + gsName));
		    for (int j = 0; j < fullText.size(); j++)
		    {
		    	String origLine = fullText.get(j);
		    	// Split the sentences of the line and save all sentences in an array.
		    	String[] sentences = sentenceDetectorME.sentDetect(origLine);
		    	for (int k = 0; k < sentences.length; k++)
		    	{
		    		writer.write(sentences[k] + "\n");
		    		testSentenceList.add(sentences[k]);
		    		testSentences++;
		    	}		    	
		    }
		    writer.close();
		    
		    // Total sentences in the testing set.
		    totalSentencesTest = totalSentencesTest + testSentenceList.size();
		    
		    // Check sentences' lists
		    totalFiles++;
		    if (testSentences == gsSentences.length)
		    {
		    	// Test and Gold Standard have the same length. High probability of guess.
		    	totalCorrect++;
		    	
		    	if (gsSentenceList.equals(testSentenceList))
		    	{
		    		// Test and Gold Standard are equal, full guess.
		    		totalCorrectSentencesIncluded++;
		    		totalSentencesCorrect = totalSentencesCorrect + testSentenceList.size();
		    	}
		    	else
		    	{
		    		// Test and Gold Standard are not equal.
		    		totalWrong++;
		    		
		    		// TODO
		    		// Check the number of sentences that match with the gold Standard.
		    		// (We didn't reach this situation when creating and evaluating the model).
		    	}
		    }
		    else
		    {
		    	// Test and Gold Standard do not have the same length.
		    	totalWrong++;
		    	System.out.println("Document " + name + " splitting does not match: " + testSentences + " vs " + gsSentences.length);
		    	
		    	// Compare the number of sentences in the Gold Standard and test set.
		    	if (testSentences > gsSentences.length)
			    {
		    		more++;
			    }
		    	else if (testSentences < gsSentences.length) 
		    	{
		    		less++;
		    	}
		    	
		    	// Check the number of sentences that match with the gold Standard.
		    	// Variables "j" and "k" indicate the positions of the current sentences we are analyzing.
		    	// "j" for the test set position.
		    	// "k" for the gold standard position.
		    	int k = 0; 
		    	for (int j = 0; j < testSentenceList.size(); j++)
		    	{
		    		String testSentence = testSentenceList.get(j);
		    		String gsSentence = gsSentenceList.get(k);
		    		if (testSentence.equals(gsSentence))
		    		{
		    			// We found the same sentence in test and GS
		    			totalSentencesCorrect++;
		    			k++;
		    		}
		    		else
		    		{
		    			// Sentences don't match in test and GS
		    			/*
		    			 * It could happen that the sentences in the test set were not correctly splitted,
		    			 * or they splitted when there was no need for it. 
		    			 * 
		    			 * In this algorithm, we consider that the correct Gold sentence could be inside the test sentence if the 
		    			 * test sentence is longer. Or the test sentence could be inside the Gold sentence if the Gold one is longer.
		    			 * 
		    			 * The following algorithm checks the next sentences after the failed ones, and update the positions after detecting the
		    			 * correct ones.  
		    			 */
		    			
		    			totalSentencesWrong++;
		    			System.out.println("\t" + testSentence);
		    			
		    			// In case we reached the end of the document, and the last sentences don't match, end the loop.
		    			if (k+1 == gsSentenceList.size() || j+1 == testSentenceList.size())
		    			{
		    				break;
		    			}

		    			if (testSentence.contains(gsSentence))
		    			{
		    				/*
		    				 * The sentence in the testing set is longer than the gold sentence, the gold sentence is part of the test sentence.
		    				 */
		    				int before = k;
		    				k++;
		    				String nextTest = testSentenceList.get(j+1);	// Get the next test sentence.
		    				String nextGS = gsSentenceList.get(k);			// Get the next gold sentence.
		    				
		    				boolean end = false;
		    				// Check the next sentences in the gold standard and stop when we meet the sentence that matches the one with the test.
		    				while (!nextTest.equals(nextGS))		    				
		    				{
		    					k++;		    					
		    					try
		    					{
		    						nextGS = gsSentenceList.get(k);
		    					}
		    					catch (Exception e)
		    					{
		    						// We didn't reach the sentence we were looking for, continue normally without updating the positions.
		    						end = true;
		    						break;
		    					}
		    				}
		    				if (end)
		    				{
		    					k = before;
		    				}
		    			}
		    			else if (gsSentence.contains(testSentence))
		    			{
		    				/*
		    				 * The sentence in the gold standard is longer than the test sentence, the test sentence is part of the gold sentence.
		    				 */
		    				int before = j;
		    				j++;
		    				String nextTest = testSentenceList.get(j);	// Get the next test sentence.
		    				String nextGS = gsSentenceList.get(k+1);	// Get the next gold sentence.
		    				
		    				boolean end = false;
		    				// Check the next sentences in the test set and stop when we meet the sentence that matches the one with the gold standard.
		    				while (!nextTest.equals(nextGS))
		    				{
		    					j++;
		    					try
		    					{
		    						nextTest = testSentenceList.get(j);
		    					}
		    					catch (Exception e)
		    					{
		    						// We didn't reach the sentence we were looking for, continue normally without updating the positions.
		    						end = true;
		    						break;
		    					}
		    				}
		    				if (!end)
		    				{
		    					j--;
		    				}
		    				else
		    				{
		    					j = before;
		    				}
		    				k++;
		    			}
		    			else
		    			{ 		
		    				/*
		    				 * Gold and test sentences do not match and are not part of each other.
		    				 * Depending on the number of sentences of test and gold, update the positions of current sentences for 
		    				 * the set with more sentences.
		    				 */
		    				if (testSentenceList.size() > gsSentenceList.size())
		    				{
		    					j++;
		    				}
		    				else if (testSentenceList.size() < gsSentenceList.size()) 
		    				{
		    					k++;
		    					j--;
		    				}
		    				else
		    				{
		    					k++;
		    				}
		    			}
		    		}
		    	}
		    }
		}
		
		
		/*
		 * Display the evaluation results for document level and sentence level.
		 */
		
		// The following lines show the number of documents that have equal sentence splitting results.
		System.out.println("Total correct with same number of sentences: " + totalCorrect);
		double percent = totalCorrect * 100 / totalFiles;
		System.out.println("Results by number of sentences: " + percent + "%");
		System.out.println("Total correct with same sentences: " + totalCorrectSentencesIncluded);
		double percentSameSentences = totalCorrectSentencesIncluded * 100 / totalFiles;
		System.out.println("Results by same sentences: " + percentSameSentences + "%");
		System.out.println("Total wrong: " + totalWrong);
		System.out.println("Total files: " + totalFiles);
		System.out.println("More sentences generated in test: " + more);
		System.out.println("More sentences found in gold standard: " + less);
		
		// The following lines show the number of sentences that have been correctly splitted,
		// together with the Precision, Recall and F-Measure.
		System.out.println("Sentences in Gold Standard: " + totalSentencesGS);
		System.out.println("Sentences in Test: " + totalSentencesTest);
		System.out.println("Sentences correct: " + totalSentencesCorrect);
		System.out.println("Sentences wrong: " + totalSentencesWrong);
		double precision = totalSentencesCorrect * 100 / totalSentencesTest;
		double recall = totalSentencesCorrect * 100 / totalSentencesGS;
		double F1 = (2 * precision * recall) / (precision + recall);
		System.out.println("Precision: " + precision);
		System.out.println("Recall: " + recall);
		System.out.println("F1: " + F1);
	}

}
