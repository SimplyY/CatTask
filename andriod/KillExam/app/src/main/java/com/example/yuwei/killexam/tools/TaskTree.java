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
    private MyDate treeFinishTime;
    private Task mTask;

    private ArrayList<TaskTree> childTaskTreeArrayList;

    public static TaskTree newInstance(ArrayList<Task> theAllTaskArrayList){
        allTaskArrayList = theAllTaskArrayList;

        TaskTree taskTree = new TaskTree(0, null);
        taskTree.isFirstRoot = true;

        taskTree.setTreeFinishTime();
        sort(taskTree);
        classfyByHasFinished();
        return taskTree;
    }

//TODO:之后需要在taskList里动态调用
    public static void classfyByHasFinished(){
    }



//根据从自己开始的tree里task最早完成的时间sort
    public static void sort(TaskTree taskTree){
        if (taskTree.isHasChild()){
            for (TaskTree theTaskTree : taskTree.childTaskTreeArrayList){

            }
        }

    }

    public TaskTree(int theAttribute, Task theTask){
        setAttribute(theAttribute);
        mTask = theTask;

        initChildTreeTaskArrayList();
    }

    private void setTreeFinishTime(){
        if (!isHasChild()){
            treeFinishTime = mTask.getFinishedDate();
        }
        else{
            treeFinishTime = new MyDate(2200, 0, 0);
            for (TaskTree theTaskTree : childTaskTreeArrayList){
                MyDate theFinishTime = theTaskTree.getTreeFinishTime();
                if (theFinishTime.isBefore(treeFinishTime)){
                    treeFinishTime = theFinishTime;
                }
            }
        }
    }

    public boolean isHasChild(){
        return !childTaskTreeArrayList.isEmpty();
    }

    private void initChildTreeTaskArrayList(){
        childTaskTreeArrayList = new ArrayList<>();
        for (Task task : allTaskArrayList){
            if (task.getTaskAttribute().getAttribute() == getChildAttribute()){
//              所有attribute为1的task的父节点都是theFirstRoot
                if (getChildAttribute()==1 || task.getBelongName().equals(mTask.getTaskName())) {
                    TaskTree taskTree = new TaskTree(getChildAttribute(), task);
                    childTaskTreeArrayList.add(taskTree);
                }
            }
        }
    }

    public Task getmTask() {
        return mTask;
    }

    public MyDate getTreeFinishTime() {
        setTreeFinishTime();
        return treeFinishTime;
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
