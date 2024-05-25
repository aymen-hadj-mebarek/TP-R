import java.util.*;
import org.json.simple.*;

public class Propagation {
    // Retrieves the label of a node given its relationship to another node
    public static String getLabel(JSONObject reseauSemantique, JSONObject node, String relation) {
        List<String> nodeRelationEdgesLabel = new ArrayList<>();
        JSONArray edges = (JSONArray) reseauSemantique.get("edges");
        JSONArray nodes = (JSONArray) reseauSemantique.get("nodes");

        for (Object o : edges) {
            JSONObject edge = (JSONObject) o;
            if (edge.get("to").equals(node.get("id")) && edge.get("label").equals(relation)) {
                for (Object n : nodes) {
                    JSONObject no = (JSONObject) n;
                    if (no.get("id").equals(edge.get("from"))) {
                        nodeRelationEdgesLabel.add((String) no.get("label"));
                    }
                }
            }
        }
        return "il y a un lien entre les 2 noeuds : " + String.join(", ", nodeRelationEdgesLabel);
    }

    // Performs marker propagation between nodes
    public static List<String> propagationDeMarqueurs(JSONObject reseauSemantique, List<String> node1, List<String> node2, String relation) {
        List<String> solutionsFound = new ArrayList<>();
        JSONArray nodes = (JSONArray) reseauSemantique.get("nodes");
        JSONArray edges = (JSONArray) reseauSemantique.get("edges");

        for (int i = 0; i < Math.min(node1.size(), node2.size()); i++) {
            boolean solutionFound = false;

            try {
                JSONObject M1 = null;
                JSONObject M2 = null;

                // Find the nodes by their labels
                for (Object o : nodes) {
                    JSONObject node = (JSONObject) o;
                    if (node.get("label").equals(node1.get(i))) {
                        M1 = node;
                    }
                    if (node.get("label").equals(node2.get(i))) {
                        M2 = node;
                    }
                }

                if (M1 == null || M2 == null) {
                    solutionsFound.add("Aucune reponse n'est fournie par manque de connaissances.");
                    continue;
                }

                // Find all "is_a" edges leading to M1
                List<JSONObject> propagationEdges = new ArrayList<>();
                for (Object o : edges) {
                    JSONObject edge = (JSONObject) o;
                    if (edge.get("to").equals(M1.get("id")) && edge.get("label").equals("is a")) {
                        propagationEdges.add(edge);
                    }
                }

                // Traverse the "is_a" edges to find the relationship to M2
                while (!propagationEdges.isEmpty() && !solutionFound) {
                    JSONObject tempNode = propagationEdges.remove(propagationEdges.size() - 1);
                    List<JSONObject> tempNodeContientEdges = new ArrayList<>();
                    for (Object o : edges) {
                        JSONObject edge = (JSONObject) o;
                        if (edge.get("from").equals(tempNode.get("from")) && edge.get("label").equals(relation)) {
                            tempNodeContientEdges.add(edge);
                        }
                    }
                    for (JSONObject edge : tempNodeContientEdges) {
                        if (edge.get("to").equals(M2.get("id"))) {
                            solutionFound = true;
                            break;
                        }
                    }
                    if (!solutionFound) {
                        for (Object o : edges) {
                            JSONObject edge = (JSONObject) o;
                            if (edge.get("to").equals(tempNode.get("from")) && edge.get("label").equals("is a")) {
                                propagationEdges.add(edge);
                            }
                        }
                    }
                }

                if (solutionFound) {
                    solutionsFound.add(getLabel(reseauSemantique, M2, relation));
                } else {
                    solutionsFound.add("il n'y a pas un lien entre les 2 noeuds");
                }
            } catch (Exception e) {
                solutionsFound.add("Aucune reponse n'est fournie par manque de connaissances.");
            }
        }
        return solutionsFound;
    }
}
