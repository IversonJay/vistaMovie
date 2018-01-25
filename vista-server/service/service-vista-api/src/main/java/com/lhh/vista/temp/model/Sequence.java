package com.lhh.vista.temp.model;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author LiuHJ 
 *
 */
@Setter
@Getter
public class Sequence {
	private String mid;
	private int inx;
	private int type; 
	private String merge;
	
	private String mname;
	public static final int NOWSHING = 1;
    public static final int COMESOON = 2;
}
