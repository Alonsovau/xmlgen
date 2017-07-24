package com.openacc.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class IndexController {


	// 这里指定是条状的jsp界面
	@RequestMapping(value = "/")
	public String index(Model model) {
		model.addAttribute("xml", "demo");
		return "index";
	}


}