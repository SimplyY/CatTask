package com.example.yuwei.killexam.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.CompoundButton;

import com.example.yuwei.killexam.adapter.TaskListAdapter;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.taskFragments.TaskListFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by yuwei on 15/2/26.
 */

//每次newInstance，先排序出完整地树，再建立出sortedToDoTaskArrayList(被taskList所展示的)
//其中先检测第一级taskTree是否已完成，将完成的过滤掉（不加入todo中）

//the first root = null
public class TaskTree {
    private static Context mContext;

    private static ArrayList<Task> allTaskArrayList;
    private static ArrayList<Task> sortedTodoTaskArrayList;
    private static TaskTree root;

    private static TaskListAdapter mAdapter;

    private boolean isRoot = false;
    private int firstTaskHasFinished = 0;
    private int attribute;

    //  通过lateFinishTime来判断是否过期
    private MyDate treeEarlyFinishTime;
    private MyDate treeLateFinishTime;
    private Task mTask;

    private ArrayList<TaskTree> childTaskTreeArrayList;

    //  更新taskTree都是通过给newInstance传不同的taskArray
    public static TaskTree newInstance(ArrayList<Task> theAllTaskArrayList, Context context) {
        mContext = context;
        allTaskArrayList = theAllTaskArrayList;

        root = new TaskTree(-1, null);


        root.setRoot(true);

        root.setTreeFinishTime();

        sort(root);

        initSortedTaskArrayList(root);

        initSortedTasksHeader();

        return root;
    }

    public TaskTree(int theAttribute, Task theTask) {
        setAttribute(theAttribute);
        mTask = theTask;

        initChildTreeTaskArrayList();
    }

    private static void initSortedTaskArrayList(TaskTree taskTree) {
        sortedTodoTaskArrayList = new ArrayList<>();
        for (TaskTree firstTaskTree : taskTree.childTaskTreeArrayList) {
            firstTaskTree.setFirstTaskTreeHasFinished();
//          这里将已完成任务过滤
            if (firstTaskTree.getFirstTaskHasFinished() == 0) {
                sortedTodoTaskArrayList.add(firstTaskTree.getmTask());
                setSortedTodoTaskArrayList(firstTaskTree);
            }
        }
    }

    private static void setSortedTodoTaskArrayList(TaskTree taskTree) {
        if (taskTree.isHasChild()) {
            for (TaskTree theTaskTree : taskTree.childTaskTreeArrayList) {
                sortedTodoTaskArrayList.add(theTaskTree.getmTask());
                setSortedTodoTaskArrayList(theTaskTree);
            }
        }
    }

    private static void initSortedTasksHeader() {
        for (Task task : sortedTodoTaskArrayList) {
            TaskTree firstTaskTree = root.getFirstAttributeTaskTree(task);
            task.setHeaderDate(firstTaskTree.getTreeEarlyFinishTime());

        }
    }


    //根据从自己开始的tree里task最早完成的时间sort
    public static void sort(TaskTree taskTree) {
//      对一级任务用快排
        if (taskTree.isRoot()) {
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
            if (theChildTaskTree.getTreeEarlyFinishTime().isBefore(pivotTaskTree.getTreeEarlyFinishTime())) {
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

    public static Task getTask(String taskName) throws Exception {
        for (Task theTask : allTaskArrayList) {
            String theTaskName = theTask.getTaskName();

            if (theTaskName.equals(taskName)) {
                return theTask;
            }
        }
        Log.d("taskTree getTask", "cant find this task");
        throw new Exception("cant find this task");
    }


    //  this只能为firstTaskTree
    public boolean setFirstTaskTreeHasFinished() {
        if (mTask.getHasFinished() == 0) {
            firstTaskHasFinished = 0;
            return false;
        } else if (!this.isHasChild()) {
            firstTaskHasFinished = 1;
            return true;
        }

        firstTaskHasFinished = 1;
        int theIndex = sortedTodoTaskArrayList.indexOf(mTask) + 1;
//      向后找直到attribute == 1时停下,途中只要有一个就停下
        while (isMoveToTheFirstTask(theIndex)) {
            if (sortedTodoTaskArrayList.get(theIndex).getHasFinished() == 0) {
                firstTaskHasFinished = 0;
                break;
            }
            theIndex++;
        }

        return firstTaskHasFinished == 1;
    }

    public static void renewSortedTaskArray(Task checkedTask, CompoundButton checkBox, TaskListAdapter adapter) {
        mAdapter = adapter;

        //得到被点击任务的第一级的父任务
        TaskTree taskTree = TaskListFragment.taskTree.getFirstAttributeTaskTree(checkedTask);

        taskTree.setFirstTaskTreeHasFinished();
        if (taskTree.getFirstTaskHasFinished() == 1) {
            deleteDialog(taskTree, checkedTask, checkBox);
        }
    }

    private static void deleteDialog(final TaskTree taskTree, final Task checkedTask,final CompoundButton checkBox) {
        new AlertDialog.Builder(mContext).setCancelable(false)
                .setTitle("删除任务提示").setMessage("此一级任务已完成，是否删除")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFirstTaskTree(taskTree);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskTree.firstTaskHasFinished = 0;
                        checkBox.setChecked(false);
                        MyDatabaseHelper.updateTask(mContext, checkedTask);
                    }
                }).show();
    }
//  删除一个taskTree
    private static void deleteFirstTaskTree(TaskTree taskTree) {
        int theIndex = sortedTodoTaskArrayList.indexOf(taskTree.getmTask());

        deleteTask(theIndex);
//      注意由于remove会使得后面的task的index前移1,所以theIndex不需要++
        while (isMoveToTheFirstTask(theIndex)) {
            deleteTask(theIndex);
        }


        TaskListFragment.renewListView();

    }
//  向后找直到attribute == 1时停下,途中只要有一个没有完成就停下
    private static boolean isMoveToTheFirstTask(int theIndex){
        return theIndex < sortedTodoTaskArrayList.size() &&
                sortedTodoTaskArrayList.get(theIndex).getTaskAttribute().getSelectedPosition() != 0;
    }
//  单个任务的删除
    public static void deleteTask(int theIndex){
        MyDatabaseHelper.deleteTask(mContext, sortedTodoTaskArrayList.get(theIndex));

        sortedTodoTaskArrayList.remove(theIndex);
    }
    public int getFirstTaskHasFinished() {
        return firstTaskHasFinished;
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

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean isFirstRoot) {
        this.isRoot = isFirstRoot;
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

    public static ArrayList<Task> getSortedTodoTaskArrayList() {
        return sortedTodoTaskArrayList;
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
