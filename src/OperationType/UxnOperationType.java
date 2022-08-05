package OperationType;

public enum UxnOperationType {
    BRK, LIT, INC, POP, NIP, SWP, ROT, DUP, OVR, EQU, NEQ, GTH, LTH, JMP, JCN, JSR, STH, LDZ, STZ,
    LDR, STR, LDA, STA, DEI, DEO, ADD, SUB, MUL, DIV, AND, ORA, EOR, SFT;

    private String operation;

    UxnOperationType(String operation) {
        this.operation = operation;
    }

    UxnOperationType() {

    }

    public String getOperation() {
        return operation;
    }


    @Override
    public String toString() {
        return this.getOperation();
    }
}
