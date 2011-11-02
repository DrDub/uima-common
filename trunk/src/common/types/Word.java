

/* First created by JCasGen Mon Oct 31 11:51:41 CET 2011 */
package common.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Mon Oct 31 13:13:56 CET 2011
 * XML source: /media/MyPassport/current/public/research/UIMA-USER-DEV-ENV/workspace/uima-common/desc/common/types/commonTS.xml
 * @generated */
public class Word extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Word.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Word() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Word(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Word(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Word(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets Lemma of the word.
   * @generated */
  public String getLemma() {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "common.types.Word");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets Lemma of the word. 
   * @generated */
  public void setLemma(String v) {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "common.types.Word");
    jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: mph

  /** getter for mph - gets ?
   * @generated */
  public String getMph() {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_mph == null)
      jcasType.jcas.throwFeatMissing("mph", "common.types.Word");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type)jcasType).casFeatCode_mph);}
    
  /** setter for mph - sets ? 
   * @generated */
  public void setMph(String v) {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_mph == null)
      jcasType.jcas.throwFeatMissing("mph", "common.types.Word");
    jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type)jcasType).casFeatCode_mph, v);}    
   
    
  //*--------------*
  //* Feature: posTag

  /** getter for posTag - gets 
   * @generated */
  public String getPosTag() {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_posTag == null)
      jcasType.jcas.throwFeatMissing("posTag", "common.types.Word");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type)jcasType).casFeatCode_posTag);}
    
  /** setter for posTag - sets  
   * @generated */
  public void setPosTag(String v) {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_posTag == null)
      jcasType.jcas.throwFeatMissing("posTag", "common.types.Word");
    jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type)jcasType).casFeatCode_posTag, v);}    
   
    
  //*--------------*
  //* Feature: chunkTag

  /** getter for chunkTag - gets 
   * @generated */
  public String getChunkTag() {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_chunkTag == null)
      jcasType.jcas.throwFeatMissing("chunkTag", "common.types.Word");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type)jcasType).casFeatCode_chunkTag);}
    
  /** setter for chunkTag - sets  
   * @generated */
  public void setChunkTag(String v) {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_chunkTag == null)
      jcasType.jcas.throwFeatMissing("chunkTag", "common.types.Word");
    jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type)jcasType).casFeatCode_chunkTag, v);}    
   
    
  //*--------------*
  //* Feature: raw

  /** getter for raw - gets 
   * @generated */
  public String getRaw() {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_raw == null)
      jcasType.jcas.throwFeatMissing("raw", "common.types.Word");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type)jcasType).casFeatCode_raw);}
    
  /** setter for raw - sets  
   * @generated */
  public void setRaw(String v) {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_raw == null)
      jcasType.jcas.throwFeatMissing("raw", "common.types.Word");
    jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type)jcasType).casFeatCode_raw, v);}    
   
    
  //*--------------*
  //* Feature: subCat

  /** getter for subCat - gets 
   * @generated */
  public String getSubCat() {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_subCat == null)
      jcasType.jcas.throwFeatMissing("subCat", "common.types.Word");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type)jcasType).casFeatCode_subCat);}
    
  /** setter for subCat - sets  
   * @generated */
  public void setSubCat(String v) {
    if (Word_Type.featOkTst && ((Word_Type)jcasType).casFeat_subCat == null)
      jcasType.jcas.throwFeatMissing("subCat", "common.types.Word");
    jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type)jcasType).casFeatCode_subCat, v);}    
  }

    