package com.example.yuwei.killexam.tools;

import java.util.ArrayList;

/**
 * Created by yuwei on 15/2/26.
 */

//the first root = null
public class TaskTree {
    private static ArrayList<Task> allTaskArrayList;
    private static ArrayList<Task> sortedTaskArrayList;

    private boolean isFirstRoot = false;

    private int attribute;
    private Task mTask;

    private ArrayList<TaskTree> taskTreeChildArrayList;

    public static TaskTree newInstance(ArrayList<Task> theAllTaskArrayList){
        allTaskArrayList = theAllTaskArrayList;

        TaskTree taskTree = new TaskTree(0, null);
        taskTree.isFirstRoot = true;
        return taskTree;
    }

    public TaskTree(int theAttribute, Task theTask){
        setAttribute(theAttribute);

        mTask = theTask;

        initChildTreeTaskArrayList();
    }

//根据子任务最早完成时间sort
    public void sort(){
//TODO:
    }

    public boolean isHasChild(){
        return taskTreeChildArrayList != null;
    }

    private void initChildTreeTaskArrayList(){
        taskTreeChildArrayList = new ArrayList<>();
        for (Task task : allTaskArrayList){
            if (task.getTaskAttribute().getAttribute() == getChildAttribute()){
//              所有attribute为1的task的父节点都是theFirstRoot
                if (getChildAttribute()==1 || task.getBelongName().equals(mTask.getTaskName())) {
                    TaskTree taskTree = new TaskTree(getChildAttribute(), task);
                    taskTreeChildArrayList.add(taskTree);
                }
            }
        }
    }

    private int getChildAttribute(){
        return attribute + 1;
    }


    public int getAttribute() {
        return attribute;
    }

    private void setAttribute(int attribute) {
        this.attribute = attribute;
    }


}
