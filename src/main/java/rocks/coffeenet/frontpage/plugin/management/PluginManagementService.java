package rocks.coffeenet.frontpage.plugin.management;

import rocks.coffeenet.frontpage.plugin.management.PluginConfigurationProperties.Resource;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Tobias Schneider
 */
@Service
public class PluginManagementService {

    private final PluginConfigurationProperties pluginConfigurationProperties;
    private final GitHubBuilder gitHubBuilder;

    @Autowired
    public PluginManagementService(PluginConfigurationProperties pluginConfigurationProperties, GitHubBuilder gitHubBuilder) {

        this.pluginConfigurationProperties = pluginConfigurationProperties;
        this.gitHubBuilder = gitHubBuilder;
    }


    Optional<List<AvailablePlugins>> getListOfAvailablePlugins() {

        List<Resource> pluginResources = pluginConfigurationProperties.getResource();

        if (pluginResources == null) {
            return Optional.empty();
        }

        List<AvailablePlugins> availablePlugins = new ArrayList<>();

        for (Resource pluginResource : pluginResources) {
            GHRelease latestRelease = getLatestRelease(pluginResource);
            List<AvailablePluginAssets> latestReleaseAssets = getAvailablePluginAssets(latestRelease);

            availablePlugins.add(new AvailablePlugins(pluginResource.getRepositoryName(), latestReleaseAssets));
        }

        return Optional.of(availablePlugins);
    }

    private List<AvailablePluginAssets> getAvailablePluginAssets(GHRelease release) {
        try {

            if (release == null) {
                return Collections.emptyList();
            }

            return release.getAssets()
                .stream()
                .map(ghAsset -> new AvailablePluginAssets(ghAsset.getName(), ghAsset.getBrowserDownloadUrl()))
                .collect(toList());
        } catch (IOException e) {
            throw new PluginManagementException("");
        }
    }

    private GHRelease getLatestRelease(Resource resource) {
        try {
            GitHub github = gitHubBuilder.build();
            return github.getRepository(resource.getRepositoryName()).getLatestRelease();
        } catch (IOException e) {
            throw new PluginManagementException(format("Could not retrieve lists of release from %s", resource.getRepositoryName()));
        }
    }
}
