
import java.util.HashMap;
import java.util.Map;

/**
 * Implements construction, encoding, and decoding logic of the Huffman coding algorithm. Characters
 * not in the given seed or alphabet should not be compressible, and attempts to use those
 * characters should result in the throwing of an {@link IllegalArgumentException} if used in {@link
 * #compress(String)}.
 */
public class Huffman {

    public class Node {
        private Character c;
        private int freq;
        private Node lNode;
        private Node rNode;

        public Node(Character character, int frequency, Node leftNode, Node rightNode) {
            c = character;
            freq = frequency;
            lNode = leftNode;
            rNode = rightNode;
        }

        public Node(int frequency, Node leftNode, Node rightNode) {
            freq = frequency;
            lNode = leftNode;
            rNode = rightNode;
        }

    }

    /**
     * Constructs a {@code Huffman} instance from a seed string, from which to deduce the alphabet
     * and corresponding frequencies.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param seed the String from which to build the encoding
     * @throws IllegalArgumentException seed is null, seed is empty, or resulting alphabet only has
     *                                  1 character
     */
    private BinaryMinHeapImpl<Integer, Node> pq = new BinaryMinHeapImpl<>();
    Node root;
    private int inputLength;
    private int outputLength;
    private boolean isCompressed = false;
    private StringBuilder expectedInput = new StringBuilder();
    private HashMap<Character, Integer> encodingLength = new HashMap<>();
    public Huffman(String seed) {
        if (seed == null || seed.isEmpty()) {
            throw new IllegalArgumentException();
        }
        expectedInput.append(seed);

        //check if string has only 1 letter
        boolean sameChar = true;
        for (int i = 1; i < seed.length(); i++) {
            if (seed.charAt(i) != seed.charAt(0)) {
                sameChar = false;
            }
        }
        if (sameChar) {
            throw new IllegalArgumentException("one char");
        }

        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < seed.length(); i++) {
            if (map.containsKey(seed.charAt(i))) {
                map.put(seed.charAt(i), map.get(seed.charAt(i)) + 1);
            } else {
                map.put(seed.charAt(i), 1);
            }

        }

        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            pq.add(entry.getValue(), new Node(entry.getKey(), entry.getValue(), null, null));
        }

        makePriorityQueue();



    }

    /**
     * Constructs a {@code Huffman} instance from a frequency map of the input alphabet.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param alphabet a frequency map for characters in the alphabet
     * @throws IllegalArgumentException if the alphabet is null, empty, has fewer than 2 characters,
     *                                  or has any non-positive frequencies
     */
    public Huffman(Map<Character, Integer> alphabet) {


        if (alphabet == null || alphabet.isEmpty() || alphabet.size() < 2) {
            throw new IllegalArgumentException();
        }
        expectedInput = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : alphabet.entrySet()) {
            if (entry.getValue() <= 0) {
                throw new IllegalArgumentException("negative frequency");
            }
            expectedInput.append(entry.getKey());
        }

        for (Map.Entry<Character, Integer> entry : alphabet.entrySet()) {
            pq.add(entry.getValue(), new Node(entry.getKey(), entry.getValue(), null, null));
        }

        makePriorityQueue();




    }

    private void makePriorityQueue() {
        BinaryMinHeapImpl.Entry<Integer, Node> min1;
        BinaryMinHeapImpl.Entry<Integer, Node> min2;


        while (pq.size() > 2) {
            min1 = pq.extractMin();
            min2 = pq.extractMin();

            pq.add(min1.key + min2.key, new Node(min1.key + min2.key,
                    min1.value, min2.value));
        }
        if (pq.size() == 2) {
            min1 = pq.extractMin();
            min2 = pq.extractMin();
            root = new Node(min1.key + min2.key, min1.value, min2.value);
            pq.add(min1.key + min2.key, root);
        }

    }



    /**
     * Compresses the input string.
     *
     * @param input the string to compress, can be the empty string
     * @return a string of ones and zeroes, representing the binary encoding of the inputted String.
     * @throws IllegalArgumentException if the input is null or if the input contains characters
     *                                  that are not compressible
     */
    public String compress(String input) {
        if (input == null) {
            throw new IllegalArgumentException("null input in compress");
        }

        //check if the characters are within the given alphabet
        for (int i = 0; i < input.length(); i++) {
            boolean validInput = false;
            for (int j = 0; j < expectedInput.length(); j++) {
                if (input.charAt(i) == expectedInput.charAt(j)) {
                    validInput = true;
                }
            }
            if (!validInput) {
                throw new IllegalArgumentException();
            }
        }

        isCompressed = true;

        inputLength += input.length();
        if (input.equals("")) {
            return "";
        } else {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < input.length(); i++) {
                //finds the corresponding bit through recursion
                char c = input.charAt(i);
                StringBuilder code = new StringBuilder();
                encodeCharacter(root, c, code, str);

            }
            outputLength += str.length();
            return str.toString();
        }
    }

    private void encodeCharacter(Node node, char c, StringBuilder code, StringBuilder str) {

        if (node != null && node.c != null && node.c.equals(c)) {
            str.append(code);

        } else if (node != null && node.c == null) {

            code.append("0");
            encodeCharacter(node.lNode, c, code, str);
            //appended a string, have to get rid of it if it doesn't work
            code.deleteCharAt(code.length() - 1);
            code.append("1");
            encodeCharacter(node.rNode, c, code, str);
            code.deleteCharAt(code.length() - 1);

        }

    }

    /**
     * Decompresses the input string.
     *
     * @param input the String of binary digits to decompress, given that it was generated by a
     *              matching instance of the same compression strategy
     * @return the decoded version of the compressed input string
     * @throws IllegalArgumentException if the input is null, or if the input contains characters
     *                                  that are NOT 0 or 1, or input contains a sequence of bits
     *                                  that is not decodable
     */
    public String decompress(String input) {
        if (input == null) {
            throw new IllegalArgumentException("null input");
        }
        for (int i = 0; i < input.length(); i++) {
            int a = input.charAt(i);
            if (input.charAt(i) != '0' && input.charAt(i) != '1') {
                throw new IllegalArgumentException("char is not 0 or 1");
            }
        }

        if (input.equals("")) {
            return "";
        } else {

            StringBuilder str = new StringBuilder();
            Node current = root;

            for (int i = 0; i < input.length(); i++) {
                char currChar = input.charAt(i);
                if (currChar == '0') {
                    current = current.lNode;
                } else {
                    current = current.rNode;
                }

                if (current.c != null) {
                    str.append(current.c);
                    current = root;
                }
            }

            if (current != root) {
                throw new IllegalArgumentException();
            }

            return str.toString();

        }
    }

    /**
     * Computes the compression ratio so far. This is the length of all output strings from {@link
     * #compress(String)} divided by the length of all input strings to {@link #compress(String)}.
     * Assume that each char in the input string is a 16 bit int.
     *
     * @return the ratio of the total output length to the total input length in bits
     * @throws IllegalStateException if no calls have been made to {@link #compress(String)} before
     *                               calling this method
     */
    public double compressionRatio() {
        if (!isCompressed) {
            throw new IllegalStateException("compressed has not been called");
        } else {
            return (double)(outputLength) / (double)(inputLength * 16);
        }
    }

    /**
     * Computes the expected encoding length of an arbitrary character in the alphabet based on the
     * objective function of the compression.
     * <p>
     * The expected encoding length is simply the sum of the length of the encoding of each
     * character multiplied by the probability that character occurs.
     *
     * @return the expected encoding length of an arbitrary character in the alphabet
     */
    public double expectedEncodingLength() {
        HashMap<Character, Integer> tracker = new HashMap<>();
        //find numerator of probability
        for (int i = 0; i < expectedInput.length(); i++) {
            if (tracker.containsKey(expectedInput.charAt(i))) {
                tracker.put(expectedInput.charAt(i),
                        tracker.get(expectedInput.charAt(i)) + 1);
            } else {
                tracker.put(expectedInput.charAt(i), 1);
            }
            //find length of each character
            encodingLength.put(expectedInput.charAt(i),
                    compress(expectedInput.substring(i, i + 1)).length());
        }


        double denominator = expectedInput.length();
        double total = 0;
        for (char c : tracker.keySet()) {
            total += (encodingLength.get(c) * (double) tracker.get(c) / denominator);
        }

        return total;



    }
}
