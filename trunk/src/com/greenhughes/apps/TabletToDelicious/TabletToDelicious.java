/**
 * Tablet To Delicious
 * A simple app to make the full save bookmark to Delicious
 * web page available via the Share menu on Android tablets
 * @author Liam Green-Hughes
 * greenhughes.com
 * July 2011
 */

package com.greenhughes.apps.TabletToDelicious;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main activity class
 */
public class TabletToDelicious extends Activity {
	TextView authorSite; // the author and site line on the information page
	Button btnDone; // done/close button
	
    /** Called when the activity is first created. 
     * Will act differently depending on whether sharing or
     * displaying information page. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        // Get information about the call to start this activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();
        
        // Find out if Sharing or if app has been launched from icon
        if (action.equals(Intent.ACTION_SEND)) {
        	// ACTION_SEND is called when sharing, get the title and URL from 
        	// the call
        	String pageUrl = extras.getString("android.intent.extra.TEXT");
			String pageTitle = extras.getString("android.intent.extra.SUBJECT");
			// Some apps do not provide the page title, so in this case use the URL again
            if (pageTitle == null) {
            	pageTitle = pageUrl;
            }
            // Start to build the Delicious URL
			Uri.Builder deliciousSaveUrl = Uri.parse("http://www.delicious.com").buildUpon();
			deliciousSaveUrl.appendPath("save");
			// Add the parameters from the call
			deliciousSaveUrl.appendQueryParameter("url", pageUrl);
			deliciousSaveUrl.appendQueryParameter("title", pageTitle);
			deliciousSaveUrl.appendQueryParameter("notes", "");
			deliciousSaveUrl.appendQueryParameter("v", "6");
			deliciousSaveUrl.appendQueryParameter("jump", "yes");
			
			// Load the constructed URL in the browser
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(deliciousSaveUrl.build());
			// If user has more then one browser installed give them a chance to
			// select which one they want to use 
			startActivity(Intent.createChooser(i, getString(R.string.which_browser)));
			// That is all this app needs to do, so call finish()
			this.finish();
        }
        else {
        	// app has been launched from menu - show information window
        	setContentView(R.layout.main);
        	// Make the author/site line clickable and launch my website
        	authorSite = (TextView)findViewById(R.id.authorSite);
            authorSite.setOnClickListener(new OnClickListener() {
     			public void onClick(View v) {
     				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.greenhughes.com"));
     				startActivity(Intent.createChooser(i, getString(R.string.which_browser)));
     			}
            });
            // handle done/close button
            btnDone = (Button)findViewById(R.id.btnDone);
            btnDone.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// close the app
					TabletToDelicious.this.finish();
				}
            });
        }
    }
}