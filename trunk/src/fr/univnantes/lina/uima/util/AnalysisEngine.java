/** 
 * Analysis Engine 
 * Copyright (C) 2010  Nicolas Hernandez
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.univnantes.lina.uima.util;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;


/**
 * UIMA Analysis Engine
 * @author Nicolas Hernandez
 */
public abstract class AnalysisEngine extends JCasAnnotator_ImplBase {

	/**
	 * When you use the code source as an AE example, please redefine the 
	 * "Common component constants Default" and the "Common component parameter values"
	 */

	/** Common component constants
	 * To define when you create a new component 
	 * */
	
	/**
	 * Name of the component
	 */
	private String COMPONENT_NAME = "";
	private String COMPONENT_VERSION = "";
	private String COMPONENT_ID = COMPONENT_NAME + "-"
	+ COMPONENT_VERSION;
	//exception
	private final String MESSAGE_DIGEST = COMPONENT_ID+"_Messages";
	//tmp file
	//log
	
	
	/** Common component parameters in descriptor file*/
	// View name to consider as the view to process
	private static String PARAM_NAME_INPUT_VIEW = "InputView";
	// Type name of the annotations to consider as the context annotations in
	// which
	// the process will be performed
	private static String PARAM_NAME_CONTEXT_ANNOTATION = "ContextAnnotation";
	// Type name of the annotations to consider as the token units to be
	// processed
	private static String PARAM_NAME_INPUT_ANNOTATION = "InputAnnotation";
	// Feature name of the annotations to consider as the token units to be
	// processed
	private static String PARAM_NAME_INPUT_FEATURE = "InputFeature";
	// View name to consider as the view to receive the result
	private static String PARAM_NAME_OUTPUT_VIEW = "OutputView";
	// Type mime to consider for storing the result in the sofaDataString
	private static String PARAM_NAME_OUTPUT_VIEW_TYPE_MIME = "OutputViewTypeMime";	
	// Type name of the annotations to create as the analysis result
	private static String PARAM_NAME_OUTPUT_ANNOTATION = "OutputAnnotation";
	// Type name of the feature to create as the analysis result
	private static String PARAM_NAME_OUTPUT_FEATURE = "OutputFeature";
	// An identifier for the current run.
	// This identifier is added into all the annotations that are created during 
	// the current execution.
	private static String PARAM_NAME_RUNID = "RunId";


	/** Default common component parameter values in descriptor file**/
	// Default view name if none are specified by the view parameter
	private static String DEFAULT_INPUT_VIEW = "_InitialView";
	// Default context annotation name if none is specified by the context
	// annotation parameter
	private static String DEFAULT_CONTEXT_ANNOTATION = "uima.tcas.DocumentAnnotation";
	// Default annotation name if none are specified by the input annotation
	// parameter
	// Default feature name if none are specified by the input feature
	// parameter when the input annotation parameter is set
	public static String DEFAULT_INPUT_FEATURE = "coveredText";
	//public static String DEFAULT_INPUT_ANNOTATION = "TokenAnnotation";
	// Default annotation name if none are specified by the output annotation
	// parameter
	//public static String DEFAULT_OUTPUT_ANNOTATION = "fr.univnantes.lina.uima.shell.types.ShellAnnotation";
	// Default feature name if none are specified by the output feature
	// parameter
	//public static String DEFAULT_OUTPUT_FEATURE = "value";
	// Default type mime value for the sofaDataString in case of the output type is view
	private static String DEFAULT_OUTPUTVIEW_TYPEMIME = "text/plain";
	// 
	private static String INPUTTYPE_ANNOTATION = "annotation";
	private static String INPUTTYPE_VIEW = "view";
	protected static String OUTPUTTYPE_ANNOTATION = "annotation";
	private static String OUTPUTTYPE_VIEW = "view";



	/** Common component variables */
	private String runIdString = null;

	/**
	 * InputView List of view names to process
	 */
	protected String inputViewString = null;
	/**
	 * ContextAnnotation List of annotation names which delimits the text areas or the covered annotations to process
	 */
	protected String contextAnnotationString = null;
	//protected String inputAnnotationString = null;




