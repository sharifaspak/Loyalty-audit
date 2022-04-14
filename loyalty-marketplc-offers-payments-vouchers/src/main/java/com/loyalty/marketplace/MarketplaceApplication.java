package com.loyalty.marketplace;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
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
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;

import com.loyalty.marketplace.audit.Audit;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.offers.points.bank.inbound.dto.LifeTimeSavingsVouchersEvent;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.dto.PushNotificationRequestDto;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.eventobject.AccountCancelEvent;
import com.loyalty.marketplace.outbound.events.eventobject.AccountChangeEvent;
import com.loyalty.marketplace.outbound.events.eventobject.EnrollEvent;
import com.loyalty.marketplace.outbound.events.eventobject.Event;
import com.loyalty.marketplace.outbound.events.eventobject.MemberMergeEvent;
import com.loyalty.marketplace.payment.inbound.dto.ATGEnrollmentDTO;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class MarketplaceApplication {
	
	@Value("${integration.proxy.url}")
	private String proxyUrl;
	
	@Value("${integration.proxy.port}")
	private String proxyPort;
		
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
        messageSource.setBasenames("classpath:offerMessages","classpath:subscriptionMessages","classpath:voucherMessages","classpath:imageMessages","classpath:equivalentPointsMessages","classpath:giftingMessages", "classpath:customerSegmentMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
    
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
    
//    @Bean
//    @Primary
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }
    
    @Bean
    @Primary
    public RestTemplate getRestTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }
   
    @Bean(name = "getTemplateBean")
    public RestTemplate customRestTemplateForGetcall(final RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(55000)).setReadTimeout(Duration.ofMillis(60000)).build();
    }
    
    @Bean(name="customRestTemplateBean")
    public RestTemplate customRestTemplate(RestTemplateBuilder builder){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        LOG.info("Proxy server information: Url: {} and port: {}",proxyUrl,proxyPort);
        Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyUrl, Integer.parseInt(proxyPort)));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }
    
    @Bean
    public Docket swaggerProviderApi10() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.loyalty.marketplace"))
                .paths(PathSelectors.any()).build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Marketplace API")
                .description("Documentation Resource Downtime API v1.0").termsOfServiceUrl("termsofserviceurl")
                .license("Apache License Version 2.0").version("2.0").title("Resource Downtime API").build();
    }
    
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        Map<String, Class<?>> classes = new HashMap<>();
        classes.put("SMSRequestDto", SMSRequestDto.class);
        classes.put("EmailRequestDto", EmailRequestDto.class);
        classes.put("String", String.class);
        classes.put("Audit", Audit.class);
        classes.put("LifeTimeSavingsVouchersEvent", LifeTimeSavingsVouchersEvent.class);
        classes.put("PushNotificationRequestDto", PushNotificationRequestDto.class);
        classes.put("ServiceCallLogsDto", ServiceCallLogsDto.class);
        classes.put("AccountChangeEvent", AccountChangeEvent.class);
        classes.put("EnrollEvent", EnrollEvent.class);
        classes.put("AccountCancelEvent", AccountCancelEvent.class);
        classes.put("MemberMergeEvent", MemberMergeEvent.class);
		classes.put("ATGEnrollmentDTO", ATGEnrollmentDTO.class);
      	classes.put("Event", Event.class);
        converter.setTypeIdMappings(classes);
        return converter;
    }
    
    @Configuration
	 public class ProgramManagement {
    	
    	@Autowired
    	RestTemplateBuilder builder;
		 
		public String getProgramCode(String program) throws Exception {
			if (!getProgramCodes(builder).isEmpty()) {
				if (program == null || program.isEmpty()) {
					program = getProgramCodes(builder).get(0);
				} else {
					for (String prgmCode : getProgramCodes(builder)) {
						if (program.equalsIgnoreCase(prgmCode)) {
							return program;
						}
					}
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
		 @SuppressWarnings("unchecked")
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

//    @Bean
//    public RetryTemplate retryTemplate() {
//    	RetryTemplate retryTemplate = new RetryTemplate();
//    	FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
//    	fixedBackOffPolicy.setBackOffPeriod(1000l);
//    	retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
//    	SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
//    	retryPolicy.setMaxAttempts(3);
//    	retryTemplate.setRetryPolicy(retryPolicy);
//    	return retryTemplate;
//    }
    
    
	@Bean(name="defaultRetry")
	public RetryTemplate retryTemplate() {
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3);
	    RetryTemplate template = new RetryTemplate();
	    template.setRetryPolicy(retryPolicy);
	    return template;

	}
	
//	@Bean(name="retryWithSpecificExceptions")
//	public RetryTemplate retryTemplateWithExceptions() {
//		RetryTemplate template = new RetryTemplate();
//		Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
//		
//	    SimpleRetryPolicy recoverablePolicy = new SimpleRetryPolicy();
//	    recoverablePolicy.setMaxAttempts(MAX_RECOVERABLE_RETRIES);
//	    policyMap.put(SubscriptionManagementException.class, recoverablePolicy);    
//	    
//	    NeverRetryPolicy baseException = new NeverRetryPolicy();
//	    policyMap.put(Exception.class, baseException);

//	    AlwaysRetryPolicy transientPolicy = new AlwaysRetryPolicy();
//	    policyMap.put(SQLTransientException.class, transientPolicy);
	    
//	    ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();
//	    retryPolicy.setPolicyMap(policyMap);
//	    template.setRetryPolicy(retryPolicy);
//	    
//	    //ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
//	    //backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
//	    //backOffPolicy.setMaxInterval(MAX_INTERVAL);
//	    //backOffPolicy.setMultiplier(MULTIPLIER);
//	    //template.setBackOffPolicy(backOffPolicy);
//	    return template;
//	}
	
    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR)
    public Executor asyncExecutorForPopulatingEligibleOffers() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("JDAsync-");
        return executor;
    }
    
    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_FREE_VOUCHER)
    public Executor asyncExecutorForGiftingFreeVouchers() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(MarketplaceConfigurationConstants.FREE_VOUCHER_THREAD_PREFIX);
        return executor;
    }
    
    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_FREE_VOUCHER_AFTER_LOGIN)
    public Executor asyncExecutorForGiftingFreeVouchersAfterLogin() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(MarketplaceConfigurationConstants.FREE_VOUCHER_AFTER_LOGIN_THREAD_PREFIX);
        return executor;
    }
    
    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_PREMIUM_VOUCHER_GIFT)
    public Executor asyncExecutorForPremimVoucherGift() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(MarketplaceConfigurationConstants.PREMIUM_VOUCHER_GIFT_THREAD_PREFIX);
        return executor;
    }
    
    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_RENEWAL_REPORT)
    public Executor asyncExecutorForEmailingRenewalReport() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(MarketplaceConfigurationConstants.RENEWAL_REPORT_THREAD_PREFIX);
        return executor;
    }
    


    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_REFUND)
    public Executor asyncExecutorForRefund() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(MarketplaceConfigurationConstants.REFUND_THREAD_PREFIX);
        return executor;
    }
    
    @Bean(MarketplaceConfigurationConstants.MONGO_TRANSACTION_MANAGER)
	MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
		TransactionOptions txnOptions = TransactionOptions.builder().readPreference(ReadPreference.primary())
				.readConcern(ReadConcern.MAJORITY).writeConcern(WriteConcern.MAJORITY).build();
		return new MongoTransactionManager(dbFactory, txnOptions);
	}
    
    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_MERCHANT_OFFER_COUNT_UPDATE)
    public Executor asyncExecutorForMerchantOfferCount() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(MarketplaceConfigurationConstants.MERCHANT_OFFER_COUNT_UPDATE_THREAD_PREFIX);
        return executor;
    }
    
    @Bean(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_CASH_VOUCHER_ACCRUAL)
    public Executor asyncExecutorForCashVoucherAccrual() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(MarketplaceConfigurationConstants.CASH_VOUCHER_ACCRUAL_THREAD_PREFIX);
        return executor;
    }
    
}
