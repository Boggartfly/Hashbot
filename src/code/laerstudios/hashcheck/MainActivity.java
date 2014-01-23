package code.laerstudios.hashcheck;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import code.laerstudios.hashbot.R;

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
		         String name=data.getStringExtra("filename");
		         if(resultantpath!=null)
		         {	 
		        	 Intent intent = new Intent(this, DigestUtil.class);
		         	 
		         	intent.putExtra("path", resultantpath);
		         	intent.putExtra("filename", name);
		         	 startActivityForResult(intent,2);
		         	 
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
	         TextView hashtext=(TextView) findViewById(R.id.text_id);
	        if(hashtext!=null)
	         {
	        	hashtext.setText("SHA-1 hash:-\n"+hashes);
	     }
	        else
	        	Toast.makeText(this, "hashtext is null", Toast.LENGTH_SHORT).show();	
		 }
	     if (resultCode == RESULT_CANCELED) {    
	         //Write your code if there's no result
	     }
	  }
	}//onActivityResult
}
