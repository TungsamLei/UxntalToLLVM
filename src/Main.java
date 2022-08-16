import LLVMGenerator.ZeroPageConvert;
import Tokens.AddressLabel;
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
        if (tal.get("Zero Page")!= null && tal.get("Zero Page").size() != 0){
            List<TokenObject> zeroPageTokenList = tokensMap.tokensMap(tal.get("Zero Page"));
//            test



            System.out.println("Convert Zero Page into LLVM.");
//            zeroPageConvert.convert(zeroPageTokenList);
        }
        System.out.println("Zero Page is null.");

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Convert Main Program tokens into token objects.");
        List<TokenObject> tokenList = tokensMap.tokensMap(tal.get("Main Program"));
        tokensMap.functionConvert(tokenList);
        System.out.println("AddressLabel object:");
        for (AddressLabel a : tokensMap.addressLabelList) {
            if (a.isAbsolute(a.getIndication())) {
                System.out.println(a.getIndication() + "" + a.getContent());
            }
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.println("TokenList:");
        for (TokenObject token : tokenList) {
            if (!token.getType().equals("unknown"))
                System.out.print(token.toString() + "  ");
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Functions:");
        for (Function func : tokensMap.functionList) {
            System.out.println("@" + func.getName());
        }

        System.exit(0);
    }
}
