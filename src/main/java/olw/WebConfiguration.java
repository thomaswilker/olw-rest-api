package olw;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import olw.model.Area;
import olw.model.Assistant;
import olw.model.Collection;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Material;
import olw.service.CollectionToIndexConverter;
import olw.service.MaterialToIndexConverter;

@Configuration
@EnableElasticsearchRepositories(basePackages = "olw.repository.index")
@EnableSpringDataWebSupport
public class WebConfiguration extends RepositoryRestMvcConfiguration {

	@Autowired
	MaterialToIndexConverter materialIndexConverter;

	@Autowired
	CollectionToIndexConverter collectionIndexConverter;

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
	}

	@Override
	protected void configureConversionService(ConfigurableConversionService conversionService) {

		conversionService.addConverter(materialIndexConverter);
		conversionService.addConverter(collectionIndexConverter);
	}

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		List<Class> classes = Arrays.asList(Area.class, Language.class, Lecturer.class, License.class, Material.class,
				Collection.class, Assistant.class);
		config.exposeIdsFor(classes.toArray(new Class[] {}));
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false).favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true)
				.useJaf(false).defaultContentType(MediaType.APPLICATION_JSON)
				.mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON);
	}

}
