package com.unlimitedcompanies.comsClient.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController
{
	@RequestMapping("/webServiceTest")
	public ModelAndView displayTest()
	{
		return new ModelAndView("test.jsp");
	}
}
