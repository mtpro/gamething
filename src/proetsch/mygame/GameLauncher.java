package proetsch.mygame;

import android.app.Activity;
import android.os.Bundle;

public class GameLauncher extends Activity {
	
	@Override
	public void onCreate(Bundle prevState) {
		super.onCreate(prevState);
		setContentView(R.layout.gamelayout);
		MyGameSurface mGS = (MyGameSurface) findViewById(R.id.gamesurface);
		
		mGS.requestFocus();
	}
	
    protected void onStart() {
    	super.onStart();
    }
    
    protected void onRestart() {
    	super.onRestart();
    }

    protected void onResume() {
    	super.onResume();
    }

    protected void onPause() {
    	super.onPause();
    }

    protected void onStop() {
    	super.onStop();
    }

    protected void onDestroy() {
    	super.onDestroy();
    }
}


