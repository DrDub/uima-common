package fr.univnantes.lina.uima.resources;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.apache.uima.util.Level;

/**
 * 
 * Based on 
 * http://code.google.com/p/uima-sandbox/source/browse/trunk/checker/sources/fr/free/rocheteau/jerome/models/Metrics.java
 * http://en.wikipedia.org/wiki/Precision_and_recall
 * @author rocheteau, hernandez
 */
public class RelevantRetrievedCorrectCounterResource extends RelevantRetrievedCorrectCounter implements RelevantRetrievedCorrectCounterResource_interface, SharedResourceObject  {

	public RelevantRetrievedCorrectCounterResource() {
	
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		// System.out.println("Debug: load(DataResource data)");

		
		if (mMap.isEmpty()) {
			mMap.put("retrieved", 0);
			mMap.put("relevant", 0);
			mMap.put("correct", 0);
			mMap.put("enableWritten", 0);
		}

	}

}
