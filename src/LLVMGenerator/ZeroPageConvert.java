package LLVMGenerator;

import Tokens.AddressLabel;

import java.util.List;

public class ZeroPageConvert {

    public String convert(List<AddressLabel> list) {
        StringBuilder sb = new StringBuilder();
        String changeLine = " \r\n ";
        for (AddressLabel object : list) {
            String name = "@" + object.getContent();
            String command = name + "= global i16 u0x0000";
            sb.append(command);
            sb.append(changeLine);
        }
        return sb.toString();
    }
}
