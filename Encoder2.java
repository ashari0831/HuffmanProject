import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.Gson;



public class Encoder2 {

    static class Node implements Comparable<Node> {
        char character;
        int frequency;
        Node leftChild;
        Node rightChild;

        Node(char character, int frequency, Node leftChild, Node rightChild) {
            this.character = character;
            this.frequency = frequency;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
        //check if a node is leaf or not
        boolean isLeaf() {
            return this.leftChild == null && this.rightChild == null;
        }

        //a method for comparing chars with their frequency
        @Override
        public int compareTo(Node that) {
            int freqComparison = Integer.compare(this.frequency, that.frequency);
            if (freqComparison != 0) {
                return freqComparison;
            }

            return Integer.compare(this.character, that.character);
        }
    }

    //a queue which help us to arrange nodes' frequencies
    static Node buildTree(int[] freq) {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (char i = 0; i < ALPHA_SIZE; i++) {
            if (freq[i] > 0) {
                pq.add(new Node(i, freq[i], null, null));
            }
        }

        if (pq.size() == 1) {
            pq.add(new Node('\0', 1, null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            pq.add(parent);
        }
        return pq.poll();
    }


    static int ALPHA_SIZE = 256;

    // this static class help us to encode the ...
    static class EncodedResult {
        Node root;
        String encodedData;

        public EncodedResult(String encodedData, Node root) {
            this.encodedData = encodedData;
            this.root = root;
        }
        // this will give us the root node
        public Node getRoot() {
            return this.root;
        }
    } //end encodedResult class

    //this will encode the tree including chars and frequencies
    public EncodedResult compress(String data) {

        int[] freq = buildFrequencyTable(data);
        Node root = buildTree(freq);
        Map<Character, String> lookupTable = buildLookup(root);

        return new EncodedResult(genCodeData(data, lookupTable), root);
    }

    private static String genCodeData(String data, Map<Character, String> lookupTable) {

        StringBuilder builder = new StringBuilder();
        for (char character : data.toCharArray()) {
            builder.append(lookupTable.get(character));
        }
        return builder.toString();
    }

    //this will decode the encoded data and give us the text
    public String decompress(EncodedResult result) {
        Node curr = result.getRoot();
        int i = 0;
        StringBuilder str = new StringBuilder();

        while (i < result.encodedData.length()) {

            while (!curr.isLeaf()) {
                char bit = result.encodedData.charAt(i);

                if (bit == '0') {
                    curr = curr.leftChild;
                } else if (bit == '1') {
                    curr = curr.rightChild;
                }
                i++;
            }
            str.append(curr.character);
            curr = result.getRoot();
        }
        return str.toString();
    }

    //it counts number of each char in the string
    static int[] buildFrequencyTable(String data) {
        int[] freq = new int[ALPHA_SIZE];
        for (char character : data.toCharArray()) {
            freq[character]++;
        }
        return freq;
    }


    static Map<Character, String> buildLookup(Node root) {
        Map<Character, String> lookup = new HashMap<>();
        buildLookupCoding(root, "", lookup);

        return lookup;
    }

    static void buildLookupCoding(Node node, String s, Map<Character, String> lookupTable) {

        if (!node.isLeaf()) {
            buildLookupCoding(node.leftChild, s + '0', lookupTable);
            buildLookupCoding(node.rightChild, s + '1', lookupTable);
        } else {
            lookupTable.put(node.character, s);
        }
    }


    public static void main(String[] args) {

        StringBuilder str = new StringBuilder();

        Scanner fileAddress = new Scanner(System.in);

        System.out.print("Give me the address of file: ");
        String path = fileAddress.next();
        String[] arr = path.split("[.]",0);
        String typeOfFile = arr[arr.length - 1];

        Encoder2 enc = new Encoder2();

        //this class help us to store the object of class in .cmp file with Json form
        Gson gson = new Gson();

        //it checks if the given file is .txt then encode it into .cmp file
        if (typeOfFile.equals("txt")) {
            try (Scanner input = new Scanner(Paths.get(path))) {

                while (input.hasNext()) {
                    str.append(input.nextLine());
                }
            } catch (IOException | NoSuchElementException e) {
                e.printStackTrace();
            }

            EncodedResult result = enc.compress(str.toString());


            String json = gson.toJson(result);


            try (Formatter output = new Formatter("src/encodedText.cmp")) {
                output.format("%s%n", json);
            } catch (FileNotFoundException | FormatterClosedException e) {
                e.printStackTrace();
            }

            //it checks if the given file is .cmp then decode it into .txt file
        } else if (typeOfFile.equals("cmp")) {
            try (Scanner input = new Scanner(Paths.get(path))) {

                String className = input.nextLine();
                EncodedResult result = gson.fromJson(className, EncodedResult.class);
                String text = enc.decompress(result);

                try (Formatter output = new Formatter("src/text.txt")) {
                    output.format("%s%n", text);
                } catch (FileNotFoundException | FormatterClosedException e) {
                    e.printStackTrace();
                }


            } catch (IOException | NoSuchElementException  e) {
                e.printStackTrace();
            }
         }


    }

}
