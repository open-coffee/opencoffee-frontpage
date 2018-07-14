package coffee.synyx.frontpage;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;
import java.util.UUID;

public interface PluginRepository extends MongoRepository<PluginInstance, String> {

    Set<PluginInstance> findAllByUsername(String username);

    PluginInstance findById(UUID id);

    void deleteById(UUID id);
}
