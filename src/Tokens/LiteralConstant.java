/*
Literal constants:
they start with `#` or are preceded by `LIT` or `LIT2`
(so `#07` is just a shorter syntax for LIT 07 and `#1a4b` is short for LIT2 1a4b )
 */
package Tokens;

public class LiteralConstant {
    String indication;
    String Content;

    public LiteralConstant() {
    }

    public LiteralConstant(String indication, String content) throws Exception {
        this.indication = indicationConvert(indication);
        this.Content = contentConvert(content);
    }

    public String indicationConvert(String indication) throws Exception {
        if (isLiteral(indication)) {
            return "#";
        } else throw new Exception("Error: Not a literal indication.");
    }

    public String contentConvert(String Content) {
        if (!Content.equals("")) {
            return Integer.valueOf(Content, 16) + "";
        } else
            return null;
    }

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
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return getIndication() + getContent();
    }
}
