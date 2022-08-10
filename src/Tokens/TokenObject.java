package Tokens;

public class TokenObject {
    String content;
    String type;
    byte byteCount;

    public String getContent() {
        if (content == null || "".equals(content)) {
            return "null";
        } else return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte getByteCount() {
        return byteCount;
    }

    public void setByteCount(byte byteCount) {
        this.byteCount = byteCount;
    }

    public String toString() {
        return getContent();
    }
}
