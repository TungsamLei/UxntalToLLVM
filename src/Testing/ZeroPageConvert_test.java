package Testing;

import LLVMGenerator.ZeroPageConvert;
import Tokens.TokenObject;
import org.junit.Assert;
import org.junit.Test;
import utils.TokensMap;

import java.util.Arrays;
import java.util.List;

public class ZeroPageConvert_test {
    ZeroPageConvert zeroPageConvert = new ZeroPageConvert();
    TokensMap tokensMap = new TokensMap();

    @Test
    public void zeroPageConvertTest() throws Exception {
        List<String> strings = Arrays.asList("|0000", "@x", "$2");
        List<TokenObject> input = tokensMap.tokensMap(strings);
        String actual = zeroPageConvert.convert(input);
        String expected = "@x = global i16 u0x0000\n";
        Assert.assertEquals(expected,actual);
        System.out.println("zeroPageConvertTest success.");
    }
}
