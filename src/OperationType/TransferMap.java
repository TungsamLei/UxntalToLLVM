package OperationType;

import java.util.HashMap;
import java.util.Map;

public class TransferMap {
    public static Map<String, String> map = new HashMap<>();

    static {
        map.put(UxnOperationType.ADD.name(), LLVMOperationType.add.toString());
        map.put(UxnOperationType.SUB.name(), LLVMOperationType.sub.toString());
        map.put(UxnOperationType.MUL.name(), LLVMOperationType.mul.toString());
        map.put(UxnOperationType.DIV.name(), LLVMOperationType.div.toString());
        map.put(UxnOperationType.AND.name(), LLVMOperationType.and.toString());
        map.put(UxnOperationType.ORA.name(), LLVMOperationType.or.toString());
        map.put(UxnOperationType.EQU.name(), "icmp eq");
        map.put(UxnOperationType.NEQ.name(), "icmp ne");
        map.put(UxnOperationType.JSR.name(), LLVMOperationType.call.toString());
        map.put(UxnOperationType.GTH.name(), "icmp ugt");
        map.put(UxnOperationType.LTH.name(), "icmp ult");
//        map.put(UxnOperationType.POP.name(), LLVMOperationType.Drop.toString());
//        map.put(UxnOperationType.JMP.name(), LLVMOperationType.Return.toString());
//        map.put(UxnOperationType.SFT.name(), LLVMOperationType.LeftShift.toString());
//        map.put(UxnOperationType.DEO.name(), LLVMOperationType.Drop.toString() + " call $log ");
        map.put(UxnOperationType.LDZ.name(), LLVMOperationType.load.toString());
        map.put(UxnOperationType.STZ.name(), LLVMOperationType.store.toString());
//        map.put(UxnOperationType.DUP.name(), "local.tee $c local.get $c");
//        map.put(UxnOperationType.SWP.name(), "local.set $c local.set $b local.get $c local.get $b ");
//        map.put(UxnOperationType.NIP.name(), "i32.const 1 select");
//        map.put(UxnOperationType.ROT.name(), "local.set $c local.set $b local.set $a local.get $b local.get $c local.get $a");
//        map.put(UxnOperationType.OVR.name(), "local.set $c local.tee $b local.get $c local.get $b");
    }
}
