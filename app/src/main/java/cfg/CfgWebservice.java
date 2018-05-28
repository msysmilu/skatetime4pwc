package cfg;

public class CfgWebservice extends SharedPreferencesManager
{

	private static final String	SPK_WEBSERVER_ADDRESS		= "webserver_address_SPK_string";
	private static final String	SPK_WEBSERVER_USERNAME		= "webserver_username_SPK_string";
	private static final String	SPK_WEBSERVER_PASSWORD		= "webserver_password_SPK_string";

	private static final String	SPK_WEBSERVER_ADDRESS_DEF	= "http://10.0.0.99/";				//	http://10.0.0.89/
	private static final String	SPK_WEBSERVER_USERNAME_DEF	= "admin";							//	admin	
	private static final String	SPK_WEBSERVER_PASSWORD_DEF	= "2bmsapsap";						//	2bmsapsap

	// WEBSERVER ========================================================================================
	// Webserver Address ------------------------------------------------------------
	public String getWebserverAddress() {
		return prefs().getString(SPK_WEBSERVER_ADDRESS, SPK_WEBSERVER_ADDRESS_DEF);
	}

	public void setWebserverAddress(String newVal) {
		editor().putString(SPK_WEBSERVER_ADDRESS, newVal);
		editor().commit();
	}

	// Webserver Username ------------------------------------------------------------
	public String getWebserverUn() {
		return prefs().getString(SPK_WEBSERVER_USERNAME, SPK_WEBSERVER_USERNAME_DEF);
	}

	public void setWebserverUn(String newVal) {
		editor().putString(SPK_WEBSERVER_USERNAME, newVal);
		editor().commit();
	}

	// Webserver Password------------------------------------------------------------
	public String getWebserverPw() {
		return prefs().getString(SPK_WEBSERVER_PASSWORD, SPK_WEBSERVER_PASSWORD_DEF);
	}

	public void setWebserverPw(String newVal) {
		editor().putString(SPK_WEBSERVER_PASSWORD, newVal);
		editor().commit();
	}

}
