package fr.univnantes.lina.uima.util.nlp;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.CasToInlineXml;
import fr.univnantes.lina.uima.util.AnalysisEngine;
import fr.univnantes.lina.uima.util.AnnotationCollectionUtils;
import fr.univnantes.lina.uima.util.AnnotationUtils;
import fr.univnantes.lina.uima.util.DocumentAnnotationUtils;
import fr.univnantes.lina.uima.util.JCasSofaViewUtils;
import fr.univnantes.lina.uima.util.UIMAUtils;
import fr.univnantes.lina.util.JavaUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Create a new view by substituting a list of source regex expressions to a list of target ones (target) 
 * <p>
 * This AE takes two parameters:
 * <ul>
 * <li><code>InputView</code> - view to perform the processing. Optional, by default 
 * <code>_InitialView</code></li>
 * <li><code>OutputView</code> - view to create with the substitution result. Mandatory.</li>
 * <li><code>RegexSources</code> - Regex expression to substitute to RegexTarget. Mandatory</li>
 * <li><code>RegexTargets</code> - Regex expression target. Optional</li>
 * </ul>
 * 
 * 
 */
public class RegexSubstituteAE extends AnalysisEngine {

	/*
	 * PARAMETERS NAMES
	 */
	/**
	 * Parameter name of the RegexSources
	 */
	public static final String PARAM_SOURCE_REGEX = "SourceRegex";

	/**
	 * Parameter name of the RegexTargets
	 */
	public static final String PARAM_TARGET_REGEX = "TargetRegex";

	/*
	 * PARAMETERS DEFAULT VALUES
	 */

	/*
	 * LOCAL VARIABLES
	 */
	private Pattern[] sourceRegexPatterns;
	private String[] targetRegexStringArray;

	/*
	 * Accessors
	 */


	/*
	 * Methods 
	 */

	/**
	 * Get the parameter values, setting the variables and checking the values 
	 */
	public void initialize(UimaContext aContext) throws ResourceInitializationException {

		// SUPER PARAMETER SETTINGS
		super.initialize(aContext);

		// CURRENT AE PARAMETER SETTINGS
		
		// Get the source regex
		String[] sourceRegexStringArray  = (String[]) aContext.getConfigParameterValue(PARAM_SOURCE_REGEX);

		// compile regular expressions
		sourceRegexPatterns = new Pattern[sourceRegexStringArray.length];
		for (int i = 0; i < sourceRegexStringArray.length; i++) {
			sourceRegexPatterns[i] = Pattern.compile(sourceRegexStringArray[i]);
		}

		// Get the target regex
		targetRegexStringArray = (String[]) aContext.getConfigParameterValue(PARAM_TARGET_REGEX);
		if (targetRegexStringArray == null) {
			for (int i = 0; i < sourceRegexStringArray.length; i++) {
				targetRegexStringArray[i] = "";
			}
		}
	}

	/**
	 * 
	 * Create a text by substituting source regex to target regex.
	 * 
	 * @return the dataString of the outputView to create
	 */
	@Override
	protected String processInputView(JCas inputViewJCas,
			FSIterator contextAnnotationsFSIter,
			HashMap<String, Integer> inputAnnotationStringHashMap,
			String inputFeatureString, JCas outputViewJCas,
			String outputAnnotationString, String ouputFeatureString)
	throws AnalysisEngineProcessException {

		String processedText = JCasSofaViewUtils.getSofaDataString(inputViewJCas);
		// for each sourceRegexPattern to proceed
		 for (int i = 0; i < sourceRegexPatterns.length; i++) {
			 Matcher m = sourceRegexPatterns[i].matcher(processedText);
			 processedText = m.replaceAll(targetRegexStringArray[i]);
		}
		 
		// return the outputText of the new outputview
		return processedText;
	}



}




