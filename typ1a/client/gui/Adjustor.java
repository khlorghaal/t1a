package com.typ1a.client.gui;

/**add to a clickable's action to make it set another clickable's state automatically*/
public class Adjustor implements Runnable{
	final Clickable putter, puttee;
	public Adjustor(Clickable putter, Clickable puttee){
		this.putter= putter;
		this.puttee= puttee;
	}
	@Override
	public void run() {
		puttee.state= putter.state;
	}
	
//	public static class NumberTextToState extends Adjustor{
//		public NumberTextToState(TextField putter, Clickable puttee) {
//			super(putter, puttee);
//		}
//		@Override
//		public void run(){
//			StringBuilder str= ((TextField)putter).str;
//			if(! Character.isDigit( str.charAt(str.length()) ))
//					str.deleteCharAt(str.length());
//			puttee.state= Integer.valueOf(str.toString());
//		}
//	}
	public static class StateToNumberText extends Adjustor{
		public StateToNumberText(Clickable putter, TextField puttee){
			super(putter, puttee);
		}
		@Override
		public void run(){
			((TextField)puttee).str= ((TextField)puttee).str
			.delete(0, ((TextField)puttee).str.length())
			.append(putter.state);
			
			System.out.println(((TextField)puttee).str.toString());
		}
	}
}