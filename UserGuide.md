This page mainly presents the  `fr.univnantes.lina.uima.common.CommonAE` class which allow you to develop AEs handling input/output view/annotations generically.

## How does the class work ? ##

The  `CommonAE` class extends the class `org.apache.uima.analysis_component.JCasAnnotator_ImplBase`. Similarly you will find the same `initialize` and `process` methods.

In extension,  for each specified _InputView_ (with default the `_InitialView`), the `process` method calls  the `processInputView` method.
In turn, for each specified _ContextAnnotation_ (annotation covering some text area or others annotations with DocumentAnnotation as default value), the `processInputView` method calls  the `processContextAnnotation` method.
In turn, for each specified _InputAnnotation_ (with a default value corresponding to the _ContextAnnotation_), the `processContextAnnotation` method calls the `processInputAnnotation` method
which eventually calls the `processAnnotationFeatureStringValue` on a String feature value of the _InputAnnotation_.
The current basic `processAnnotationFeatureStringValue` (with _coveredText_ as default) only echoes the value.

Each methods requires some parameters definitions.
The `process` method requires the _InputView_ parameters.
The `processInputView` method requires the  _ContextAnnotation_ parameters.
The `processContextAnnotation` method requires the  _InputAnnotation_ parameters.
The `processInputAnnotation` method requires the  _InputFeature_ parameters.
As mentioned above all these parameters have got some default values.

Potentially there is a creation of an _output view_ at the process level based on the returned result of the embedded methods. And there is a creation of an _output annotation_ at the `processInputAnnotation` level. In practice, depending on the values of the _ContextAnnotation_ and the _InputAnnotation_ parameters you may create an output over the _InputView_ (no _ContextAnnotation_ neither an _InputAnnotation_ should be specified) or the _ContextAnnotation_(at least a _ContextAnnotation_ should be defined).

Many of the tricks works because the handled annotations are based on `uima.tcas.Annotation` and `uima.tcas.DocumentAnnotation`. Part of the tricks relies also on the default values for the _InputView_, _ContextAnnotation_ and _InputAnnotation_ parameters.

## How can I use it to develop my own generic Annotator Engine? In a few words ##

Each extension of the `CommonAE` class can choose the methods to re-implement to short-circuit the procedure and to take the benefit from the subsuming methods.

_Roughly speaking_ you have to extend the `CommonAE` class.

Then you implement `initialize` method if you have some configParameterValues to get from the AE descriptor.
```
        public void initialize(UimaContext aContext)
        throws ResourceInitializationException {
                super.initialize(aContext);

                // Strongly recommanded !!! (you have to define COMPONENT* in the header file as private static String for example)
                this.setCOMPONENT_NAME(COMPONENT_NAME);
                this.setCOMPONENT_VERSION(COMPONENT_VERSION);
                this.setCOMPONENT_ID(COMPONENT_NAME+"-"+COMPONENT_VERSION);
   
                // TODO Here you get all your own ConfigParameterValues from the UimaContext
        }
```

Then you implement the processing method you want depending on what you want to work with: at the view level, at the context annotation level (a view area) or at the annotation level (annotations occurring in a given view area).
  * implement the `process` method if you want to do the same thing as you do with the class `org.apache.uima.analysis_component.JCasAnnotator_ImplBase` of the Apache API. You probably won't do that since it does not offer to much comparing to the traditional Apache API. At least it gives you directly access to input/output parameters that you just have to declare (by overing them) in your AE descriptor.
  * implement the `processInputView` method if you want **to perform your processing on each defined _InputView_**. You will have to declare and define the _InputView_ array parameter for that. Since the method requires a return value, if is not relevant for your case, just return the coveredTest dataString itself by adding at the end `return inputViewJCas.getSofaDataString();`.
```
protected String processInputView(JCas inputViewJCas,
			FSIterator contextAnnotationsFSIter,
			HashMap<String, Integer> inputAnnotationStringHashMap,
			String inputFeatureString, JCas outputViewJCas,
			String outputAnnotationString, String ouputFeatureString)
	throws AnalysisEngineProcessException {
 // Here your code
}
```
  * implement the `processContextAnnotation` method if you want **to perform your processing on some specific text areas** (covered by some known annotations), potentially on a list of defined `InputView`. You will have to declare and define the _ContextAnnotation_ parameter to list the annotation names which delimits the text area you want to proceed. Since the method requires a return value, if is not relevant for your case, just return the coveredTest itself by adding at the end `return contextAnnotation.getCoveredText();`
