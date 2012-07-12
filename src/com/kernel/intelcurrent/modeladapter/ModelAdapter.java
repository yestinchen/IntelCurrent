package com.kernel.intelcurrent.modeladapter;

import com.kernel.intelcurrent.model.ICModel;
import com.kernel.intelcurrent.model.Task;

/**
 * classname:ModelAdapter.java
 * @author 许凌霄
 * */
public abstract class ModelAdapter extends Thread 
{
    Task task;
    ICModel model;
    
    public ModelAdapter(Task t)
    {
    	task=t;
    	model=ICModel.getICModel();
    }
    /**
     * 子类需实现run方法来执行任务，同一个任务的最后一个线程完成时需要回调model的callback方法
     * */
    public abstract void run();
}
