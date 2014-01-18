package code.laerstudios.hashcheck;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, FileChooser.class);
		startActivityForResult(intent,1);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         String resultantpath=data.getStringExtra("filepath");
		         if(resultantpath!=null)
		         {	 Intent intent = new Intent(this, DigestUtil.class);
		         	 Toast.makeText(this, "Success Path Obtained! It is:  "+resultantpath, Toast.LENGTH_SHORT).show();
		         	 //startActivityForResult(intent,2);
		         }
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		//onActivityResult
	 if (requestCode == 2) {

		 if(resultCode == RESULT_OK){      
	         String hashes=data.getStringExtra("hashes");
	         TextView txtView = (TextView) findViewById(R.id.text_id);
	         txtView.setText(hashes);
	     }
	     if (resultCode == RESULT_CANCELED) {    
	         //Write your code if there's no result
	     }
	  }
	}//onActivityResult
}
