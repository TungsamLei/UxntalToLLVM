import Tokens.*;

import java.util.*;

public class TokensMap {
    public List<AddressLabel> addressLabelList;
    public List<Function> functionList;
//    public LinkedHashMap<>;


    /*
      几个list： stringList, addressLabelList, functionList, tokenObjectList，contentTokenList， subLabelList
      传参stringList
      stringList(i)调用singleMap判断token类型，把判断类型后的tokenObject存到tokenObjectList
      如果tokenObject是addressLabel，存到addressLabelList
          如果tokenObject是绝对地址，i++
          stringList(i)即currentString调用singleMap判断token类型，把判断类型后的content加到contentTokenList
          如果content是相对地址，把content存到subLabelList和addressLabelList
          如果currentString是一个function或者addressLabel的结尾，break
      更新tokenObject这个addressLabel类的contentTokenList 和 subLabelList
     */
    public List<TokenObject> tokensMap(List<String> stringList) throws Exception {
        if (stringList == null || stringList.size() == 0) {
            throw new Exception("TokensMap.Class tokensMap method Error: No stringList was input.");
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

//            if (tokenObject instanceof AddressLabel) {
//                addressLabelList.add((AddressLabel) tokenObject);
//
//                if (((AddressLabel) tokenObject).isAbsolute(((AddressLabel) tokenObject).getIndication())) {
//                    List<TokenObject> contentTokenList = new ArrayList<>();
//                    List<AddressLabel> subLabelList = new ArrayList<>();
//                    i++;
//                    while (i < stringList.size()) {
//                        String currentString = stringList.get(i);
//                        // these contents mean the end of a label or a function
//                        if (currentString.equals("BRK") || currentString.charAt(0) == '@' || currentString.charAt(0) == '|') {
//                            i--;
//                            break;
//                        }
//                        TokenObject content = singleMap(stringList.get(i));
//                        if (content.getType() == null) content.setType("" + content.getClass());
//                        contentTokenList.add(content);
//
//                        // subLabel
//                        // tokenObjectList.add(content);
//
//                        // if it is subLabel
//                        if (content instanceof AddressLabel && ((AddressLabel) content).isRelative(((AddressLabel) content).getIndication())) {
//                            subLabelList.add((AddressLabel) content);
//                            addressLabelList.add((AddressLabel) content);
//                        }
//                        if (currentString.equals("JMP2r") || currentString.equals("JMP2")) {
//                            break;
//                        }
//                        i++;
//                    }
//                    // add subLabel and contentToken to addressLabel
//                    ((AddressLabel) tokenObject).setSubLabel(subLabelList);
//                    ((AddressLabel) tokenObject).setContentToken(contentTokenList);
//                }
//            }
            // as for other labels, we directly add them
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
        Register register = new Register();
        RegisteredOperation registeredOperation = new RegisteredOperation();

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
        if (literal.isLiteral(indication + "")) {
            return new LiteralConstant("" + indication, followContent);
        } else if (literal.isLiteral(str.substring(0, 3))) {
            return new LiteralConstant(str.substring(0, 3), str.substring(3));
        }

        if (str.length() >= 3 && operation.isOperationType(str)) {
            return new Operation(str);
//            operation.operationConvert(str);
//            operation.setType("Operation");
//            return operation;
        }
        if (rawConstant.isRawContent(str)) {
            rawConstant.setContent(str);
            rawConstant.setType("RawContent");
            return rawConstant;
        }
        TokenObject tokenObject = new TokenObject();
        tokenObject.setContent(str);
        tokenObject.setType("unknown");
        return tokenObject;
    }

    // check if the token in the TokenObject List means a function
//  if it is, then add the token to functionList, and remove it from addressLabelList
//  do this because both addressLabel and function are possible to begin with '@'
    public void functionConvert(List<TokenObject> list) {
        functionList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TokenObject token = list.get(i);
            if (token instanceof AddressLabel) {
                Function func = functionDetermine((AddressLabel) token);
                if (func != null) {
                    list.remove(i);
                    addressLabelList.remove(token);
                    functionList.add(func);
                    i--;
                }
            }
        }
    }

    //    check if it is a function
    public Function functionDetermine(AddressLabel addressLabel) {
        List<TokenObject> content = addressLabel.getContentToken();
        int lastIndex = content.size() - 1;
        String lastElement = content.get(lastIndex).toString();
        Function func = new Function();
        if ("JMP".equals(lastElement) || "JMP2".equals(lastElement) || "JMP2r".equals(lastElement)) {
            func.setName(addressLabel.getContent());
            func.setTokenObject(content);
        } else {
            func = null;
        }
        return func;
    }

}
