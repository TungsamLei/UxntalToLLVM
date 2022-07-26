/*
Literal constants:
they start with `#` or are preceded by `LIT` or `LIT2`
(so `#07` is just a shorter syntax for LIT 07 and `#1a4b` is short for LIT2 1a4b )
 */
package Tokens;

public class LiteralConstant extends TokenObject {
    String indication; // # LIT LIT2
    String content; // the number behind #

    public LiteralConstant() {
    }

    public LiteralConstant(String indication, String content) throws Exception {
        this.indication = indicationConvert(indication);
        this.content = content;
        this.setType("LiteralConstant");
    }

    public String indicationConvert(String indication) throws Exception {
        if (isLiteral(indication)) {
            return "#";
        } else throw new Exception("Error: Not a literal indication.");
    }

    //    不转十进制，方便 ’2‘ 操作
//    public String contentConvert(String content) {
//        if (!content.equals("")) {
//            return Integer.parseInt(content, 16) + "";  //Convert to decimal
//        } else
//            return null;
//    }

    public boolean isLiteral(String indication) {
        return indication.equals("#") || indication.equals("LIT") || indication.equals("LIT2");
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        content = content;
    }

    @Override
    public String toString() {
        return getIndication() + getContent();
    }

//    @Override
//    public void setType(String type) {
//        if (isLiteral(indication)) {
//            this.type = "AddressLabelReference";
//        }
//    }
}
