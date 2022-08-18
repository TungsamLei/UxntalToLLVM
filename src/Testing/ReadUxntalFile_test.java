package Testing;

import org.junit.Assert;
import org.junit.Test;
import utils.ReadUxntalFile;
import utils.TokensMap;

import java.util.*;


public class ReadUxntalFile_test {
    ReadUxntalFile readUxntalFile = new ReadUxntalFile();

    /**
     * test the readUxntal method in  ReadUxntalFile class
     */
    @Test
    public void readUxntalTest() {
        List<String> actual = readUxntalFile.readUxntalTest("ex01_0_simple_calc");
        List<String> expected = Arrays.asList("|0100", "@main", "#0006", "#0007", "MUL2", "#18", "DEO", "BRK");

        Assert.assertEquals(expected.size(), actual.size());
        Assert.assertTrue(actual.containsAll(expected));
        System.out.println("readUxntalTest success.");
    }


    @Test
    public void clearCommentTest() {
        String origin = "|0100 ( every program starts at address 256 )\n" +
                "@main\n" +
                "#0006 ( put 6 on the stack as a 2-byte (16-bit, short in Java/C) constant )\n" +
                "#0007 ( put 7 on the stack as a 2-byte (16-bit, short in Java/C) constant )\n" +
                "MUL2 ( multiply the value on the top of the stack, 6, with the value below that, 7 )\n" +
                "#18 DEO ( prints '*' in the terminal, because 42 is the ASCII code for '*' )\n" +
                "( DEO means 'device output' and #18 is the output port for the terminal, like StdOut in Java )\n" +
                "\n" +
                "BRK";

        String actual = readUxntalFile.clearComment(origin);
        String expected = "|0100 \n" +
                "@main\n" +
                "#0006 \n" +
                "#0007 \n" +
                "MUL2 \n" +
                "#18 DEO \n" +
                "\n" +
                "\n" +
                "BRK";
        Assert.assertEquals(expected, actual);
        System.out.println("clearCommentTest success.");
    }

    @Test
    public void splitBlockTest() {
        List<String> input = Arrays.asList("|0000", "@x", "$1", "|0100", "@main", "#0006", "#0007", "MUL2", "#18", "DEO", "BRK");
        Map<String,List<String>> actual = readUxntalFile.splitBlock(input);
        Map<String,List<String>> expected = new HashMap();
        List<String> zero = Arrays.asList("|0000", "@x", "$1");
        expected.put("Zero Page", zero);
        List<String> main = Arrays.asList("|0100", "@main", "#0006", "#0007", "MUL2", "#18", "DEO", "BRK");
        expected.put("Main Program", main);
        List<String> label = new ArrayList<>();
        expected.put("Label", label);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.size(), actual.size());
        Assert.assertEquals(expected.size(), actual.size());
        for(Map.Entry<String,List<String>> entry : actual.entrySet()) {
            Assert.assertEquals(expected.get(entry.getKey()), actual.get(entry.getKey()));
        }
        System.out.println("splitBlockTest success.");
    }
}