	/*
	 * InputAnnotation	Ordered list of annotation names to consider as the Feature Structure index to process
	 */
	protected String[] inputAnnotationStringArray = null;
	protected HashMap<String, Integer> inputAnnotationStringHashMap = null;
	/*
	 * InputFeature Feature name of the annotations whose string value will be processed
	 */
	protected String inputFeatureString = null;
	/*
	 * OutputView View name to consider as the view to receive the result; to be created whether OutputAnnotation is empty or simply to edit if OutputAnnotation is defined
	 */
	protected String outputViewString = null;
	protected String outputViewTypeMimeString = null;
	/*
	 * OutputAnnotation Name of the annotation to create as the analysis result
	 */
	protected String outputAnnotationString = null;
	/*
	 * OutputFeature Feature name of the annotation whose string value will contain the analysis result
	 */
	protected String outputFeatureString = null;

	protected String inputType = "";
	protected String outputType = "";


	/**
	 * @see AnalysisComponent#initialize(UimaContext)
	 */
	public void initialize(UimaContext aContext)
	throws ResourceInitializationException {
		super.initialize(aContext);

		/** Get parameter values **/
		runIdString = (String) aContext
		.getConfigParameterValue(PARAM_NAME_RUNID); 

		inputViewString = (String) aContext.getConfigParameterValue(PARAM_NAME_INPUT_VIEW);
		if (inputViewString == null) {
			// If no input view is specified, we use the default (i.e. _InitialView)
			inputViewString = DEFAULT_INPUT_VIEW;
		}

		contextAnnotationString = (String) aContext
		.getConfigParameterValue(PARAM_NAME_CONTEXT_ANNOTATION);
		if (contextAnnotationString == null) {
			// If no context specified, we do over the whole CAS
			// en d'autres termes, on traite le uima.tcas.DocumentAnnotation
			contextAnnotationString = DEFAULT_CONTEXT_ANNOTATION;
		}
		// ... otherwise over segments covered by the contextAnnotation
		// parameter

		//inputAnnotationString = (String) aContext
		//.getConfigParameterValue(PARAM_NAME_INPUT_ANNOTATION); 

		inputAnnotationStringArray = (String[]) aContext
		.getConfigParameterValue(PARAM_NAME_INPUT_ANNOTATION);
		//System.out.println("Debug: après getConfigParameterValue inputAnnotationStringArray" +inputAnnotationStringArray + "size" + inputAnnotationStringArray.length);

		inputAnnotationStringHashMap = new HashMap<String, Integer>();

		// un getConfigParameterValue d'un parameter multivalued vide donne (parfois?) une variable tableau vide mais non null
		// donc ne peut être comparer à null
		// attention on ne peut comparer sa lengh qu'après un getConfigParameterValue sans quoi on obtiendrait un null
		// je décide de faire les 2
		//System.out.println("Debug: après getConfigParameterValue inputAnnotationStringArray" +inputAnnotationStringArray);

		if	(inputAnnotationStringArray != null) { 
			if 	(inputAnnotationStringArray.length != 0) {
				for (String inputAnnotationString : inputAnnotationStringArray) {
					inputAnnotationStringHashMap.put(inputAnnotationString, 1);
				}
			}
		}
		inputFeatureString = (String) aContext
		.getConfigParameterValue(PARAM_NAME_INPUT_FEATURE);
		if (
				((inputFeatureString != null)  && (inputAnnotationStringArray == null))
				||
				((inputFeatureString != null)  && ((inputAnnotationStringArray != null) && (inputAnnotationStringArray.length == 0)))
				|| 
				((inputFeatureString == null) && ((inputAnnotationStringArray != null) && (inputAnnotationStringArray.length != 0)))
		)
		{
			String errmsg = "Error: If one of the parameter " + PARAM_NAME_INPUT_ANNOTATION
			+ " or " + PARAM_NAME_INPUT_FEATURE
			+ " is defined, both must be !";

			//System.out.println("Debug: inputFeatureString " + inputFeatureString + " inputAnnotationStringArray" + inputAnnotationStringArray +":");
			//for (String s : inputAnnotationStringArray) {
			//	System.out.print(">"+ s + "<");
			//}System.out.println();

			throw new  ResourceInitializationException(errmsg,
					new Object[] {  });	
			//e.printStackTrace();
		}
		
		// Ce test intervient après avoir vérifier que le couple Annotation/Feature était bien complet
		//
		// Il est possible de ne pas avoir une méthode dédiée au traitement de vue 
		// et de ne pas y faire appel dans la méthode process 
		// en utilisant DocumentAnnotation et dans la méthode processContextAnnotation 
		// créer un index de InputAnnotation à partir des ContextAnnotation 
		// Rajoute le fait que l'on doit gérer un param supplémentaire le ContextFeature
		// Mais si on veut spécifier des ContextAnnotation a proprement parlé et pas des InputView
		// Il faut quand même rajouter ce param et  par conséquent il faudra tester sa co-existence avec ContextAnnotation
		// Donc le test et l'affectation suivante n'est là que si on décide de ne pas passer par processInputView en natif
		// On décide de ne pas ajouter le param ContextFeature donc le teste suivante sert pour 2 cas
		//
		// Si inputFeatureString est null c'est que soit ContextAnnotation soit InputView
		// car si InputAnnotation alors exception aurait été levée donc on le laisse par défaut
		if (inputFeatureString == null) {
			inputFeatureString = DEFAULT_INPUT_FEATURE;
		}

		outputViewString = (String) aContext.getConfigParameterValue(PARAM_NAME_OUTPUT_VIEW);
		if (outputViewString == null) {
			// If no output view is specified, we set it to inputViewString
			outputViewString = inputViewString;
		}
		outputViewTypeMimeString = (String) aContext.getConfigParameterValue(PARAM_NAME_OUTPUT_VIEW_TYPE_MIME);
		if (outputViewTypeMimeString == null) {
			// If no output view type mime is specified, we set it a default one
			outputViewTypeMimeString = DEFAULT_OUTPUTVIEW_TYPEMIME;
		}
		outputAnnotationString = (String) aContext
		.getConfigParameterValue(PARAM_NAME_OUTPUT_ANNOTATION); 
		outputFeatureString = (String) aContext
		.getConfigParameterValue(PARAM_NAME_OUTPUT_FEATURE); 
		// outputAnnotationString ET outputFeatureString doivent être initialisés les deux à la fois ou aucun d'eux
		if (((outputAnnotationString != null) && (outputFeatureString == null)) || ((outputAnnotationString == null) && (outputFeatureString != null)) ){
			String errmsg = "Error: If one of the parameter " + PARAM_NAME_OUTPUT_ANNOTATION
			+ " or " + PARAM_NAME_OUTPUT_FEATURE
			+ " is defined, both must be !";
			throw new  ResourceInitializationException(errmsg,
					new Object[] {  });	
			//e.printStackTrace();
		}

		// Si l'input_type est annotation, alors on va traiter chacune d'elle
		if (inputAnnotationStringArray != null) inputType = INPUTTYPE_ANNOTATION;
		// Sinon on va traiter le datastring de la vue
		else  {
			inputType = INPUTTYPE_VIEW;
			
			
		}

		if ((outputAnnotationString != null) && (outputFeatureString != null)) {	
			outputType = OUTPUTTYPE_ANNOTATION;
		}	else outputType = OUTPUTTYPE_VIEW;



		// String[] patternStrings = (String[])
		// aContext.getConfigParameterValue("Patterns");
		// mLocations = (String[])
		// aContext.getConfigParameterValue("Locations");

		// compile regular expressions
		// mPatterns = new Pattern[patternStrings.length];
		// for (int i = 0; i < patternStrings.length; i++) {
		// mPatterns[i] = Pattern.compile(patternStrings[i]);
		// }
		
		// dans le process
		// Vérifier que context, input et output AnnotationString  si !=null alors ont un type défini dans le TS
		// Vérifier aussi que l'input view existe

	}


