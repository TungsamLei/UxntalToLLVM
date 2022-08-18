package LLVMGenerator;

import Tokens.AddressLabel;
import Tokens.Padding;
import Tokens.TokenObject;

import java.util.List;

public class ZeroPageConvert {

    public String convert(List<TokenObject> list) {
        StringBuilder sb = new StringBuilder();
        String changeLine = "\n";
        for (TokenObject object : list) {
            String name = "";
            if (object.getType().equals("AddressLabel")) {
                name = "@" + object.getContent();
                sb.append(name);
            } else if (object.toString().contains("$")) {
//                int temp = Integer.valueOf(object.getContent());
//                int i = 8 * temp;
                sb.append(" = global i16 u0x0000");
                sb.append(changeLine);
            }
        }
        return sb.toString();
    }
}
