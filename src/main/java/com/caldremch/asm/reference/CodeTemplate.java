package com.caldremch.asm.reference;

public class CodeTemplate extends AbsCodeTemplate {

    protected void onResume() {
        super.onResume();
        boolean isOk = isNeedLogin();
        if (isOk) {
            loginResume();
        }
    }

    public void loginResume() {

    }

    protected boolean isNeedLogin() {
        return false;
    }

}


/**
 * 分析一下生成字节码指令
 * <p>
 * protected onResume()V
 * L0
 * LINENUMBER 5 L0
 * ALOAD 0
 * INVOKESPECIAL com/caldremch/asm/AbsCodeTemplate.onResume ()V
 * L1
 * LINENUMBER 6 L1
 * ALOAD 0
 * INVOKEVIRTUAL com/caldremch/asm/CodeTemplate.isNeedLogin ()Z
 * ISTORE 1
 * L2
 * LINENUMBER 7 L2
 * ILOAD 1
 * IFEQ L3
 * L4
 * LINENUMBER 8 L4
 * ALOAD 0
 * INVOKEVIRTUAL com/caldremch/asm/CodeTemplate.loginResume ()V
 * L3
 * LINENUMBER 10 L3
 * FRAME APPEND [I]
 * RETURN
 * <p>
 * 从L0-L4, 一共是5行代码, 分辨代表5行代码, 分辨是从 本间第5行代码开始, 到第10行代码结束, 可以从上面的代码中看得出
 * <p>
 * 变量文档可以查看 https://www.yuque.com/mikaelzero/asm/yt94kf
 * ALOAD: ALOAD 用于加载任意非基元值，即对象和数组引用, 读取一个局部变量，并将它的值压到操作数栈中, 它们的参数是必须读取的局部变量的索引 i,
 * ILOAD: 指令读取一个局部变量，并将它的值压到操作数栈中。它们的参数是必须读取的局部变量的索引 i
 * ISTORE: 从操作数栈中弹出一个值，并将它存储在由其索引 i 指定的局部变量中。
 **/