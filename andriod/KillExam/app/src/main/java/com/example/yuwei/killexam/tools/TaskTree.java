package com.example.yuwei.killexam.tools;

import java.util.ArrayList;

/**
 * Created by yuwei on 15/2/26.
 */

//the first root = null
public class TaskTree {
    private static ArrayList<Task> allTaskArrayList;
    private static ArrayList<Task> sortedTaskArrayList ;

    private static ArrayList<Task> sortedFinishedTaskArrayList;
    private static ArrayList<Task> sortedUnfinishedTaskArrayList;

    private boolean isFirstRoot = false;

    private int attribute;

//  通过lateFinishTime来判断是否过期
    private MyDate treeEarlyFinishTime;
    private MyDate treeLateFinishTime;
    private Task mTask;

    private ArrayList<TaskTree> childTaskTreeArrayList;


    public static TaskTree newInstance(ArrayList<Task> theAllTaskArrayList) {
        allTaskArrayList = theAllTaskArrayList;

        TaskTree taskTree = new TaskTree(0, null);
        taskTree.setFirstRoot(true);

        taskTree.setTreeFinishTime();
        sort(taskTree);
        initSortedTaskArrayList(taskTree);

        initSortedTasksHeader();

        classfyByHasFinished();
        return taskTree;
    }

    public TaskTree() {
    }
    public TaskTree(int theAttribute, Task theTask) {
        setAttribute(theAttribute);
        mTask = theTask;

        initChildTreeTaskArrayList();
    }

    private static void initSortedTaskArrayList(TaskTree taskTree){
        sortedTaskArrayList = new ArrayList<>();
        setSortedTaskArrayList(taskTree);
    }

    private static void setSortedTaskArrayList(TaskTree taskTree){
        if (taskTree.isHasChild()){
            for (TaskTree theTaskTree : taskTree.childTaskTreeArrayList){
                sortedTaskArrayList.add(theTaskTree.getmTask());
                setSortedTaskArrayList(theTaskTree);
            }
        }
    }

    private static void initSortedTasksHeader(){
        MyDate headerDate = new MyDate();
        for (Task task : sortedTaskArrayList){
            if (task.getTaskAttribute().getSelectedName().equals("一级")){
                headerDate = task.getFinishedDate();
                task.setHeaderDate(headerDate);
            }
            else{
                task.setHeaderDate(headerDate);
            }
        }
    }



    //TODO:之后需要在taskList里动态调用
    public static void classfyByHasFinished() {
    }

    //根据从自己开始的tree里task最早完成的时间sort
    public static void sort(TaskTree taskTree) {
        if (taskTree.isFirstRoot()) {
            qucikSort(taskTree, 0, taskTree.childTaskTreeArrayList.size() - 1);
        }

        for (TaskTree theTaskTree : taskTree.childTaskTreeArrayList) {
            if (theTaskTree.isHasChild()) {
                sortChild(theTaskTree);
            }
        }
    }
//  对第一级任务排序
    private static void qucikSort(TaskTree taskTree, int left, int right) {
        if (left < right) {
            int pivot = left;
            int newPivot = partition(taskTree, left, right, pivot);

            qucikSort(taskTree, left, newPivot - 1);
            qucikSort(taskTree, newPivot + 1, right);
        }
    }

    private static int partition(TaskTree taskTree, int left, int right, int pivot) {
        ArrayList<TaskTree> childTaskTreeArrayList = taskTree.childTaskTreeArrayList;
        TaskTree pivotTaskTree = childTaskTreeArrayList.get(pivot);

        swap(childTaskTreeArrayList, pivot, right);
        int newPivot = left;

        for (int i = left; i < right; i++) {
            TaskTree theChildTaskTree = childTaskTreeArrayList.get(i);
            if (theChildTaskTree.getmTask().getFinishedDate().isBefore(pivotTaskTree.getTreeEarlyFinishTime())) {
                swap(childTaskTreeArrayList, i, newPivot);
                newPivot++;
            }
        }

        swap(childTaskTreeArrayList, newPivot, right);
        return newPivot;
    }

