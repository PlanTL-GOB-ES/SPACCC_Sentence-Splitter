# The Sentence Splitter (SS) for Clinical Cases Written in Spanish

## Digital Object Identifier (DOI) and access to dataset files
Zenodo link

## Introduction
This repository contains the sentence splitting model trained using the SPACCC_SPLIT corpus (https://github.com/PlanTL-SANIDAD/SPACCC_SPLIT). The model was trained using the 90% of the corpus (900 clinical cases) and tested against the 10% (100 clinical cases). This model is a great resource to split sentences in biomedical documents, specially clinical cases written in Spanish.

This model was created using the Apache OpenNLP machine learning toolkit (https://opennlp.apache.org/), with the release number 1.8.4, released in December 2017. 

This repository contains the model, training set, testing set, Gold Standard, executable file, and the source code.

## Prerequisites
This software has been compiled with Java SE 1.8 and it should work with recent versions. You can download Java from the following website: https://www.java.com/en/download

The executable file already includes the Apache OpenNLP dependencies inside, so the download of this toolkit is not necessary. However, you may download the latest version from this website: https://opennlp.apache.org/download.html

The library file we have used to compile is "opennlp-tools-1.8.4.jar". The source code should be able to compile with the latest version of OpenNLP, "opennlp-tools-RELEASE_NUMBER.jar". In case there are compilation or execution errors, please let us know and we will make all the necessary updates.

## Directory structure
<pre>
exec/
  An executable file that can be used to apply the sentence splitter to your documents. You can find the notes about its execution below in section "_USAGE".

gold_standard/
  The clinical cases used as gold standard to evaluate the model's performance.
  
model/
  The sentence splitting model, a binary file.
  
src/
  The source code to create the model (CreateModelSS.java) and evaluate it (EvaluateModelSS.java). 
  The directory includes an example about how to use the model inside your code (SentenceSplitter.java).
  File "abbreviations.dat" contains a list of abbreviations, essential to build the model.

test_set/
  The clinical cases used as test set to evaluate the model's performance.

train_set/
  The clinical cases used to build the model. We use a single file with all documents present in 
  directory "train_set_docs" concatented.

train_set_docs/
  The clinical cases used to build the model. For each record the sentences are already splitted.

</pre>

## Usage

## Model creation

## Model evaluation

## Contact

Ander Intxaurrondo (ander.intxaurrondo@bsc.es)


## License

<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.

Copyright (c) 2018 Secretaría de Estado para el Avance Digital (SEAD)