```
	protected String processContextAnnotation(JCas inputViewJCas,
			FSIterator contextAnnotationsFSIter,
			Annotation contextAnnotation, 
			FSIterator contextualizedInputAnnotationsFSIter,
                        String inputFeatureString,
			JCas outputViewJCas, 
                        String outputAnnotationString,
			String ouputFeatureString) throws AnalysisEngineProcessException {
 // Here your code
}
```
  * implement the `processInputAnnotation` method to specify the processing to perform to the value of the  _InputFeature_ of the current `InputAnnotation`. The _InputFeature_ is a string Feature name which can be different from `coveredText`. By default if no _InputAnnotation_ is defined, it takes the _ContextAnnotation_ value whose default value is `DocumentAnnotation`. The _InputFeature_, the _InputAnnotation_ may be set.
```
	protected String processInputAnnotation(JCas inputViewJCas,
			FSIterator contextAnnotationsFSIter,
                   	Annotation contextAnnotation, 
			FSIterator contextualizedInputAnnotationsFSIter,    
                        Object annotationObject, 
                        String inputFeatureString,
			JCas outputViewJCas, String outputAnnotationString,
			String ouputFeatureString) throws AnalysisEngineProcessException {
  // Here your code
}
```
  * implement the `processAnnotationFeatureStringValue` to benefit from the whole process chain and by only specifying the process you want to perform over an existing annotation type (the DocumentAnnotation at the InputView  level, or the ContextAnnotations, or some InputAnnotations) which holds a String feature.
```
        protected String processAnnotationFeatureStringValue(
			JCas inputViewJCas, 
                        String inputTextToProcess,
			int beginFeatureValue, 
                        int endFeatureValue)
	throws AnalysisEngineProcessException {
          // Here your code
        }
```

## What are the parameters to override in my Annotator Engine descriptor? ##

  * `InputView` List of view names to process
  * `ContextAnnotation` List of annotation names which delimits the text areas or the covered annotations to process
  * `InputAnnotation`	Ordered list of annotation names to consider as the Feature Structure index to process
  * `InputFeature`  Feature name of the inputAnnotation whose String Value will be actually processed
  * `OutputView` View name to consider as the view to receive the result; to be created whether `OutputAnnotation` is empty or simply to edit if `OutputAnnotation` is defined
  * `OutputAnnotation` Name of the annotation to create as the analysis result
  * `OutputFeature` Feature name of the annotation whose string value will contain the analysis result

