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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


public class DigestUtil extends Activity {
	 
	private static final String TAG = "hashing";
	
	
	 public String md5;
	 public String sha1;
	 public String sha256;
	 public String sha512;
    // private ProgressDialog mProgress;
    

    
	 public DigestUtil() {
		// TODO Auto-generated constructor stub
	}
	 public void hashMD5( File file){
		 MessageDigest digest;
		
			try {
				digest = MessageDigest.getInstance("MD5");
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
		 
			byte[] buffer = new byte[32768];
			int read;
			try {
				while ((read = is.read(buffer)) > 0) {
					digest.update(buffer, 0, read);
				}
				byte[] md5sum = digest.digest();
				
				//convert the byte to hex format method 1
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < md5sum.length; i++) {
		          sb.append(Integer.toString((md5sum[i] & 0xff) + 0x100, 16).substring(1));
		        }
				md5=sb.toString();
				Log.i(TAG, "Generated: " + md5);
				
				//Toast.makeText(this, "Hash  is:  "+output, Toast.LENGTH_SHORT).show();
				
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
	 public  void hashSHA1( File file) {
		  	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-1");
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
		 
			byte[] buffer = new byte[32768];
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
				sha1=output;
				//Toast.makeText(this, "Hash  is:  "+output, Toast.LENGTH_SHORT).show();
				
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
	 public  void hashSHA256( File file) {
		  	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-256");
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
		 
			byte[] buffer = new byte[32768];
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
				sha256=output;
				//Toast.makeText(this, "Hash  is:  "+output, Toast.LENGTH_SHORT).show();
				
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
	 public  void hashSHA512( File file) {
		  	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-512");
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
		 
			byte[] buffer = new byte[32768];
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
				sha512=output;
				//Toast.makeText(this, "Hash  is:  "+output, Toast.LENGTH_SHORT).show();
				
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
			/*	mProgress = new ProgressDialog(this);
				mProgress.setMessage("Calculating Hashes ");
				mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgress.setIndeterminate(true);
				mProgress.show();*/
				Intent intent=getIntent();
				String receivedpath=intent.getStringExtra("path");
				File filetobehashed = new File(receivedpath);
				
				hashSHA1(filetobehashed);
				hashSHA256(filetobehashed);
				hashSHA512(filetobehashed);
				hashMD5(filetobehashed);
				//mProgress.setProgress(100);
				Intent returnintent = new Intent(this, MainActivity.class);
				  returnintent.putExtra("sha1", sha1);
				  returnintent.putExtra("sha256", sha256);
				  returnintent.putExtra("sha512", sha512);
				  returnintent.putExtra("md5", md5);
				  
				  setResult(RESULT_OK,returnintent);
				 // mProgress.dismiss();
				  finish(); 
				 
			}
		
}
