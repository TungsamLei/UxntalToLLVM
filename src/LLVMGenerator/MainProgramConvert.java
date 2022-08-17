package LLVMGenerator;

import Tokens.Operation;
import Tokens.TokenObject;
import utils.RegisteredOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainProgramConvert {
    public String convert(List<TokenObject> list, Stack stack) {
        int localVariable = 0;
        List<RegisteredOperation> registeredOperations = new ArrayList<>();
        int registerCount = 0;
        StringBuilder sb = new StringBuilder();
        String changeLine = "\r\n";
        String tab = "\t";
        sb.append("define i16 @main() {");  // |0100  本程序默认i16
        if (list.get(1).toString() == "@main") {
            list.remove(2);
        } //remove @main
        sb.append(changeLine);
        sb.append(tab);


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

        for (int i = 1; i <= list.size() - 4; i++) {

//            LiteralConstant
            if (list.get(i).getType().equals("LiteralConstant")) {
                String content = list.get(i).getContent();
                if (content.length() == 4) {
                    stack.push(content); // 不用转换进制，用 u0×
                } else if (content.length() == 2) {
                    stack.push("00" + content);
                } else {
                    i++;
                    String rawContent = list.get(i).getContent();
                    if (rawContent.length() == 4) {
                        stack.push(rawContent);
                    } else if (rawContent.length() == 2) {
                        stack.push("00" + rawContent);
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
                    i++;
                    String operation = list.get(i).toString().substring(0, 3);
                    String pattern = list.get(i).toString().substring(3);
                    if (operation.equals("STZ")) {
                        String top = (String) stack.pop();
                        String temp;
                        if (top.contains("%")) {
                            temp = top;
                        } else {
                            temp = "u0×" + top;
                        }
                        if (pattern.contains("2")) {
                            //        store i16 u0x0006, i16* @x
                            sb.append("store i16 " + temp + ", i16* @" + content); //调 zero page的全局变量
                            sb.append(changeLine);
                            sb.append(tab);
//                            System.out.println("82 line: " + sb);
                        } else {
                            String str2 = top.substring(0, 2);
                            String str1 = top.substring(2);
                            sb.append("store i16 u0×00" + str1 + "i16* @" + content); //调 zero page的全局变量
                            sb.append(changeLine);
                            sb.append(tab);
                            stack.push("00" + str2);
//                            System.out.println("90 : " + sb);
                        }
                        if (pattern.contains("k")) {
                            stack.push(top);
                        }
                        registerCount++;
                        RegisteredOperation registeredOperation = new RegisteredOperation();
                        registeredOperation.setCount(registerCount);
                        registeredOperation.setName("@" + content);
                        registeredOperation.setValue(top);
                        registeredOperations.add(registeredOperation);
//                        registeredOperation.setOperation();
                    } else {
                        localVariable++;
//                        if (pattern.contains("2")) {
                        //        %r1 = load i16, i16* @x
                        sb.append("%r" + localVariable + " = load i16, i16* @" + content); //调 zero page的全局变量
                        sb.append(changeLine);
                        sb.append(tab);
//                        System.out.println("102 : " + sb);


//                        } else {
//                            String str2 = top.substring(0, 2);
//                            String str1 = top.substring(2);
//                            sb.append("%r" + localVariable + " = load i16, i16* @" + content); //调 zero page的全局变量
//                            sb.append(changeLine);
//                            stack.push("00" + str2);
//                        }
//                        if (pattern.contains("k")) {
//                            stack.push(top);
//                        }
                        stack.push("%r" + localVariable);
                    }
                    continue;
                } else if (indication == ';') {
                    String top = (String) stack.pop();
//                    String top2 = (String) stack.pop();
//                      %r1 = call i16 @loop(i16 u0x0010)
                    if (top.contains("%")) {
                        sb.append("call i16 @" + content + "(i16 " + top + ")");  //调function,传参：stack现在最顶上的两个byte
                        sb.append(changeLine);
                        sb.append(tab);
                        continue;
                    } else {
                        sb.append("call i16 @" + content + "(i16 u0×" + top + ")");  //调function,传参：stack现在最顶上的两个byte
                        sb.append(changeLine);
                        sb.append(tab);
                        continue;
                    }
                } else { // ',&'
                    String name = list.get(i).toString().substring(2);
                    sb.append("%" + name + " = "); //调 label 里面的临时变量
//                    sb.append(changeLine);
                    continue;
                }
            }


//            Operation
            if (list.get(i).getType().equals("Operation")) {
                String content = list.get(i).toString();
                String operation = content.substring(0, 3);
                String pattern = content.substring(3);
                if (operation.equals("ADD") || operation.equals("SUB") || operation.equals("MUL") || operation.equals("DIV")) {
                    String top = (String) stack.pop();
                    String top2 = (String) stack.pop();
                    localVariable++;
                    if (pattern.contains("2")) {
                        String temp1 = top;
                        String temp2 = top2;
                        if (!top.contains("%")) {
                            temp1 = "u0×" + top;
                        }
                        if (!top2.contains("%")) {
                            temp2 = "u0×" + top2;
                        }
                        //  %r1 = mul i16 u0x0007, u0x0006
                        sb.append("%r" + localVariable + " = " + operation.toLowerCase() + " i16 " + temp1 + "," + temp2);
                        sb.append(changeLine);
                        sb.append(tab);
                    } else {
                        String str2 = top.substring(0, 2);
                        String str1 = top.substring(2);
                        sb.append("%r" + localVariable + " = " + operation.toLowerCase() + " i16 " + "u0×" + str1 + "," + "u0×" + str2);
                        sb.append(changeLine);
                        sb.append(tab);
                    }
                    if (pattern.contains("k")) {
                        stack.push(top2);
                        stack.push(top);
                    }
                    if (pattern.contains("r")) {
//                        sb.append("Not yet support return mode");
//                        sb.append(changeLine);
                    }
                    stack.push("%r" + localVariable);
                    continue;
                }
                if (operation.equals("INC")) {
                    String top = (String) stack.pop();
//                    System.out.println("194 : " + top);
                    String temp;
                    if (top.contains("%")) {
                        temp = top;
                    } else {
                        temp = "u0×" + top;
                    }
//                    %r1 = add i16 1,u0x0006
                    localVariable++;
                    sb.append("%r" + localVariable + " = add i16 1," + temp);
//                    System.out.println("204" + sb);
                    sb.append(changeLine);
                    sb.append(tab);
                    if (pattern.contains("k")) {
                        stack.push(top);
                    }
                    stack.push("%r" + localVariable);
                    continue;
                }
                if (operation.equals("JSR")) {
                    continue;
                }
                if (operation.equals("POP")) {
                    stack.pop();
                }
                if (operation.equals("DUP")) {
                    String top = (String) stack.pop();
                    stack.push(top);
                    stack.push(top);
                }
                if (operation.equals("LDZ")) {
                    localVariable++;
//                    System.out.println("226: registercount" + registerCount);
                    stack.push(registeredOperations.get(registerCount - 1).getValue());
//                            %r1 = load i16, i16* @x
                    sb.append("%r" + localVariable + " = load i16, i16* " + registeredOperations.get(registerCount-1).getName());
                    sb.append(changeLine);
                    sb.append(tab);
                }
            }
        }

        sb.append("call i16 @putc(i16 %r" + localVariable + ") \n" +
                "    ret i16 0\n" +
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
