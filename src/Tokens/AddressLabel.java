/*
Address labels: they start with `@` or `&` and are arbitrary strings, for example `@mult-acc` or `&loop`.
 `@` is an absolute address, which takes 2 bytes.
 `&` is a relative reference, so it represents the difference from the current address, and is only 1 byte.
 */

package Tokens;

import java.util.ArrayList;
import java.util.List;

public class AddressLabel extends TokenObject{
    char indication; //the `@` or `&`
    String content; // the `mult-acc` or `loop` of the `@mult-acc` or `&loop`
    byte byteCount; //`@` takes 2 bytes. `&`is only 1 byte.
    List<AddressLabel> subLabel; // '&'
    List<TokenObject> contentToken; //separate the following content into tokens


    public AddressLabel() {
    }

    public AddressLabel(char indication, String content) {
        this.indication = indication;
        this.content = content;
        if (isAbsolute(this.indication)) {
            byteCount = 2;
        } else {
            byteCount = 1;
        }
    }


    public boolean isAddressLabel(char indication) {
        return isAbsolute(indication) || isRelative(indication);
    }

    public boolean isAbsolute(char indication) {
        return indication == '@';
    }

    public boolean isRelative(char indication) {
        return indication == '&';
    }

    public char getIndication() {
        return indication;
    }

    public void setIndication(char indication) {
        this.indication = indication;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public byte getByteCount() {
        return byteCount;
    }

    public void setByteCount(byte byteCount) {
        this.byteCount = byteCount;
    }

    public List<AddressLabel> getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(List<AddressLabel> subLabel) {
        this.subLabel = subLabel;
    }

    public List<TokenObject> getContentToken() {
        return contentToken;
    }

    public void setContentToken(List<TokenObject> contentToken) {
        this.contentToken = contentToken;
    }

    @Override
    public String toString() {
        return getIndication() + getContent();
    }


//    @Override
//    public void setType(String type) {
//        if (isAddressLabel(indication)){
//            this.type = "AddressLabel";
//        }
//    }
}
