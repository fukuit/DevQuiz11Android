package com.google.android.apps.gddquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.apps.gddquiz.IQuizService;
public class MyAppActivity extends Activity {
	private IQuizService quizServiceIf = null;
	private ServiceConnection quizServiceConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			quizServiceIf = IQuizService.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			quizServiceConn = null;
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent intent = new Intent(IQuizService.class.getName());
		bindService(intent, quizServiceConn, BIND_AUTO_CREATE);
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doConnect();
			}
		});
		unbindService(quizServiceConn);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(quizServiceConn);
	}

	private void doConnect() {
		final EditText etext = (EditText) findViewById(R.id.editText1);
		try {
			etext.setText(quizServiceIf.getCode());
		} catch (Exception e) {
			etext.setText(e.toString());
		}
	}
}
