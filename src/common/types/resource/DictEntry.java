

/* First created by JCasGen Tue Nov 08 16:32:44 CET 2011 */
package common.types.resource;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import common.types.text.Annotation;
import org.apache.uima.jcas.cas.StringArray;


/** 
 * Updated by JCasGen Tue Nov 08 16:34:08 CET 2011
 * XML source: /media/MyPassport/current/public/research/UIMA-USER-DEV-ENV/workspace/uima-common/desc/common/types/commonTS.xml
 * @generated */
public class DictEntry extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(DictEntry.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected DictEntry() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DictEntry(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DictEntry(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DictEntry(JCas jcas, int begin, int end) {
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
  //* Feature: values

  /** getter for values - gets 
   * @generated */
  public StringArray getValues() {
    if (DictEntry_Type.featOkTst && ((DictEntry_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "common.types.resource.DictEntry");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DictEntry_Type)jcasType).casFeatCode_values)));}
    
  /** setter for values - sets  
   * @generated */
  public void setValues(StringArray v) {
    if (DictEntry_Type.featOkTst && ((DictEntry_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "common.types.resource.DictEntry");
    jcasType.ll_cas.ll_setRefValue(addr, ((DictEntry_Type)jcasType).casFeatCode_values, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for values - gets an indexed value - 
   * @generated */
  public String getValues(int i) {
    if (DictEntry_Type.featOkTst && ((DictEntry_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "common.types.resource.DictEntry");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DictEntry_Type)jcasType).casFeatCode_values), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DictEntry_Type)jcasType).casFeatCode_values), i);}

  /** indexed setter for values - sets an indexed value - 
   * @generated */
  public void setValues(int i, String v) { 
    if (DictEntry_Type.featOkTst && ((DictEntry_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "common.types.resource.DictEntry");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DictEntry_Type)jcasType).casFeatCode_values), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DictEntry_Type)jcasType).casFeatCode_values), i, v);}
  }

    