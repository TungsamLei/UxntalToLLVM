package Testing;

import LLVMGenerator.FunctionConvert;
import LLVMGenerator.LabelConvert;
import LLVMGenerator.MainProgramConvert;
import LLVMGenerator.ZeroPageConvert;
import Tokens.TokenObject;
import org.junit.Test;
import utils.ReadUxntalFile;
import utils.TokensMap;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class ex05_test {
    /**
     * Test whether the program can compile the ex05_simple_calc_registers2.tal correctly.
     *
     * @throws Exception
     */
    @Test
    public void ex05_test() throws Exception {

        ReadUxntalFile readUxntalFile = new ReadUxntalFile();
        Stack stack = new Stack();
        ZeroPageConvert zeroPageConvert = new ZeroPageConvert();
        MainProgramConvert mainProgramConvert = new MainProgramConvert();
        FunctionConvert functionConvert = new FunctionConvert();
        LabelConvert labelConvert = new LabelConvert();
        TokensMap tokensMap = new TokensMap();
        String test = "";

        Map<String, List<String>> tal = readUxntalFile.splitBlock(readUxntalFile.readUxntalTest("ex05_simple_calc_registers2"));

//        zero page convert
        if (tal.get("Zero Page") != null && tal.get("Zero Page").size() != 0) {
            List<TokenObject> zeroPageTokenList = tokensMap.tokensMap(tal.get("Zero Page"));
            String zeroPageLLVM = zeroPageConvert.convert(zeroPageTokenList);
//            System.out.println(zeroPageLLVM);
            test += zeroPageLLVM;
        }

//        main program convert
        List<TokenObject> mainProgramTokenList = tokensMap.tokensMap(tal.get("Main Program"));
        String mainProgramLLVM = mainProgramConvert.convert(mainProgramTokenList, stack);
//        System.out.println(mainProgramLLVM);
        test += mainProgramLLVM;

//        function convert
        if (tal.size() > 3) {
            for (int i = 1; i <= tal.size() - 3; i++) {
                String str = "utils.Function " + i;
                if (tal.get(str) != null && tal.get(str).size() != 0) {
                    List<TokenObject> functionTokenList = tokensMap.tokensMap(tal.get(str));
                    String functionLLVM = functionConvert.convert(functionTokenList, stack);
//                    System.out.println(functionLLVM);
                    test += functionLLVM;
                }
            }
        }

//        label convert
        if (tal.get("Label") != null && tal.get("Label").size() != 0) {
            List<TokenObject> labelTokenList = tokensMap.tokensMap(tal.get("Label"));
            String labelLLVM = labelConvert.convert(labelTokenList, stack);
//            System.out.println(labelLLVM);
            test = test + labelLLVM;
        }

        assertEquals("ex05_test failed","@x1 = global i16 u0x0000\n" +
                "@x2 = global i16 u0x0000\n" +
                "define i16 @main() {\n" +
                "\tstore i16 u0x0006, i16* @x1\n" +
                "\t%r1 = load i16, i16* @x1\n" +
                "\t%r2 = add i16 1,%r1\n" +
                "\tstore i16 %r2, i16* @x2\n" +
                "\t%r3 = load i16, i16* @x1\n" +
                "\t%r4 = load i16, i16* @x2\n" +
                "\t%r5 = mul i16 %r4,%r3\n" +
                "\tcall i16 @putc(i16 %r5)\n" +
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
                "@.str = private unnamed_addr constant [3 x i8] c\"%c\\00\", align 1\n", test);
        System.out.println("ex05_test pass successfully.");
    }
}
