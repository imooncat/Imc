package org.imc.widget;

/*
	Author:		imooncat
	Date:		14-7-11
	Version:	1.0
	Function:	Easy use View to set Action Bar.
	Usage:		new ImcActionBar(YOUR_ACTIVITY);
				setLayoutId(R.layout.YOUR_LAYOUT);
				setActionBarView(R.layout.YOUT_LAYOUT | YOUR_VIEW | (BLANK),
					SHOW_HOME_OR_NOT);
				getActionBar().ORIGINAL_ACTION_BAR_FUNCTION(PARAM1, PARAM2, ...);
*/

import android.app.*;
import android.os.*;
import android.content.*;
import android.app.ActionBar.*;
import android.widget.*;
import android.view.*;
import android.graphics.drawable.*;

public class ImcActionBar
{
	private Activity con;
	private ActionBar actBar;
	private int layoutId = 0;
	private LayoutInflater inflater;
	private View actView;
	
	public ImcActionBar(Activity c)
	{
		con = c;
		actBar = c.getActionBar();
		inflater = c.getLayoutInflater();
	}
	
	public ActionBar getActionBar()
	{
		return actBar;
	}
	
	public void setActionBarView(int id, boolean withHome)
	{
		if(!withHome)
			actBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		else
			actBar.setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE
				);
		actBar.setCustomView(id);
		actBar.show();
	}
	
	public void setActionBarView(View view, boolean withHome)
	{
		if(!withHome)
			actBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		else
			actBar.setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE
				);
		actBar.setCustomView(view);
		actBar.show();
	}
	
	public void setActionBarView(boolean withHome) //setLayoutId() must be used before using this method
	{
		if(layoutId == 0)
			return ;
		if(!withHome)
			actBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		else
			actBar.setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE
			);
		actBar.setCustomView(actView);
		actBar.show();
	}
	
	public View setLayoutId(int id)
	{
		layoutId = id;
		if(con.getResources().getLayout(id) == null)
			return null;
		actView = inflater.inflate(id, null);
		return actView;
	}
	
	public View findViewById(int id) //setLayoutId() must be used before using this method
	{
		if(layoutId == 0)
			return null;
		if(actView == null)
			return null;
		return actView.findViewById(id);
	}
	
	public View getActionBarView()
	{
		return actView;
	}
}
