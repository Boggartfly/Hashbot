package code.laerstudios.hashcheck;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import code.laerstudios.hashbot.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("unused")
public class DigestUtil extends Activity {
	 
	private static final String TAG = "hashing";
	
	 public  String hashes;
	 public DigestUtil() {
		// TODO Auto-generated constructor stub
	}
	 public  void hashSHA1( File file) {
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
				//Toast.makeText(this, "Hash  is:  "+output, Toast.LENGTH_SHORT).show();
				Intent returnintent = new Intent(this, MainActivity.class);
				  returnintent.putExtra("hashes", hashes);
				  setResult(RESULT_OK,returnintent);
				  finish();
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
	 
		
		 @Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				Intent intent=getIntent();
				String receivedpath=intent.getStringExtra("path");
				File filetobehashed = new File(receivedpath);
				hashSHA1(filetobehashed);
				
				  
				 
			}
		
}
