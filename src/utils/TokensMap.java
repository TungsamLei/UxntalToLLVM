package utils;

import Tokens.*;

import java.util.*;

public class TokensMap {
    public List<AddressLabel> addressLabelList;
    public List<Function> functionList;


    public List<TokenObject> tokensMap(List<String> stringList) throws Exception {
        if (stringList == null || stringList.size() == 0) {
            throw new Exception("utils.TokensMap.Class tokensMap method Error: No stringList was input.");
        }
        addressLabelList = new ArrayList<>();
        List<TokenObject> tokenObjectList = new ArrayList<>();

        int i = 0;
        while (i < stringList.size()) {
            TokenObject tokenObject = singleMap(stringList.get(i));
            if (tokenObject.getType() == null) {
                tokenObject.setType("" + tokenObject.getClass());
            }
            tokenObjectList.add(tokenObject);
            i++;
        }
        return tokenObjectList;
    }

    /**
     * 单个token的映射
     *
     * @param str
     * @return
     * @throws Exception
     */
    public TokenObject singleMap(String str) throws Exception {
        if (str.trim().equals("")) {
            throw new Exception("Error: Nothing was input.");
        }

        AddressLabel addressLabel = new AddressLabel();
        AddressLabelReference addressLabelReference = new AddressLabelReference();
        LiteralConstant literal = new LiteralConstant();
        Operation operation = new Operation();
        Padding padding = new Padding();
        RawConstant rawConstant = new RawConstant();
//        Register register = new Register();
//        RegisteredOperation registeredOperation = new RegisteredOperation();

        char indication = str.charAt(0);
        String followContent = str.substring(1);

        if (padding.isPadding(indication)) {
            padding.setIndication(indication);
            padding.setContent(followContent);
            padding.setType("Padding");
            return padding;
        }
        if (addressLabel.isAddressLabel(indication)) {
            addressLabel.setIndication(indication);
            addressLabel.setContent(followContent);
            addressLabel.setType("AddressLabel");
            return addressLabel;
        }
        if (addressLabelReference.isAddressLabelReference(indication, followContent)) {
            addressLabelReference.setIndication(indication);
            addressLabelReference.setContent(followContent);
            addressLabelReference.setType("AddressLabelReference");
            return addressLabelReference;
        }
        if (literal.isLiteral(indication + "")) { //#
            return new LiteralConstant("" + indication, followContent);  // #0000
        } else if (literal.isLiteral(str.substring(0, 3))) { // LIT
            return new LiteralConstant(str.substring(0, 3), ""); // LIT 或者 LIT2 都改成 # ，没有content， content是下一个RawContent
        }

        if (str.length() >= 3 && operation.isOperationType(str)) {
            return new Operation(str);
        }
        if (rawConstant.isRawContent(str)) {
            rawConstant.setContent(str);
            rawConstant.setType("RawConstant");
            return rawConstant;
        }
        TokenObject tokenObject = new TokenObject();
        tokenObject.setContent(str);
        tokenObject.setType("unknown");
        return tokenObject;
    }

}
