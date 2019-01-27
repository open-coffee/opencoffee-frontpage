package rocks.coffeenet.frontpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import javax.annotation.PostConstruct;

@Configuration
public class MongoDbConfiguration {

    private final MappingMongoConverter mongoConverter;

    @Autowired
    public MongoDbConfiguration(MappingMongoConverter mongoConverter) {
        this.mongoConverter = mongoConverter;
    }

    /**
     * Converts . into a mongo friendly char
     */
    @PostConstruct
    public void setUpMongoEscapeCharacterConversion() {

        mongoConverter.setMapKeyDotReplacement("_");
    }
}
