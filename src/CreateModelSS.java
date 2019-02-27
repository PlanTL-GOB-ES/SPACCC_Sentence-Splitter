package SentenceSplitting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelType;

/*
 * This class is used to create the sentence splitter model for Spanish clinical cases using the Apache OpenNLP API.
 * Input:
 * - Training file, all documents' sentences splitted, one sentence per line, everything in a single file.
 * - Path where the final model will be printed.
 * - Name of the created model's file.
 * - File with a list of abbreviations, one abbreviation per line.
 * We recommend using the abbreviations' file in order to get a better model.
 * Output:
 * - Sentence splitting model.
 */

public class CreateModelSS {
	
	public static void main (String args[]) throws IOException
	{
		String trainFile = args[0];
		String outModel = args[1];
		String modelName = args[2];
		String abbrFile = args[3];
		
		// directory to save the model file that is to be generated, create this directory in prior
		File destDir = new File(outModel);
		
		// Load dictionary of abbreviations.
		Dictionary abbrDictionary = makeAbbrDictionary(abbrFile);
		
		// Load train set.
        InputStreamFactory in = new MarkableFileInputStreamFactory(new File(trainFile));
 
        // parameters used by machine learning algorithm, Maxent, to train its weights
        TrainingParameters mlParams = new TrainingParameters();
        mlParams.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(4000));
        mlParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(3)); 
        mlParams.put(TrainingParameters.TRAINER_TYPE_PARAM, EventTrainer.EVENT_VALUE);
        mlParams.put(TrainingParameters.ALGORITHM_PARAM, ModelType.MAXENT.name());
        
        // Train the model.
        SentenceDetectorFactory sentenceDetectorFactory = SentenceDetectorFactory.create(null, "es", true, abbrDictionary, ".?!".toCharArray());
        SentenceModel sentdetectModel = SentenceDetectorME.train(
                "es",
                new SentenceSampleStream(new PlainTextByLineStream(in, StandardCharsets.UTF_8)),
                sentenceDetectorFactory,
                mlParams);
  //              mlParams.defaultParams());
 
     // Print out the model into a file.
        File outFile = new File(destDir,modelName); 
        FileOutputStream outFileStream = new FileOutputStream(outFile); 
        sentdetectModel.serialize(outFileStream);		
	}
	
	/*
	 * This method reads the abbreviation list file and loads the complete list into a dictionary.
	 */
	public static Dictionary makeAbbrDictionary(String abbrFile) throws IOException
	{
		Dictionary dictionary = new Dictionary();
		
		Reader reader = new BufferedReader(new FileReader(abbrFile));		
		dictionary = Dictionary.parseOneEntryPerLine(reader);
		
		return dictionary;
	}
}
