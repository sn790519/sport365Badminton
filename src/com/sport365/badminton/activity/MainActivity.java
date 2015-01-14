package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;

public class MainActivity extends BaseActivity {
	private String		TAG	= MainActivity.class.getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn=(Button) findViewById(R.id.btn);
		btn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Intent.ACTION_SEND);   
		        intent.setType("image/*");   
		        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");   
		        intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");    
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		        startActivity(Intent.createChooser(intent, getTitle()));   				
			}
		});
	}

}
