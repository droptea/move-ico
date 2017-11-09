package com.example.moveico.view;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;


public class MoveImageView extends CircleImageView{

	private int screenHeight;
	private int screenWidth;
	private DisplayMetrics dm;
    private int lastX, lastY; 
    private int mode = 0;
    private final int moreTouch = 1;
    private final int oneTouch = 0;
    private float curRate = 1F;
    private float oScale = 1F;
    private float curScale = 1F;
    private int mWidth;
    private int mHeight;
    private float maxScale = 2F;
    private float minScale = 0.3F; 
    private float maxScaleTruth = 1.5F;
    private float minScaleTruth = 0.5F;
    private OnTouchClickListener listener;
    public int index;
   
    protected void init() {
    	dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels-getStatusBarHeight();
        screenWidth = dm.widthPixels;
    }
    
    public void setScreenWidth(int value){
    	screenWidth = value;
    }
    
    @Override    
    public boolean dispatchTouchEvent(MotionEvent ev) {   
        getParent().requestDisallowInterceptTouchEvent(true);  
        return super.dispatchTouchEvent(ev);    
    }  
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = getWidth();
		mHeight = getHeight(); 
	}

	public MoveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveImageView(Context context) {
        super(context);
        init();
       
    }

    @SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				setScaleX(curScale);
		    	setScaleY(curScale);
		    	postInvalidate(); 
				break;
			default:
				break;
			}
		}
    	
    };
    public void initScale(){
    	if(curScale > maxScaleTruth){
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						curScale = curScale - 0.1F;
						handler.sendEmptyMessage(0);
						if(curScale < maxScaleTruth){
							curScale = maxScaleTruth;
							oScale = curScale;
							dWValue = (int)(mWidth*(1-curScale)/2);//view缩放后宽度差值的一半
					    	dHValue = (int)(mHeight*(1-curScale)/2);//view高度变化差值的一半
							break;
						}
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}).start();
    	}else if(curScale < minScaleTruth){
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						curScale = curScale + 0.1F;
						handler.sendEmptyMessage(0);
						if(curScale > minScaleTruth){
							curScale = minScaleTruth;
							oScale = curScale;
							dWValue = (int)(mWidth*(1-curScale)/2);//view缩放后宽度差值的一半
					    	dHValue = (int)(mHeight*(1-curScale)/2);//view高度变化差值的一半
							break;
						}
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}).start();
    		
    	}else{
	    	oScale = curScale;
	    	dWValue = (int)(mWidth*(1-curScale)/2);//view缩放后宽度差值的一半
	    	dHValue = (int)(mHeight*(1-curScale)/2);//view高度变化差值的一半
    	}
    }

    
    private int dWValue = 0;
    private int dHValue = 0;
    int ll=0;   
    int bb=0;  
    int rr=0;  
    int tt=0;  
    @SuppressLint("ClickableViewAccessibility")
	@Override
   	public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
	    switch(event.getAction()&MotionEvent.ACTION_MASK){  
	    case MotionEvent.ACTION_DOWN:
	    	toMove = true;
	    	autoMoveScrollView();
	    	mode = oneTouch;
		    lastX=(int)event.getRawX();//获取触摸事件触摸位置的原始X坐标  
		    lastY=(int)event.getRawY();  
		    return true;
	    case MotionEvent.ACTION_MOVE:
	    	if(mode==moreTouch){
	    		break;
	    	}
		    int dx=(int)event.getRawX()-lastX;  
		    int dy=(int)event.getRawY()-lastY;
		    if(Math.abs(dx)<10 && Math.abs(dy)<10){
		    	break;
		    }
		    ll=getLeft()+dx;   
		    bb=getBottom()+dy;  
		    rr=getRight()+dx;  
		    tt=getTop()+dy;  
		    //下面判断移动是否超出屏幕  
		    if((ll+dWValue)<0){  
		    	ll=0-dWValue;      
		    	rr=ll+mWidth;  
		    }else if((rr-dWValue)>screenWidth){  
		    	rr=screenWidth+dWValue;  
		    	ll=rr-mWidth;  
		    }  
		    if((tt+dHValue)<0){  
		    	tt=0-dHValue;  
		    	bb=tt+mHeight;  
		    }else if((bb-dHValue)>screenHeight){  
		    	bb=screenHeight+dHValue;  
		    	tt=bb-mHeight;  
		    }
	    	layout(ll, tt, rr, bb);  
		    
		    //Log.e("test", l+" , "+t+" , "+r+" , "+b+"  __ "+(int)(mWidth*(1-curScale)));
		    lastX=(int)event.getRawX();  
		    lastY=(int)event.getRawY();  
		    postInvalidate();             
		    break;  
	    case MotionEvent.ACTION_UP:  
	    	toMove = false;
	    	try {
	    		listener.onTouchclick(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	 //下面判断移动是否超出屏幕  
		    if((ll+dWValue)<0){  
		    	ll=0-dWValue;      
		    	rr=ll+mWidth;  
		    }else if((rr-dWValue)>screenWidth){  
		    	rr=screenWidth+dWValue;  
		    	ll=rr-mWidth;  
		    }  
		    if((tt+dHValue)<0){  
		    	tt=0-dHValue;  
		    	bb=tt+mHeight;  
		    }else if((bb-dHValue)>screenHeight){  
		    	bb=screenHeight+dHValue;  
		    	tt=bb-mHeight;  
		    }
	    	layout(ll, tt, rr, bb); 
	    	postInvalidate();   
	    	break;         
	    case MotionEvent.ACTION_POINTER_DOWN:
	    	mode = moreTouch;
	    	break;
	    case MotionEvent.ACTION_POINTER_UP:
	    	break;
	    	
	    }
	    return false;
    }
    private boolean toMove = false;
    private boolean isAuto = false;
    private synchronized void autoMoveScrollView(){
    	new Thread(new Runnable() {
			@Override
			public void run() {
				if(!isAuto){
					isAuto = true;
			    	while (toMove) {
			    		try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		if((rr-dWValue)>=(OverHorizontalScrollView.getScrollViewScrollX()+dm.widthPixels)){
			    			autoHandler.sendEmptyMessage(0);
			    		}
			    		if((ll+dWValue)<=(OverHorizontalScrollView.getScrollViewScrollX())){
			    			autoHandler.sendEmptyMessage(1);
			    		}
						
					}
					isAuto = false;

		    	}
				
			}
		}).start();
    	
    }
    private Handler autoHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			int d = 30;
			switch (msg.what) {
			case 0:
				OverHorizontalScrollView.getScrollView().scrollTo(OverHorizontalScrollView.getScrollViewScrollX()+d, 0);
				if(!((ll+dWValue)<=0||(rr-dWValue)>=screenWidth)){
					ll = ll+d;
					rr = rr+d;
					layout(ll, tt, rr, bb);  
				    postInvalidate();             
				}
				break;
			case 1:
				OverHorizontalScrollView.getScrollView().scrollTo(OverHorizontalScrollView.getScrollViewScrollX()-d, 0);
				if(!((ll+dWValue)<=0||(rr-dWValue)>=screenWidth)){
					ll = ll-d;
					rr = rr-d;
					layout(ll, tt, rr, bb);  
				    postInvalidate();             
				}
				break;
			default:
				break;
			}
		}
    	
    };
    public void changeLayout(int l, int t, int r, int b){
    	layout(ll, tt, rr, bb);  
	    postInvalidate();             
    }
    public void changeSize(float rate){
    	if(curRate != rate && rate!= 0){
			if(oScale*rate > maxScale){
				curScale = maxScale;
			}else if(oScale*rate < minScale){
				curScale = minScale;
			}else{
				curScale = oScale*rate;
			}
			setScaleX(curScale);
	    	setScaleY(curScale);
	    	postInvalidate();   
	    	curRate = rate;
	    	Log.e("test", "curScale"+curScale);
    	}
    }
	private int getStatusBarHeight() {  
        Class<?> c = null;  
  
        Object obj = null;  
  
        Field field = null;  
  
        int x = 0, sbar = 0;  
  
        try {  
  
            c = Class.forName("com.android.internal.R$dimen");  
  
            obj = c.newInstance();  
  
            field = c.getField("status_bar_height");  
  
            x = Integer.parseInt(field.get(obj).toString());  
  
            sbar = getContext().getResources().getDimensionPixelSize(x);  
  
        } catch (Exception e1) {  
  
            e1.printStackTrace();  
  
        }  
  
        return sbar;  
    }  
	private boolean isFirstRun = true;
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if(isFirstRun){
			ll=left+OverHorizontalScrollView.getScrollViewScrollX();
		    tt=top;
		    rr=right+OverHorizontalScrollView.getScrollViewScrollX();
		    bb=bottom;
		    isFirstRun = false;
			layout(ll, tt, rr, bb);  
		    postInvalidate();  
		}else{
			if(ll+tt+rr+bb!=0&&(left!=ll||top!=tt||right!=rr||bottom!=bb)){
				layout(ll, tt, rr, bb);  
			    postInvalidate();         
			}else{
				super.onLayout(changed, left, top, right, bottom);
			}
		}
		Log.e(">>>>", left+"_"+top+"_"+right+"_"+bottom+changed+" "+OverHorizontalScrollView.getScrollViewScrollX());
	}

	public void setOnTouchClickListener(OnTouchClickListener listener){
		this.listener = listener;
	}
	
	interface OnTouchClickListener{
		public void onTouchclick(int index);
	}

}