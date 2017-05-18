package com.wz.mobilemedia.bean;

import java.io.Serializable;

public class StorageInfo implements Serializable {
	/**
	 * 
	 */
	public String path;
	public String state;
	public boolean isRemoveable;
 
	public StorageInfo(String path) {
		this.path = path;
	}
 
	public boolean isMounted() {
		return "mounted".equals(state);
	}
}