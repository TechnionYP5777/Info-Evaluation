package test.Analyze;

import static org.junit.Assert.*;
import org.junit.Test;
import main.Analyze.AnalyzeSources;
public class AnalyzeSourcesTest{
	String src1="Tito Ortiz was arrested for driving under the influence after he drove his Porsche Panamera into the concrete median on Jan 6 on the 405 freeway in L.A. at 4am.\n"

+"David Cassidy was arrested for driving under influence on Jan 10 after he allegedly took a breathalyzer test and was found to be over twice the legal limit.\n"

+"Soulja Boy was arrested for possession of a loaded gun in Los Angeles on Jan 22.\n"

+"Justin Bieber was arrested for driving under the influence, driving with an expired license, and non-violently resisting arrest in Miami Beach. Bieber was arrested again on Sep 1 for dangerous driving and assault after an alleged collision between a mini-van and the pop-star's ATV.\n"

+"Chris Kattan was arrested for drunk driving on Feb 10 on the 101 Freeway in the San Fernando Valley. Kattan was reportedly weaving erratically on the freeway before running into the back of a Department of Transportation truck.\n"

+"Sam Worthington was arrested after punching a paparazzo who had allegedly kicked Worthington's girlfriend in the shin on Feb 23 in New York.\n"

+"Chris Pine was arrested for driving under the influence in New Zealand on Mar 1. Pine plead guilty and was forced to pay $93 New Zealand dollars (around $79 U.S.). Pine also had his New Zealand driver's license suspended for six months.\n"

+"Chris Brown was arrested for violating his probation on Mar 14 after he was kicked out of rehab for failure to follow the rules of the program.\n"

+"Columbus Short was arrested for physically attacking his wife on February 14 2014.\n"

+"Columbus Short was arrested for allegedly punching a man in a bar fight on March 21 2014.\n"

+"Judge Joe Brown was arrested for contempt of court in March 26 after an outburst he allegedly made while representing a woman during a child support hearing.\n"

+"Joe Francis was arrested for allegedly attacking an employee at the Girls Gone Wild offices in L.A on May 16.\n"

+"Ray J was arrested after allegedly groping a woman in the lobby on May 30 at the Beverly Wilshire Hotel. However, he reportedly refused to leave and allegedly began behaving belligerently towards police.\n"

+"Hoppy Sollo was arrested after assaulting her half-sister and her nephew on June 21 .\n"

+"Shia LaBeouf was arrested after disrupting a performance of Cabaret at New York's Studio 54 on June 26 , and then refusing to leave the theater.\n"

+"Rick Ross was arrested for possession of marijuana on June 27.\n"

+"Will Hayden was arrested for molestation and rape of a pre-teen girl on Aug 11 . Several additional charges of aggravated rape of different girls were added after the initial charges.\n"

+"Keyshia Cole was arrested after allegedly assaulting a woman who had supposedly been spending a lot of time with Cole's boyfriend on Sep 18 .\n"

+"Amanda Bynes was arrested for driving under the influence of a controlled substance on Sep 28.\n"

+"Michael Phelps was arrested for driving under the influence on Sep 30 in Baltimore after failing a field sobriety test.\n"

+"Waka Flocka Flame was arrested after security and police at Atlanta's Hartsfield-Jackson International Airport found a handgun in his carry-on luggage on Oct 10.\n"

+"Nicholas Brendon was arrested for allegedly causing a disturbance in the hotel lobby outside of the Tree City Comic Con on Oct 17 at a hotel in Boise, Idaho.\n"

+"Suge Knight & Kat Williams  were arrested  for allegedly stealing a camera from a female paparazzo on Oct 29.\n"

+"Andy Dick was arrested after allegedly stealing a necklace off a man on Hollywood Blvd on Nov 8. He was charged with grand theft.\n"

+"Buddy Valastro was arrested for intoxicated driving on Nov 13. He allegedly told police, You can't arrest me! I'm the Cake Boss.\n"

+"Dustin Diamond was arrested for possession of a switchblade which the actor allegedly pulled out during a bar fight in which a man was stabbed on Dec 26.\n";
String src2= "Ricardo Medina Jr. was arrested for allegedly stabbing his roommate to death with a sword on Feb 1. No charges against Medina were ever filed.\n"

+"John Stamos was arrested for allegedly driving under the influence on June 12 after the Beverly Hills police received numerous calls reporting a possible drunk driver. He pleaded no contest to one misdemeanor account of driving under the influence and was placed under three years of probation.\n"

+"Sean Combs was arrested for allegedly assaulting with a deadly weapon on June 22. Diddy's son, Justin, is a defensive back for the Bruins, and the rapper got into an altercation with his son's coach, allegedly attacking him with a kettlebell. Later the Los Angeles County District Attorney declined to file felony charges and passed the case to the Los Angeles City Attorney for a possible misdemeanor prosecution. Diddy was granted an office hearing, which seeks to resolve the matter without any criminal charges.\n"

+"Kim Richards was arrested for shoplifting from a Target in Van Nuys, California on Aug 2.\n"

+"Mark Salling was arrested for felony possession of child pornography on Dec 29. His representative had no comment on the matter.";
	
	
//	@Test public void test1(){
//		AnalyzeSources as= new AnalyzeSources();
//		assertEquals(0, as.getNumOfSources());
//		as.addSource(
//				"Rihanna was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nKanye West hates dogs. He spet on a dog and therefore was arrested and put to jail.");
//		assertEquals(1, as.getNumOfSources());
//	}
//	@Test public void test2(){
//		AnalyzeSources as= new AnalyzeSources();
//		assertEquals(0, as.getNumOfSources());
//		as.addSource(
//				"Rihanna was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nKanye West hates dogs. He spet on a dog and therefore was arrested and put to jail.");
//		assertEquals(1, as.getNumOfSources());
//		assertEquals(2, as.getData().getNumOfTuples());
//	}
//	@Test public void test3(){
//		AnalyzeSources as= new AnalyzeSources();
//		assertEquals(0, as.getNumOfSources());
//		as.addSource(
//				"Rihanna was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nKanye West hates dogs. He spet on a dog and therefore was arrested and put to jail.");
//		as.addSource(
//				"Lebron James was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nBritney Spears hates dogs. He spet on a dog and therefore was arrested and put to jail.");
//
//		assertEquals(2, as.getNumOfSources());
//		assertEquals(4, as.getData().getNumOfTuples());
//	}

	@Test
	public void testPrint() {
		AnalyzeSources as= new AnalyzeSources();
		as.addSource(src1);
		as.addSource(src2);
		as.getData().printList();
		
	}
}