Some other parameters allows you
  * to specify the output view [media type (also called type MIME)](http://en.wikipedia.org/wiki/Internet_media_type) to create.


## Where can I find an extension example or some descriptor examples ? ##

The current package contains a descriptor example n the `desc/common` directory. The Annotor won t work if none parameter is defined. This configuration will correspond to the creation of an OutputView whose default value will be the InputView value (_InitialView or whatever). Consequently the view to create will already exist and an error will occur. In the current example, only the_OutputView_parameter is set. A new view is created with a full echo of the InputView._

You may consult  the [uima-shell AE](http://code.google.com/p/uima-shell/) project. Its [ShellAE.java class](http://code.google.com/p/uima-shell/source/browse/trunk/src/fr/univnantes/lina/uima/shell/ae/ShellAE.java) is an extension of the `CommonAE` class. The project has also some descriptor examples with the `desc/shell/test*` files.


## Tell me again what are the default values and the parameter constraints ##

  * `InputView` a pour valeur par défaut `_InitialView`
  * 1 ou n `InputView` on traite les vues les unes après les autres

  * `OutputView` a pour valeur par défaut la valeur de l'`InputView`
  * 1 seule possible

  * `ContextAnnotation` a pour valeur par défaut `uima.tcas.DocumentAnnotation`
  * 1 ou n `ContextAnnotation` on traite un FSIndex de `ContextAnnotation` les unes après les autres

  * `InputAnnotation` n'a pas de valeur par défaut
  * 0 `InputAnnotation` on traite la zone de texte couverte par les `ContextAnnotation`
  * 1 ou n `InputAnnotation` on traite un FSIndex de `InputAnnotation` soit chaque `InputAnnotation` les unes après les autres soit comme un tout ou autre si on étend la méthode processInputAnnotation

  * Si `InputAnnotation` ou `InputFeature` est défini, alors les deux doivent l'être
  * Si `OutputAnnotation` ou `OutputFeature` est défini, alors les deux doivent l'être

  * Si `OutputAnnotation` existe alors on éditera seulement l'`OutputView` pour y rajouter des annotations sinon on créera l'`OutputView` spécifiée


(ne pas tenir compte des codes (`x`))
  * (4) Si `OutputAnnotation` == null alors `OutputView` est à créer ; si `OutputView` n'existe pas alors on la crée PASS ; sinon si existe FAIL
  * (1) Si `OutputView` == default i.e. `InputView` donc FAIL car existe déjà lorsqu'on tente de créer

  * (5) Si `OutputAnnotation` != null alors `OutputView` est à éditer ; si `OutputView` est non existante alors FAIL ; si existante alors PASS mais offsets des annotations créées peuvent déborder de la vue
  * (2) Si `OutputView` == default i.e. `InputView` donc PASS sans débordement d'annotations

  * n `OutputAnnotation`
  * (3) idem que (5,2) ; ALERT crée n annotation avec même value ; doute sur intérêt donc non implémentée
  * (6) idem que (3)

  * n `OutputView`
  * (7) idem que (4) ; ALERT crée n vues avec le même contenu ; doute sur intérêt donc non implémentée

  * n `OutputView` et n `OutputAnnotation`
  * (8) idem que (5) ; ALERT créé une même annotation sur plusieurs vues existantes distinctes ; doute sur intérêt donc non implémentée
  * (9) idem que (6)


## How does work each method ? ##

**` initialize`**
  * Getting the list of `InputView` name, `ContextAnnotation` type name, `InputAnnotation` type name, `OutputView` name, `OutputAnnotation` type name

**`process`**
  * From the given JCas, process sequentially each InputView. Eventually may create a view with the concatenated results obtained for each view. Previously the method was intending to route toward the correct subprocess method depending on the InputType which is either Annotation or View. Recent modifications led to use the same subprocess method. Indeed the InputView will be processed indirectly by the  DocumentAnnotation.
```
process () {
   foreach InputView {
      define the corresponding OutputView;
      if OutputType.isView then get the OutputView;

      build the index of ContextAnnotation of the current InputView; // If the parameter was empty, as a default contains DocumentAnnotation         

      concatenatedContextAnnotationResult += processInputView();   
   }
   if OutputType.isView then createView OutputView with concatenatedContextAnnotationResult as content;
}
}
```

**`processInputView`**
  * From a given InputView, process sequentially each ContextAnnotation. Returns the contatenated results obtained for each Context Annotation.
```
processInputView() {
   foreach ContextAnnotation {
      build the index of InputAnnotation; // If the parameter was empty, as a default contains ContextAnnotations          

      concatenatedInputAnnotationResult += processContextAnnotation()
   }
   return concatenatedInputAnnotationResult
}
```

**`processContextAnnotation`**
  * Process each InputAnnotation and returns a contatenated result obtained from each InputAnnotation
```
processContextAnnotation() {
   foreach InputAnnotation {
       concatenatedInputAnnotationResult += processInputAnnotation() ;
       
   }
   return concatenatedInputAnnotationResult
}
```

**`processInputAnnotation`**
  * Analyze the value of its given InputFeature. If some OutputAnnotations are specified then they are created and their OutputFeature is set with the analysis result.
```
processInputAnnotation() {
   annotationFeatureStringValueResult=processAnnotationFeatureStringValue()
   if OutputType.isAnnotation then createAnnotation in the given OutputView;

   // any process
   return annotationFeatureStringValueResult
}
```


**`processAnnotationFeatureStringValue`**
  * This  method corresponds to the actual process performed  on the String Value of a given Feature of a given Annotation. Here the echo serves as  an example.
```
processAnnotationFeatureStringValue () {
   echo the String feature value received as parameter
}
```