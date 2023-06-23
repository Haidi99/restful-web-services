package com.example.restfulwebservices.helloworld;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.web.JsonPath;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
private MessageSource messageSource;
	
	public HelloWorldController(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	
	@GetMapping(path = "/hello")
	public String helloWorld() {
		
		return "Hello Haidi its nice to see you here!";
	}
	
	@GetMapping(path = "/hello-bean")
	public HellowBean helloWorldBean() {
		
		return new HellowBean("Hello Haidi");
	}
	
	@GetMapping(path = "/hello-bean/path-var/{name}")
	public HellowBean helloWorldBeanPath(@PathVariable String name) {
		
		return new HellowBean(String.format("holaaaa + %s", name));
	}
	
	@GetMapping(path = "/hello-world-internationalized")
	public String helloWorldInternationalized() {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage("good.morning.message", null, "Default Message", locale );
		

	}

}
