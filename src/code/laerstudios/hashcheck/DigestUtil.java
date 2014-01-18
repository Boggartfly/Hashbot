package code.laerstudios.hashcheck;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DigestUtil extends Activity {
	 public static final int BUFFER_SIZE = 2048;
	 String hashes="";
	 public DigestUtil() {
		// TODO Auto-generated constructor stub
	}
	 public static byte[] getDigest(InputStream in, String algorithm) throws Throwable {
		  MessageDigest md = MessageDigest.getInstance(algorithm);
		  try {
		   DigestInputStream dis = new DigestInputStream(in, md);
		   byte[] buffer = new byte[BUFFER_SIZE];
		   while (dis.read(buffer) != -1) {
		    //
		   }
		   dis.close();
		  } finally {
		   in.close();
		  }
		  return md.digest();
		 }

		 public static String getDigestString(InputStream in, String algorithm) throws Throwable {
		  byte[] digest = getDigest(in, algorithm);
		  StringBuilder sb = new StringBuilder();
		  for (int i = 0; i < digest.length; i++) {
		   sb.append(String.format("%x", digest[i]));
		  }
		  return sb.toString();
		 }
		 @Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				File f = new File("/tmp/1.iso.gz");
				  try {
					String SHA_1=("SHA-1: " + getDigestString(new FileInputStream(f), "SHA-1"));
					String SHA_256=("SHA-256: " + getDigestString(new FileInputStream(f), "SHA-256"));
					String MD5=("MD5: " + getDigestString(new FileInputStream(f), "MD5"));
					 hashes=SHA_1+SHA_256+MD5;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  Intent returnintent = new Intent(this, MainActivity.class);
				  returnintent.putExtra("filepath", hashes);
				  setResult(RESULT_OK,returnintent);
				  finish();
			}
		
}
