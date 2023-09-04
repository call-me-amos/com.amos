package com.test.opennl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class OpenNLPTimeDemo {
    public static void main(String[] args) throws IOException {
        // 加载句子检测模型
        InputStream sentModelIn = new FileInputStream("en-sent.bin");
        SentenceModel sentModel = new SentenceModel(sentModelIn);
        SentenceDetectorME sentDetector = new SentenceDetectorME(sentModel);

        // 加载时间识别模型
        InputStream timeModelIn = new FileInputStream("en-ner-time.bin");
        TokenNameFinderModel timeModel = new TokenNameFinderModel(timeModelIn);
        NameFinderME timeFinder = new NameFinderME(timeModel);

        // 待处理的句子
        String sentence = "我计划下个月装修。next day";

        // 检测句子
        String[] sentences = sentDetector.sentDetect(sentence);
        System.out.println("句子： " + sentences[0]);

        // 识别时间
        Span[] timeSpans = timeFinder.find(sentences);
        String[] times = Span.spansToStrings(timeSpans, sentences);
        System.out.println("时间信息： " + Arrays.toString(times));
    }
}