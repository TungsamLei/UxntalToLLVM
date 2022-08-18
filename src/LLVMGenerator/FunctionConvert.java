package LLVMGenerator;

import Tokens.TokenObject;
import utils.RegisteredOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FunctionConvert {
    public String convert(List<TokenObject> list, Stack stack) {
        int localVariable = 0;
        List<RegisteredOperation> registeredOperations = new ArrayList<>();
        int registerCount = 0;
        StringBuilder sb = new StringBuilder();
        String changeLine = "\n";
        String tab = "\t";


        /**
         *  function 只有以下几种 token 类型：
         *  AddressLabel ： 仅 @方法名
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

        for (int i = 0; i < list.size(); i++) {
//            Padding
            if (list.get(i).getType().equals("AddressLabel")) {
                String name = list.get(i).toString().substring(1);
                sb.append("define i16 @" + name + "(i16 %r1, i16 %r2) {");  // method name
                sb.append(changeLine);
                sb.append(tab);
                stack.push("%r2");
                stack.push("%r1");
            }

//            LiteralConstant
            if (list.get(i).getType().equals("LiteralConstant")) {
                String content = list.get(i).getContent();
                if (content.length() == 4) {
                    stack.push(content); // 不用转换进制，用 u0x
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
                            temp = "u0x" + top;
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
                            sb.append("store i16 u0x00" + str1 + "i16* @" + content); //调 zero page的全局变量
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
                        stack.push("%r"+localVariable);
                        continue;
                    } else {
                        sb.append("call i16 @" + content + "(i16 u0x" + top + ")");  //调function,传参：stack现在最顶上的两个byte
                        sb.append(changeLine);
                        sb.append(tab);
                        stack.push("%r"+localVariable);
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
                            temp1 = "u0x" + top;
                        }
                        if (!top2.contains("%")) {
                            temp2 = "u0x" + top2;
                        }
                        //  %r1 = mul i16 u0x0007, u0x0006
                        if (temp1.contains("u0x") && temp2.contains("u0x")) {
                            sb.append("%r" + localVariable + " = " + operation.toLowerCase() + " i16 " + temp1 + ", " + temp2);
                        } else {
                            sb.append("%r" + localVariable + " = " + operation.toLowerCase() + " i16 " + temp1 + "," + temp2);
                        }
                        sb.append(changeLine);
                        sb.append(tab);
                    } else {
                        String str2 = top.substring(0, 2);
                        String str1 = top.substring(2);
                        sb.append("%r" + localVariable + " = " + operation.toLowerCase() + " i16 " + "u0x" + str1 + ", " + "u0x" + str2);
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
                        temp = "u0x" + top;
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
//                    System.out.println("230 :"+sb);
                }
                if (operation.equals("LDZ")) {
                    localVariable++;
//                    System.out.println("226: registercount" + registerCount);
                    stack.push(registeredOperations.get(registerCount - 1).getValue());
//                            %r1 = load i16, i16* @x
                    sb.append("%r" + localVariable + " = load i16, i16* " + registeredOperations.get(registerCount - 1).getName());
                    sb.append(changeLine);
                    sb.append(tab);
                }
                if (operation.equals("JMP")) {
                    String top = (String) stack.pop();
                    sb.append("ret i16 " + top + "\n" +
                            "}");
                }
                if (operation.equals("SWP")) {
                    String top = (String) stack.pop();
                    String top2 = (String) stack.pop();
                    stack.push(top);
                    stack.push(top2);
                }
            }
        }
        return sb.toString();
    }
}
