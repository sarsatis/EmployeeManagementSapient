package com.sapient.employeemanagement.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Sarthak Satish
 * 
 * @class SwaggerConfiguration
 * 
 * @description This is a Configuration File which provides Swagger
 *              configuration
 */
@Configuration
@Controller
public class SwaggerConfiguration {
	/* Swagger Configurations */

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
				.paths(postPaths()).build();
	}

	private Predicate<String> postPaths() {
		return regex("/employees.*");
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Employee Management").description("Employee Management application")
				.contact("sarthak8055@gmail.com").license("License").licenseUrl("sarthak8055@gmail.com")
				.version("1.0").build();
	}

	/**
	 * @method swaggerui
	 * @description This method helps in redirecting to swagger ui
	 * @return swagger webpage
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody ModelAndView swaggerui() {
		return new ModelAndView("redirect:swagger-ui.html");
	}

}
