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
 * ����һ�������ֽ���ָ��
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
 * ��L0-L4, һ����5�д���, �ֱ����5�д���, �ֱ��Ǵ� �����5�д��뿪ʼ, ����10�д������, ���Դ�����Ĵ����п��ó�
 * <p>
 * �����ĵ����Բ鿴 https://www.yuque.com/mikaelzero/asm/yt94kf
 * ALOAD: ALOAD ���ڼ�������ǻ�Ԫֵ�����������������, ��ȡһ���ֲ���������������ֵѹ��������ջ��, ���ǵĲ����Ǳ����ȡ�ľֲ����������� i,
 * ILOAD: ָ���ȡһ���ֲ���������������ֵѹ��������ջ�С����ǵĲ����Ǳ����ȡ�ľֲ����������� i
 * ISTORE: �Ӳ�����ջ�е���һ��ֵ���������洢���������� i ָ���ľֲ������С�
 **/