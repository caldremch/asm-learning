package com.caldremch.asm;

import com.caldremch.asm.annotation.BaseAnno;
import com.caldremch.asm.annotation.NeedLogin;
import com.caldremch.asm.annotation.NotLoginResume;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.objectweb.asm.Opcodes.*;

public class App {

    public static void main(String[] args) {

        /**
         * 获取当前目录
         */
        Scanner in = new Scanner(System.in);

        String projectPath = System.getProperty("user.dir");
        String buildPath = "\\build\\classes\\java\\main\\";
        log("projectPath -> " + projectPath);
        log("buildPath -> " + buildPath);
        String classDir = projectPath.replaceAll("\\\\", "/") + buildPath.replaceAll("\\\\", "/");
        log("classDir -> " + classDir);

        /**
         * 1.获取文件inputstream
         * 2.asm处理, 返回文件字节数组 byte[]
         * 3.重新写入文件
         */


        List<String> clzName = new ArrayList<>();
        clzName.add(BaseActivity.class.getName());
        clzName.add(Activity1.class.getName());


        for (String clazzName : clzName
        ) {
            log("handle-->" + clazzName);
            try {
                //create ClassReader to read class
                ClassReader cr = new ClassReader(clazzName);
                //base on ClassReader and create a ClassWriter
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
                //create visitor, visit all the class contain annotation, and add new method
                ClassVisitor cv = new MyAnnotationVisitor(cw);
                //start visit
                cr.accept(cv, ClassReader.EXPAND_FRAMES);
                //get the newCode byte array
                byte[] newCode = cw.toByteArray();
                FileOutputStream fileOutputStream = new FileOutputStream(classDir + clazzName.replaceAll("\\.", "/") + ".class");
                fileOutputStream.write(newCode);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static class MyAnnotationVisitor extends ClassVisitor {

        private AnnotationVisitor baseaAnnoVisitor;
        private AnnotationVisitor needLoginVisitor;
        private List<MethodVisitor> baseAnnoMethodList;

        public List<MethodVisitor> getBaseAnnoMethodList() {
            if (baseAnnoMethodList == null) baseAnnoMethodList = new ArrayList<>();
            return baseAnnoMethodList;
        }

        public MyAnnotationVisitor(ClassVisitor classVisitor) {
            super(ASM7, classVisitor);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            System.out.println("find the annotation descriptor = " + descriptor);
            if (descriptor.equals(getObjectSignature(BaseAnno.class))) {
                baseaAnnoVisitor = super.visitAnnotation(descriptor, visible);
                return baseaAnnoVisitor;
            } else if (descriptor.equals(getObjectSignature(NeedLogin.class))) {
                needLoginVisitor = super.visitAnnotation(descriptor, visible);
                return needLoginVisitor;
            } else {
                return super.visitAnnotation(descriptor, visible);
            }
        }

        @Override
        public void visitEnd() {
            if (baseaAnnoVisitor != null) {
                System.out.println("the annotation is not null, you can add a new method here");
                System.out.println("add method starting....");
                try {
                    // TODO: 2019/10/26 isNeedLogin 应全局统一起来
                    MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PROTECTED,
                            "isNeedLogin",
                            "()Z",
                            null,
                            null
                    );
                    //ICONST_0 is false, ICONST_1 is true
                    mv.visitInsn(Opcodes.ICONST_0);
                    mv.visitInsn(Opcodes.IRETURN);
                    mv.visitMaxs(1, 1);// Specify max stack and local vars
                    mv.visitEnd();
                    System.out.println("add method end....");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                baseaAnnoVisitor = null;
            } else if (needLoginVisitor != null) {
                log("need to login...");
                try {
                    MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PROTECTED,
                            "isNeedLogin",
                            "()Z",
                            null,
                            null
                    );
                    mv.visitInsn(Opcodes.ICONST_1);
                    mv.visitInsn(Opcodes.IRETURN);
                    mv.visitMaxs(1, 1);// Specify max stack and local vars
                    mv.visitEnd();
                    System.out.println("add method end....");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                needLoginVisitor = null;
            }
        }

        //this method will visit all the class method,
        //include annotation, but how to visit the annotation of method? well, we can
        //create a AdviceAdapter(or MethodVisitor, not test) to visit the method's annotation


        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            System.out.println("annotation in method====> \n descriptor = " + descriptor + ", name = " + name + ", signature = " + signature);
            if (name.equals("onResume")) {
                log("find the onResume method");
                if (getBaseAnnoMethodList().isEmpty()) {
                    log("BaseAnnoMethodList is empty");
                    getBaseAnnoMethodList().add(cv.visitMethod(access, name, descriptor, signature, exceptions));
                    return cv.visitMethod(access, name, descriptor, signature, exceptions);
                } else {
                    log("already get the loginResume");
                    MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);

                    mv = new AdviceAdapter(ASM7, mv, access, name, descriptor) {

                        @Override
                        public void visitEnd() {

                        }

                        @Override
                        protected void onMethodExit(int opcode) {
                            try {
                                /*******************if(boolean)********************/
                                mv.visitVarInsn(Opcodes.ALOAD, 0);
                                // TODO: 2019/10/26 owner 动态获取, 文件移动的时候, 这里不能实时同步
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/caldremch/asm/BaseActivity", "isNeedLogin", "()Z", false);
                                mv.visitVarInsn(ISTORE, 1);//从操作数栈中弹出一个值，并将它存储在由其索引 1 指定的局部变量中。
                                mv.visitVarInsn(ILOAD, 1);//读取索引为1的局部变量, 就是上面ISTORE存储的在1的值
                                Label l3 = new Label();
                                mv.visitJumpInsn(IFEQ, l3);
                                /*******************if(boolean)********************/
                                Label l4 = new Label();
                                mv.visitLabel(l4);
                                mv.visitVarInsn(Opcodes.ALOAD, 0);
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/caldremch/asm/BaseActivity", "loginResume", "()V", false);
                                mv.visitLabel(l3);
                                mv.visitMaxs(1, 1);
                                mv.visitEnd();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void visitCode() {

                        }
                    };

                    return mv;
                }

            } else {
                log("here is not ");
                MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
                mv = new AdviceAdapter(ASM7, mv, access, name, descriptor) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
//                    System.out.println("annotation in method====> \n descriptor = " + descriptor + ", name = " + name + ", signature = " + signature);
                        //record this method info , and invoke in where you need
                        log("visit the method annotation --> " + descriptor);
                        if (descriptor.equals(getObjectSignature(NotLoginResume.class))) {
                            getBaseAnnoMethodList().add(mv);
                        }
                        return super.visitAnnotation(descriptor, visible);
                    }
                };

                if (baseaAnnoVisitor != null) {
                }
                return mv;
            }
        }

    }

    public static String getObjectSignature(Class clz) {
        String clzName = clz.getName();
        return "L" + clzName.replaceAll("\\.", "/") + ";";
    }

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void log(Object msg) {
        System.out.println(msg);
    }
}
