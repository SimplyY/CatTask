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

    public static TaskTree newInstance(ArrayList<Task> theAllTaskArrayList) {
        allTaskArrayList = theAllTaskArrayList;

        TaskTree taskTree = new TaskTree(0, null);
        taskTree.setFirstRoot(true);

        taskTree.setTreeFinishTime();
        sort(taskTree);
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
            if (theChildTaskTree.getmTask().getFinishedDate().isBefore(pivotTaskTree.getTreeFinishTime())) {
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
                if (theTaskTreeI.getTreeFinishTime().isBefore(theTaskTreeJ.getTreeFinishTime())){
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

    private void setTreeFinishTime() {
        if (!isHasChild()) {
            treeFinishTime = mTask.getFinishedDate();
        } else {
            treeFinishTime = new MyDate(2200, 0, 0);
            for (TaskTree theTaskTree : childTaskTreeArrayList) {
                MyDate theFinishTime = theTaskTree.getTreeFinishTime();
                if (theFinishTime.isBefore(treeFinishTime)) {
                    treeFinishTime = theFinishTime;
                }
            }
        }
    }

    public boolean isHasChild() {
        return !childTaskTreeArrayList.isEmpty();
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

    public Task getmTask() {
        return mTask;
    }

    public MyDate getTreeFinishTime() {
        setTreeFinishTime();
        return treeFinishTime;
    }

    public boolean isFirstRoot() {
        return isFirstRoot;
    }

    public void setFirstRoot(boolean isFirstRoot) {
        this.isFirstRoot = isFirstRoot;
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
