import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choisissez la partie que vous voulez tester");
        System.out.println("1) Partie 1 :  l'algorithm de propagation de marqueurs");
        System.out.println("2) Partie 2 :  l'algorithm d'heritage");
        System.out.println("3) Partie 3 :  l'algorithm de propagation de marqueurs avec exception");

        String choix = scanner.nextLine();

        if (choix.equals("1")) {
            System.out.println("Partie 1: l'algorithm de propagation de marqueurs");
            try {
                FileReader reader = new FileReader("Bases/propagation.json");
                JSONObject reseauSemantique = (JSONObject) new JSONParser().parse(reader);

                List<String> M1_node = Arrays.asList(
                        "Modes de Representations des connaissances",
                        "Modes de Representations des connaissances",
                        "Modes de Representations des connaissances",
                        "Modes de Representations des connaissances"
                );

                List<String> M2_node = Arrays.asList(
                        "Axiome A7",
                        "Axiome A4",
                        "Axe-IA",
                        "Axiome A9"
                );

                String relation = "contient";
                List<String> solutions = Propagation.propagationDeMarqueurs(reseauSemantique, M1_node, M2_node, relation);

                int i = 0;
                for (String solution : solutions) {
                    System.out.println(M1_node.get(i) + " " + relation + " " + M2_node.get(i));
                    System.out.println(solution);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (choix.equals("2")) {
            System.out.println("Partie 2: l'algorithm d'heritage");

            try {
                FileReader reader = new FileReader("Bases/heritage.json");
                JSONObject reseauSemantique = (JSONObject) new JSONParser().parse(reader);

                String name = "Titi";

                System.out.println("Resultat de l'inference utiliser:");
                System.out.println(name);
                List<String> legacy = Heritage.heritage(reseauSemantique, name);
                List<String> properties = Heritage.getProperties(reseauSemantique, name);

                for (String l : legacy) {
                    System.out.println(l);
                }

                System.out.println("Deduction des priorites:");
                for (String p : properties) {
                    System.out.println(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (choix.equals("3")) {
            System.out.println("Partie 3: l'algorithm de propagation de marqueurs avec exception");
            try {
                FileReader reader = new FileReader("Bases/exception.json");
                JSONObject reseauSemantique = (JSONObject) new JSONParser().parse(reader);

                List<String> M1_node = Collections.singletonList("Modes de Representations des connaissances");
                List<String> M2_node = Collections.singletonList("Axiome A7");
                String relation = "contient";

                List<String> solutions = Exceptions.propagationDeMarqueurs(reseauSemantique, M1_node, M2_node, relation);

                int i = 0;
                for (String solution : solutions) {
                    System.out.println(M1_node.get(i) + " " + relation + " " + M2_node.get(i));
                    System.out.println(solution);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("choix invalide");
        }

        scanner.close();
    }
}
