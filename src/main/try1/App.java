package main.try1;

import java.util.Arrays;

import edu.stanford.nlp.simple.Sentence;

public class App {
	public static void main(final String[] args) {
		System.out.println("Hello World!");
		final Sentence sent = new Sentence("Justin Bieber is in the sky with diamonds.");
		System.out.println(Arrays.toString(sent.nerTags().toArray()));
		final String firstPOSTag = sent.posTag(0); // NNP
		System.out.println(sent.word(0));
		System.out.println(firstPOSTag);
	}
}
