/*
Padding, this is a way to allocate memory. Absolute padding is indicated with `|`, relative padding with `$`.
 */
package Tokens;

public class Padding extends TokenObject{
    char indication;
    String content;

    public Padding(char indication, String content) {
        this.indication = indication;
        this.content = content;
    }

    public Padding() {

    }

    public boolean isPadding(char indication) {
        return isAbsolute(indication) || isRelative(indication);
    }

    public boolean isAbsolute(char indication) {
        return indication == '|';
    }

    public boolean isRelative(char indication) {
        return indication == '$';
    }

    public char getIndication() {
        return indication;
    }

    public void setIndication(char indication) {
        this.indication = indication;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return getIndication() + getContent();
    }
}