	/**
	 * Depending on the InputAnnotation parameter, the InputType is either Annotation or View
	 * The current method routes toward the correct subprocess method
	 * @see JCasAnnotator_ImplBase#process(JCas)
	 */
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		/** -- process the analysis **/

		log("-----------------------------------------------------------------------------------------------------------------");
		if (inputType.equalsIgnoreCase(INPUTTYPE_ANNOTATION)) {
			log("Process the input annotation of a given type (potentially covered by a context annotation of a given type)");
			//		processContextAnnotations(aJCas, inputViewString, contextAnnotationString, inputAnnotationStringArray,  inputFeatureString, outputViewString, outputViewTypeMimeString, outputAnnotationString, outputFeatureString);
		}
		else {
			log("Process the input view; actually the default ContextAnnotation DocumentAnnotation");
		//	log("Process the input view");
		//	processInputViewType(aJCas, inputViewString, contextAnnotationString, inputAnnotationStringArray,  inputFeatureString, outputViewString, outputViewTypeMimeString, outputAnnotationString, outputFeatureString);
		}
		processContextAnnotations(aJCas, inputViewString, contextAnnotationString, inputAnnotationStringArray,  inputFeatureString, outputViewString, outputViewTypeMimeString, outputAnnotationString, outputFeatureString);
			
			
	}



	/**
	 * This method is invoked when the analysis has to be processed for   
	 * some input annotations which are covered by specific contextAnnotation.
	 * 
	 * @param aJCas
	 *            the CAS over which the process is performed
	 * @param inputViewString
	 * @param contextAnnotationType
	 *            Type name of the annotations to consider as the context
	 *            annotations in which the process will be performed
	 * @param inputAnnotationType
	 *            Type name of the annotations to consider as the token units to
	 *            be processed
	 * @param inputFeatureString
	 * @param outputViewString
	 * @param outputViewTypeMimeString
	 * @param outputAnnotationString
	 * @param ouputFeatureString
	 * @throws AnalysisEngineProcessException
	 */
	private void processContextAnnotations(JCas aJCas, String inputViewString, String contextAnnotationString, String[] inputAnnotationStringArray, String inputFeatureString, String outputViewString, String outputViewTypeMimeString, String outputAnnotationString, String ouputFeatureString) throws AnalysisEngineProcessException {

		/** -- Prepare the view to be processed**/
		log("Getting the inputViewJCas");
		JCas inputViewJCas = UIMAUtilities.getView(aJCas,inputViewString);

		/** -- In case of the output type is annotation, get the view to store the result **/
		// si les param annotations sont renseignés alors cela signifie que l'on
		// suppose qu'une vue existe pour accueillir les annotations
		// on effectue ici le getView pour d'éviter de le faire à chaque tour de boucle 
		// si l'inputType est annotation
		JCas outputViewJCas = null;
		if (outputType.equalsIgnoreCase(OUTPUTTYPE_ANNOTATION)) {
			log("Getting the outputViewJCas");
			outputViewJCas = UIMAUtilities.getView(aJCas,outputViewString);		
		}	

		// Récupère les types de context annotations 
		AnnotationIndex<Annotation> contextAnnotationIndex = null; 
		Type contextAnnotationType = null;
		contextAnnotationType = UIMAUtilities.getType(inputViewJCas, contextAnnotationString);

		log("Getting the Context Annotation index");
		// Récupère une liste de contexts
		// Récupération d'index d'annotations à partir de type d'annotation!
		// soit comme cela
		//   AnnotationIndex idxMonType = (AnnotationIndex)
		//   cas.getAnnotationIndex(inputAnnotationType);
		//   FSIterator monTypeIt = idxMonType.iterator();
		//   while (monTypeIt.hasNext()) {
		//      On peut le manipuler comme on veut ...
		//   }
		// soit comme cela
		// sans reflect
		//    FSIndex tokenAnnotationFSIdx =
		//    aJCas.getAnnotationIndex(TokenAnnotation.type);
		// avec reflect
		//    FSIndex<Annotation> inputAnnotationFSIdx = aJCas
		//    .getAnnotationIndex(inputAnnotationType);
		Iterator<Annotation> contextAnnotationIndexIterator = null;
		contextAnnotationIndex = (AnnotationIndex<Annotation>) inputViewJCas
		.getAnnotationIndex(contextAnnotationType);
		contextAnnotationIndexIterator = contextAnnotationIndex.iterator();

		// var to concat the results in case of a view as the output type 
		String commandResultString = "";

		// Pour chaque context 
		while (contextAnnotationIndexIterator.hasNext()) {

			// Context Annotation suivante de même type
			Annotation contextAnnotation = null ;
			contextAnnotation = (Annotation) contextAnnotationIndexIterator.next();

			// Récupère le type d'input annotations 
			//				Type inputAnnotationType = null;
			//				inputAnnotationType = UIMAUtilities.getType(inputViewJCas, inputAnnotationString);
			//Type inputAnnotationType = null;
			//inputAnnotationType = UIMAUtilities.getType(inputViewJCas, inputAnnotationStringHashMap.keySet().iterator().next());


			// Si InputAnnotation n'est pas renseigné 
			// alors on traite la valeur String d'une certaine feature de chaque ContextAnnotation
			// Par défaut ContextAnnotation est au moins égal à DocumentAnnotation
			// et possède la feature coveredText
			if (inputAnnotationStringHashMap.isEmpty()) {
				//System.out.println("Debug: contextAnnotationString"+contextAnnotationString);
				inputAnnotationStringHashMap.put(contextAnnotationString, 1);
				log("Building an Input Annotations index from the ContextAnnotations");
			}
			// Si InputAnnotation est renseigné 
			// alors on construit un iterator ordonné à partir de celles renseignées
			//else 
			{
				log("Getting the Input Annotations index ");
			}
				// Récupération de la liste des inputAnnotation
				//Iterator<Annotation> inputAnnotationIterator = null;
				//inputAnnotationIterator = inputViewJCas.getAnnotationIndex(inputAnnotationType).subiterator(contextAnnotation);
				FSIterator contextualizedInputAnnotationsFSIter = null;
				contextualizedInputAnnotationsFSIter = UIMAUtilities.subiterator(inputViewJCas, contextAnnotation, inputAnnotationStringHashMap,false);

				commandResultString = processInputAnnotations(inputViewJCas,
						contextualizedInputAnnotationsFSIter, inputFeatureString,
						outputViewJCas, outputAnnotationString);
			
		}


		/** -- Create view **/
		// output_v_string est défini ; potentiellement il est égal à input_v ; normalement la vue n'existe pas et est à créer
		if (outputType.equalsIgnoreCase(OUTPUTTYPE_VIEW)) {
			log("Creating output view");
			// ici on suppose que outputViewString ne correspond à aucune vue existante (a fortiori est différent de inputViewString) 
			// et que createView génèrera une erreur si la vue existe déjà
			UIMAUtilities.createView(aJCas, outputViewString, commandResultString, outputViewTypeMimeString);
		}

	}


	/**
	 * Process each InputAnnotation by analyzing the value of its InputFeature
	 * If some OutputAnnotations are specified then they are created and their OutFeature is set with the analysis result 
	 * (Else) it returns the concatenation of the analysis result for each InputAnnotation
	 * 
	 * @param inputViewJCas
	 * @param contextualizedInputAnnotationsFSIter
	 * @param inputFeatureString
	 * @param outputViewJCas
	 * @param outputAnnotationString
	 * 
	 * @return returns the concatenation of the analysis result for each InputAnnotation
	 * 
	 * @throws AnalysisEngineProcessException
	 */
	protected String processInputAnnotations(JCas inputViewJCas,
			FSIterator contextualizedInputAnnotationsFSIter,
			String inputFeatureString, JCas outputViewJCas,
			String outputAnnotationString)
	throws AnalysisEngineProcessException {

		log("Debug: AnalysisEngine - processInputAnnotations");

		String commandResultString = "";

		// Pour chaque inputAnnotation présent dans le context 
		while (contextualizedInputAnnotationsFSIter.hasNext()) {

			// Récupère le texte à traiter et ses offsets qui pourront éventuellement servir
			// si l'outputType est Annotation
			log("Getting the current annotation to be proceeded");

			// Récupère et cast l'inputAnnotation courante à manipuler
			Object annotationObject = contextualizedInputAnnotationsFSIter.next();

			Class  annotationClass = annotationObject.getClass();
			//if (annotationClass != null ) {				
			String className = "null";
			className = annotationClass.getName(); //.toString(
			//System.out.println("Debug: class>"+className+"<");
			Class<Annotation> inputAnnotationClass = UIMAUtilities.getClass(className);

			Annotation inputAnnotation = (Annotation) annotationObject;
			inputAnnotationClass.cast(inputAnnotation);
			//System.out.println("inputAnnotationType.getName()>"+inputAnnotation.getType().getName()+"<");
			//System.out.println("inputAnnotation.coveredText>"+inputAnnotation.getCoveredText()+"<");
			//System.out.println("inputAnnotation.begin>"+inputAnnotation.getBegin()+"<");
			//System.out.println("inputAnnotation.end>"+inputAnnotation.getEnd()+"<");

			// Invoque la récupération de la valeur dont l'inputFeatureString est spécifiée pour l'annotation courante 
			String inputTextToProcess = "" ;
			// inputTextToProcess = inputAnnotation.getCoveredText();

			inputTextToProcess = UIMAUtilities.invokeStringGetterMethod(inputAnnotation, UIMAUtilities.getStringGetterMethod(inputAnnotationClass,inputFeatureString));
			//log ("Debug: inputTextToProcess>"+inputTextToProcess+"<");


			int beginFeatureValueFromAnnotationToCreate; 
			int endFeatureValueFromAnnotationToCreate; 
			beginFeatureValueFromAnnotationToCreate = inputAnnotation.getBegin(); 
			endFeatureValueFromAnnotationToCreate= inputAnnotation.getEnd(); 

			/** -- Execute and get result **/
			log("Executing and getting the result");
			String commandLocalResultString = "";
			commandLocalResultString =  processAnnotationFeatureStringValue (inputViewJCas, inputTextToProcess, beginFeatureValueFromAnnotationToCreate, endFeatureValueFromAnnotationToCreate);

			// Soit pour chaque annotation en entrée à traiter soit pour la vue en entrée
			if (outputType.equalsIgnoreCase(OUTPUTTYPE_ANNOTATION)) {
				/** -- Create annotation**/
				log("Creating output annotation");
				UIMAUtilities.createAnnotation(outputViewJCas,outputAnnotationString, beginFeatureValueFromAnnotationToCreate,endFeatureValueFromAnnotationToCreate,outputFeatureString,commandLocalResultString);
			}
			else { 
				// L'output_type est view
				// On stocke les résultats obtenus pour chaque annotation
				// On copiera le tout dans le sofaDataString en une seule fois
				log("Concating the result");
				commandResultString += 
					commandLocalResultString;
			}
		}
		return commandResultString;
	}

	/**
	 * This method is invoked when the analysis has to be processed for some views 
	 *  
	 * @param aJCas
	 *            the CAS over which the process is performed
	 * @param inputViewString
	 * @param contextAnnotationType
	 *            Type name of the annotations to consider as the context
	 *            annotations in which the process will be performed
	 * @param inputAnnotationType
	 *            Type name of the annotations to consider as the token units to
	 *            be processed
	 * @param inputFeatureString
	 * @param outputViewString
	 * @param outputViewTypeMimeString
	 * @param outputAnnotationString
	 * @param ouputFeatureString
	 * @throws AnalysisEngineProcessException
	 */
	private void processInputViewType(JCas aJCas, String inputViewString, String contextAnnotationString, String[] inputAnnotationStringArray, String inputFeatureString, String outputViewString, String outputViewTypeMimeString, String outputAnnotationString, String ouputFeatureString) throws AnalysisEngineProcessException {

		/** -- Prepare the view to be processed**/
		log("Getting the inputViewJCas");
		JCas inputViewJCas = UIMAUtilities.getView(aJCas,inputViewString);

		/** -- In case of the output type is annotation, get the view to store the result **/
		// si les param annotations sont renseignés alors cela signifie que l'on
		// suppose qu'une vue existe pour accueillir les annotations
		// on effectue ici le getView pour d'éviter de le faire à chaque tour de boucle 
		// si l'inputType est annotation
		JCas outputViewJCas = null;
		String inputTextToProcess = "" ;
		inputTextToProcess = inputViewJCas.getSofaDataString();
		int beginFeatureValueFromAnnotationToCreate = 0; 
		int endFeatureValueFromAnnotationToCreate = inputViewJCas.getSofaDataString().length(); //+1; 

		/** -- Execute and get result **/
		log("Executing and gettint the result");
		String commandResultString = "";
		commandResultString =  processAnnotationFeatureStringValue (inputViewJCas, inputTextToProcess, beginFeatureValueFromAnnotationToCreate, endFeatureValueFromAnnotationToCreate);


		// Soit pour chaque annotation en entrée à traiter soit pour la vue en entrée
		if (outputType.equalsIgnoreCase(OUTPUTTYPE_ANNOTATION)) {
			/** -- Create annotation**/
			log("Getting the outputViewJCas");
			outputViewJCas = UIMAUtilities.getView(aJCas,outputViewString);	

			log("Creating output annotation");
			//createANewAnnotation(aJCas, inputAnnotation.getBegin(),inputAnnotation.getEnd(),commandLocalResultString);
			UIMAUtilities.createAnnotation(outputViewJCas,outputAnnotationString, beginFeatureValueFromAnnotationToCreate,endFeatureValueFromAnnotationToCreate,outputFeatureString,commandResultString);
		}
		else { 
			/** -- Create view**/
			// L'output_type est view
			// On stocke les résultats obtenus pour chaque annotation
			// On copiera le tout dans le sofaDataString en une seule fois
			//if (commandResultString == null ) {commandResultString = commandLocalResultString;}
			//else {
			log("Creating output view");
			// ici on suppose que outputViewString ne correspond à aucune vue existante (a fortiori est différent de inputViewString) 
			// et que createView génèrera une erreur si la vue existe déjà
			UIMAUtilities.createView(aJCas, outputViewString, commandResultString, outputViewTypeMimeString);

		}	

	}


	/**
	 * This abstract method corresponds to the actual process to perform 
	 * on the String Value of a given Feature of a given Annotation 
	 * 
	 * @param inputViewJCas the CAS view that will be processed. 
	 * @param inputTextToProcess the text to process (actually it corresponds to the String Value of a given Feature of a given Annotation )
	 * @param beginFeatureValue the begin offset of the Annotation whose one Feature is going to be processed
	 * @param endFeatureValue the end offset of the Annotation whose one Feature is going to be processed
	 * 
	 * @throws AnalysisEngineProcessException if something wrong happened
	 * while processing this CAS view. 
	 * 
	 * @return return the result of the performed processing 
	 */
	protected abstract String processAnnotationFeatureStringValue(JCas inputViewJCas, String inputTextToProcess, int beginFeatureValue, int endFeatureValue) throws AnalysisEngineProcessException;

	/**
	 * Log messages
	 * @param message to log 
	 */
	protected void log(String message) {
		//getContext()
		//.getLogger()
		//.log(Level.FINEST,	COMPONENT_ID + "- "+ message);
		System.out.println(COMPONENT_ID + "- "+ message);
	}


	/**
	 * @return the cOMPONENT_NAME
	 */
	protected String getCOMPONENT_NAME() {
		return COMPONENT_NAME;
	}


	/**
	 * @param cOMPONENTNAME the cOMPONENT_NAME to set
	 */
	protected void setCOMPONENT_NAME(String cOMPONENTNAME) {
		COMPONENT_NAME = cOMPONENTNAME;
	}


	/**
	 * @return the cOMPONENT_VERSION
	 */
	protected String getCOMPONENT_VERSION() {
		return COMPONENT_VERSION;
	}


	/**
	 * @param cOMPONENTVERSION the cOMPONENT_VERSION to set
	 */
	protected void setCOMPONENT_VERSION(String cOMPONENTVERSION) {
		COMPONENT_VERSION = cOMPONENTVERSION;
	}


	/**
	 * @return the cOMPONENT_ID
	 */
	protected String getCOMPONENT_ID() {
		return COMPONENT_ID;
	}


	/**
	 * @param cOMPONENTID the cOMPONENT_ID to set
	 */
	protected void setCOMPONENT_ID(String cOMPONENTID) {
		COMPONENT_ID = cOMPONENTID;
	}


	/**
	 * @return the iNPUTTYPE_ANNOTATION
	 */
	protected static String getINPUTTYPE_ANNOTATION() {
		return INPUTTYPE_ANNOTATION;
	}


	/**
	 * @param iNPUTTYPEANNOTATION the iNPUTTYPE_ANNOTATION to set
	 */
	protected static void setINPUTTYPE_ANNOTATION(String iNPUTTYPEANNOTATION) {
		INPUTTYPE_ANNOTATION = iNPUTTYPEANNOTATION;
	}


	/**
	 * @return the iNPUTTYPE_VIEW
	 */
	protected static String getINPUTTYPE_VIEW() {
		return INPUTTYPE_VIEW;
	}


	/**
	 * @param iNPUTTYPEVIEW the iNPUTTYPE_VIEW to set
	 */
	protected static void setINPUTTYPE_VIEW(String iNPUTTYPEVIEW) {
		INPUTTYPE_VIEW = iNPUTTYPEVIEW;
	}


	/**
	 * @return the oUTPUTTYPE_ANNOTATION
	 */
	protected static String getOUTPUTTYPE_ANNOTATION() {
		return OUTPUTTYPE_ANNOTATION;
	}


	/**
	 * @param oUTPUTTYPEANNOTATION the oUTPUTTYPE_ANNOTATION to set
	 */
	protected static void setOUTPUTTYPE_ANNOTATION(String oUTPUTTYPEANNOTATION) {
		OUTPUTTYPE_ANNOTATION = oUTPUTTYPEANNOTATION;
	}


	/**
	 * @return the oUTPUTTYPE_VIEW
	 */
	protected static String getOUTPUTTYPE_VIEW() {
		return OUTPUTTYPE_VIEW;
	}


	/**
	 * @param oUTPUTTYPEVIEW the oUTPUTTYPE_VIEW to set
	 */
	protected static void setOUTPUTTYPE_VIEW(String oUTPUTTYPEVIEW) {
		OUTPUTTYPE_VIEW = oUTPUTTYPEVIEW;
	}


	/**
	 * @return the runIdString
	 */
	protected String getRunIdString() {
		return runIdString;
	}


	/**
	 * @param runIdString the runIdString to set
	 */
	protected void setRunIdString(String runIdString) {
		this.runIdString = runIdString;
	}


	/**
	 * @return the inputViewString
	 */
	protected String getInputViewString() {
		return inputViewString;
	}


	/**
	 * @param inputViewString the inputViewString to set
	 */
	protected void setInputViewString(String inputViewString) {
		this.inputViewString = inputViewString;
	}


	/**
	 * @return the contextAnnotationString
	 */
	protected String getContextAnnotationString() {
		return contextAnnotationString;
	}


	/**
	 * @param contextAnnotationString the contextAnnotationString to set
	 */
	protected void setContextAnnotationString(String contextAnnotationString) {
		this.contextAnnotationString = contextAnnotationString;
	}


	/**
	 * @return the inputAnnotationString
	 */
	protected String[] getInputAnnotationString() {
		return inputAnnotationStringArray;
	}


	/**
	 * @param inputAnnotationString the inputAnnotationString to set
	 */
	protected void setInputAnnotationString(String[] inputAnnotationStringArray) {
		this.inputAnnotationStringArray = inputAnnotationStringArray;
	}


	/**
	 * @return the inputFeatureString
	 */
	protected String getInputFeatureString() {
		return inputFeatureString;
	}


	/**
	 * @param inputFeatureString the inputFeatureString to set
	 */
	protected void setInputFeatureString(String inputFeatureString) {
		this.inputFeatureString = inputFeatureString;
	}


	/**
	 * @return the outputViewString
	 */
	protected String getOutputViewString() {
		return outputViewString;
	}


	/**
	 * @param outputViewString the outputViewString to set
	 */
	protected void setOutputViewString(String outputViewString) {
		this.outputViewString = outputViewString;
	}


	/**
	 * @return the outputViewTypeMimeString
	 */
	protected String getOutputViewTypeMimeString() {
		return outputViewTypeMimeString;
	}


	/**
	 * @param outputViewTypeMimeString the outputViewTypeMimeString to set
	 */
	protected void setOutputViewTypeMimeString(String outputViewTypeMimeString) {
		this.outputViewTypeMimeString = outputViewTypeMimeString;
	}


	/**
	 * @return the outputAnnotationString
	 */
	protected String getOutputAnnotationString() {
		return outputAnnotationString;
	}


	/**
	 * @param outputAnnotationString the outputAnnotationString to set
	 */
	protected void setOutputAnnotationString(String outputAnnotationString) {
		this.outputAnnotationString = outputAnnotationString;
	}


	/**
	 * @return the outputFeatureString
	 */
	protected String getOutputFeatureString() {
		return outputFeatureString;
	}


	/**
	 * @param outputFeatureString the outputFeatureString to set
	 */
	protected void setOutputFeatureString(String outputFeatureString) {
		this.outputFeatureString = outputFeatureString;
	}


	/**
	 * @return the inputType
	 */
	protected String getInputType() {
		return inputType;
	}


	/**
	 * @param inputType the inputType to set
	 */
	protected void setInputType(String inputType) {
		this.inputType = inputType;
	}


	/**
	 * @return the outputType
	 */
	protected String getOutputType() {
		return outputType;
	}


	/**
	 * @param outputType the outputType to set
	 */
	protected void setOutputType(String outputType) {
		this.outputType = outputType;
	}



}