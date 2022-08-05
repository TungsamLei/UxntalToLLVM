import Tokens.AddressLabel;
import Tokens.TokenObject;

import java.util.List;
import java.util.Map;

public class Main {
    static ReadUxntalFile readUxntalFile = new ReadUxntalFile();

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        Map<String, List<String>> tal = readUxntalFile.splitBlock(readUxntalFile.readUnxtal());
        System.out.println("Here is the result :");
        if (tal != null) {
            for (String s : tal.keySet()) {
                System.out.println(s + "   " + tal.get(s));
            }
        }
        System.out.println();
        TokensMap tokensMap = new TokensMap();
        List<TokenObject> tokenList = tokensMap.tokenObjectMapping(tal.get("main-program"));

        tokensMap.FunctionConvert(tokenList);
        System.out.println("Here is your Addresslable object");
        for (AddressLabel a : tokensMap.lableList) {
            if (a.isAboslute(a.getIndication())) {
                System.out.println(a.getIndication() + "" + a.getName());
//                System.out.println("here are "+a.getString()+"'s content:");
//                for(TokenObject token : a.getContentToken()){
//                    System.out.print(token.getString()+"   ");
//                }
            }
        }
        System.out.println();
        System.out.println("TokenList :");
        for (TokenObject token : tokenList) {
//            if(!token.getType().equals("unknow"))
            System.out.print(token.getString() + "  ");
        }
        System.out.println();
        System.out.println("Here are functions");
        for (Function func : tokensMap.functionList) {
            System.out.println("@" + func.getName());
        }

        System.exit(0);
    }
}
