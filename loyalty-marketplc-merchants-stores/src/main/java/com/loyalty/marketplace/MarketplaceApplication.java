package com.loyalty.marketplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;

import com.loyalty.marketplace.audit.Audit;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class MarketplaceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(MarketplaceApplication.class);
	
	public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }
	
	public enum FeatureToggles implements Feature {

		@Label("Batch")
		BATCH;

	}
	
	@Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
	
	 @Bean
		public MessageConverter jacksonJmsMessageConverter() {
			MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
			converter.setTargetType(MessageType.TEXT);
			converter.setTypeIdPropertyName("_type");
			Map<String, Class<?>> classes = new HashMap<>();
			classes.put("String", String.class);
			classes.put("Audit", Audit.class);
			classes.put("ServiceCallLogsDto", ServiceCallLogsDto.class);
			converter.setTypeIdMappings(classes);
			return converter;
		}

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
    
    @Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
    
//    @Bean
//   	public RestTemplate getRestTemplate() {
//   		return new RestTemplate();
//   	}
    
    @Bean
    @Primary
    public RestTemplate getRestTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }
    
    @Bean(name = "getTemplateBean")
    public RestTemplate customRestTemplateForGetcall(final RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(15000)).setReadTimeout(Duration.ofMillis(20000)).build();
    }
    
    @Configuration
	 public class ProgramManagement {
		 
    	@Autowired
    	RestTemplateBuilder builder;
    	
		 public String getProgramCode(String program) throws Exception{
			 if (!getProgramCodes(builder).isEmpty()) {
			 if (program == null || program.isEmpty()) {
					program=getProgramCodes(builder).get(0);
				}else if(!getProgramCodes(builder).contains(program)){
					throw new MarketplaceException(this.getClass().toString(), "getProgramCode",
						"Invalid Program Code. Please check if the program exists.", 
						MarketPlaceCode.INVALID_PROGRAM_CODE);	
				}
			 }
			 return program;
		 }
		 
		 @Value("${programManagement.uri}")
		 private String programManagementUri;
		 
		 /*
		  * for Local set programManagementUri to empty string in application.properties file
		  * 
		  * */
		 @Bean
		 public List<String> getProgramCodes(RestTemplateBuilder builder) throws Exception {
			if (programManagementUri!=null && !programManagementUri.isEmpty()) {
			LOG.info("ProgramManagement.getProgramCodes : getting program code from "+programManagementUri);
			 List<String> program=getRestTemplate(builder).getForEntity(programManagementUri, List.class).getBody();
			 LOG.info("ProgramManagement.getProgramCodes :received program codes "+program);
			 if (program.isEmpty()) {
				throw new MarketplaceException(this.getClass().toString(), "getProgramCode",
							"ProgramManagement.getProgramCodes :Unable to get any program code from Program Management. Please check if any Program Code is configured", 
							MarketPlaceCode.PROGRAM_CODE_FETCH_ERROR);	
			 }
			 return program;
			 }
			 return new ArrayList<String>();
			
		   }
	 }
    
    @Bean("threadPoolTaskExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("JDAsync-");
        return executor;
    }
	
}
