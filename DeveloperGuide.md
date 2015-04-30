Following a code modification
  * Update the TODO,
  * Update the ChangeLog
  * Commit
  * Potentially tag
```
svn copy https://uima-common.googlecode.com/svn/trunk/ https://uima-common.googlecode.com/svn/tags/`date +%y%m%d` --username nicolas.hernandez -m "new tag"
```
  * Export and upload manually the javadoc http://enicolashernandez.blogspot.com/2009/12/creer-un-projet-sous-googlecode.html
    * Roughly delete the directory and add a new one
```
cd tmp
svn checkout https://uima-common.googlecode.com/svn/ uima-common --username nicolas.hernandez
cd uima-common
svn delete javadoc/*
svn commit -m "Mise à jour de la javadoc - suppression de l'ancienne"
cp -r ../../target/javadoc/* javadoc
svn add javadoc/*
find  javadoc -name "*.html" -exec svn propset svn:mime-type text/html  {} + ;
find  javadoc -name "*.css" -exec svn propset svn:mime-type text/css  {} + ;
find  javadoc -name "*.jpeg" -exec svn propset svn:mime-type image/jpeg  {} + ;
svn commit javadoc/ -m "Mise à jour de la javadoc - ajout de la nouvelle"
cd ..
rm -rf uima-common
```
  * Export jar and upload manually on the forge
    * In the download section select and delete the file ( for depreciated reason)
    * Add the new one with the same descriptor (without date reference in it) and set as Feature to display it on the home page