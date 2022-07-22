/*
Operations: three capital letters followed optionally by a `2`, `k` and/or `r`, for example
 */
package Tokens;

public class Operation {
    String content;
    char operation;

    public Operation(String content, char operation) {
        this.content = content;
        this.operation = operation;
    }

    public boolean isOperation(String content, char operation) {
        for (char c : content.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        if (operation != '2' || operation != 'k' || operation != 'r') {
            return false;
        }
        return true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public char getOperation() {
        return operation;
    }

    public void setOperation(char operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return getContent() + getOperation();
    }
}
