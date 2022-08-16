/*
Operations: three capital letters followed optionally by a `2`, `k` and/or `r`, for example
 */
package Tokens;

import OperationType.UxnOperationType;

public class Operation extends TokenObject {
    String content; // e.g. 'ADD2' or 'ADD'
    String operation; // the three capital letters
    String pattern; // the following "2,k,r"

    public Operation() {
    }

    public Operation(String content) {
        operationConvert(content);
    }

    public Operation(String operation, String pattern) {
        this.operation = operation;
        this.pattern = pattern;
    }

    public void operationConvert(String content) {
        String temp = "";
        String substr = "";
        this.content = content;

        for (char c : content.toCharArray()) {
            if (Character.isUpperCase(c)) {
                temp = temp + c;
            } else {
                substr = substr + c;
            }
        }
        if (temp.length() == 3) {
            this.operation = temp;
            if (substr.length() != 0) {
                this.pattern = substr;
            }
            this.setType("Operation");
        }
    }

    public boolean isOperationType(String content) {
        String temp = ""; //大写字母
        String substr = ""; //大写字母以外的部分

        for (char c : content.toCharArray()) {
            if (Character.isUpperCase(c)) {
                temp = temp + c;
            } else {
                substr = substr + c;
            }
        }

//        test
//        System.out.println("Operation.class isOperationType() temp " + temp);
//        System.out.println("Operation.class isOperationType() substr " + substr);

        if (temp.length() == 3) {
//            if (isOperation(temp)) {
            this.operation = temp;
//            }
//            if (substr.length() != 0 && isPattern(substr)) {
            if (substr.length() != 0) {
                this.pattern = substr;
            }
            return true;
        }
        System.out.println("Operation.class isOperationType Error: Not an valid operation.");
        return false;
    }

    public boolean isOperation(String operation) {
        for (UxnOperationType e : UxnOperationType.values()) {
            if (e.getOperation() == operation) {
                return true;
            }
        }
        return false;
    }

    public boolean isPattern(String pattern) {
        for (char c : pattern.toCharArray()) {
            if (Character.isLowerCase(c)) {
                if (c == '2' || c == 'k' || c != 'r' || c != '\0') {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String content) {
        operationConvert(content);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String content) {
        operationConvert(content);
    }


    @Override
    public String toString() {
        return getContent();
    }

//    @Override
//    public void setType(String type) {
//        if (isOperationType(content)) {
//            this.type = "Operation";
//        }
//    }
}

