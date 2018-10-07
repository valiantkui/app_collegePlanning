package com.planning.college.tools;

import com.planning.college.model.Subject;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



/**
 * 学科的树形节点
 * @author KUIKUI
 */
public class SubjectMap {
	public Map<String,Subject> map;//保存学科编号与学科的映射

	public String [] sNoArray;//保存学科编号的数组


	/**
	 * 一行信息存储了一个学科的所需基础学科
	 * 一列信息存储了一个学科的后继学科
	 */
	public int [][]  matrixRelation;

	public SubjectMap(Map<String, Subject> map) {
		super();
		this.map = map;
		int size = map.size();
		this.sNoArray = new String[size];
		int i = 0;
		for(Entry<String, Subject> entry:map.entrySet()) {
			sNoArray[i++] = entry.getKey();
		}

		this.matrixRelation = new int[size][size];

	}


	/**
	 * 在指定的集合，指定的位置处判断是否存在某个元素
	 * @return 如果存在  返回true,否则返回false;
	 */
	public static boolean isVisit(List<Integer> list,int start,int end,Integer elem){
		if(start < 0 || end>list.size()){
			return false;
		}

		for(int i = start;i<end;i++){
			if(list.get(i).equals(elem)){
				return true;
			}
		}

		return false;
	}





}
