

/* First created by JCasGen Tue Nov 08 16:32:44 CET 2011 */
package common.types.text;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Tue Nov 08 16:34:09 CET 2011
 * XML source: /media/MyPassport/current/public/research/UIMA-USER-DEV-ENV/workspace/uima-common/desc/common/types/commonTS.xml
 * @generated */
public class LexicalUnit extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(LexicalUnit.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected LexicalUnit() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public LexicalUnit(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public LexicalUnit(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public LexicalUnit(JCas jcas, int begin, int end) {
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
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "common.types.text.LexicalUnit");
    return jcasType.ll_cas.ll_getStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets Lemma of the word. 
   * @generated */
  public void setLemma(String v) {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "common.types.text.LexicalUnit");
    jcasType.ll_cas.ll_setStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: mph

  /** getter for mph - gets ?
   * @generated */
  public String getMph() {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_mph == null)
      jcasType.jcas.throwFeatMissing("mph", "common.types.text.LexicalUnit");
    return jcasType.ll_cas.ll_getStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_mph);}
    
  /** setter for mph - sets ? 
   * @generated */
  public void setMph(String v) {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_mph == null)
      jcasType.jcas.throwFeatMissing("mph", "common.types.text.LexicalUnit");
    jcasType.ll_cas.ll_setStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_mph, v);}    
   
    
  //*--------------*
  //* Feature: posTag

  /** getter for posTag - gets 
   * @generated */
  public String getPosTag() {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_posTag == null)
      jcasType.jcas.throwFeatMissing("posTag", "common.types.text.LexicalUnit");
    return jcasType.ll_cas.ll_getStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_posTag);}
    
  /** setter for posTag - sets  
   * @generated */
  public void setPosTag(String v) {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_posTag == null)
      jcasType.jcas.throwFeatMissing("posTag", "common.types.text.LexicalUnit");
    jcasType.ll_cas.ll_setStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_posTag, v);}    
   
    
  //*--------------*
  //* Feature: chunkTag

  /** getter for chunkTag - gets 
   * @generated */
  public String getChunkTag() {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_chunkTag == null)
      jcasType.jcas.throwFeatMissing("chunkTag", "common.types.text.LexicalUnit");
    return jcasType.ll_cas.ll_getStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_chunkTag);}
    
  /** setter for chunkTag - sets  
   * @generated */
  public void setChunkTag(String v) {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_chunkTag == null)
      jcasType.jcas.throwFeatMissing("chunkTag", "common.types.text.LexicalUnit");
    jcasType.ll_cas.ll_setStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_chunkTag, v);}    
   
    
  //*--------------*
  //* Feature: raw

  /** getter for raw - gets 
   * @generated */
  public String getRaw() {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_raw == null)
      jcasType.jcas.throwFeatMissing("raw", "common.types.text.LexicalUnit");
    return jcasType.ll_cas.ll_getStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_raw);}
    
  /** setter for raw - sets  
   * @generated */
  public void setRaw(String v) {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_raw == null)
      jcasType.jcas.throwFeatMissing("raw", "common.types.text.LexicalUnit");
    jcasType.ll_cas.ll_setStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_raw, v);}    
   
    
  //*--------------*
  //* Feature: subCat

  /** getter for subCat - gets 
   * @generated */
  public String getSubCat() {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_subCat == null)
      jcasType.jcas.throwFeatMissing("subCat", "common.types.text.LexicalUnit");
    return jcasType.ll_cas.ll_getStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_subCat);}
    
  /** setter for subCat - sets  
   * @generated */
  public void setSubCat(String v) {
    if (LexicalUnit_Type.featOkTst && ((LexicalUnit_Type)jcasType).casFeat_subCat == null)
      jcasType.jcas.throwFeatMissing("subCat", "common.types.text.LexicalUnit");
    jcasType.ll_cas.ll_setStringValue(addr, ((LexicalUnit_Type)jcasType).casFeatCode_subCat, v);}    
  }

    