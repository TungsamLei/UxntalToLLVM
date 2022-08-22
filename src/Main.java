import LLVMGenerator.FunctionConvert;
import LLVMGenerator.LabelConvert;
import LLVMGenerator.MainProgramConvert;
import LLVMGenerator.ZeroPageConvert;
import Tokens.TokenObject;
import utils.ReadUxntalFile;
import utils.TokensMap;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Main {
    static ReadUxntalFile readUxntalFile = new ReadUxntalFile();


    public static void main(String[] args) throws Exception {
        Main main = new Main();
        Stack stack = new Stack();
        ZeroPageConvert zeroPageConvert = new ZeroPageConvert();
        MainProgramConvert mainProgramConvert = new MainProgramConvert();
        FunctionConvert functionConvert = new FunctionConvert();
        LabelConvert labelConvert = new LabelConvert();
        Map<String, List<String>> tal = readUxntalFile.splitBlock(readUxntalFile.readUnxtal());
        System.out.println("Here is the result:");
        if (tal != null) {
            for (String s : tal.keySet()) {
                System.out.println(s + "   " + tal.get(s));
            }
        }

        TokensMap tokensMap = new TokensMap();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Convert Zero Page tokens into token objects.");
        if (tal.get("Zero Page") != null && tal.get("Zero Page").size() != 0) {
            List<TokenObject> zeroPageTokenList = tokensMap.tokensMap(tal.get("Zero Page"));

//            test
            for (TokenObject tokenObject : zeroPageTokenList) {
                System.out.println(tokenObject.getType() + " " + tokenObject.toString());
            }

            System.out.println("Convert Zero Page into LLVM.");
            String zeroPageLLVM = zeroPageConvert.convert(zeroPageTokenList);
            System.out.println(zeroPageLLVM);
        } else {
            System.out.println("Zero Page is null.");
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Convert Main Program tokens into token objects.");
        List<TokenObject> mainProgramTokenList = tokensMap.tokensMap(tal.get("Main Program"));

//        test
        for (TokenObject tokenObject : mainProgramTokenList) {
            System.out.println(tokenObject.getType() + " " + tokenObject.toString());
        }

        System.out.println("Convert Main Program into LLVM.");
        String mainProgramLLVM = mainProgramConvert.convert(mainProgramTokenList, stack);
        System.out.println(mainProgramLLVM);

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Convert function into token objects.");

        if (tal.size() > 3) {
            for (int i = 1; i <= tal.size() - 3; i++) {
                String str = "utils.Function " + i;
                if (tal.get(str) != null && tal.get(str).size() != 0) {
                    List<TokenObject> functionTokenList = tokensMap.tokensMap(tal.get(str));

//        test
                    for (TokenObject tokenObject : functionTokenList) {
                        System.out.println(tokenObject.getType() + " " + tokenObject.toString());
                    }

                    System.out.println("Convert utils.Function " + i + " into LLVM.");
                    String functionLLVM = functionConvert.convert(functionTokenList, stack);
                    System.out.println(functionLLVM);
                }
            }
        } else {
            System.out.println("utils.Function is null.");
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Convert label tokens into token objects.");
        if (tal.get("Label") != null && tal.get("Label").size() != 0) {
            List<TokenObject> labelTokenList = tokensMap.tokensMap(tal.get("Label"));

//        test
            for (TokenObject tokenObject : labelTokenList) {
                System.out.println(tokenObject.getType() + " " + tokenObject.toString());
            }

            System.out.println("Convert Label into LLVM.");
            String labelLLVM = labelConvert.convert(labelTokenList, stack);
            System.out.println(labelLLVM);
        } else {
            System.out.println("Label is null.");
        }
        System.out.println("--------------------------------------------------------------------");
        System.exit(0);
    }
}
