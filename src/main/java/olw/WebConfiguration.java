package olw;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableElasticsearchRepositories(basePackages="olw.repositories")
@EnableSpringDataWebSupport
public class WebConfiguration extends WebMvcConfigurationSupport {

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
	}
	
	@Bean
	public DomainClassConverter<?> domainClassConverter() {
	    return new DomainClassConverter<FormattingConversionService>(mvcConversionService());
	}

	@Bean
    public MappingJackson2HttpMessageConverter jackson2Converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
	
	
}
