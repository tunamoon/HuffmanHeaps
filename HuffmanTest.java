import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class HuffmanTest {

    //test compression ratio - compressed not called before
    @Test (expected = IllegalStateException.class)
    public void testCompressionRatioUncalledCompression() {
        Huffman h1 = new Huffman("aaabb");

        h1.compressionRatio();
    }

    //test compression ratio - normal
    @Test
    public void testCompressionRatio1() {
        Huffman h1 = new Huffman("aaabb");
        h1.compress("aaa");
        h1.compress("a");

        assertEquals((double) 4 / (double) 64, h1.compressionRatio(), 0);
    }

    //more complicated string
    @Test
    public void testCompressionRatio2() {
        Huffman h1 = new Huffman("aaaabbd");
        h1.compress("aaa");
        h1.compress("baba");

        assertEquals((double) 9 / (double) (16 * 7), h1.compressionRatio(), 0);
    }

    //test expected encoding length
    @Test
    public void testExpectedSimple() {
        Huffman h1 = new Huffman("aaabb");
        assertEquals(1.0, h1.expectedEncodingLength(), 0);
    }

    @Test
    public void testExpectedComplex() {
        Huffman h1 = new Huffman("aaaabbd");
        double answer = 4.0 / 7.0 + 4.0 / 7.0 + 2.0 / 7.0;
        assertEquals(answer, h1.expectedEncodingLength(), 0);
    }

    //test compress null
    @Test (expected = IllegalArgumentException.class)
    public void testCompressNull() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        h1.compress(null);
    }

    //test compress illegal wrong string
    @Test (expected = IllegalArgumentException.class)
    public void testCompressWrongString() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        h1.compress("g");
    }

    //test compress - normal
    @Test
    public void testCompress() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        assertEquals("00100", h1.compress("aad"));
        assertEquals("11", h1.compress("b"));
    }

    //test decompress - null
    @Test (expected = IllegalArgumentException.class)
    public void testDeompressNull() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        h1.decompress(null);
    }

    //test decompress - not 0 or 1
    @Test (expected = IllegalArgumentException.class)
    public void testDeompressWrongCharl() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        h1.decompress("a");
    }

    //test decompress - not decodable
    @Test (expected = IllegalArgumentException.class)
    public void testDeompressNotDecoable() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        h1.decompress("01001");
    }

    //test decompress - no string
    @Test
    public void testDecompressNoString() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        assertEquals("", h1.decompress(""));
    }

    //test compress - no string
    @Test
    public void testCompressNoString() {
        Huffman h1 = new Huffman("aaaaabbbbccd");
        assertEquals("", h1.compress(""));
    }

    //check if string only has one character
    @Test (expected = IllegalArgumentException.class)
    public void testInputOneChar() {
        Huffman h1 = new Huffman("a");
    }

    //check other constructor
    @Test
    public void testMapConstructor() {
        Map<Character, Integer> map = new HashMap<>();
        map.put('a', 4);
        map.put('b', 2);
        map.put('d', 1);

        Huffman h1 = new Huffman(map);

        assertEquals("1100", h1.compress("aad"));
    }

    //test null, empty, negative freq, one node
    @Test (expected = IllegalArgumentException.class)
    public void testMapConstructorNull() {
        Map<Character, Integer> map = null;

        Huffman h1 = new Huffman(map);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMapConstructorEmpty() {
        Map<Character, Integer> map = new HashMap<>();

        Huffman h1 = new Huffman(map);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMapConstructorNegative() {
        Map<Character, Integer> map = new HashMap<>();
        map.put('a', 4);
        map.put('b', -2);
        map.put('d', 1);
        Huffman h1 = new Huffman(map);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMapConstructorOneNode() {
        Map<Character, Integer> map = new HashMap<>();
        map.put('a', 4);
        Huffman h1 = new Huffman(map);
    }

    //test seed if null or empty
    @Test (expected = IllegalArgumentException.class)
    public void testSeedNull() {
        String str = null;
        Huffman h1 = new Huffman(str);

    }

    @Test (expected = IllegalArgumentException.class)
    public void testSeedEmpty() {
        Huffman h1 = new Huffman("");
    }


}
