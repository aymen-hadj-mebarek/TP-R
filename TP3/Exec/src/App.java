import java.io.IOException;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.commons.syntax.RelationalFormula;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.fol.syntax.FolSignature;
import org.tweetyproject.logics.ml.parser.MlParser;
import org.tweetyproject.logics.ml.reasoner.SimpleMlReasoner;
import org.tweetyproject.logics.ml.syntax.MlBeliefSet;

public class App {
    public static void main(String[] args) throws ParserException, IOException {
        // Create an empty modal belief set
        MlBeliefSet beliefSet = new MlBeliefSet();
        
        // Create a parser and define a signature
        MlParser parser = new MlParser();
        FolSignature signature = new FolSignature();
        signature.add(new Predicate("a", 0)); 
        signature.add(new Predicate("b", 0)); 
        parser.setSignature(signature);
        
        // Add formulas to the belief set
        beliefSet.add((RelationalFormula) parser.parseFormula("<>(a || b)")); // Diamond operator formula: It is possibly true that a or b.
        beliefSet.add((RelationalFormula) parser.parseFormula("[](b || a)")); // It is necessarily true that b or a.
        beliefSet.add((RelationalFormula) parser.parseFormula("[](b && a)")); // It is necessarily true that b and a.
        
        // Print the modal knowledge base
        System.out.println("Modal knowledge base: " + beliefSet);
        
        // Create a reasoner and perform queries
        SimpleMlReasoner reasoner = new SimpleMlReasoner();
        // Query 1:
        System.out.println("[](!a)      " + reasoner.query(beliefSet, (FolFormula) parser.parseFormula("[](!a)")));
        // Query 2:
        System.out.println("<>(a && b)      " + reasoner.query(beliefSet, (FolFormula) parser.parseFormula("<>(a && b)")));
        // Query 3:
        System.out.println("[](b && <>(!(b)))      " + reasoner.query(beliefSet, (FolFormula) parser.parseFormula("[](b && <>(!(b)))")));
        // Query 4:
        System.out.println("<>(!(b && a))      " + reasoner.query(beliefSet, (FolFormula) parser.parseFormula("<>(!(b && a))")));
    }
}