    private static void sortChild(TaskTree taskTree) {
        insertSort(taskTree);
        for (TaskTree theTaskTree : taskTree.childTaskTreeArrayList) {
            if (theTaskTree.isHasChild()) {
                sortChild(theTaskTree);
            }
        }
    }

//  taskTree的孩子进行排序
    private static void insertSort(TaskTree taskTree) {
        ArrayList<TaskTree> taskTreeArrayList = taskTree.childTaskTreeArrayList;
        for (int i = 1; i < taskTreeArrayList.size(); i++) {
            TaskTree theTaskTreeI = taskTreeArrayList.get(i);
            int j;
            for (j = i - 1; j >= 0; j--) {
                TaskTree theTaskTreeJ = taskTreeArrayList.get(j);
                if (theTaskTreeI.getTreeEarlyFinishTime().isBefore(theTaskTreeJ.getTreeEarlyFinishTime())){
                    taskTreeArrayList.set(j+1, theTaskTreeJ);
                }
                else{

                    break;
                }
            }
            taskTreeArrayList.set(j + 1, theTaskTreeI);
        }
    }

    private static void swap(ArrayList<TaskTree> taskTreeArrayList, int index1, int index2) {
        TaskTree tmp = taskTreeArrayList.get(index1);
        taskTreeArrayList.set(index1, taskTreeArrayList.get(index2));
        taskTreeArrayList.set(index2, tmp);
    }

    public MyDate getTreeEarlyFinishTime() {
        return treeEarlyFinishTime;
    }

    public MyDate getTreeLateFinishTime(){
        return treeLateFinishTime;
    }

    public boolean isHasChild() {
        return !childTaskTreeArrayList.isEmpty();
    }

    public Task getmTask() {
        return mTask;
    }

    public boolean isFirstRoot() {
        return isFirstRoot;
    }

    public void setFirstRoot(boolean isFirstRoot) {
        this.isFirstRoot = isFirstRoot;
    }

//  分别设置taskTree的最早完成时间和最晚完成时间
    private void setTreeFinishTime() {
        if (!isHasChild()) {
            treeEarlyFinishTime = mTask.getFinishedDate();
            treeLateFinishTime = mTask.getFinishedDate();
        } else {
            treeEarlyFinishTime = new MyDate(2200, 0, 0);
            treeLateFinishTime = new MyDate(2000, 0 ,0);

//          取子节点最值
            for (TaskTree theTaskTree : childTaskTreeArrayList) {
                theTaskTree.setTreeFinishTime();
                MyDate theEarlyFinishTime = theTaskTree.getTreeEarlyFinishTime();
                if (theEarlyFinishTime.isBefore(treeEarlyFinishTime)) {
                    treeEarlyFinishTime = theEarlyFinishTime;
                }
                MyDate theLateFinishTime = theTaskTree.getTreeLateFinishTime();
                if (treeLateFinishTime.isBefore(theLateFinishTime)){
                    treeLateFinishTime = theLateFinishTime;
                }
            }

//          与根节点比较
            if (mTask != null && mTask.getFinishedDate().isBefore(treeEarlyFinishTime)){
                treeEarlyFinishTime = mTask.getFinishedDate();
            }
            if (mTask != null && treeLateFinishTime.isBefore(mTask.getFinishedDate())){
                treeLateFinishTime = mTask.getFinishedDate();
            }
        }
    }


    private void initChildTreeTaskArrayList() {
        childTaskTreeArrayList = new ArrayList<>();
        for (Task task : allTaskArrayList) {
            if (task.getTaskAttribute().getAttribute() == getChildAttribute()) {
//              所有attribute为1的task的父节点都是theFirstRoot
                if (getChildAttribute() == 1 || task.getBelongName().equals(mTask.getTaskName())) {
                    TaskTree taskTree = new TaskTree(getChildAttribute(), task);
                    childTaskTreeArrayList.add(taskTree);
                }
            }
        }
    }

    public static ArrayList<Task> getSortedTaskArrayList() {
        return sortedTaskArrayList;
    }

    private int getChildAttribute() {
        return attribute + 1;
    }

    public int getAttribute() {
        return attribute;
    }

    private void setAttribute(int attribute) {
        this.attribute = attribute;
    }
}
