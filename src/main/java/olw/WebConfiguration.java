package olw;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import olw.service.MaterialToIndexConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableElasticsearchRepositories(basePackages="olw.repositories")
@EnableSpringDataWebSupport
public class WebConfiguration extends RepositoryRestMvcConfiguration {

	@Autowired
	MaterialToIndexConverter materialConverter;
	
	
	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
	}
	
	@Override
	protected void configureConversionService(ConfigurableConversionService conversionService) {
    	
    	conversionService.addConverter(materialConverter);
    }
	
   
}
