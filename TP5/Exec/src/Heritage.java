import java.util.*;
import org.json.simple.*;

public class Heritage {
    public static String getLabel(JSONObject reseauSemantique, String nodeId) {
        JSONArray nodes = (JSONArray) reseauSemantique.get("nodes");
        for (Object o : nodes) {
            JSONObject node = (JSONObject) o;
            if (node.get("id").equals(nodeId)) {
                return (String) node.get("label");
            }
        }
        return "";
    }

    public static List<String> heritage(JSONObject reseauSemantique, String name) {
        List<String> legacy = new ArrayList<>();
        boolean theEnd = false;

        JSONArray nodes = (JSONArray) reseauSemantique.get("nodes");
        JSONArray edges = (JSONArray) reseauSemantique.get("edges");

        JSONObject node = null;
        for (Object o : nodes) {
            JSONObject n = (JSONObject) o;
            if (n.get("label").equals(name)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            return legacy;
        }

        List<String> legacyEdges = new ArrayList<>();
        for (Object o : edges) {
            JSONObject edge = (JSONObject) o;
            if (edge.get("from").equals(node.get("id")) && edge.get("label").equals("is_a")) {
                legacyEdges.add((String) edge.get("to"));
            }
        }

        while (!theEnd) {
            if (legacyEdges.isEmpty()) {
                theEnd = true;
            } else {
                String n = legacyEdges.remove(legacyEdges.size() - 1);
                legacy.add(getLabel(reseauSemantique, n));
                for (Object o : edges) {
                    JSONObject edge = (JSONObject) o;
                    if (edge.get("from").equals(n) && edge.get("label").equals("is_a")) {
                        legacyEdges.add((String) edge.get("to"));
                    }
                }
            }
        }
        return legacy;
    }

    public static List<String> getProperties(JSONObject reseauSemantique, String name) {
        List<String> properties = new ArrayList<>();
        JSONArray nodes = (JSONArray) reseauSemantique.get("nodes");
        JSONArray edges = (JSONArray) reseauSemantique.get("edges");

        JSONObject node = null;
        for (Object o : nodes) {
            JSONObject n = (JSONObject) o;
            if (n.get("label").equals(name)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            return properties;
        }

        List<String> legacyEdges = new ArrayList<>();
        for (Object o : edges) {
            JSONObject edge = (JSONObject) o;
            if (edge.get("from").equals(node.get("id")) && edge.get("label").equals("is_a")) {
                legacyEdges.add((String) edge.get("to"));
            }
        }

        while (!legacyEdges.isEmpty()) {
            String n = legacyEdges.remove(legacyEdges.size() - 1);
            List<JSONObject> propertiesNodes = new ArrayList<>();
            for (Object o : edges) {
                JSONObject edge = (JSONObject) o;
                if (edge.get("from").equals(n) && !edge.get("label").equals("is_a")) {
                    propertiesNodes.add(edge);
                }
            }
            for (JSONObject pn : propertiesNodes) {
                properties.add(pn.get("label") + ": " + getLabel(reseauSemantique, (String) pn.get("to")));
            }
            for (Object o : edges) {
                JSONObject edge = (JSONObject) o;
                if (edge.get("from").equals(n) && edge.get("label").equals("is_a")) {
                    legacyEdges.add((String) edge.get("to"));
                }
            }
        }
        return properties;
    }
}
