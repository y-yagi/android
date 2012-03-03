package com.jpn.yanagi.FixedFormMailCreation;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public class TransmissionData implements Serializable {
	private static final long serialVersionUID = 8458470057342191437L;
	public String name;
	public String address;
	public String subject;
	public String body;
	public int seq;
	private final String BR = System.getProperty("line.separator"); 
	
	public TransmissionData(int seq, String name, String address, 
			String subject, String body) {
		this.seq = seq;
		this.name = name;
		this.address = address;
		this.subject = subject;
		this.body = body;
	}
	
	@Override
	public String toString()
	{
		return "アドレス:" + this.address + this.BR + "件名:" + this.subject + this.BR + "本文:" + this.body;
	}

}
