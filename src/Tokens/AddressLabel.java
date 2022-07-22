/*
Address labels: they start with `@` or `&` and are arbitrary strings, for example `@mult-acc` or `&loop`.
 `@` is an absolute address, which takes 2 bytes.
 `&` is a relative reference, so it represents the difference from the current address, and is only 1 byte.
 */

package Tokens;

public class AddressLabel {
    char indication;
    String content;
    byte byteCount;

    public AddressLabel() {
    }

    public AddressLabel(char indication, String content) {
        this.indication = indication;
        this.content = content;
        if (isAboslute(this.indication)) {
            byteCount = 2;
        } else {
            byteCount = 1;
        }
    }


    public boolean isAddressLabel(char indication) {
        return isAboslute(indication) || isRelative(indication);
    }

    public boolean isAboslute(char indication) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte getByteCount() {
        return byteCount;
    }

    public void setByteCount(byte byteCount) {
        this.byteCount = byteCount;
    }

    @Override
    public String toString() {
        return getIndication() + getContent();
    }
}
