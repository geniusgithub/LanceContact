package com.geniusgithub.contact.base;

import android.content.Context;

public interface ITabInterface {
	public void setTab(int position);
	public void onTabSelect(int position);
	public void onTabClick(int posotion);
	public int getCurrentTabPosition(Context context);
}
