package Testing;

import Tokens.TokenObject;
import org.junit.Assert;
import org.junit.Test;
import utils.TokensMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokensMap_test {

    TokensMap tokensMap = new TokensMap();
    TokenObject tokenObject = new TokenObject();

    @Test
    public void tokensMapTest() {
        List<String> input = Arrays.asList("|0100", "@main", "#0006", "MUL2", ".x", "0034");
        try {
            List<TokenObject> actual = tokensMap.tokensMap(input);
            Assert.assertEquals("Padding", actual.get(0).getType());
            Assert.assertEquals("AddressLabel", actual.get(1).getType());
            Assert.assertEquals("LiteralConstant", actual.get(2).getType());
            Assert.assertEquals("Operation", actual.get(3).getType());
            Assert.assertEquals("AddressLabelReference", actual.get(4).getType());
            Assert.assertEquals("RawConstant", actual.get(5).getType());
            System.out.println("tokensMapTest success.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
