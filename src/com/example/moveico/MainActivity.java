package com.example.moveico;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.example.moveico.adapter.Bean;
import com.example.moveico.adapter.CommonAdapter;
import com.example.moveico.adapter.ViewHolder;
import com.example.moveico.view.FloatButton;
import com.example.moveico.view.FloatButton.FloatBtnClickListener;
import com.example.moveico.view.FloatListView;
import com.example.moveico.view.ShadowLayout;


public class MainActivity extends Activity {

	private ShadowLayout layout;
	private Button editBtn;
	private FloatListView floatListView;
	private FloatButton floatBtn;
	private CommonAdapter<Bean> adapter;
	private List<Bean> mDatas = new ArrayList<Bean>();
	private Context context;
	public String typeLight = "light";
	public String typeCamera = "camera";
	public String typeCurtain = "curtain";
	public String typeDoorMagnetic = "door_magnetic";
	 private boolean isEdit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }

    
    private void init(){
    	context = this;
    	floatListView = (FloatListView)findViewById(R.id.floatListView);
    	floatBtn = (FloatButton)findViewById(R.id.floatBtn);
    	layout = (ShadowLayout)findViewById(R.id.layout);
    	editBtn = (Button)findViewById(R.id.editBtn);
    	Bean bean = new Bean("灯", R.drawable.light, "light");
    	Bean bean2 = new Bean("摄像头", R.drawable.camera, "camera");
    	Bean bean3 = new Bean("窗帘电机", R.drawable.curtain, "curtain");
    	Bean bean4 = new Bean("门磁", R.drawable.door_magnetic, "door_magnetic");
    	mDatas.add(bean);
    	mDatas.add(bean2);
    	mDatas.add(bean3);
    	mDatas.add(bean4);
    	floatListView.setAdapter(adapter = new CommonAdapter<Bean>(
    			getApplicationContext(), mDatas, R.layout.item_float_listview) {

					@Override
					public void convert(ViewHolder helper, Bean item) {
						// TODO Auto-generated method stub
						helper.setText(R.id.name, item.getName());
						helper.setImageResource(R.id.img, item.getImage());
					}
		});
    	floatListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int location,
					long arg3) {
				layout.addView(mDatas.get(location).getImage());
			}
		});
    	if(isEdit){
    		editBtn.setText("完成");
    		floatBtn.setVisibility(View.VISIBLE);
    	}else{
    		editBtn.setText("编辑");
    		floatBtn.setVisibility(View.INVISIBLE);
    	}
    }
    
    private void setListener(){
    	editBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layout.changeShadowSwitch();
				if(isEdit){
					editBtn.setText("编辑");
					floatBtn.close();
					floatBtn.setVisibility(View.INVISIBLE);
					floatListView.outAlpha();
		    		isEdit = false;
		    	}else{
		    		editBtn.setText("完成");
		    		floatBtn.setVisibility(View.VISIBLE);
		    		isEdit = true;
		    		
		    	}
			}
		});
    	floatBtn.setOnFloatBtnClickListener(new FloatBtnClickListener() {
			
			@Override
			public void onclick(boolean toOpen) {
				if(toOpen){
					floatListView.inAlpha();
				}else{
					floatListView.outAlpha();
				}
			}
		});
    }
    


}
