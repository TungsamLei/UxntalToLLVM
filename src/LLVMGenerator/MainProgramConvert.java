package LLVMGenerator;

import Tokens.TokenObject;

import java.util.List;
import java.util.Stack;

public class MainProgramConvert {
    public String convert(List<TokenObject> list, Stack stack) {
        StringBuilder sb = new StringBuilder();
        String changeLine = "\r\n";
        sb.append("define i8 @main() {");  // |0100
        if (list.get(2).toString() == "@main") {
            list.remove(2);
        } //remove @main
        sb.append(changeLine);

        /**
         *  main program只有以下几种 token 类型：
         *  Padding ： 仅 |0100 ，for循环中忽略
         *  AddressLabel ： 仅 @main  ，for循环中忽略
         *  AddressLabelReference : 以 ';'  '.'  ',&' 开头的， '.x'变成'@x'，调用zero page的全局变量。 ';f1'变成'@f1'，调用方法
         *  LiteralConstant : 以 '#' 开头的。 有两种可能： #+十六进制数字，这种情况把十六进制数字push到Stack里,每两位数存一次，先进后出。 单纯#，这种情况把 list.get(i+1) 的RawContent push到Stack，同理。
         *  Operation : 三个大写字母开始，可能带有’2‘’k'‘r'模式.
         *  RawContent : 除以上以外的，即没有任何特殊符号或operation，只由数字和小写字母组成
         *
         *  最后的 #18 DEO BRK 已独立写到最后，for循环中忽略
         *
         * Operation详细需求：
         *
         */

        for (int i = 2; i < list.size() - 4; i++) {

//            LiteralConstant
            if (list.get(i).getType().equals("LiteralConstant")) {
                String content = list.get(i).getContent();
                if (content.length() == 4) {
                    stack.push(content.substring(0, 1)); // 不用转换进制，用 u0×
                    stack.push(content.substring(2, 3));
                } else if (content.length() == 2) {
                    stack.push(content);
                } else {
                    i++;
                    String rawContent = list.get(i).getContent();
                    if (rawContent.length() == 4) {
                        stack.push(rawContent.substring(0, 1));
                        stack.push(rawContent.substring(2, 3));
                    } else if (rawContent.length() == 2) {
                        stack.push(rawContent);
                    }
                    continue;
                }
                continue;
            }

//            AddressLabelReference
            if (list.get(i).getType().equals("AddressLabelReference")) {
                char indication = list.get(i).toString().charAt(0);
                String content = list.get(i).toString().substring(1);
                if (indication == '.') {
                    sb.append("i16* @" + content); //调 zero page的全局变量
                    sb.append(changeLine);
                    continue;
                } else if (indication == ';') { //后面一定是跟 JSR2，可以把JSR2 remove了
                    int top = (int) stack.pop();
                    int top2 = (int) stack.pop();
                    sb.append("call i16 @" + content + "(i16 u0×" + top2 + top + ")");  //调function,传参：stack现在最顶上的两个值
                    sb.append(changeLine);
                    i++;
                    list.remove(i);
                    continue;
                } else { // ',&'
                    String name = list.get(i).toString().substring(2);
                    sb.append("%" + name + " = "); //调 label 里面的临时变量
                    sb.append(changeLine);
                    continue;
                }
            }


//            Operation
            if (list.get(i).getType().equals("Operation")) {

            }
        }

        sb.append(changeLine);
        sb.append("call i16 @putc(i16 %r4) \n" +
                "    ret i8 0\n" +
                "}");
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
        sb.append(changeLine);
        return sb.toString();
    }
}
