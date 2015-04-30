**Overview**

The **uima-common** package aims at **assembling common and generic code snippets that can be usefully reused** in several distinct UIMA developments (like analysis engine or any application).
It also includes some machine learning (ml) and natural language processing (nlp) analysis engines.
cas, ae ,ressources
mlnlp processing and connexion  via ae

It is made of various parts
  * UIMA utilities for handling CAS objects (`common.cas`)
  * UIMA utilities for handling resources objects (`common.ressources`)
  * a generic implementation of analysis engine called `CommonAE` whose class extends `JCasAnnotator_ImplBase` (`common.ae`)
  * In addition, it comes with basic ML/NLP components (such as concordancer, variantRecognizer, RegexSubstitute) (`mlnlp`)

The implementation is hardly based on the java.lang.reflect API.

**Why to use this package?**

The library allows you to
  * to **handle the various UIMA objects (i.e. view, annotation, feature) refering to them by their string names**. It centralizes redundant codes in particular for parsing collections of these objects, creating, getting and setting them.
  * **to develop type system independent analysis engines** and so to **handle generically the views, the annotations and the features** you process. This is made possible by specifying the names of the handled views, annotations and features by parameters;


The generic AE allows you to
  * to **perform some process (e.g. _does it start with an uppercase letter_) on some annotations** (also called _InputAnnotation_, e.g. _the tokens_)  **covered by some others** (also called _ContextAnnotation_, e.g. _the sentences_) which are **only present in some views** (also called _InputView_, e.g. _the extracted text of a pdf file_) and to specify their name by parameters.  This is made possible by simply overwriting the right methods.
  * to **create a new view** (also called _OutputView_) **or a new annotation** (also called _OutputAnnotation_) **to receive the processing result and to set their name by parameters**.




**How/Where To Start ?**

  * If you want to develop a generic AE, look at the [UserGuide](http://code.google.com/p/uima-common/wiki/UserGuide) to learn more on how to extend the `CommonAE`  class
  * If you only want to use some of the generic methods you may consult the [Javadoc](http://uima-common.googlecode.com/svn/javadoc/index.html) to see the utilities offered by the UIMAUtilities and JavaUtilities classes
  * In addition, you may consult the following examples which are extensions of the `CommonAE` class and which consequently use also the UIMA and Java Utilities.
    * [ShellAE.java class](http://code.google.com/p/uima-shell/source/browse/trunk/src/fr/univnantes/lina/uima/shell/ae/ShellAE.java) of the [uima-shell AE](http://code.google.com/p/uima-shell/) project. The descriptor examples issustrate various combination of generic parameters. The Java code illustrates the extension of the processAnnotationFeatureStringValue method and the use of parameters defined in the parent class.
    * The various [UIMA Connectors AE classes](https://code.google.com/p/uima-connectors)
    * [TextSegmenterAE.java](https://code.google.com/p/uima-text-segmenter/source/browse/trunk/src/fr/univnantes/lina/uima/textSegmenter/TextSegmenterAE.java) and its two sub extension classes, [C99AE.java](https://code.google.com/p/uima-text-segmenter/source/browse/trunk/src/fr/univnantes/lina/uima/textSegmenter/C99/C99AE.java)  and [JTextTileAE.java](https://code.google.com/p/uima-text-segmenter/source/browse/trunk/src/fr/univnantes/lina/uima/textSegmenter/JTextTile/JTextTileAE.java).

If you want to receive notifications on major updates, please send an email to the `nicolas.hernandez`'s gmail account with the following subject:  `uima-common request for notifcation`.

**Major changes since the last release (one major release each six months)**

See the [Committed Changes](http://code.google.com/p/uima-common/source/list) or the [ChangeLog](http://code.google.com/p/uima-common/source/browse/trunk/ChangeLog) file for more details
  * a common and simple type system oriented for (French) NLP applications
  * a data model for handling prefix tree
  * numerous methods over annotation collections (removeDuplicateAnnotations, removeSubsumed... ), for handling generically features (getFeatureValue), for getting the name of the current document
  * NLP AE such as regex substitute