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
public class RelevantRetrievedCorrectCounter {

	
	  protected Map<String, Integer> mMap = new HashMap<String, Integer>();
	  
	/**
	 * number of test (from the "automatic" tested system)
	 */
	//private float retrieved;
	
	public void addRetrieved(int retrieved) {
		mMap.put("retrieved", mMap.get("retrieved") + retrieved);
		//System.out.println("Debug: addRetrieved "+ mMap.get("retrieved"));

	}
	
	/**
	 * number of gold (from the "human" reference)
	 */
	//private float relevant;
	
	public void addRelevant(int relevant) {
		mMap.put("relevant", mMap.get("relevant") + relevant);
		//System.out.println("Debug: addRelevant "+ mMap.get("relevant"));
	}
	
	/**
	 * number of correct results = intersection of relevant and retrieved
	 */
	//private float correct;
	
	public void addCorrect() {
		mMap.put("correct", mMap.get("correct") + 1);
		//System.out.println("Debug: addCorrect " + mMap.get("correct"));

	}
	
	
	public RelevantRetrievedCorrectCounter() {
		/*this.retrieved = 0;
		this.relevant = 0;
		this.correct = 0;
		this.enableWritten(false);*/
		if (mMap.isEmpty()) {
			//System.out.println("Debug: RelevantRetrievedCorrectCounterResource initialized to zero ");

			mMap.put("retrieved", 0);
			mMap.put("relevant", 0);
			mMap.put("correct", 0);
			mMap.put("enableWritten", 0);
		}
	}
	
	
	/**
	 * Precision = correct  / retrieved
	 * @return
	 */
	private float getPrecision() {
		//if (mMap.get("retrieved") == 0) return 0;
		return mMap.get("correct") / mMap.get("retrieved");
	}
	
	private String getPrecisionString() {
		return mMap.get("correct") + "/" + mMap.get("retrieved");
	}
	
	/**
	 * Recall = correct  / relevant
	 * @return
	 */
	private float getRecall() {
		//if (mMap.get("relevant") == 0) return 0;
		return Float.valueOf(mMap.get("correct") / mMap.get("relevant"));
	}
	
	private String getRecallString() {
		return mMap.get("correct") + "/" + mMap.get("relevant");
	}
	
	/**
	 * F measure = 2 precision . recall / (precision + recall)
	 * @return
	 */
	private float getFMeasure() {
		//if (mMap.get("relevant") == 0) return 0;
		return Float.valueOf(2 * this.getRecall() * this.getPrecision() / ( this.getRecall() + this.getPrecision()));
	}
	
	private String getFMeasureString() {
		return "2 * "+this.getRecall()+" * "+ this.getPrecision()+" / ( "+this.getRecall()+" + "+this.getPrecision()+")";
	}
	
	//private boolean written;
	
	private void enableWritten(boolean enabled) {
		if (enabled) 
		mMap.put("enableWritten", 1);
		else 		mMap.put("enableWritten", 0);

		//this.written = enabled;
	}
	
	private boolean isEnableWritten() {
		if (mMap.get("enableWritten") == 1) return true; 
		else return false;
	}
	
	public void display() {
		//System.out.println(this.getRecall());
		try {
			if (!this.isEnableWritten()) {
				this.enableWritten(true);
				String message = "Evaluation Precision: %6.2f" + this.getPrecision() + "(" + this.getPrecisionString() + "); ";
				message += "Recall:" + String.valueOf(this.getRecall()) + "(" + this.getRecallString() + ")";
				//UIMAFramework.getLogger().log(Level.INFO,message);
				//System.out.printf("INFO: Evaluation Precision=%6.2f",  this.getPrecision(), " (" + this.getPrecisionString() + "); Recall=%6.2f", this.getRecall()," (" + this.getRecallString() +"); F-measure=%6.2f", this.getFMeasure()," ("+getFMeasureString() +")\n" );
				System.out.printf("INFO: Evaluation Precision=%6.2f (" + this.getPrecisionString() + "); Recall=%6.2f (" + this.getRecallString() +"); F-measure=%6.2f ("+getFMeasureString() +")\n", this.getPrecision(), this.getRecall(), this.getFMeasure() );



			}
		} catch (Exception e) {
			UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
		}
	}
	

}
