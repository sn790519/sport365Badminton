package com.sport365.badminton.view.advertisement;

public interface ISwithcer<T> {

	/**
	 * 获取图片切换容器所需的URL数据.用于转换各种不同类
	 * 
	 * @param data
	 * @return
	 */
	public abstract String getUrlString(T data);

}
