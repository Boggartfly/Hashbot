package code.laerstudios.hashbot;





import com.google.android.gms.ads.*;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import code.laerstudios.hashbot.R;
public class MainActivity extends Activity {
	 private AdView adView;
	 private static final String MY_AD_UNIT_ID="";
	 private static final String TAG = "Adbuild";
	 private InterstitialAd interstitial;
	 final Context context = this;
	 SharedPreferences prefs = null;
	 String name;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		 if (prefs.getBoolean("firstrun", true)) {
	            // Do first run stuff here then set 'firstrun' as false
	            // using the following line to edit/commit prefs
			 
			 AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        	builder.setMessage(R.string.helpmessage)
				       .setCancelable(false)
				       .setIcon(R.drawable.ic_launcher)
				       .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       }).show();
	            prefs.edit().putBoolean("firstrun", false).commit();
	        }
	    
		interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId(MY_AD_UNIT_ID);
	    
		

	                
		adView = new AdView(this);
	    adView.setAdUnitId(MY_AD_UNIT_ID);
	    adView.setAdSize(AdSize.BANNER);
	    RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
	    layout.addView(adView);
	    AdRequest adRequest = new AdRequest.Builder().addTestDevice("E05D4CDEB622522AFBFB3FC12EA4677C").build();
	    AdRequest adBannerRequest = new AdRequest.Builder().addTestDevice("E05D4CDEB622522AFBFB3FC12EA4677C").build();
	    Log.v(TAG, "(ads) isTestDevice=" + adRequest.isTestDevice(this));
	    
	    interstitial.loadAd(adRequest);
	    adView.loadAd(adBannerRequest);
	    interstitial.setAdListener(new AdListener() {
	        @Override
	        public void onAdLoaded() {
	        	prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
	        	
	        	if (prefs.getBoolean("firstrun", false)==false) {
		            // Do first run stuff here then set 'firstrun' as false
		            // using the following line to edit/commit prefs
	        		interstitial.show();
				        }
	        	
	        }

	        @Override
	        public void onAdFailedToLoad(int errorCode) {
	         
	        }
	      });
	    
	  
	 }
	

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    case R.id.upgrade:
        {	
        	

        	Uri uri = Uri.parse("market://details?id=code.laerstudios.hashbotpro");
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
            }
	      

	       
       return true;
        }    
	    
	    case R.id.opengit:
	            {
	            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://boggartfly.github.io/Hashbot"));
	            	startActivity(browserIntent);
	            	return true;
	            }
	        
	    case R.id.rate:
	        { 
	        	Uri uri = Uri.parse("market://details?id=" + getPackageName());
	            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
	            try {
	                startActivity(myAppLinkToMarket);
	            } catch (ActivityNotFoundException e) {
	                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
	            }
	        return true;
	        }
	    case R.id.help:
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        	builder.setMessage(R.string.helpmessage)
			       .setCancelable(false)
			       .setIcon(R.drawable.ic_launcher)
			       .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       }).show();
        	return true;
        }
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
		         name=data.getStringExtra("filename");
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
	         String sha1=data.getStringExtra("sha1");
	         String sha256=data.getStringExtra("sha256");
	         String sha512=data.getStringExtra("sha512");
	         String md5=data.getStringExtra("md5");
	          
	         TextView hashtext=(TextView) findViewById(R.id.text_id);
	        if(hashtext!=null)
	         {
	        	hashtext.setText("File Selected is: "+name+"\nSHA-1 hash:-\n"+sha1+"\n\n"+"SHA-256 hash:-\n"+sha256+"\n\n"+"SHA-512 hash:-\n"+sha512+"\n\n"+"MD5 hash:-\n"+md5);
	        	Button p1_button = (Button)findViewById(R.id.button1);
				p1_button.setText("Select Another File");	    
	         }
	        else
	        	Toast.makeText(this, "hashtext is null", Toast.LENGTH_SHORT).show();	
		 }
	     if (resultCode == RESULT_CANCELED) {    
	         //Write your code if there's no result
	     }
	  }
	//onActivityResult
	}
	

	  }