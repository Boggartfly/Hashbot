package code.laerstudios.hashcheck;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class DigestUtil extends Activity {
	 
	private static final String TAG = "hashing";
	 Context context;
	  String hashes;
	 public DigestUtil() {
		// TODO Auto-generated constructor stub
	}
	 public static void hashSHA1( File file) {
		  	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA1");
			} catch (NoSuchAlgorithmException e) {
				Log.e(TAG, "Exception while getting Digest", e);
				return;
			}
		 
			InputStream is;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				Log.e(TAG, "Exception while getting FileInputStream", e);
				return;
			}
		 
			byte[] buffer = new byte[8192];
			int read;
			try {
				while ((read = is.read(buffer)) > 0) {
					digest.update(buffer, 0, read);
				}
				byte[] md5sum = digest.digest();
				BigInteger bigInt = new BigInteger(1, md5sum);
				String output = bigInt.toString(16);
				// Fill to 40 chars
				output = String.format("%40s", output).replace(' ', '0');
		        
				
				Log.i(TAG, "Generated: " + output);
				hashes=output;
				
			} catch (IOException e) {
				throw new RuntimeException("Unable to process file for MD5", e);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					Log.e(TAG, "Exception on closing MD5 input stream", e);
				}
			}
		}
	 
		 protected void onActivityResult(int requestCode, int resultCode, Intent data) {

			  if (requestCode == 2) {

			     if(resultCode == RESULT_OK){      
			        
			       finish();  
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
			  }
			//onActivityResult
		
		}//onActivityResult
		 @Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				Intent intent=getIntent();
				String receivedpath=intent.getStringExtra("path");
				//String name=intent.getStringExtra("filename");
				
				File filetobehashed = new File(receivedpath);
				hashSHA1(filetobehashed);
				  Intent returnintent = new Intent(this, MainActivity.class);
				  returnintent.putExtra("hashes", hashes);
				  setResult(RESULT_OK,returnintent);
				  startActivity(returnintent);
				  finish();
				 
			}
		
}
