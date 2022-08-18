package Testing;

import LLVMGenerator.MainProgramConvert;
import Tokens.TokenObject;
import org.junit.Assert;
import org.junit.Test;
import utils.TokensMap;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class MainProgramConvert_test {
    MainProgramConvert mainProgramConvert = new MainProgramConvert();
    TokensMap tokensMap = new TokensMap();
    Stack stack = new Stack();

    @Test
    public void mainProgramConvertTest() throws Exception {
        List<String> strings = Arrays.asList("|0100", "@main", "#0006", "#0007", "MUL2", "#18", "DEO", "BRK");
        List<TokenObject> input = tokensMap.tokensMap(strings);
        String actual = mainProgramConvert.convert(input, stack);
        String expected = "define i16 @main() {\n" +
                "\t%r1 = mul i16 u0x0007, u0x0006\n" +
                "\tcall i16 @putc(i16 %r1)\n" +
                "\tret i16 0\n" +
                "}\n" +
                "declare dso_local i16 @printf(i8*, ...)\n" +
                "\n" +
                "define i16 @putc(i16 %r1) {\n" +
                "    call i16 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i16 %r1)\n" +
                "  \n" +
                "    ret i16 0\n" +
                "}\n" +
                "\n" +
                "@.str = private unnamed_addr constant [3 x i8] c\"%c\\00\", align 1\n";
        Assert.assertEquals(expected, actual);
        System.out.println("mainProgramConvertTest success.");
    }
}
