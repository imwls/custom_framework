package com.sunmi.internal.telephony;

import android.util.Log;
import android.os.Environment;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.android.internal.util.XmlUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

/**
 * @author: liuqiong
 * @date:2017-09-21
 * @note:Telephony tools class
 */
public class TelephonyUtils {
	private static final String SUNMI_APNS_PATH = "etc/sunmi-apns-conf.xml";

        /**
        * @note:Get apn config xml
        * @param
        * @return File
        */
	public static XmlPullParser  getAPNConfigXml() {
                File apn = new File(Environment.getRootDirectory(), SUNMI_APNS_PATH);
        
                if (apn.exists())  {
                    try {
                        FileReader confreader = new FileReader(apn);
                        return getXmlParser(confreader);
                    } catch (FileNotFoundException e) {
                    }
                } else  {
                    Log.d("JerryLiu", "TelephonyUtils getAPNConfigXml sunmi apns file not found"); 
                }

                return null;
	}

        /**
        * @note:Get xml parser
        * @param:File
        * @return xml
        */
        private static XmlPullParser getXmlParser(FileReader confreader)   {
            XmlPullParser confparser = null;
            try {
                confparser = Xml.newPullParser();
                confparser.setInput(confreader);
                XmlUtils.beginDocument(confparser, "apns");

            } catch (Exception e) {
                Log.e("JerryLiu", "Exception while parsing apns xml file" + e);
            }

            return confparser;
        }

}
