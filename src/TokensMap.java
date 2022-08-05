import Tokens.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TokensMap {
    public List<AddressLabel> lableList;
    public List<Function> functionList;
    public List<TokenObject> tokenObjectMapping(List<String> list) throws Exception {
        if(list==null || list.size()==0){
            throw new Exception("You just input nothing here, plz try again");
        }
        lableList = new ArrayList<>();
        List<TokenObject> res = new ArrayList<>();
        int idx=0;
        while(idx<list.size()){
            TokenObject instance = getSingleMapping(list.get(idx));
            if (instance.getType()==null) instance.setType(""+instance.getClass());
            res.add(instance);
            // only do content fill when it's a address label, it's a bit long
            // but time complexity is O(n)
            if(instance instanceof AddressLabel){
                lableList.add((AddressLabel) instance);
                // Tokenclass is generated while adding content and child labels to the parent Label
                if(((AddressLabel) instance).isAboslute(((AddressLabel) instance).getIndication())){
                    List<TokenObject> contentToken = new ArrayList<>();
                    List<AddressLabel> subLabel = new ArrayList<>();
                    idx++;
                    while(idx<list.size()){
                        String cur = list.get(idx);
                        // end with special content
                        if(cur.equals("BRK") || cur.charAt(0)=='@' || cur.charAt(0)=='|'){
                            idx--;
                            break;
                        }
                        // add this object to res, and it's also the content of our abs Addresslable:instance
                        TokenObject content = getSingleMapping(list.get(idx));
                        if (content.getType()==null) content.setType(""+content.getClass());

                        // If i need subLables show in list, then i need this line
                        // res.add(content);

                        contentToken.add(content);
                        // if it is sub lables
                        if(content instanceof AddressLabel && ((AddressLabel) content).isRelative(((AddressLabel) content).getIndication())){
                            subLabel.add((AddressLabel) content);
                            lableList.add((AddressLabel) content);
                        }
                        if(cur.equals("JMP2r") || cur.equals("JMP2") || cur.equals("]")){
                            break;
                        }
                        idx++;
                    }
                    // AddressLabel add sub lables and content token class
                    ((AddressLabel) instance).setSubLabel(subLabel);
                    ((AddressLabel) instance).setContentToken(contentToken);
                }
            }
            // as for other labels, we directly add them
            idx++;
        }
        return res;
    }

    public TokenObject getSingleMapping(String str) throws Exception {
        if(str.trim().equals("")){
            throw new Exception("You just input nothing here, plz try again");
        }
        AddressLabel addressLabel = new AddressLabel();
        AddressLabelReference addressLabelReference = new AddressLabelReference();
        LiteralConstant literal = new LiteralConstant();
        Operation Operation = new Operation();
        Padding padding = new Padding();
        RawConstant rawConstant = new RawConstant();
        char indication = str.charAt(0);
        String followContent = str.substring(1);
        if(padding.isPadding(indication)){
            padding.setIndication(indication);
            padding.setContent(followContent);
            return padding;
        }
        if(addressLabel.isAddressLabel(indication)){
            addressLabel.setIndication(indication);
            addressLabel.setName(followContent);
            return addressLabel;
        }
        if(addressLabelReference.isAddressLabelRefrence(indication,followContent)){
            addressLabelReference.setIndication(indication);
            addressLabelReference.setContent(followContent);
            return addressLabelReference;
        }
        if(literal.isLiteral(indication+"")){
            return new LiteralConstant(""+indication,followContent);
        }
        else if(str.length()>=3 && literal.isLiteral(str.substring(0,3))){
            try {
                return new LiteralConstant(str.substring(0, 3), str.substring(3));
            }
            catch (Exception e){
                return new LiteralConstant(str.substring(0,3));
            }
        }
        if(str.length()>=3 && Operation.isOperations(str.substring(0,3),str.substring(3))){
            Operation.setCapital(str.substring(0,3));
            Operation.setFollowInfo(str.substring(3));
            return Operation;
        }

        if(rawConstant.isRawContent(str)){
            rawConstant.setContent(str);
            return rawConstant;
        }
        TokenObject tokenObject = new TokenObject();
        tokenObject.setStr(str);
        tokenObject.setType("unknow");
        return tokenObject;
    }


    public void FunctionConvert(List<TokenObject> list){
        functionList = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            TokenObject token = list.get(i);
            if(token instanceof AddressLabel){
                Function func = functionDetermin((AddressLabel) token);
                if(func!=null){
                    list.remove(i);
                    lableList.remove(token);
                    functionList.add(func);
                    // never forget i--, because once we remove a element, index of list --;
                    i--;
                }
            }
        }
    }

    public Function functionDetermin(AddressLabel addressLabel){
        List<TokenObject>  content = addressLabel.getContentToken();
        int lastIndex = content.size()-1;
        String lastElement = content.get(lastIndex).getString();
        Function res = new Function();
        if("JMP2".equals(lastElement) || "JMP2r".equals(lastElement)){
            res.setName(addressLabel.getName());
            res.setTokenContent(content);
        }
        else{
            res = null;
        }
        return res;
    }

    // test:
    // |00 @System [ &vector $2 &pad $6 &r $2 &g $2 &b $2 ] |20 @Screen [ &vector $2 &width $2 &height $2 &pad $2 &x $2 &y $2 &addr $2 &pixel $1 &sprite $1 ]
    public static void main(String[] args) throws Exception {
        TokensMap tokensMap = new TokensMap();
        TokenObject instance = tokensMap.getSingleMapping("|0000");
        if(instance instanceof Padding){
            System.out.println(instance.getString() +"  "+ ((Padding) instance).getContent());
        }

        Scanner sc = new Scanner(System.in);
        List<String> test = new ArrayList<>(Arrays.asList(sc.nextLine().trim().split("\s+")));
        List<TokenObject> res = tokensMap.tokenObjectMapping(test);
        tokensMap.FunctionConvert(res);
        System.out.println("Here is your Addresslable object");
        for(AddressLabel a : tokensMap.lableList){
//            if(a.isAboslute(a.getIndication())){
//                System.out.println("here are "+a.getString()+"'s content:");
//                for(TokenObject token : a.getContentToken()){
//                    System.out.print(token.getString()+"   ");
//                }
//            }
//            else
//                System.out.println(a.getIndication()+""+a.getName());
        }
        System.out.println();
        System.out.println("res is");
        for(TokenObject token:res){
            // expand the content of AddressLabel
//            if(token instanceof AddressLabel){
//                System.out.println(token.getString() + "  "+ token.getType());
//                for(TokenObject sub: ((AddressLabel) token).getContentToken()){
//                    if(!sub.getType().equals("unknow"))
//                        System.out.println(sub.getString() + "  "+ sub.getType());
//                }
//            }
            if(!token.getType().equals("unknow"))
                System.out.println(token.getString() + "  "+ token.getType());
        }
        System.out.println();
        System.out.println("Here are functions");
        for(Function func : t.functionList){
            System.out.println("@"+ func.getName());
//            System.out.println("here are function content");
//            for(TokenObject token : func.getTokenContent()){
//                System.out.println(token.getString());
//            }
        }
    }
}
