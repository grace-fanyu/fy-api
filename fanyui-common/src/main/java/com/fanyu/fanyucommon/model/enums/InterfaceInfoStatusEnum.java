package com.fanyu.fanyucommon.model.enums;

/**
 * @author fanyu
 * @date 2023/06/07 13:57
 **/
public enum InterfaceInfoStatusEnum {
	OFFINE("关闭",0),
	ONLINE("上线",1);

	private final String text;
	private final int value;

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}

	InterfaceInfoStatusEnum(String text, int value) {
		this.text = text;
		this.value = value;
	}
}
