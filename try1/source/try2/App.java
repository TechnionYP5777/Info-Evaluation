package try2;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.simple.*;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Sentence sent = new Sentence("Justin Bieber is in the sky with diamonds.");
        List<String> nerTags = sent.nerTags();  // [PERSON, O, O, O, O, O, O, O]
        System.out.println(Arrays.toString(nerTags.toArray()));
        String firstPOSTag = sent.posTag(0);   // NNP
        System.out.println(sent.word(0));
        System.out.println(firstPOSTag);
    }
}
