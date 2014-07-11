package org.imc;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import org.imc.widget.*;
import android.view.View.*;

public class MainActivity extends Activity
{
	ImcActionBar iActBar;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		iActBar = new ImcActionBar(this);
		iActBar.setActionBarView(R.layout.action_bar_a, false);
    }
	
	public void onSetA(View v)
	{
		iActBar.setActionBarView(R.layout.action_bar_a, false);
	}
	
	public void onSetB(View v)
	{
		iActBar.setLayoutId(R.layout.action_bar_b);
		(iActBar.findViewById(R.id.actionbarbClose))
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					MainActivity.this.finish();
				}

				
			});
		iActBar.setActionBarView(false);
		iActBar.clear();
	}
}
