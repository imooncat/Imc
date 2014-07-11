package org.imc.net;

/*
	Author:		imooncat
	Date:		14-7-10
	Version:	1.0
	Function:	Wifi controller.
				Includes:
					open/close WIFI
					get WIFI state
					connect WIFI with/without password
*/

/*
	Need permission statement in AndroidManifest.xml:
		<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
		<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
		<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	 	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

*/

import java.util.*;

import android.net.wifi.*;
import android.net.*;
import android.content.*;
import android.util.*;
import android.os.*;


public class ImcWifi
{
	private final String tag = ".ImcWifi";
	private WifiManager iWifiManager;
	private WifiInfo iWifiInfo;
	private List<ScanResult> iWifiList = null;
	private List<WifiConfiguration> iWifiConf;
	private WifiManager.WifiLock iWifiLock;
	private DhcpInfo iDhcpInfo;
	private Context context;
	
	public final int
		WC_NOPASS = 1,
		WC_WEP = 2,
		WC_WPA = 3;
	
	public ImcWifi(Context c) //Use this pointer to initialize.
	{
		iWifiManager = (WifiManager) c.getSystemService(c.WIFI_SERVICE);
		iWifiInfo = iWifiManager.getConnectionInfo();
		context = c;
	}
	
	public boolean openWifi()
	{
		if(!iWifiManager.isWifiEnabled())
			iWifiManager.setWifiEnabled(true);
		Log.d(tag, "openWifi()-Wifi opened");
		return iWifiManager.isWifiEnabled();
	}
	
	public void closeWifi()
	{
		if(iWifiManager.isWifiEnabled())
			iWifiManager.setWifiEnabled(false);
		Log.d(tag, "closeWifi()-Wifi closed");
	}
	
	public int getWifiState()
	{
		Log.d(tag, "getWifiState()-" + iWifiManager.getWifiState());
		return iWifiManager.getWifiState();
	}
	
	public void acquireWifiLock()
	{
		iWifiLock.acquire();
	}
	
	public void releaseWifiLock()
	{
		if(iWifiLock.isHeld())
			iWifiLock.acquire();
	}
	
	public void createWifiLock(String tag)
	{
		iWifiManager.createWifiLock(tag);
		Log.d(tag, "creareWifiLock(" + tag + ")");
	}
	
	public List<WifiConfiguration> getWifiConf()
	{
		return iWifiConf;
	}
	
	public List<ScanResult> getWifiList()
	{
		return iWifiList;
	}
	
	public void connectWifiWithConf(int index)
	{
		if(index > iWifiConf.size())
			return ;
		iWifiManager.enableNetwork(iWifiConf.get(index).networkId, true);
	}
	
	public void scanWifi()
	{
		Log.d(tag, "scanWifi()-Result" + iWifiManager.startScan());
		iWifiList = iWifiManager.getScanResults();
		iWifiConf = iWifiManager.getConfiguredNetworks();
	}
	
	public StringBuilder showScanReaults()
	{
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < iWifiList.size(); ++i) {
			str.append("No." + new Integer(i + 1).toString());
			str.append(iWifiList.get(i).toString());
			str.append("\n");
		}
		return str;
	}
	
	public String getMac()
	{
		if(iWifiInfo == null)
			return null;
		return iWifiInfo.getMacAddress();
	}
	
	public String getBSSID()
	{
		return iWifiInfo.getBSSID();
	}
	
	public DhcpInfo getDhcpInfo()
	{
		return iDhcpInfo = iWifiManager.getDhcpInfo();
	}
	
	public int getIp()
	{
		return iWifiInfo.getIpAddress();
	}
	
	public int getNetworkId()
	{
		return iWifiInfo.getNetworkId();
	}
	
	public WifiInfo getWifiInfo()
	{
		return iWifiInfo = iWifiManager.getConnectionInfo();
	}
	
	public boolean addNetwork(WifiConfiguration wc)
	{
		int id = iWifiManager.addNetwork(wc);
		return iWifiManager.enableNetwork(id, true);
	}
	
	public boolean disconnectWifi(int netId)
	{
		iWifiManager.disableNetwork(netId);
		return iWifiManager.disconnect();
	}
	
	private WifiConfiguration exists(String SSID) {
		List<WifiConfiguration> existingConfigs = iWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs)
			if (existingConfig.SSID.equals("\"" + SSID + "\""))
				return existingConfig;
		return null;
	}
	
	public WifiConfiguration createWifiInfo(String SSID, String Password, int Type)
	{
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = exists(SSID);

		if (tempConfig != null)
			iWifiManager.removeNetwork(tempConfig.networkId);

		if (Type == 1) { // WC_NOPASS
		 	config.wepKeys[0] = "";
	 	 	config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) { // WC_WEP
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) { // WC_WPA
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}
	
	public void connectWifi(final String ssid, final String pass, final int type)
	{
		addNetwork(createWifiInfo(ssid, pass, type));
	}
	
	public boolean isConnected()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifiNetworkInfo.isConnected())
			return true ;
		return false ;
	}
}
