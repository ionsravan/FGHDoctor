package com.ambitious.fghdoctor.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//implementation 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2'


public class Utility {

    public static Context appContext;
    private static String PREFERENCE = "FGHDoctor";
    private static int MAX_IMAGE_DIMENSION = 720;
//    public static Set<String>sets = new HashSet<>();


    // for username string preferences
    public static void setSharedPreference(Context context, String name,
                                           String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static void savetoCart(Context context, String name, List<String> carts) {

        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> sets = new HashSet<>();
        sets.addAll(carts);
        editor.putStringSet(name, sets);
        editor.commit();
    }


    // for username string preferences
    public static void setIntegerSharedPreference(Context context, String name,
                                                  int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }

    //Drawable
    public static void setDrawableSharedPreference(Context context, String name,
                                                   int value) {
        appContext = context;
        SharedPreferences settings = context
                .getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public static Set<String> getSaveCart(Context context, String name) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFERENCE, 0);
        return settings.getStringSet(name, null);
    }

    public static String getUseridSharedPreferences(Context context, String name) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "0");
    }

    public static int getIngerSharedPreferences(Context context, String name) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFERENCE, 0);
        return settings.getInt(name, 1);
    }

    public static void setSharedPreferenceBoolean(Context context, String name,
                                                  boolean value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission") NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetooth = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        NetworkInfo wimax = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

        if (wifi == null && mobile == null && bluetooth == null && wimax == null) {
            return false;
        }

        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        if (bluetooth != null && bluetooth.isConnected()) {
            return true;
        }

        if (wimax != null && wimax.isConnected()) {
            return true;
        }

        return false;
    }


    public static boolean getSharedPreferencesBoolean(Context context, String name, boolean default_Val) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getBoolean(name, default_Val);
    }

    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /*public static Drawable getImageFromURL(String photoDomain) {
        Drawable drawable = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(photoDomain.trim());
            HttpResponse response = httpClient.execute(request);
            InputStream is = response.getEntity().getContent();
            drawable = Drawable.createFromStream(is, "src");
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return drawable;
    }
    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }*/
    /*public static String postParamsAndfindJSON(String url,
                                               ArrayList<NameValuePair> params) {
        String result = null;
        int TIMEOUT_MILLISEC = 100000; // = 10 seconds
//        System.out.println("URL comes in jsonparser class is:  " + url);
        try {
            URL myurl=new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) myurl
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(TIMEOUT_MILLISEC);
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
            InputStream is=urlConnection.getInputStream();
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                } finally {
                    is.close();
                }
                result = sb.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }*/
	/*public static String postParamsAndfindJSON(String url,
                                               ArrayList<NameValuePair> params) {
		// TODO Auto-generated method stub
//		JSONObject jObj = new JSONObject();
		String result = "";

		System.out.println("URL comes in jsonparser class is:  " + url);
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
//			String result = null;
//        System.out.println("URL comes in jsonparser class is:  " + url);
			try {
				URL myurl=new URL(url);
				HttpURLConnection urlConnection = (HttpURLConnection) myurl
						.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoInput(true);
				urlConnection.connect();
				InputStream is=urlConnection.getInputStream();
				if (is != null) {
					StringBuilder sb = new StringBuilder();
					String line;
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is));
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}
						reader.close();
					} finally {
						is.close();
					}
					result = sb.toString();
				}
			}catch (Exception e){
				result=null;
			}
			return result;
		*//*	HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			 UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
//			    entity.setContentEncoding(HTTP.UTF_8);
//			    entity.setContentType("text/html");

//			httpPost.setEntity(new UrlEncodedFormEntity(params));
			// httpGet.setURI(new URI(url));
//			    httpPost.setHeader("Content-Type", "application/json");
//			    httpPost.setHeader("Accept-Encoding", "text/html");
			  httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int status = httpResponse.getStatusLine().getStatusCode();

			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}

			is.close();
			result = sb.toString();

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			e.printStackTrace();
			return null;
		}
		return result;*//*
	}
*/

    public static Bitmap DownloadImageDirect(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        // String imageEncoded = Base64Coder.encodeTobase64(image);

        // Log.d("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static void alertBoxShow(Context context, int msg) {
        // set type_dialog for user's current location set for searching theaters.
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Alert");
        dialog.setMessage(msg);
        dialog.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void ShowStringAlertWithMessage(Context context, int alerttitle,
                                                  int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Cancel the type_dialog
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void ShowStringAlert2WithMessage(final Context context, int alerttitle,
                                                   int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("GET STARTED", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Cancel the type_dialog
                dialog.cancel();
                //((HomeActivity) context).moveToNextActivity("GET STARTED");
                //Intent intent=new Intent(context,GetStartedActivity.class);
            }
        });
        builder.setNegativeButton("FAQ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Cancel the type_dialog
                dialog.cancel();
                //((HomeActivity) context).moveToNextActivity("FAQ");
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String findJSONFromUrl(String url) {
		/*// TODO Auto-generated method stub
		JSONObject jObj = new JSONObject();
		String result = "";

		System.out.println("URL comes in jsonparser class is:  " + url);
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			// httpGet.setURI(new URI(url));
			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
// Set verifier
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int status = httpResponse.getStatusLine().getStatusCode();

			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}

			is.close();
			result = sb.toString();
			System.out.println("result  in jsonparser class ........" + result);

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			e.printStackTrace();
			return null;
		}
		return result;*/
        String result = null;
//        System.out.println("URL comes in jsonparser class is:  " + url);
        try {
            URL myurl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) myurl
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                } finally {
                    is.close();
                }
                result = sb.toString();
            }
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static Bitmap getBitmap(String url) {
        Bitmap imageBitmap = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            try {
                imageBitmap = BitmapFactory
                        .decodeStream(new FlushedInputStream(is));
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                System.out.println("exception in get bitma putility");
            }

            bis.close();
            is.close();
            final int IMAGE_MAX_SIZE = 50;
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                // b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = imageBitmap.getHeight();
                int width = imageBitmap.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap,
                        (int) x, (int) y, true);
                imageBitmap.recycle();
                imageBitmap = scaledBitmap;

                System.gc();
            } else {
                // b = BitmapFactory.decodeStream(in);
            }

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            System.out.println("exception in get bitma putility");
        } catch (Exception e) {
            System.out.println("exception in get bitma putility");
            e.printStackTrace();
        }
        return imageBitmap;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
    }

    public static byte[] scaleImage(Context context, Uri photoUri)
            throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = 0;// getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION
                || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth)
                    / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight)
                    / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*
         * if (type.equals("image/png")) {
         * srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); } else if
         * (type.equals("image/jpg") || type.equals("image/jpeg")) {
         * srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); }
         */
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }

    static int mMaxWidth, mMaxHeight;

    public static Bitmap loadResizedImage(Context mContext, final File imageFile) {
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mMaxWidth = display.getWidth();
        mMaxHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        int scale = calculateInSampleSize(options, mMaxWidth, mMaxHeight);
        while (options.outWidth / scale > mMaxWidth
                || options.outHeight / scale > mMaxHeight) {
            scale++;
        }
        Bitmap bitmap = null;
        Bitmap scaledBitmap = null;
        if (scale > 1) {
            try {
                scale--;
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPurgeable = true;
                options.inTempStorage = new byte[16 * 100];
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
                        options);
                if (bitmap == null) {
                    return null;
                }

                // resize to desired dimensions
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double newWidth;
                double newHeight;
                if ((double) width / mMaxWidth < (double) height / mMaxHeight) {
                    newHeight = mMaxHeight;
                    newWidth = (newHeight / height) * width;
                } else {
                    newWidth = mMaxWidth;
                    newHeight = (newWidth / width) * height;
                }

                scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                        Math.round((float) newWidth),
                        Math.round((float) newHeight), true);
                bitmap.recycle();
                bitmap = scaledBitmap;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }
            System.gc();
        } else {
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }

        return rotateImage(bitmap, imageFile);
    }

    public static Bitmap rotateImage(final Bitmap bitmap,
                                     final File fileWithExifInfo) {
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = bitmap;
        int orientation = 0;
        try {
            orientation = getImageOrientation(fileWithExifInfo
                    .getAbsolutePath());
            if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation, (float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static int getImageOrientation(final String file) throws IOException {
        ExifInterface exif = new ExifInterface(file);
        int orientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    public static StateListDrawable setSelector(Context context, int DrwlPressed, int DrwlFocused, int DrwlNormal) {

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, context.getResources().getDrawable(DrwlPressed));
        states.addState(new int[]{android.R.attr.state_focused}, context.getResources().getDrawable(DrwlFocused));
        states.addState(new int[]{}, context.getResources().getDrawable(DrwlNormal));

        return states;
    }


    public static Typeface Appttf(Context context, String name) {
        return Typeface.createFromAsset(context.getAssets(), name);
    }

	/* public static boolean isConnectingToInternet(Context context){
	        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null)
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null)
	                  for (int i = 0; i < info.length; i++)
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }

	          }
	          return false;
	    }
	 */

    public static class CustomComparator implements Comparator<JSONObject> {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            double first = 0;
            double second = 0;
            try {
                first = o1.getDouble("distance_km");
                second = o2.getDouble("distance_km");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return (first < second) ? -1 : (first < second) ? 1 : 0;

        }
    }

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }


/*

public static JSONArray saveCartList(ArrayList<JSONObject> ArrayList,Context appContext) {
		// TODO Auto-generated method stub
		JSONArray array=new JSONArray();
		try {
			for (int i = 0; i < ArrayList.size(); i++) {
				array.put(ArrayList.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Utility.setSharedPreference(appContext, Constants.CART_LIST, array.toString());
		return array;
	}


public static JSONArray saveMsgList(ArrayList<JSONObject> ArrayList,Context appContext) {
	// TODO Auto-generated method stub
	JSONArray array=new JSONArray();
	try {
		for (int i = 0; i < ArrayList.size(); i++) {
			array.put(ArrayList.get(i));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	Utility.setSharedPreference(appContext, Constants.LAST_MSG, array.toString());
	return array;
}

public static JSONArray saveLastMsgList(ArrayList<JSONObject> ArrayList,Context appContext) {
	// TODO Auto-generated method stub
	JSONArray array=new JSONArray();
	try {
		for (int i = 0; i < ArrayList.size(); i++) {
			array.put(ArrayList.get(i));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	Utility.setSharedPreference(appContext, Constants.LAST_MSG, array.toString());
	return array;
}



	public static ArrayList<JSONObject> getCartList(Context appContext) {
		// TODO Auto-generated method stub
		ArrayList<JSONObject> ArrayList=new ArrayList<JSONObject>();
		String data=Utility.getSharedPreferences(appContext, Constants.CART_LIST);
		if (data.length()>5) {
			try {
				JSONArray array=new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					ArrayList.add(array.getJSONObject(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ArrayList;
	}

	public static ArrayList<Bean> getLastMsgList(Context appContext) {
		ArrayList<Bean> ArrayList=new ArrayList<Bean>();
		String data=Utility.getSharedPreferences(appContext, Constants.LAST_MSG);
		if (data.length()>0) {
			try {
				JSONArray array=new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					JSONObject casts = (JSONObject)array.getJSONObject(i);
					Bean bean = new Bean();

					bean.setImage(casts.getString("image"));
					bean.setLastmsg(casts.getString("lastmsg"));
					bean.setName(casts.getString("name"));
					bean.setTime(casts.getString("time"));
					bean.setSender_id(casts.getString("sender_id"));
					bean.setReciver_id(casts.getString("reciver_id"));

					ArrayList.add(bean);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ArrayList;
	}
*/


}// final class ends here
