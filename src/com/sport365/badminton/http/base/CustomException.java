package com.sport365.badminton.http.base;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.utils.FileTools;

public class CustomException implements UncaughtExceptionHandler {
	// 获取application 对象；
	private Context mContext;

	private Thread.UncaughtExceptionHandler defaultExceptionHandler;
	// 单例声明CustomException;
	private static CustomException customException;

	private CustomException() {
	}

	public static CustomException getInstance() {
		if (customException == null) {
			customException = new CustomException();
		}
		return customException;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		// TODO Auto-generated method stub
		if (defaultExceptionHandler != null) {
			StringBuffer sb = new StringBuffer();

			Writer writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			exception.printStackTrace(pw);
			Throwable cause = exception.getCause();
			// 循环着把所有的异常信息写入writer中
			while (cause != null) {
				cause.printStackTrace(pw);
				cause = cause.getCause();
			}
			pw.close();// 记得关闭
			String result = writer.toString();
			sb.append(result);

			FileTools.writeObjectToFile(sb, SystemConfig.EXCEPTION_DATADIR,
					SystemConfig.STACKTRACE_DATADIR);
			// 将异常抛出，则应用会弹出异常对话框.这里先注释掉
			defaultExceptionHandler.uncaughtException(thread, exception);
		}
	}

	public void init(Context context) {
		mContext = context;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
}
