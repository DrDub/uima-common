package fr.univnantes.lina.uima.util;
/** 
 * UIMA common
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.ConstraintFactory;
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
import org.apache.uima.jcas.tcas.Annotation;

/**
 * <p>
 * Methods dealing with annotation collection handling such as 
 * <ul>
 * <li>removeDuplicateFSAnnotationFromCASIndex</li>
 * <li> subiterator unsensitive to annotation priority within index</li>
 * <ul>
 * </p>
 * 
 * @author hernandez
 */
public class AnnotationCollectionUtil {

	/**
	 * retrieveAndCastAnAnnotation
	 * 
	 * TODO FAILED
	 * 
	 * @param annotationToLineFSIterator
	 * @param annotationToLine
	 * @return class Class<Annotation> inputAnnotationClass = 
	 * @throws AnalysisEngineProcessException
	 */
	public static Class retrieveAndCastAnAnnotation(FSIterator annotationToLineFSIterator, Annotation annotationToLine)
	throws AnalysisEngineProcessException {

		// Récupère et cast l'annotationToLine courante à manipuler
		Object annotationObject = annotationToLineFSIterator.next();
		Class  annotationClass = annotationObject.getClass();
		String className = "null";
		className = annotationClass.getName();
		//System.out.println("Debug: class>"+className+"<");
		Class<Annotation> inputAnnotationClass = AnnotationUtil.getAnnotationClass(className);
		annotationToLine = (Annotation) annotationObject;
		inputAnnotationClass.cast(annotationToLine);
		return inputAnnotationClass;
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
	public static FSIterator subiterator(JCas aJCas, Annotation contextAnnotation, HashMap inputAnnotationHashMap,
			boolean isStrict) throws AnalysisEngineProcessException {
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

		//		System.out.println("Debug: contextAnnotationType.getName()>"+contextAnnotationType.getName()+"<");
		//		System.out.println("Debug: contextAnnotation.getCoveredText()>"+contextAnnotation.getCoveredText()+"<");
		//		System.out.println("Debug: contextAnnotation.getBegin()>"+contextAnnotation.getBegin()+"<");
		//		System.out.println("Debug: contextAnnotation.getEnd()>"+contextAnnotation.getEnd()+"<");

		Iterator keyIter = inputAnnotationHashMap.keySet().iterator();
		while (keyIter.hasNext()){
			String key = (String) keyIter.next();
			//			System.out.println("Debug: key>"+key+"<");
			typeConstraint.add(JCasSofaViewUtil.getJCasType(aJCas,key));
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
	 * Remove duplicate annotations at the same offsets
	 * from the index
	 * 
	 * TODO to implement
	 * 
	 * http://www.mail-archive.com/uima-user@incubator.apache.org/msg01645.html
	 * http://uima.apache.org/downloads/releaseDocs/2.3.0-incubating/docs/api/org/apache/uima/jcas/JCas.html#removeFsFromIndexes(org.apache.uima.cas.FeatureStructure)
	 * 
	 * @param aJCas  the CAS which contains the FSindex       

	 * @author hernandez
	 * @throws AnalysisEngineProcessException 
	 * 
	 */
	public static void removeDuplicateFSAnnotationFromCASIndex(JCas aJCas)
	throws AnalysisEngineProcessException {
		HashMap<String, String> alreadySeenFS = new HashMap<String, String>();

		// parse the FSIndex
		// compute an hash of the current Annotation from Class.name alphabetcally ordered list of features with their value.toString()
		// if alreadySeenFS this hash them remove from FS
		// else add to alreadSeenFS

		//byte[] hash      = null;
		//try {
		//	hash= MessageDigest.getInstance("MD5").digest(aJCas.getSofaDataString().getBytes());
		//} catch (NoSuchAlgorithmException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}

	}

	/**
	 * This method get an FeatureStructure Array of selected annotation types
	 * Leave empty if all annotations should be considered
	 * 
	 * @param aJCas
	 *            the CAS over which the process is performed
	 * @param annotationHashMap
	 * 			Map of annotations to filter in the JCas
	 * @return ArrayList<FeatureStructure>
	 * 			Filtered JCas with the selected annotation 
	 * @throws AnalysisEngineProcessException 
	 */
	public static ArrayList<FeatureStructure> getAnnotationArray(JCas aJCas, HashMap<String, Integer> annotationHashMap) throws AnalysisEngineProcessException {

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
				if ((annotationHashMap.size() == 0) || (annotationHashMap.containsKey(className))) {
					result.add((FeatureStructure) annotationObject);
					Annotation annotation = (Annotation) annotationObject;
					//System.out.println("Debug: "+className + "\t" + annotation.getCoveredText()+ "\t" + annotation.getBegin() + "\t" +annotation.getEnd());
				}
			}
		}
		return result;
	}


}