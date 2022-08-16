/*
-创建一个全局寄存器计数器，只是一个整数，设置为1作为初始值;
对于每个block(一个块要么是主块|0100…BRK，要么是一个函数，@... ... JMP2r):
—创建空Stack
-创建一个新的，空的 List<Token> ;这些将是你的LLVM IR程序中那个block的tokens
-遍历块中的token list
—对于每个Token，检查其类型。
-如果它是一个LiteralConstant，一个AddressLabelReference或一个Register，你把它放在stack上
—如果是一个operation，从堆栈中获取的项数量与该操作的参数数量相同。例如ADD2需要2个参数，INC2需要1个参数。这意味着您将需要一个表来查找每个操作的参数数量。
—如果操作是栈操作(POP、NIP、DUP、OVR、ROT、SWP)，则在栈上执行该操作。
例如，如果操作是NIP，这意味着您从堆栈中获取2项，然后将第一项放回堆栈中。
—如果操作不是堆栈操作，则
-使用全局寄存器计数器为其结果创建一个Register令牌。
-把那个Register标记放到堆栈上
-增加全局寄存器计数器。
-为这个操作创建一个RegisterToken，并将其添加到LLVM IR程序的令牌列表中
使用这种算法，Uxntal程序中的每个令牌要么被删除，要么被一个新的RegisteredOperation令牌取代。下一步是使用LLVM IR语法打印这个列表
 */
package Tokens;

import java.util.List;

public class RegisteredOperation {
    Operation operation;
    List<TokenObject> arguments;
    Register retval;
}
