package com.zebra.jamesswinton.monitorandtogglenetwork.profilemanager;

import java.util.Map;

public class XML {

    public static final String DISABLE_ETHERNET_PROFILE_NAME = "DisableEthernetEnableWifi";
    public static final String ENABLE_ETHERNET_PROFILE_NAME = "EnableEthernetDisableWifi";


    public static MXProfile getDisableEthernetEnableWifiXML() {
        return new MXProfile(DISABLE_ETHERNET_PROFILE_NAME, "<wap-provisioningdoc>\n" +
                "  <characteristic type=\"Profile\">\n" +
                "    <parm name=\"ProfileName\" value=\"DisableEthernetEnableWifi\"/>\n" +
                "    <characteristic version=\"6.3\" type=\"EthernetMgr\">\n" +
                "      <parm name=\"EthernetState\" value=\"2\" />\n" +
                "      <parm name=\"ConfigIpAddr\" value=\"0\" />\n" +
                "      <parm name=\"SetEthernetProxySettings\" value=\"0\" />\n" +
                "    </characteristic>\n" +
                "    <characteristic version=\"5.2\" type=\"Wi-Fi\">\n" +
                "      <characteristic type=\"System\">\n" +
                "        <parm name=\"WiFiAction\" value=\"enable\" />\n" +
                "      </characteristic>\n" +
                "      <parm name=\"UseRegulatory\" value=\"0\" />\n" +
                "      <parm name=\"UseDiagnosticOptions\" value=\"0\" />\n" +
                "      <parm name=\"UseAdvancedOptions\" value=\"0\" />\n" +
                "      <parm name=\"UseHotspotOptions\" value=\"0\" />\n" +
                "    </characteristic>\n" +
                "  </characteristic>" +
                "</wap-provisioningdoc>");
    }

    public static MXProfile getEnableEthernetDisableWifiProfile() {
        return  new MXProfile(ENABLE_ETHERNET_PROFILE_NAME,
                "<wap-provisioningdoc>\n" +
                "  <characteristic type=\"Profile\">\n" +
                "    <parm name=\"ProfileName\" value=\"EnableEthernetDisableWifi\"/>\n" +
                "    <characteristic version=\"6.3\" type=\"EthernetMgr\">\n" +
                "      <parm name=\"EthernetState\" value=\"1\" />\n" +
                "      <parm name=\"ConfigIpAddr\" value=\"0\" />\n" +
                "      <parm name=\"SetEthernetProxySettings\" value=\"0\" />\n" +
                "    </characteristic>\n" +
                "    <characteristic version=\"5.2\" type=\"Wi-Fi\">\n" +
                "      <characteristic type=\"System\">\n" +
                "        <parm name=\"WiFiAction\" value=\"disable\" />\n" +
                "      </characteristic>\n" +
                "      <parm name=\"UseRegulatory\" value=\"0\" />\n" +
                "      <parm name=\"UseDiagnosticOptions\" value=\"0\" />\n" +
                "      <parm name=\"UseAdvancedOptions\" value=\"0\" />\n" +
                "      <parm name=\"UseHotspotOptions\" value=\"0\" />\n" +
                "    </characteristic>\n" +
                "  </characteristic>" +
                "</wap-provisioningdoc>");
    }

}
