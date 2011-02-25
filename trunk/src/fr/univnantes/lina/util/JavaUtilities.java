/** 
 * Java Utilities
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
package fr.univnantes.lina.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * @author hernandez-n
 *
 */
public class JavaUtilities {


	/*
	 * Fusionne deux tableaux de Type T en un seul
	 * */
	public static <T> T[] concat (T[] a, T[] b) {
		final int alen = a.length;
		final int blen = b.length;
		final T[] result = (T[]) java.lang.reflect.Array.
		newInstance(a.getClass().getComponentType(), alen + blen);
		System.arraycopy(a, 0, result, 0, alen);
		System.arraycopy(b, 0, result, alen, blen);
		return result;
	}

	/**
	 * Create a temporary text file with a text given in parameter and return its absolute path
	 * @param prefixTmpFile
	 * @param suffixTmpFile
	 * @param text
	 * @return fileAbsPathStrg
	 * @throws IOException 
	 */
	public static String createTempTextFile (String prefixTmpFile, String suffixTmpFile, String text) throws IOException {
		String fileAbsPathStrg = null;
		File file = File.createTempFile(prefixTmpFile, suffixTmpFile);
		fileAbsPathStrg = file.getAbsolutePath();

		FileWriter fstream = new FileWriter(fileAbsPathStrg);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(text);
		out.close();
		return fileAbsPathStrg;
	}


	/**
	 * Utility method to write a given text to a file
	 * fileName doit intégrer le nom du répertoire +"/"
	 */
	public static boolean writeLineArrayToFile(String fileName, String[] lines  ) {

		Writer out = null;
		//System.out.println("Debug: full Output File Name "+ fileName);

		try {

			out = new OutputStreamWriter(new FileOutputStream(fileName),"UTF8");

			for (int l = 0 ;  l< lines.length ; l++) {
				//System.out.println("Debug: current line "+ lines[l]);

				out.write(lines[l]);
			}
			out.close();
		} catch (FileNotFoundException ex) {
			return (false);
		} catch (IOException ex) {
			return (false);
		}
		return (true);

	}


	/**
	 * Utility method to read a given text from a file
	 * fileName doit intégrer le nom du répertoire dans le filename"/"
	 * @throws IOException 
	 */
	public static ArrayList<String> readFromFileToLineArray(String fileName){
		System.out.println("Debug: full input File Name "+ fileName);
		ArrayList<String> lines = new ArrayList<String>();

		// Open the file that is the first 
		// command line parameter
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine; 
		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   {
				lines.add(strLine);
				// Print the content on the console
				//System.out.println (strLine);
			}
			//Close the input stream
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lines;
	}
	

	/**
	 * Utility method to read a given text from a file
	 * fileName doit intégrer le nom du répertoire dans le filename"/"
	 * @throws IOException 
	 */
	public static ArrayList<String> readFromFileToLineArray(File file){
		System.out.println("Debug: full input File Name "+ file);
		ArrayList<String> lines = new ArrayList<String>();

		// Open the file that is the first 
		// command line parameter
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine; 
		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   {
				lines.add(strLine);
				// Print the content on the console
				//System.out.println (strLine);
			}
			//Close the input stream
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lines;
	}

}
