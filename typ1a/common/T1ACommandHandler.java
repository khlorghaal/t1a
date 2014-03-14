package com.typ1a.common;

import java.util.List;
import java.util.Vector;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
public class T1ACommandHandler implements ICommand, IConsoleHandler{

	@Override
	public boolean handleCommand(String command, Object... data) {
		System.out.println(command);
		return false;
	}
	
	
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "asdf";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "DERP";
	}

	@Override
	public List getCommandAliases() {
		List a= new Vector();
		a.add("pingas");
		return a;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		System.out.println(var1.getCommandSenderName());
		
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		// TODO Auto-generated method stub
		return false;
	}

}
