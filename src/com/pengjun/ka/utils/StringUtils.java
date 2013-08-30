package com.pengjun.ka.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class StringUtils {

	public static final String STRING_NULL_VALUE = "";

	public static void initSegmentationTool() {
		getSegmentationList("");
	}

	public static List<String> getSegmentationList(String sentence) {

		List<String> sgmList = new ArrayList<String>();

		// max word-length segmentation
		IKSegmenter ik = new IKSegmenter(new StringReader(sentence), true);
		Lexeme lexeme = null;
		try {
			while ((lexeme = ik.next()) != null) {
				sgmList.add(lexeme.getLexemeText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sgmList;
	}
}
