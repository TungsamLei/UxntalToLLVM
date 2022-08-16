import LLVMGenerator.ZeroPageConvert;
import Tokens.AddressLabel;
import Tokens.Padding;
import Tokens.TokenObject;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Main {
    static ReadUxntalFile readUxntalFile = new ReadUxntalFile();
    Stack stack = new Stack();

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        ZeroPageConvert zeroPageConvert = new ZeroPageConvert();
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
//            zeroPageConvert.convert(zeroPageTokenList);
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

//        tokensMap.functionConvert(mainProgramTokenList);
//        System.out.println("AddressLabel object:");
//        for (AddressLabel a : tokensMap.addressLabelList) {
//            if (a.isAbsolute(a.getIndication())) {
//                System.out.println(a.getIndication() + "" + a.getContent());
//            }
//        }
//        System.out.println("TokenList:");
//        for (TokenObject token : mainProgramTokenList) {
//            if (!token.getType().equals("unknown"))
//                System.out.print(token.toString() + "  ");
//        }
//        System.out.println();
//        System.out.println("Functions:");
//        for (Function func : tokensMap.functionList) {
//            System.out.println("@" + func.getName());
//        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Convert Function tokens into token objects.");
//        todo:循环functionTokenList生成
        if (tal.get("Function ") != null && tal.get("Zero Page").size() != 0) {
            List<TokenObject> functionTokenList = tokensMap.tokensMap(tal.get("Function"));

//        test
            for (TokenObject tokenObject : functionTokenList) {
                System.out.println(tokenObject.getType() + " " + tokenObject.toString());
            }

            System.out.println("Convert Function into LLVM.");
//            zeroPageConvert.convert(zeroPageTokenList);
        } else {
            System.out.println("Function is null.");
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
//            zeroPageConvert.convert(zeroPageTokenList);
        } else {
            System.out.println("Label is null.");
        }
        System.out.println("--------------------------------------------------------------------");

        System.exit(0);
    }
}
