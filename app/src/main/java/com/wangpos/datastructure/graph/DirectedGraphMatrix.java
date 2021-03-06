package com.wangpos.datastructure.graph;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiyue on 2018/1/9.
 * <p>
 * 有向图邻接矩阵
 */

public class DirectedGraphMatrix {


    public String[] getmVexs() {
        return mVexs;
    }

    public void setmVexs(String[] mVexs) {
        this.mVexs = mVexs;
    }

    public int[][] getmMatrix() {
        return mMatrix;
    }

    public void setmMatrix(int[][] mMatrix) {
        this.mMatrix = mMatrix;
    }

    private String[] mVexs;       // 顶点集合
    private int[][] mMatrix;    // 邻接矩阵

    int[] inDegrees;  //入度
    int[] outDegrees;  //出度


    public DirectedGraphMatrix(String[] vexs, String[][] edges) {


        // 初始化"顶点数"和"边数"
        int vlen = vexs.length;
        int elen = edges.length;

        // 初始化"顶点"
        mVexs = new String[vlen];
        for (int i = 0; i < mVexs.length; i++)
            mVexs[i] = vexs[i];

        // 初始化"边"
        mMatrix = new int[vlen][vlen];
        for (int i = 0; i < elen; i++) {
            // 读取边的起始顶点和结束顶点
            int p1 = getPosition(edges[i][0]);
            int p2 = getPosition(edges[i][1]);

            /**
             * 有向图有固定方向
             */
            mMatrix[p1][p2] = 1;
//            mMatrix[p2][p1] = 1;
        }

        /**
         * 计算入度
         */
        calculateInDegree();
    }


    private int getPosition(String s) {

        for (int i = 0; i < mVexs.length; i++) {
            if (mVexs[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 这个算法只适合矩阵表示的计算入度
     */
    public void calculateInDegree() {
        inDegrees = new int[mVexs.length];

        for (int i = 0; i < mVexs.length; i++) {
            int in = 0;
            for (int j = 0; j < mVexs.length; j++) {
                if (mMatrix[j][i] == 1) {
                    in++;

                }
            }
            inDegrees[i] = in;
        }

    }

    /**
     * 这个算法只适合矩阵表示的计算出度
     */
    public void calculateOutDegree() {
        inDegrees = new int[mVexs.length];

        for (int i = 0; i < mVexs.length; i++) {
            int out = 0;
            for (int j = 0; j < mVexs.length; j++) {
                if (mMatrix[i][j] == 1) {
                    out++;
                }
            }
            outDegrees[i] = out;
        }

    }


    /**
     * 将相邻点入度 -1
     *
     * @param position
     */
    public void deletePositin(int position) {

        for (int i = 0; i < mVexs.length; i++) {
            if (mMatrix[position][i] == 1) {
                inDegrees[i]--;
            }
        }
    }


    public List<Integer> getDfsResult() {
        return dfsResult;
    }

    List<Integer>dfsResult = new ArrayList<>();

    public void toplogicSortByDFS(){


        boolean[] visited = new boolean[mVexs.length];       // 顶点访问标记

        // 初始化所有顶点都没有被访问
        for (int i = 0; i < mVexs.length; i++)
            visited[i] = false;

        Log.i("tu","DFS: ");
        for (int i = 0; i < mVexs.length; i++) {
            if (!visited[i]) {
                DFS(i, visited);
            }
        }

        for (int i=dfsResult.size()-1;i>=0;i--){
            Log.i("topo",mVexs[dfsResult.get(i).intValue()]);
        }
    }



    /*
     * 深度优先搜索遍历图的递归实现
     */
    private void DFS(int i, boolean[] visited) {

        visited[i] = true;
        Log.i("tu",mVexs[i]);
        // 遍历该顶点的所有邻接顶点。若是没有访问过，那么继续往下走
        for (int w = firstVertex(i); w >= 0; w = nextVertex(i, w)) {
            if (!visited[w]) {
                DFS(w, visited);

            }
        }
        dfsResult.add(i);
    }



    /**
     * kahn算法
     */
    public void toplogicSort() {
        int header = 0;
        int result[] = new int[mVexs.length];
        for (int i = 0; i < mVexs.length; i++) {
            result[i] = -1;
        }

        for (int i = 0; ; i++) {
            if (inDegrees[i] == 0) {

                boolean isVisit = false;
                for (int m = 0; m < header; m++) {
                    if (result[m] == i) {
                        isVisit = true;
                    }
                }
                if (!isVisit) {
                    result[header] = i;
                    deletePositin(i);
                    header++;
                    if (header == inDegrees.length) {
                        break;
                    }

                }
                if (i == 5) {
                    i = 0;
                }
            }
        }

        /***
         * 输出
         */

        for (int i = 0; i < header; i++) {
            Log.i("tu", mVexs[result[i]]);
        }

    }

    public void printPointDegree() {

        for (int i = 0; i < mVexs.length; i++) {
            Log.i("tu", mVexs[i] + " : " + inDegrees[i]);
        }
    }



    /*
   * 返回顶点v的第一个邻接顶点的索引，失败则返回-1
   */
    private int firstVertex(int v) {

        if (v<0 || v>(mVexs.length-1))
            return -1;

        for (int i = 0; i < mVexs.length; i++)
            if (mMatrix[v][i] == 1)
                return i;

        return -1;
    }

    /*
     * 返回顶点v相对于w的下一个邻接顶点的索引，失败则返回-1
     */
    private int nextVertex(int v, int w) {

        if (v<0 || v>(mVexs.length-1) || w<0 || w>(mVexs.length-1))
            return -1;

        for (int i = w + 1; i < mVexs.length; i++)
            if (mMatrix[v][i] == 1)
                return i;

        return -1;
    }
}
