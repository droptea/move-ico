package com.example.moveico.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.moveico.R;
import com.example.moveico.adapter.Bean;
import com.example.moveico.adapter.CommonAdapter;

public class FloatListView extends RelativeLayout{
	private ListView listView;
	private Context context;
	private View view;
	private boolean isOpen;
	public FloatListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public FloatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FloatListView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		this.context = context;
		view = this;
		try {
			 LayoutInflater.from(context).inflate(R.layout.float_listview, this);
			 listView = (ListView)findViewById(R.id.listView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setAdapter(CommonAdapter<Bean> adapter){
		listView.setAdapter(adapter);
	}
	public void inAlpha(){
		isOpen = true;
		view.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.in_alpha);  
		listView.startAnimation(animation);
	}
	public void outAlpha(){
		isOpen = false;
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.out_alpha);  
		listView.startAnimation(animation);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(500);
					if(!isOpen){
						handler.sendEmptyMessage(1);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
			}
		}).start();
	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				view.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
		
	};
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		listView.setOnItemClickListener(onItemClickListener);
	}
}
