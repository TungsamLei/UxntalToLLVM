/*
Raw constants: these are hexadecimal numbers without any special character, for example `a12b` or `42` or `2a`
 */
package Tokens;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawConstant extends TokenObject{
    String content;

    public RawConstant(String content) {
        this.content = contentConvert(content);
    }

    public RawConstant() {
    }


    /**
     * Any special character?
     *
     * @param content
     * @return True: include special character. False: not include.
     */
    public boolean isRawContent(String content) {
        String regEx = "[ _`[email protected]#$%^&*()+=|{}‘:;‘,\\[\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return !m.find();
    }

    public String contentConvert(String Content) {
        if (!Content.equals("") && isRawContent(content)) {
            return Integer.valueOf(Content, 16) + "";
        } else
            return null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return getContent();
    }

//    @Override
//    public void setType(String type) {
//        if (isRawContent(content)) {
//            this.type = "RawContent";
//        }
//    }
}
