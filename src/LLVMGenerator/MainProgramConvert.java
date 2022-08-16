package LLVMGenerator;

import Tokens.TokenObject;

import java.util.List;
import java.util.Stack;

public class MainProgramConvert {
    public String convert(List<TokenObject> list, Stack stack) {
        StringBuilder sb = new StringBuilder();
        String changeLine = " \r\n ";
        sb.append("define i8 @main() {");
        sb.append(changeLine);

        for (int i = 2; i < list.size() - 4; i++) {
//            if (){
//
//            }
        }
        sb.append(changeLine);
        sb.append("declare dso_local i16 @printf(i8*, ...)\n" +
                "\n" +
                "define i16 @putc(i16 %r1) {\n" +
                "    call i16 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i16 %r1)\n" +
                "  \n" +
                "    ret i16 0\n" +
                "}\n" +
                "\n" +
                "@.str = private unnamed_addr constant [3 x i8] c\"%c\\00\", align 1");
//        sb.append("declare dso_local i16 @printf(i8*, ...)");
//        sb.append(changeLine);
//        sb.append("define i16 @putc(i16 %r1) { \r\n " +
//                "call i16 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i16 %r1) \r\n " +
//                "ret i16 0 \r\n } \r\n @.str = private unnamed_addr constant [3 x i8] c\"%c\\00\", align 1" );
        sb.append(changeLine);
        return sb.toString();
    }
}
