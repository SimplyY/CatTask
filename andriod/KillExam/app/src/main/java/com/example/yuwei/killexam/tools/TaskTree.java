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
    public int hasFinished = 0;
    private int attribute;

    //  通过lateFinishTime来判断是否过期
    private MyDate treeEarlyFinishTime;
    private MyDate treeLateFinishTime;
    private Task mTask;

    private ArrayList<TaskTree> childTaskTreeArrayList;

//  更新taskTree都是通过给newInstance传不同的taskArray
    public static TaskTree newInstance(ArrayList<Task> theAllTaskArrayList) {
        allTaskArrayList = theAllTaskArrayList;

        TaskTree taskTree = new TaskTree(-1, null);
        taskTree.setFirstRoot(true);

        taskTree.setTreeFinishTime();

        sort(taskTree);

        initSortedTaskArrayList(taskTree);

        initSortedTasksHeader();

        return taskTree;
    }

    public TaskTree(int theAttribute, Task theTask) {
        setAttribute(theAttribute);
        mTask = theTask;

        initChildTreeTaskArrayList();
    }

    private static void initSortedTaskArrayList(TaskTree taskTree) {
        sortedTaskArrayList = new ArrayList<>();
        setSortedTaskArrayList(taskTree);
    }

    private static void setSortedTaskArrayList(TaskTree taskTree) {
        if (taskTree.isHasChild()) {
            for (TaskTree theTaskTree : taskTree.childTaskTreeArrayList) {
                sortedTaskArrayList.add(theTaskTree.getmTask());
                setSortedTaskArrayList(theTaskTree);
            }
        }
    }

    private static void initSortedTasksHeader() {
        MyDate headerDate = new MyDate();
        for (Task task : sortedTaskArrayList) {
            if (task.getTaskAttribute().getSelectedName().equals("一级")) {
                headerDate = task.getFinishedDate();
                task.setHeaderDate(headerDate);
            } else {
                task.setHeaderDate(headerDate);
            }
        }
    }


    //根据从自己开始的tree里task最早完成的时间sort
    public static void sort(TaskTree taskTree) {
        if (taskTree.isFirstRoot()) {
            quickSort(taskTree, 0, taskTree.childTaskTreeArrayList.size() - 1);
        }

        for (TaskTree theTaskTree : taskTree.childTaskTreeArrayList) {
            if (theTaskTree.isHasChild()) {
                sortChild(theTaskTree);
            }
        }
    }

    //  对第一级任务排序
    private static void quickSort(TaskTree taskTree, int left, int right) {
        if (left < right) {
            int pivot = left;
            int newPivot = partition(taskTree, left, right, pivot);

            quickSort(taskTree, left, newPivot - 1);
            quickSort(taskTree, newPivot + 1, right);
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
                if (theTaskTreeI.getTreeEarlyFinishTime().isBefore(theTaskTreeJ.getTreeEarlyFinishTime())) {
                    taskTreeArrayList.set(j + 1, theTaskTreeJ);
                } else {

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


    public TaskTree getFirstAttributeTaskTree(Task task) {
//      如果是第一级任务,直接从最高层的taskTree的child里面找到相应tree
        if (task.getTaskAttribute().getSelectedPosition() == 0) {
            for (TaskTree theTaskTree : childTaskTreeArrayList) {
                if (theTaskTree.getmTask().getTaskName().equals(task.getTaskName())) {
                    return theTaskTree;
                }
            }
        }
//      如果不是,找到belong,进行递归
        else {
            for (Task theTask : allTaskArrayList) {
                if (theTask.getTaskName().equals(task.getBelongName())) {
                    task = theTask;
                    return getFirstAttributeTaskTree(task);
                }
            }

        }
        return this;
    }

    public static Task getTask(String taskName, int length) throws Exception {
        for (Task theTask : allTaskArrayList) {
            taskName = getLegalTaskName(taskName, length);
            String theTaskName = getLegalTaskName(theTask.getTaskName(), length);

            if (theTaskName.equals(taskName)) {
                return theTask;
            }
        }
        throw new Exception("cant find this task");
    }

    private static String getLegalTaskName(String theTaskName, int length){
         return theTaskName.length()<length ? theTaskName : theTaskName.substring(0, length);
    }

    public boolean isFinished() {
        if (mTask.getHasFinished() == 0) {
            return false;
        }

        if (!isHasChild()) {
            hasFinished = 1;
            return true;
        }

        for (TaskTree theTaskTree : childTaskTreeArrayList) {
            if (!theTaskTree.isFinished()) {
                return false;
            }
        }

        hasFinished = 1;
        return true;
    }

    public MyDate getTreeEarlyFinishTime() {
        return treeEarlyFinishTime;
    }

    public MyDate getTreeLateFinishTime() {
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
            treeLateFinishTime = new MyDate(2000, 0, 0);

//          取子节点最值
            for (TaskTree theTaskTree : childTaskTreeArrayList) {
                theTaskTree.setTreeFinishTime();
                MyDate theEarlyFinishTime = theTaskTree.getTreeEarlyFinishTime();
                if (theEarlyFinishTime.isBefore(treeEarlyFinishTime)) {
                    treeEarlyFinishTime = theEarlyFinishTime;
                }
                MyDate theLateFinishTime = theTaskTree.getTreeLateFinishTime();
                if (treeLateFinishTime.isBefore(theLateFinishTime)) {
                    treeLateFinishTime = theLateFinishTime;
                }
            }

//          与根节点比较
            if (mTask != null && mTask.getFinishedDate().isBefore(treeEarlyFinishTime)) {
                treeEarlyFinishTime = mTask.getFinishedDate();
            }
            if (mTask != null && treeLateFinishTime.isBefore(mTask.getFinishedDate())) {
                treeLateFinishTime = mTask.getFinishedDate();
            }
        }
    }


    private void initChildTreeTaskArrayList() {
        childTaskTreeArrayList = new ArrayList<>();
//      只选取没完成的一级任务树
        if (getAttribute() == 0 && hasFinished == 1) {
            return;
        }
        for (Task task : allTaskArrayList) {
//          选取attribute是此taskTree的child的attribute的task
            if (task.getTaskAttribute().getSelectedPosition() == getChildAttribute()) {
//              选取belongName和mTask相同的task，其中theFirstRoot是没有mTask的
                if (mTask == null || task.getBelongName().equals(mTask.getTaskName())) {
//                  通过调用构造函数达到间接递归的效果
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
