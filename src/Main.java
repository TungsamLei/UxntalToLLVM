import Tokens.AddressLabel;
import Tokens.TokenObject;

import java.util.List;
import java.util.Map;

public class Main {
    static ReadUxntalFile readUxntalFile = new ReadUxntalFile();

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        Map<String, List<String>> tal = readUxntalFile.splitBlock(readUxntalFile.readUnxtal());
        System.out.println("Here is the result:");
        if (tal != null) {
            for (String s : tal.keySet()) {
                System.out.println(s + "   " + tal.get(s));
            }
        }
        System.out.println("--------------------------------------------------------------------");
        TokensMap tokensMap = new TokensMap();
        List<TokenObject> tokenList = tokensMap.tokensMap(tal.get("Main Program"));

        tokensMap.functionConvert(tokenList);
        System.out.println("AddressLabel object:");
        for (AddressLabel a : tokensMap.addressLabelList) {
            if (a.isAbsolute(a.getIndication())) {
                System.out.println(a.getIndication() + "" + a.getContent());
//                System.out.println("here are "+a.getString()+"'s content:");
//                for(TokenObject token : a.getContentToken()){
//                    System.out.print(token.getString()+"   ");
//                }
            }
        }
        System.out.println();
        System.out.println("TokenList:");
        for (TokenObject token : tokenList) {
//            if(!token.getType().equals("unknown"))
            System.out.print(token.toString() + "  ");
        }
        System.out.println();
        System.out.println("Functions:");
        for (Function func : tokensMap.functionList) {
            System.out.println("@" + func.getName());
        }

        System.exit(0);
    }
}
