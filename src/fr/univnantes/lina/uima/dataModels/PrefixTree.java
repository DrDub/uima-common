package fr.univnantes.lina.uima.dataModels;

import java.util.ArrayList;
import java.util.List;

public interface PrefixTree {

	
        public boolean isLeaf();
        /**
         * Character obtained from a char
         * @param c
         * @return
         */
        public PrefixTree getChild(Character c);
        /**
         * Unicode code point
         * @param c
         * @return
         */
        public PrefixTree getChild(int c);
        public void add(String characters,int index,int length, ArrayList<String> values);
        public List<String> getValues();

        
}