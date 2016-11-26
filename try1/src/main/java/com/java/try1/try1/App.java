package com.java.try1.try1;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.simple.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Sentence sent = new Sentence("I am going to visit in Haifa tomorrow. I will visit the Technion.");
        List<String> nerTags = sent.nerTags(); 
        System.out.println(Arrays.toString(nerTags.toArray()));
        System.out.println(sent.posTag(0));
    }
}
