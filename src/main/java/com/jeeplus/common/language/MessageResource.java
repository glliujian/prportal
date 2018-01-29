package com.jeeplus.common.language;

/**
* messageSource自定义代码
*/

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.jeeplus.common.web.Servlets;
import com.jeeplus.modules.sys.utils.DictUtils;


public class MessageResource extends ReloadableResourceBundleMessageSource implements ResourceLoaderAware
{
    @SuppressWarnings("unused")
    private ResourceLoader resourceLoader;
    

    public MessageResource(){
    }

    /**
     * 
     * 描述：TODO
     * @param code   
     * @param locale 本地化语言
     * @return
     */
    private String getText(String code, Locale locale) {
    	return DictUtils.getLanguageLabel(code,locale.getLanguage());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader)
    {
        this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale)
    {
        String msg = getText(code, locale);
        MessageFormat result = createMessageFormat(msg, locale);
        return result;
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String result = getText(code, locale);
        return result;
    }
    
    public String getLanguageLabel(String text){
    	Locale locale = RequestContextUtils.getLocaleResolver(Servlets.getRequest()).resolveLocale(Servlets.getRequest());
		return this.getMessage(text,null,locale);
    }
}