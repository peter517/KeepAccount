package com.pengjun.ka.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import android.content.Context;

public class IKAnalyzerUtils<T> {

	public static void initSegmentationTool(Context context) {
		IKAnalyzerUtils.getSegmentationList(context, "");
	}

	public static List<String> getSegmentationList(Context context, String sentence) {

		// single app must lager than 32m
		if (ResourceUtils.getSingleAppMemeryLimit(context) <= ResourceUtils.SINGLE_APP_MEMORY_LIMIT_32) {
			return new ArrayList<String>();
		}

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
