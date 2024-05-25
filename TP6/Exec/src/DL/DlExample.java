/*
 *  This file is part of "TweetyProject", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  TweetyProject is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2016 The TweetyProject Team <http://tweetyproject.org/contact/>
 */


 import java.io.IOException;
 import org.tweetyproject.commons.ParserException;
 import org.tweetyproject.logics.dl.syntax.EquivalenceAxiom;
 import org.tweetyproject.logics.dl.syntax.Individual;
 import org.tweetyproject.logics.dl.syntax.RoleAssertion;
 import org.tweetyproject.logics.dl.syntax.Union;
 import org.tweetyproject.logics.dl.reasoner.NaiveDlReasoner;
 import org.tweetyproject.logics.dl.syntax.AtomicConcept;
 import org.tweetyproject.logics.dl.syntax.AtomicRole;
 import org.tweetyproject.logics.dl.syntax.Complement;
 import org.tweetyproject.logics.dl.syntax.ConceptAssertion;
 import org.tweetyproject.logics.dl.syntax.DlBeliefSet;
 /**
  * 
  * Examples for using the description logic syntax classes and parser.
  * 
  * @author Anna Gessler
  *
  */
 public class DlExample {
 
	 public static void main(String[] args) throws ParserException, IOException {
		 
		 //Create description logics signature
		 AtomicConcept human = new AtomicConcept("Human");
		 AtomicConcept male = new AtomicConcept("Male");
		 AtomicConcept female = new AtomicConcept("Female");
		 AtomicConcept house = new AtomicConcept("House");
		 AtomicConcept father = new AtomicConcept("Father");
		 AtomicRole fatherOf = new AtomicRole("fatherOf");
		 Individual bob = new Individual("Bob");
		 Individual alice = new Individual("Alice");
 
		 //Create some terminological axioms
		 EquivalenceAxiom femaleHuman = new EquivalenceAxiom(female,human);
		 EquivalenceAxiom maleHuman = new EquivalenceAxiom(male,human);
		 EquivalenceAxiom femaleNotMale = new EquivalenceAxiom(female,new Complement(male));
		 EquivalenceAxiom maleNotFemale = new EquivalenceAxiom(male,new Complement(female));
		 
		 EquivalenceAxiom fatherEq = new EquivalenceAxiom(father, new Union(male,fatherOf)); 
		 EquivalenceAxiom houseNotHuman = new EquivalenceAxiom(house,new Complement(human));
		 
		 //Create some assertional axioms
		 ConceptAssertion aliceHuman = new ConceptAssertion(alice,human);
		 ConceptAssertion bobHuman = new ConceptAssertion(bob,human);
		 ConceptAssertion aliceFemale = new ConceptAssertion(alice,female);
		 ConceptAssertion bobMale = new ConceptAssertion(bob,male);
		 RoleAssertion bobFatherOfAlice = new RoleAssertion(bob,alice,fatherOf);
		 
		 //Add axioms to knowledge base
		 DlBeliefSet dbs = new DlBeliefSet();
		 dbs.add(femaleHuman);
		 dbs.add(maleHuman);
		 dbs.add(maleNotFemale);
		 dbs.add(femaleNotMale);
		 dbs.add(maleHuman);
		 dbs.add(fatherEq);
		 dbs.add(houseNotHuman);
		 dbs.add(aliceHuman);
		 dbs.add(bobHuman);
		 dbs.add(aliceFemale);
		 dbs.add(bobMale);
		 dbs.add(bobFatherOfAlice);
			 
		 //Print knowledge base
		 System.out.println("dbs: " + dbs);
		 System.out.println("------------------------------");
		 System.out.println("Only the ABox: " + dbs.getABox());
		 System.out.println("Only the TBox: " + dbs.getTBox());
		 System.out.println("------------------------------");	
			 
		 
		 //Naive DL reasoner
		 DlBeliefSet dbs2 = new DlBeliefSet();
		 Individual tweety = new Individual("Tweety");
		 ConceptAssertion tweetyMale = new ConceptAssertion(tweety,male);
		 ConceptAssertion tweetyHuman = new ConceptAssertion(tweety,human);
		 dbs2.add(aliceFemale);
		 dbs2.add(tweetyMale);
		 dbs2.add(maleNotFemale);
		 dbs2.add(aliceHuman);
		 // dbs2.add(maleHuman);
		 NaiveDlReasoner reasoner = new NaiveDlReasoner();
		 System.out.println("\n"+reasoner.query(dbs2,femaleHuman ));
		 System.out.println(reasoner.query(dbs2,tweetyHuman ));
		 System.out.println(reasoner.query(dbs2,aliceHuman ));
	 }
 }
 