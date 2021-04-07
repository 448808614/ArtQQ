package org.artqq.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ListUtil {

    /*
     * 一瞬冻千秋
     * @return java.util.List
     * @author 伏秋洛
     * @creed: 将一个List重新排序 删除其中null的东西
     * @date 2020/6/6 0:41
     */
    public static List ReorderRemoveNull(List list, List newList){
        for(Object m : list){
            if(m!=null){
                newList.add(m);
            }
        }
        return newList;
    }

}

