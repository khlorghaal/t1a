package com.typ1a.common;

import java.util.EnumSet;

public class T1ATickHandlerCommon implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Ticker.updateAll();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks(){
		return EnumSet.of(TickType.SERVER, TickType.CLIENT);}

	@Override
	public String getLabel() {return "T1A";}

}
