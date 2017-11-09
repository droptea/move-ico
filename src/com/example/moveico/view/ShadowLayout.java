package com.example.moveico.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.moveico.R;
import com.example.moveico.view.MoveImageView.OnTouchClickListener;

public class ShadowLayout extends RelativeLayout{
	
	private boolean shadowSwitch = false;
	private Context context;
	 //定义三种模式：None、Drag、Zoom
	private static final int Mode_None=0;
	private static final int Mode_Drag=1;
	private static final int Mode_Zoom=2;
    private float distance;
    private int onClickIndex = 0;
    //当前操作模式
    private int mMode=Mode_None;
    private List<MoveImageView> list = new ArrayList<MoveImageView>();

	public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	public ShadowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ShadowLayout(Context context) {
		super(context);
		this.context = context;
	}
	
	
	int lastX = 0;
	int lastY = 0;
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(shadowSwitch){
			return false;
		}
		super.dispatchTouchEvent(event);
		switch (event.getAction()&MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mMode = Mode_Drag;
			lastX=(int)event.getRawX();//获取触摸事件触摸位置的原始X坐标  
		    lastY=(int)event.getRawY();  
			break;
		case MotionEvent.ACTION_UP:
			mMode = Mode_None;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:// 当屏幕上还有触点（手指），再有一个手指压下屏幕
			mMode = Mode_Zoom;
			distance = distance(event);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mMode = Mode_Drag;
			list.get(onClickIndex).initScale();
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		 case MotionEvent.ACTION_MOVE:
			  //缩放处理
			  if(mMode==Mode_Zoom)
			  {
				  getParent().requestDisallowInterceptTouchEvent(true);
				  float curDistance = distance(event);
				  if(distance != 0 && curDistance != 0){
					  float rate = curDistance/distance;
					  list.get(onClickIndex).changeSize(rate);
				  }
			  }
			  if(mMode==Mode_Drag){
				  	/*int dx=(int)event.getRawX()-lastX;  
				   // int dy=(int)event.getRawY()-lastY;
				  	int dy = 0;
				    if(Math.abs(dx)<10 && Math.abs(dy)<10){
				    	break;
				    }
				    int l=getLeft()+dx;   
				    int b=getBottom()+dy;  
				    int r=getRight()+dx;  
				    int t=getTop()+dy;  
				    layout(l, t, r, b);  
				    //Log.e("test", l+" , "+t+" , "+r+" , "+b+"  __ "+(int)(mWidth*(1-curScale)));
				    lastX=(int)event.getRawX();  
				    lastY=(int)event.getRawY();  
				    postInvalidate();           */
			  }
			  break;
		}
		return true;
	}

	public void changeShadowSwitch(){
		if(shadowSwitch){
			shadowSwitch = false;
		}else{
			shadowSwitch = true;
		}
	}
	
	public void addView(int icoId){
    	LayoutInflater inflater = LayoutInflater.from(context);
    	View view2 = inflater.inflate(R.layout.ico, this,false);
    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getWidth(), getHeight());
    	this.addView(view2,layoutParams);
    	MoveImageView moveImageView = (MoveImageView)view2.findViewById(R.id.moveImageView);
    	moveImageView.setImageDrawable(getResources().getDrawable(icoId));
    	moveImageView.setOnTouchClickListener(new OnTouchClickListener() {
			
			@Override
			public void onTouchclick(int index) {
				onClickIndex = index;
			}
		});
    	moveImageView.setScreenWidth(getWidth());
    	moveImageView.index = list.size();
    	list.add(moveImageView);
    }
	
	/**
	 * 计算两点之间的距离
	 *
	 * @param event
	 * @return
	 */
	public float distance(MotionEvent event) {
		try {
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			return (float)Math.sqrt(dx * dx + dy * dy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
