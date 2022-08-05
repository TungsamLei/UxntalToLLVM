/*
Operations: three capital letters followed optionally by a `2`, `k` and/or `r`, for example
 */
package Tokens;

import OperationType.UxnOperationType;

public class Operation extends TokenObject {
    String content; // the three capital letters
    String pattern; // the following "2,k,r"

    public Operation() {
    }

    public Operation(String content, String pattern) {
        this.content = content;
        this.pattern = pattern;
    }

    public boolean isOperation(String content) {
        boolean include = false;
        for (UxnOperationType e : UxnOperationType.values()) {
            if (e.getOperation() == content) {
                include = true;
                break;
            }
        }
        return include;
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


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (isOperation(content)) {
            this.content = content;
        } else System.out.println("Error: Not a valid operation type.");
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        if (isOperation(pattern)) {
            this.pattern = pattern;
        } else System.out.println("Error: Not a valid operation pattern.");
    }

    @Override
    public String toString() {
        return getContent() + getPattern();
    }
}

