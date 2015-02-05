package br.com.benhurqs.prefetch.maps.ArcGIS.license;

import android.app.Activity;
import br.com.benhurqs.prefetch.R;

import com.esri.core.runtime.LicenseResult;
import com.esri.android.runtime.ArcGISRuntime;

/**
 * 
 * This shows how to set the license level of your ArcGIS application to Basic. Setting the license level to
 * Basic prevents the watermark from appearing on the map. In order to set the license level to Basic you need to edit
 * this code and assign a valid client id string to the CLIENT_ID constant.<p>
 * When you release your app, you should ensure that the client id is encrypted and saved to the device in a 
 * secure manner; this sample uses a hardcoded string instead for simplicity of example code.<p>
 * Follow these steps:
 * 
 * <ol>
 * <li>Browse to https://developers.arcgis.com.</li>
 * <li>Sign in with your ArcGIS developer account.</li>
 * <li>Create an application. This will give you access to a client id string.</li>
 * <li>Initialize the CLIENT_ID constant with the client id string and run the sample. If the license level has been
 * successfully set to Basic you won't see a watermark on the map.<p>
 * <b>NOTE:</b> When you release your app, you should ensure that the client id is encrypted and saved to the 
 * device in a secure manner; the code here uses a hardcoded string instead for simplicity.</li>
 * </ol>
 * 
 */



public class ArcGISLicense {
	
	public static void checkLicense(Activity activity, String CLIENT_ID){
		
		LicenseResult licenseResult = ArcGISRuntime.setClientId(CLIENT_ID);

//	    LicenseLevel licenseLevel = ArcGISRuntime.License.getLicenseLevel();

//	    if (licenseResult == LicenseResult.VALID && licenseLevel == LicenseLevel.BASIC) {
	    if(licenseResult == LicenseResult.VALID){
	      MessageDialogFragment.showMessage(activity.getString(R.string.license_succeeded), activity.getFragmentManager());
	    } else {
	      MessageDialogFragment.showMessage(activity.getString(R.string.license_no_succeeded), activity.getFragmentManager());
	    }
	}

}
