package com.hantaegoon.search.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileContentsFilterIO {

	public boolean runFincontensFilterIO(String path, String txt) {

		File file = new File(path);
		FileReader fr = null;
		BufferedReader br = null;
		String read = null;
		String regexe = txt;

		// BufferedReader를 사용합니다.
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			while ((read = br.readLine()) != null) {

				// Step 1: Allocate a Pattern object to compile a regexe
				Pattern pattern = Pattern.compile(regexe, Pattern.CASE_INSENSITIVE); // case-insensitive
																						// matching
				// Step 2: Allocate a Matcher object from the compiled regexe
				// pattern,and provide the input to the Matcher
				Matcher matcher = pattern.matcher(read);

				// Step 3: Perform the matching and process the matching result
				if (matcher.find()) {
					if (fr != null)
						fr.close();
					if (br != null)
						br.close();
					return true;
				}

			}
			if (fr != null)
				fr.close();
			if (br != null)
				br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
