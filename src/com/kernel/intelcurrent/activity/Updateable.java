package com.kernel.intelcurrent.activity;

public interface Updateable {
    /**
     * 其子类需继承update方法，供service更新activity
     * 
     * */
    public abstract  void update(int type ,Object param);
}
