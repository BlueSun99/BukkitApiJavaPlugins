package com.bluesun99.simplerpg;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

class LocaleManager {
	private Locale l;
	private ResourceBundle rb;
	private MessageFormat mf = new MessageFormat("");
	private String bundlepath = "SRTranslation";
	
	LocaleManager()
	{
		l = new Locale(SimpleRPGMain.plugin.getConfig().getString("Language"));
		
		SimpleRPGMain.logger.info("Loading Translation Script...");
		
		mf.setLocale(l);
		
		try {
			rb = ResourceBundle.getBundle(bundlepath, l);
			SimpleRPGMain.logger.info(rb.getString("srt_localemanager_loaded"));
		} catch (MissingResourceException e) {
			rb = ResourceBundle.getBundle(bundlepath, Locale.ENGLISH);
			SimpleRPGMain.logger.severe(
					this.format("srt_localemanager_failed", rb.getString("srt_info_language_friendly")));
		}
	}
	
	String format(String key, String... s)
	{
		mf.applyPattern(rb.getString(key));
		return mf.format(s);
	}
	
	Locale getLocale()
	{
		return l;
	}
	
	ResourceBundle getResourceBundle()
	{
		return rb;
	}
	
	String getString(String key)
	{
		return rb.getString(key);
	}
	
}
