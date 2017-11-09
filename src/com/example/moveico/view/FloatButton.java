package com.example.moveico.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.moveico.R;

public class FloatButton extends RelativeLayout{
	private View view;
	private  Animation openAnim;
	private  Animation closeAnim;
	private boolean toOpen = true;
	private RelativeLayout floatBtnLayout ;
	private FloatBtnClickListener floatBtnClickListener;
	public FloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public FloatButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FloatButton(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		try {
			 LayoutInflater.from(context).inflate(R.layout.float_button, this);
			 floatBtnLayout = (RelativeLayout)findViewById(R.id.floatBtnLayout);
			 view = findViewById(R.id.floatIco); 
			 openAnim = AnimationUtils.loadAnimation(context, R.anim.float_btn_rotate_open);  
			 closeAnim = AnimationUtils.loadAnimation(context, R.anim.float_btn_rotate_close);  
			 floatBtnLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					floatBtnClickListener.onclick(toOpen);
					if(toOpen){
						open();
					}else{
						close();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void close(){
		if(toOpen!=true){
			view.startAnimation(closeAnim);
			toOpen = true;
		}
	}
	
	public void open(){
		if(toOpen!=false){
			view.startAnimation(openAnim);
			toOpen = false;
		}
	}
	
	public interface FloatBtnClickListener{
		public void onclick(boolean toOpen);
		
	}
	public void setOnFloatBtnClickListener(FloatBtnClickListener floatBtnClickListener){
		this.floatBtnClickListener = floatBtnClickListener;
	}
}
