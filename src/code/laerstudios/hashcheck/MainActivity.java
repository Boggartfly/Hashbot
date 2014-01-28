package code.laerstudios.hashcheck;

import com.android.vending.billing.IInAppBillingService;




import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import code.laerstudios.hashbot.R;
public class MainActivity extends Activity {
	 private AdView adView;
	 private static String MY_AD_UNIT_ID="a152e534110e222";
	 private static final String TAG = "Adbuild";
	 IInAppBillingService mService;
	 boolean mIsPremium = false;
	 static final String SKU_PREMIUM = "remove_ads";
	// (arbitrary) request code for the purchase flow
	    static final int RC_REQUEST = 10001;
	    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAykonGWWeTtVsx2stMLwHAvBkXZNuefHLluS9WNibi9wveezPU/hI64bXrhTsPaDlJyAcS1pbtlindNbOqieoPaIGWttDN5lX/oK0KfR5/Ffo5+HOqsai0Z2EqoYx25NJlrCZYgpzklaXTsLAhs4bJEwt2LvJJhqr1fcCJJ0GCMVXHAhQR3MtHNBdxliXLCZMZnO2S4ndKO9vhf8BgGQcaOdW4qBAZvqnEyDknWJRqaAYcXZsRUAaa9e46RYpOxQI6M9cHjjL5V7B6hzhESBpWwhIN+VWIUJgLKtuC/p13r6eiokA/AZz7lBGpoxdCqUajQMqRRREEGYPOMvC3yE3JwIDAQAB";
	    // The helper object
	    IabHelper mHelper;
	 ServiceConnection mServiceConn = new ServiceConnection() {
	    @Override
	    public void onServiceDisconnected(ComponentName name) {
	        mService = null;
	    }

	    @Override
	    public void onServiceConnected(ComponentName name, 
	       IBinder service) {
	        mService = IInAppBillingService.Stub.asInterface(service);
	    }
	 };
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);
		
		
		  mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
	            public void onIabSetupFinished(IabResult result) {
	                Log.d(TAG, "Setup finished.");

	                if (!result.isSuccess()) {
	                    // Oh noes, there was a problem.
	                   // complain("Problem setting up in-app billing: " + result);
	                    return;
	                }

	                // Have we been disposed of in the meantime? If so, quit.
	                if (mHelper == null) return;

	                // IAB is fully set up. Now, let's get an inventory of stuff we own.
	                Log.d(TAG, "Setup successful. Querying inventory.");
	                mHelper.queryInventoryAsync(mGotInventoryListener);
	            }
	        }); 
	 if(mIsPremium!=true){
		adView = new AdView(this);
	    adView.setAdUnitId(MY_AD_UNIT_ID);
	    adView.setAdSize(AdSize.BANNER);
	    RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
	    layout.addView(adView);
	    AdRequest adRequest = new AdRequest.Builder().addTestDevice("E05D4CDEB622522AFBFB3FC12EA4677C").build();
	    //Log.v(TAG, "(ads) isTestDevice=" + adRequest.isTestDevice(this));
	    adView.loadAd(adRequest);
	 }
	}
	// Listener that's called when we finish querying the items and subscriptions we own
	    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
	        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
	            Log.d(TAG, "Query inventory finished.");

	            // Have we been disposed of in the meantime? If so, quit.
	            if (mHelper == null) return;

	            // Is it a failure?
	            if (result.isFailure()) {
	                complain("Failed to query inventory: " + result);
	                return;
	            }

	            Log.d(TAG, "Query inventory was successful.");

	            /*
	             * Check for items we own. Notice that for each purchase, we check
	             * the developer payload to see if it's correct! See
	             * verifyDeveloperPayload().
	             */

	            // Do we have the premium upgrade?
	            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
	            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
	            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

	           

	            updateUi();
	            setWaitScreen(false);
	            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
	        }
	    };

	
	 public void buy(){
		 
	 }
	 
	 @Override
	  public void onPause() {
	  //  adView.pause();
	    super.onPause();
	  }

	  @Override
	  public void onResume() {
	    super.onResume();
	   // adView.resume();
	  }

	  @Override
	  public void onDestroy() {
	   // adView.destroy();
	    super.onDestroy();
	    if (mService != null) {
	        unbindService(mServiceConn);
	    }   
	    // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
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
        	setWaitScreen(true);

	        /* TODO: for security, generate your payload here for verification. See the comments on
	         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
	         *        an empty string, but on a production app you should carefully generate this. */
	        String payload = "";

	        mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
	                mPurchaseFinishedListener, payload);
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
	         String sha1=data.getStringExtra("sha1");
	         String sha256=data.getStringExtra("sha256");
	         String sha512=data.getStringExtra("sha512");
	         String md5=data.getStringExtra("md5");
	          
	         TextView hashtext=(TextView) findViewById(R.id.text_id);
	        if(hashtext!=null)
	         {
hashtext.setText("SHA-1 hash:-\n"+sha1+"\n\n"+"SHA-256 hash:-\n"+sha256+"\n\n"+"SHA-512 hash:-\n"+sha512+"\n\n"+"MD5 hash:-\n"+md5);
	    
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

	    // User clicked the "Upgrade to Premium" button.
	    

	    // "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
	    // flow for subscription.
	  

	   
	    /** Verifies the developer payload of a purchase. */
	    boolean verifyDeveloperPayload(Purchase p) {
	       // String payload = p.getDeveloperPayload();

	        /*
	         * TODO: verify that the developer payload of the purchase is correct. It will be
	         * the same one that you sent when initiating the purchase.
	         *
	         * WARNING: Locally generating a random string when starting a purchase and
	         * verifying it here might seem like a good approach, but this will fail in the
	         * case where the user purchases an item on one device and then uses your app on
	         * a different device, because on the other device you will not have access to the
	         * random string you originally generated.
	         *
	         * So a good developer payload has these characteristics:
	         *
	         * 1. If two different users purchase an item, the payload is different between them,
	         *    so that one user's purchase can't be replayed to another user.
	         *
	         * 2. The payload must be such that you can verify it even when the app wasn't the
	         *    one who initiated the purchase flow (so that items purchased by the user on
	         *    one device work on other devices owned by the user).
	         *
	         * Using your own server to store and verify developer payloads across app
	         * installations is recommended.
	         */

	        return true;
	    }

	    // Callback for when a purchase is finished
	    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
	        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
	            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

	            // if we were disposed of in the meantime, quit.
	            if (mHelper == null)
	            	{return;
	            	}
	            if (result.isFailure()) {
	                //complain("Error purchasing: " + result);
	                //setWaitScreen(false);
	                return;
	            }
	            if (!verifyDeveloperPayload(purchase)) {
	             //   complain("Error purchasing. Authenticity verification failed.");
	             //   setWaitScreen(false);
	                return;
	            }

	            Log.d(TAG, "Purchase successful.");

	           
	             if (purchase.getSku().equals(SKU_PREMIUM)) {
	                // bought the premium upgrade!
	                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
	               // alert("Thank you for upgrading to premium!");
	                mIsPremium = true;
	              //  updateUi();
	               // setWaitScreen(false);
	            }
	           
	        }
	    };

	    // Called when consumption is complete
	    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
	        public void onConsumeFinished(Purchase purchase, IabResult result) {
	            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

	           
	        }
	    };

	    
	    

	   

	    // updates UI to reflect model
	    public void updateUi() {

	        // "Upgrade" button is only visible if the user is not premium
	        //findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);

	    }

	    // Enables or disables the "please wait" screen.
	    void setWaitScreen(boolean set) {
	      //  findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
	        //findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
	    }

	    void complain(String message) {
	        Log.e(TAG, "****  Error: " + message);
	        alert("Error: " + message);
	    }

	    void alert(String message) {
	        AlertDialog.Builder bld = new AlertDialog.Builder(this);
	        bld.setMessage(message);
	        bld.setNeutralButton("OK", null);
	        Log.d(TAG, "Showing alert dialog: " + message);
	        bld.create().show();
	    }

	    void saveData() {

	        /*
	         * WARNING: on a real application, we recommend you save data in a secure way to
	         * prevent tampering. For simplicity in this sample, we simply store the data using a
	         * SharedPreferences.
	         */

	        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
	       
	        spe.commit();
	        Log.d(TAG, "Saved data: tank = " );
	    }

	    void loadData() {
	       // SharedPreferences sp = getPreferences(MODE_PRIVATE);
	        
	        Log.d(TAG, "Loaded data: tank = " );
	    }

}