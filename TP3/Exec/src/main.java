import java.io.IOException;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.commons.syntax.RelationalFormula;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.fol.syntax.FolSignature;
import org.tweetyproject.logics.ml.parser.MlParser;
import org.tweetyproject.logics.ml.reasoner.SimpleMlReasoner;
import org.tweetyproject.logics.ml.syntax.MlBeliefSet;

public class main {
    public static void main(String[] args) throws ParserException, IOException {
        // Create an empty modal belief set
        MlBeliefSet bs = new MlBeliefSet();
        
        // Create a parser and define a signature
        MlParser parser = new MlParser();
        FolSignature sig = new FolSignature();
        sig.add(new Predicate("p", 0));
        sig.add(new Predicate("q", 0));
        parser.setSignature(sig);
        
        // Add formulas to the belief set
        bs.add((RelationalFormula) parser.parseFormula("<>(p && q)")); // Diamond operator formula
        bs.add((RelationalFormula) parser.parseFormula("[](!(p) || q)")); // Box operator formula
        bs.add((RelationalFormula) parser.parseFormula("<>(!(q && p))")); 
        
        // Print the modal knowledge base
        System.out.println("Modal knowledge base: " + bs);
        
        // Create a reasoner and perform queries
        SimpleMlReasoner reasoner = new SimpleMlReasoner();
        // Query 1: Existential operator formula
        System.out.println("[](!p)      " + reasoner.query(bs, (FolFormula) parser.parseFormula("[](!p)")) + "\n");
        // Query 2: Another existential operator formula
        System.out.println("<>(p && q)      " + reasoner.query(bs, (FolFormula) parser.parseFormula("<>(p && q)")) + "\n");
        // Query 3: Another formula
        System.out.println("[](q && <>(!(q)))      " + reasoner.query(bs, (FolFormula) parser.parseFormula("[](q && <>(!(q)))")) + "\n");
        // Query 4: Another formula
        System.out.println("<>(!(q && p))      " + reasoner.query(bs, (FolFormula) parser.parseFormula("<>(!(q && p))")) + "\n");
    }
}
