/*
Address label references:
they start with `;` or `.` or `,&`, and are arbitrary strings, for example `;mult-acc` or `.x` or `,&loop`.
`;` is the corresponding reference for `@`
`,&` is the corresponding reference for `&`.
A special case is the zero-page memory which has absolute addresses that you can reference with the `.` and are a single byte.
 */
package Tokens;

public class AddressLabelReference extends TokenObject{
    char indication;
    String content;

    public AddressLabelReference(char indication, String content) {
        this.indication = indication;
        this.content = content;
    }

    public boolean isAddressLabelReference(char indication, String content) {
        return indication == ';' || indication == '.' || (content.charAt(0) == ',' && content.charAt(1) == '&');
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
    public String toString() {
        return getIndication() + getContent();
    }
}
