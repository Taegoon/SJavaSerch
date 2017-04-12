package com.hantaegoon.search.model;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileContentsFilter {
	static Matcher matcher;
	static Pattern pattern;

	public boolean runFilterContents(String path, String txt) throws Exception {
		// TODO Auto-generated method stub
		Path from = Paths.get(path);
		long size = Files.size(from);
		FileChannel fileChannel = FileChannel.open(from);
		ByteBuffer directBuffer = ByteBuffer.allocateDirect((char) size);

		String regexe = txt;

		Charset charset = Charset.forName("UTF-8");
		while (fileChannel.read(directBuffer) > 0) {
			directBuffer.rewind();

			// Step 1: Allocate a Pattern object to compile a regexe
			pattern = Pattern.compile(regexe, Pattern.CASE_INSENSITIVE); // case-insensitive
																			// matching

			// Step 2: Allocate a Matcher object from the compiled regexe
			// pattern,and provide the input to the Matcher
			matcher = pattern.matcher(charset.decode(directBuffer));

			// Step 3: Perform the matching and process the matching result
			// System.out.println(matcher.find());

			directBuffer.flip();
		}

		fileChannel.close();
		return matcher.find();

	}
}