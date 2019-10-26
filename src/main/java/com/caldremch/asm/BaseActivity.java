package com.caldremch.asm;


import com.caldremch.asm.android.Activity;
import com.caldremch.asm.annotation.BaseAnno;
import com.caldremch.asm.annotation.NotLoginResume;

@BaseAnno
public class BaseActivity extends Activity {

    @NotLoginResume
    public void loginResume() {
    }

    protected void onCreate(String param1) {
    }

    protected void onDestroy(boolean param2) {
    }

    protected boolean isTest() {
        return false;
    }

    protected void onResume() {
        super.onResume();
    }
}
