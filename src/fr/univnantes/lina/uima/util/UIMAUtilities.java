/** 
 * UIMA Utilities
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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIntConstraint;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FSTypeConstraint;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import fr.univnantes.lina.util.JavaUtilities;

/**
 * @author hernandez
 *
 */
public class UIMAUtilities {



	/**
	 * Return the view of a jcas corresponding to the given view name
	 * @param aJCas
	 * @param viewNameString string
	 * @return viewJCas
	 * @throws AnalysisEngineProcessException
	 */
	public static JCas getView(JCas aJCas, String viewNameString) throws AnalysisEngineProcessException {
		JCas viewJCas = null;
		try {
			viewJCas = aJCas.getView(viewNameString);
		} catch (CASException exception) {
			String errmsg = "ERROR: The view " + viewNameString
			+ " does not exist in the JCAS!";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { viewNameString },exception);
			// http://uima.apache.org/downloads/releaseDocs/2.3.0-incubating/docs/api/org/apache/uima/analysis_engine/AnalysisEngineProcessException.html
			// http://uima.apache.org/downloads/releaseDocs/2.3.0-incubating/docs/api/constant-values.html#org.apache.uima.UIMAException.STANDARD_MESSAGE_CATALOG
		}
		return viewJCas;
	}


	/**
	 * Return the sofaDataString of a JCAS corresponding to the given view 
	 * @param aJCas
	 * @return inputSofaDataString
	 * @throws AnalysisEngineProcessException
	 */
	public static String getSofaDataString(JCas aJCas) throws AnalysisEngineProcessException {
		String inputSofaDataString = null ; 
		inputSofaDataString = aJCas.getSofaDataString();

		if (inputSofaDataString == null) {
			String errmsg = "ERROR: The given view " + aJCas.toString()
			+ " does not contain a sofaDataString!";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { });
		}
		return inputSofaDataString;
	}


	/**
	 * Get the type of a given annotation name and check if it exists
	 * @param aJCas
	 * @param annotationString
	 * @return annotationType
	 * @throws AnalysisEngineProcessException
	 */
	public static Type getType(JCas aJCas, String annotationString) throws AnalysisEngineProcessException {

		// récupère le type context à partir de la String le désignant
		// et vérifie son existence dans le Type System
		Type annotationType = null; 
		annotationType = aJCas.getTypeSystem().getType(
				annotationString);
		// On s'assure que le type existe bien
		if ((annotationType == null)) {
			String errmsg = "Error: Type " + annotationString
			+ " is not defined in the Type System !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { annotationType });
		}
		return annotationType;
	}

	/**
	 * Get the class of a given annotation name in order to cast annotations latter
	 * Allow to know the type of the annotation to handle only at the runtime level
	 * @param annotationString
	 * @return annotationString
	 * @throws AnalysisEngineProcessException
	 */
	public static Class<Annotation> getClass(String annotationString) throws AnalysisEngineProcessException {
		// Récupère l'annotation courante d'un type connu
		// TokenAnnotation tokenAnnotation = (TokenAnnotation)
		// inputAnnotationIter
		// .next();

		Class<Annotation> annotationClass = null;
		try {
			annotationClass = (Class<Annotation>) Class
			.forName(annotationString);
		} catch (ClassNotFoundException e) {
			String errmsg = "Error: Class " + annotationString
			+ " not found !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { annotationString },e);	
			//e.printStackTrace();
		}
		// Annotation tokenAnnotation = (Annotation) inputAnnotationIter.next();
		// InputAnnotationClass.cast(tokenAnnotation);

		return annotationClass;

	}

	/**
	 * Invoke a getter method of a given annotation Class/Annotation which return a String  
	 * Allow to know the method of the annotation to handle only at the runtime level
	 * @param InputAnnotationClass
	 * @param inputAnnotation
	 * @param inputFeatureString
	 * @return result
	 * @throws AnalysisEngineProcessException
	 */
	public static String invokeStringGetMethod(Class InputAnnotationClass, Annotation inputAnnotation, String inputFeatureString) throws AnalysisEngineProcessException {

		String result = "";

		// Récupère la méthode pour "getter" la value de l'InputFeature
		String getFeatureMethodName = "get" + inputFeatureString.substring(0, 1).toUpperCase() + inputFeatureString.substring(1);

		Method getFeatureMethod = null;
		try {
			getFeatureMethod = InputAnnotationClass.getMethod(getFeatureMethodName);
		} catch (SecurityException e) {
			String errmsg = "Error: a SecurityException with getMethod " + getFeatureMethodName
			+ " !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { getFeatureMethodName },e);	
			//e.printStackTrace();
		} catch (NoSuchMethodException e) {
			String errmsg = "Error: NoSuchMethodException getMethod " + getFeatureMethodName
			+ " !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { getFeatureMethodName },e);	
			//e.printStackTrace();
		}

		// Test contre la création d'annotations fantomes

		try {
			result = (String) getFeatureMethod.invoke(inputAnnotation);
		} catch (IllegalArgumentException e) {
			String errmsg = "Error: IllegalArgumentException invoked " + inputAnnotation
			+ " !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { inputAnnotation },e);	
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			String errmsg = "Error: IllegalAccessException invoked " + inputAnnotation
			+ " !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { inputAnnotation },e);	
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			String errmsg = "Error: InvocationTargetException invoked " + inputAnnotation
			+ " !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { inputAnnotation },e);	
			//e.printStackTrace();
		}

		return result;
	}


	/**
	 * This method create an annotation.
	 * 
	 * @param aJCas
	 *            the CAS over which the process is performed
	 * @param annotationNameToCreate
	 * @param beginFeatureValue
	 * @param endFeatureValue
	 * @param featureNameToSet            
	 * @param valueFeatureValue
	 * @throws AnalysisEngineProcessException 
	 */
	public static void createAnnotation(JCas aJCas, String annotationNameToCreate,
			int beginFeatureValue, int endFeatureValue, String featureNameToSet, String valueFeatureValue) throws AnalysisEngineProcessException {

		// Crée une annotation générique
		// SequenceMatch sequenceMatch = new SequenceMatch(aJCas);
		// sequenceMatch.setBegin(patternHashMap.get(patternKeyString).getCurrentStartIndexCursor());
		// sequenceMatch.setEnd(tokenAnnotation.getEnd());
		// sequenceMatch.setValue(patternKeyString);
		// sequenceMatch.addToIndexes();

		// Crée une annotation prédéfinie
		// Object[] args = null;


		try {
			Object[] args = null;

			Class<Annotation> TgtClass = (Class<Annotation>) Class
			.forName(annotationNameToCreate);

			// System.out.println("Debug: ----------------------------------------------------------------------"
			// );
			// System.out.println("Debug: TgtClass.getName()	= " +
			// TgtClass.getName());
			// System.out.println("Debug: patternHashMap.get(patternKeyString).getTargetType()	= "
			// + patternHashMap.get(patternKeyString).getTargetType());

			// Génére le constructeur de la classe de l'annotation à créer
			Constructor<?> tgtConstr = TgtClass
			.getConstructor(new Class[] { JCas.class });

			// Crée une annotation du type target
			Object t = null;
			t = tgtConstr.newInstance(new Object[] { aJCas });
			TgtClass.cast(t);

			// System.out.println("Debug: t.getClass().getName()	= " +
			// t.getClass().getName());
			// System.out.println("Debug: t.getClass().getDeclaredMethods().length	= "
			// + t.getClass().getDeclaredMethods().length);

			// for (int l = 0 ; l < t.getClass().getDeclaredMethods().length ;
			// l++ ) {
			// System.out.println("Debug: t.getClass().getDeclaredMethods()[l]= "
			// + t.getClass().getDeclaredMethods()[l]);
			// }

			// for (int l = 0 ; l < t.getClass().getMethods().length ; l++ ) {
			// System.out.println("Debug: t.getClass().getMethods()[l]= " +
			// t.getClass().getMethods()[l]);
			// }

			// jxpathContext = JXPathContext.newContext(t);
			// Récupère la méthode addToIndexes de la classe target
			Method addToIndexes = TgtClass.getMethod("addToIndexes",
					new Class[] {});
			// Récupère les méthodes pour accéder aux features souhaitées
			Method setBegin = TgtClass.getMethod("setBegin", Integer.TYPE);
			Method setEnd = TgtClass.getMethod("setEnd", Integer.TYPE);

			// value -> setValue
			String setFeatureMethodName = "set" + featureNameToSet.substring(0, 1).toUpperCase() + featureNameToSet.substring(1);

			Method setValue = TgtClass.getMethod(setFeatureMethodName, String.class);

			// Ajouts à l'annotation du type target
			setBegin.invoke(t, beginFeatureValue);
			setEnd.invoke(t, endFeatureValue);
			setValue.invoke(t, valueFeatureValue);


			// Test contre la création d'annotations fantomes
			if (beginFeatureValue < endFeatureValue) 
				addToIndexes.invoke(t, args);

		} catch (IllegalArgumentException e) {
			String errmsg = "Error: IllegalArgumentException  !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			String errmsg = "Error: IllegalAccessException  !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			String errmsg = "Error: InvocationTargetException  !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			String errmsg = "Error: ClassNotFoundException  !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			//e.printStackTrace();
		} catch (SecurityException e) {
			String errmsg = "Error: SecurityException  !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			//e.printStackTrace();
		} catch (NoSuchMethodException e) {
			String errmsg = "Error: NoSuchMethodException  !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			//e.printStackTrace();
		} catch (InstantiationException e) {
			String errmsg = "Error: InstantiationException  !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			//e.printStackTrace();
		}
	}

	/**
	 * This method create a view.
	 * 
	 * @param aJCas
	 *            the CAS over which the process is performed
	 * @param outputViewString
	 * @param sofaDataString
	 * @param sofaDataStringTypeMimeString

	 * @throws AnalysisEngineProcessException 
	 */
	public static void createView(JCas aJCas, String outputViewString, String sofaDataString, String sofaDataStringTypeMimeString) throws AnalysisEngineProcessException {

		try {
			aJCas.createView(outputViewString);
			JCas outputView = UIMAUtilities.getView(aJCas, outputViewString);
			//outputView.setDocumentText(commandResultString);
			outputView.setSofaDataString(sofaDataString,sofaDataStringTypeMimeString);

		} catch (CASException e) {
			String errmsg = "Error: Cannot create the view "+outputViewString +" !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] {  },e);	
			// e.printStackTrace();
		}
	}

	/**
	 * This method get an FeatureStructure Array of selected annotation types
	 * 
	 * @param aJCas
	 *            the CAS over which the process is performed
	 * @param annotationHashMap
	 * 			Map of annotations to filter in the JCas
	 * @return ArrayList<FeatureStructure>
	 * 			Filtered JCas with the selected annotation 
	 * @throws AnalysisEngineProcessException 
	 */
	public static ArrayList<FeatureStructure> getAnnotationArray(JCas aJCas, HashMap annotationHashMap) throws AnalysisEngineProcessException {

		//System.out.println("Debug: getAnnotationArray HashMap.size>"+annotationHashMap.size()+"<");

		ArrayList<FeatureStructure> result =  new ArrayList<FeatureStructure>();
		//FSIndex<Annotation> result =  null;
		AnnotationIndex annotationIndex = (AnnotationIndex)
		aJCas.getAnnotationIndex();
		FSIterator annotationIndexIterator = annotationIndex.iterator();
		
		//Iterator keyIter = annotationHashMap.keySet().iterator();
		//while (keyIter.hasNext()){
		//	String key = (String) keyIter.next();
		//	System.out.println("Debug: key>"+key+"<");
		//}
		
		while (annotationIndexIterator.hasNext()) {
			//      On peut le manipuler comme on veut ...
			Object annotationObject = annotationIndexIterator.next();
			Class  annotationClass = annotationObject.getClass();
			String className = "null";
			if (annotationClass != null ) {
				className = annotationClass.getName(); //.toString();
				//System.out.println("Debug: class>"+className+"<");
				if (annotationHashMap.containsKey(className)) {
					result.add((FeatureStructure) annotationObject);
					Annotation annotation = (Annotation) annotationObject;
					//System.out.println("Debug: "+className + "\t" + annotation.getCoveredText()+ "\t" + annotation.getBegin() + "\t" +annotation.getEnd());
				
				}
			}
				
		}

		return result;
	}


	
	/**
	 * 
	 * This method provides an iterator over typed annotations that either 
	 * have an offset embedded in that of a given annotation in a document, 
	 * or have the same offset as these annotation. 
	 * 
	 * @param aJCas  the document in which stand the source and
	 *                                         target annotations
	 * @param contextAnnotation  the source annotation under which target 
	 *                                         annotations that have to be drawn out
	 * @param inputAnnotationType            the type of the target annotations that have 
	 *                                         to be drawn out from the document under 
	 *                                         the source annotation
	 * @param isStrict              the boolean that defines the offset matching,
	 *                                         offsets strictly equal if isStrict is true, begin 
	 *                                         offsets greater or equal and end offsets less 
	 *                                         or equal otherwise.  	
	 * @return                           the iterator over the type theType annotations 
	 *                                          which stand under the annotation theAnnotation 
	 *                                          in the document theDocument 
	 *                                should include the context annotation itself...          
	 * 
	 * @author Fabien Poulard
	 * @author Jérôme Rocheteau
	 * @author hernandez
	 * @throws AnalysisEngineProcessException 
	 * 
	 * @license Apache 2.0
	 */
	public static FSIterator subiterator(JCas aJCas, Annotation contextAnnotation, HashMap inputAnnotationHashMap, boolean isStrict) throws AnalysisEngineProcessException {
		//,Type inputAnnotationType,  boolean isStrict) {
	
		// Ajout: déclaration de la variable type
		Type contextAnnotationType = contextAnnotation.getType();

		// On utilise le constraint factory
		ConstraintFactory theConstraints = aJCas.getConstraintFactory();

		// On définit les contraintes sur le début de l'annotation
		FSIntConstraint beginConstraint = theConstraints.createIntConstraint();
		if (isStrict) { 
			beginConstraint.eq(contextAnnotation.getBegin());
		} else {
			beginConstraint.
			geq(contextAnnotation.getBegin()-1); 
		}
		Feature beginFeature = contextAnnotationType.getFeatureByBaseName("begin");
		FeaturePath beginPath = aJCas.createFeaturePath();
		beginPath.addFeature(beginFeature);
		FSMatchConstraint begin = theConstraints.embedConstraint(beginPath,beginConstraint);


		// ... puis sur la fin de l'annotation
		FSIntConstraint endConstraint = theConstraints.createIntConstraint();
		if (isStrict) {
			endConstraint.eq(contextAnnotation.getEnd());
		} else {
			endConstraint.leq(contextAnnotation.getEnd()+1);
		}
		Feature endFeature = contextAnnotationType.getFeatureByBaseName("end");
		FeaturePath endPath = aJCas.createFeaturePath();
		endPath.addFeature(endFeature);
		FSMatchConstraint end = theConstraints.embedConstraint(endPath, endConstraint);

		
		// JR: on définit une contrainte sur le type d'annotation
		// NH: à partir d'une Map d'annotations
		FSTypeConstraint typeConstraint = theConstraints.createTypeConstraint();

		System.out.println("Debug: contextAnnotationType.getName()>"+contextAnnotationType.getName()+"<");

		System.out.println("Debug: contextAnnotation.getCoveredText()>"+contextAnnotation.getCoveredText()+"<");
		System.out.println("Debug: contextAnnotation.getBegin()>"+contextAnnotation.getBegin()+"<");
		System.out.println("Debug: contextAnnotation.getEnd()>"+contextAnnotation.getEnd()+"<");

		Iterator keyIter = inputAnnotationHashMap.keySet().iterator();
		while (keyIter.hasNext()){
			String key = (String) keyIter.next();
			System.out.println("Debug: key>"+key+"<");
			typeConstraint.add(getType(aJCas,key));
		} 
		FeaturePath typePath = aJCas.createFeaturePath();
		FSMatchConstraint type = theConstraints.embedConstraint(typePath, typeConstraint);


		// On combine les contraintes
		FSMatchConstraint typeAndBeginAndEnd = null; 
		typeAndBeginAndEnd =  theConstraints.and(type,theConstraints.and(begin, end));


		// On génère un itérateur respectant ces contraintes
		FSIterator filteredIterator = null;
		filteredIterator = aJCas.createFilteredIterator(aJCas.getAnnotationIndex().iterator(), typeAndBeginAndEnd);


		return filteredIterator;
	}
	
	
	/**
	 * Return the sofaDataString of a JCAS corresponding to the given view 
	 * @param aJCas
	 * @return inputSofaDataString
	 * @throws AnalysisEngineProcessException
	 */
	public static String  createTempTextFile (String prefix, String suffix, String content)throws AnalysisEngineProcessException {
		String tempTextFilePath = null ; 

		try {
			tempTextFilePath = JavaUtilities.createTempTextFile (prefix,suffix,content);

		} catch (IOException ioexception) {
			String errmsg = "ERROR: Cannot create a temporary text file The view !";
			throw new AnalysisEngineProcessException(errmsg,
					new Object[] { tempTextFilePath },ioexception);
		}
		return tempTextFilePath;
	}

}
