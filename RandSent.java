// C:\Users\owner\Dropbox\Junior Spring Semester 2016\Natural Language Processing\HW 1

import java.util.Scanner;
import java.util.Random;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;

public class RandSent {

    public static void main(String[] args) throws IOException {
        boolean option = false;
        int numSent = 1;
		if (args[0].equals("-t")) {
            option = true;
            args[0] = args[1];
            if (args.length == 3) {
				numSent = Integer.parseInt(args[2]);
			}
        } else {
			if (args.length == 2) {
				numSent = Integer.parseInt(args[1]);	
			}
		}
        
		Scanner scan = new Scanner(new FileReader(new File(args[0])));
        HashMap<String, List<String>> tree = new HashMap<>();

        while (scan.hasNext()) {
            String str = scan.nextLine();
            if (str.length() != 0 && str.charAt(0) != '#') {
                String[] line = str.split("\\s+");
                String child = new String();
                child = "";

                for (int i = 2; i < line.length; i++) {
                    if (line[i].equals("#") || line[i].contains("(")) {
                        break;
                    }
                    child += line[i] + " ";
                }

                child = line[0] + " " + child;

                if (tree.containsKey(line[1])) {
                    List<String> children = tree.get(line[1]);
                    children.add(child);
                } else {
                    List<String> children = new LinkedList<>();
                    children.add(child); // currently only includes the children, not the probability
                    tree.put(line[1], children);
                }
            }
        }

        for (int i = 0; i < numSent; i++) {
            System.out.println(generateSentence("1 ROOT", tree, option));
        }
    }

    private static String generateSentence(String sentence, HashMap<String, List<String>> cfg, boolean opt) {
        String[] tokens = sentence.split("\\s+");
        String newSent = "";
        Random rand = new Random();
        //System.out.println(sentence);
        if (tokens.length == 1 && !cfg.containsKey(tokens[0])) {
            return tokens[0];
        } else if (tokens.length == 2 && !cfg.containsKey(tokens[1])) {
            return tokens[1];
        }

        for (int i = 1; i < tokens.length; i++) {
            String nextPart = "";
            if (!cfg.containsKey(tokens[i])) {
                nextPart = tokens[i];
            } else {
                List<String> children = cfg.get(tokens[i]);
                int index = 0;

                // need to base choice on token first value of each child
                double weight = 0;
                double[] distribution = new double[children.size()];
                double denom = 0;
                for (int j = 0; j < distribution.length; j++) {
                    String[] tkn = children.get(j).split("\\s+");
                    weight = Double.parseDouble(tkn[0]);
                    distribution[j] = denom + weight;
                    denom += weight;
                }

                double prob = rand.nextDouble() * denom;

                for (int j = 0; j < distribution.length; j++) {
                    if (prob <= distribution[j] ) {
                        index = j;
                        break;
                    }
                }

                nextPart = children.get(index);
            }

            if (opt && cfg.containsKey(tokens[i])) {
                newSent += "(" + tokens[i] + " " + generateSentence(nextPart, cfg, opt) + ")";
            } else {
                newSent += generateSentence(nextPart, cfg, opt) + " ";
            }
        }

        return newSent.trim();
    }
}
