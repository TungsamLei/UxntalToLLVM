package Testing;

import LLVMGenerator.FunctionConvert;
import Tokens.TokenObject;
import org.junit.Assert;
import org.junit.Test;
import utils.TokensMap;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class FunctionConvert_test {
    FunctionConvert functionConvert = new FunctionConvert();
    TokensMap tokensMap = new TokensMap();
    Stack stack = new Stack();

    @Test
    public void functionConvertTest() throws Exception {
        List<String> strings = Arrays.asList("@sum-sq", "DUP2", "MUL2", "JMP2r");
        List<TokenObject> input = tokensMap.tokensMap(strings);
        String actual = functionConvert.convert(input, stack);
        String expected = "define i16 @sum-sq(i16 %r1, i16 %r2) {\n" +
                "\t%r1 = mul i16 %r1,%r1\n" +
                "\tret i16 %r1\n" +
                "}";
        Assert.assertEquals(expected, actual);
        System.out.println("functionConvertTest success.");
    }
}
