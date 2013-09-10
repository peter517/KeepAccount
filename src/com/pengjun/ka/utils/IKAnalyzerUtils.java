package com.pengjun.ka.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.pengjun.ka.android.activity.KaApplication;

public class IKAnalyzerUtils<T> {

	public static void initSegmentationTool() {
		IKAnalyzerUtils.getSegmentationList("");
	}

	public static List<String> getSegmentationList(String sentence) {
	
		// single app must lager than 32m
		if (ResourceUtils.getSingleAppMemeryLimit(KaApplication.instance()) <= ResourceUtils.SINGLE_APP_MEMORY_LIMIT_32) {
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