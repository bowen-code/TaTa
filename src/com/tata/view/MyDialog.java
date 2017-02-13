package com.tata.view;


import com.tata.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MyDialog {
	private Context mContext;  
    private Dialog  mDialog;  
  
    public MyDialog(Context context)  
    {  
        mContext = context;  
    }  
   	public Dialog myDialog()  
       {  
           mDialog = new Dialog(mContext, R.style.dialog);  
           LayoutInflater in = LayoutInflater.from(mContext);  
           View viewDialog = in.inflate(R.layout.dialog, null);  
           TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
           textView.setVisibility(View.GONE);
           viewDialog.setBackgroundColor(0x00000000);  
//           viewDialog.setAlpha(0.5f);
           mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
           // �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
           // LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
           // mDialog.setContentView(viewDialog, layoutParams);  
           mDialog.setContentView(viewDialog);  
           mDialog.setCanceledOnTouchOutside(false);  
           return mDialog;  
       }  
    
    
    @SuppressLint("NewApi")
	public Dialog loadDialog1()  
    {  
        mDialog = new Dialog(mContext, R.style.dialog);  
        LayoutInflater in = LayoutInflater.from(mContext);  
        View viewDialog = in.inflate(R.layout.dialog, null);  
        TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("���Ե�");
        viewDialog.setBackgroundColor(0x7f000000);  
        viewDialog.setAlpha(0.5f);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        // �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
        // LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
        // mDialog.setContentView(viewDialog, layoutParams);  
        mDialog.setContentView(viewDialog);  
        mDialog.setCanceledOnTouchOutside(false);  
        return mDialog;  
    }  
    @SuppressLint("NewApi")
    public Dialog updateVersion()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("���ڻ�ȡ���°汾��Ϣ");
    	viewDialog.setBackgroundColor(0x7f000000);  
    	viewDialog.setAlpha(0.5f);
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(false);  
    	return mDialog;  
    }  
    @SuppressLint("NewApi")
	public Dialog xiadan()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("�����µ�");
    	viewDialog.setBackgroundColor(0x7f000000);  
    	viewDialog.setAlpha(0.5f);
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(false);  
    	return mDialog;  
    }  
    @SuppressLint("NewApi")
	public Dialog tuikuan()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("�˿���");
    	viewDialog.setBackgroundColor(0x7f000000);  
    	viewDialog.setAlpha(0.5f);
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(false);  
    	return mDialog;  
    }  
    @SuppressLint("NewApi")
	public Dialog loadDialog2()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog1);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("��¼��");
    	viewDialog.setBackgroundColor(0x7f000000); 
    	viewDialog.setAlpha(0.9f);
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(false);  
    	return mDialog;  
    }  
    public Dialog loadDialog3()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog1);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("�ף����ڼ�����");
    	viewDialog.setBackgroundColor(0x7f000000);  
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(true);  
    	mDialog.show();  
    	return mDialog;  
    }  
    public Dialog loadplace()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog1);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("�ף����ڶ�λ��");
    	viewDialog.setBackgroundColor(0x7f000000);  
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(true);  
    	mDialog.show();  
    	return mDialog;  
    }  
    public Dialog loadLesson()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog1);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("����������");
    	viewDialog.setBackgroundColor(0x7f000000);  
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(true);  
    	mDialog.show();  
    	return mDialog;  
    }  
    @SuppressLint("NewApi")
	public Dialog loadupload()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog1);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("������");
    	viewDialog.setAlpha(0.9f);
    	viewDialog.setBackgroundColor(0x7f000000);  
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(true);  
    	mDialog.show();  
    	return mDialog;  
    }  
    public Dialog loaduploadFile()  
    {  
    	mDialog = new Dialog(mContext, R.style.dialog1);  
    	LayoutInflater in = LayoutInflater.from(mContext);  
    	View viewDialog = in.inflate(R.layout.dialog, null);  
    	TextView textView=(TextView) viewDialog.findViewById(R.id.textView);
    	textView.setText("�����ϴ�");
    	viewDialog.setBackgroundColor(0x7f000000);  
    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	// �����������dialog�Ĵ�С����ȻҲ��������dialog title��  
    	// LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);  
    	// mDialog.setContentView(viewDialog, layoutParams);  
    	mDialog.setContentView(viewDialog);  
    	mDialog.setCanceledOnTouchOutside(true);  
    	mDialog.show();  
    	return mDialog;  
    }  
  
    public void removeDialog()  
    {  
        mDialog.dismiss();  
    }  
    
}
