package coffee.synyx.frontpage;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;
import coffee.synyx.frontpage.plugin.api.FrontpagePluginQualifier;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.plugin.core.PluginRegistry;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Collections.emptyList;


@RunWith(MockitoJUnitRunner.class)
public class DefaultPluginCollectorTest {

    private DefaultPluginCollector sut;

    @Mock
    private PluginRegistry<FrontpagePluginInterface, FrontpagePluginQualifier> pluginRegistry;

    @Before
    public void setUp() {

        sut = new DefaultPluginCollector(pluginRegistry);
    }


    @Test
    public void callsThePluginRegistry() {

        when(pluginRegistry.getPlugins()).thenReturn(emptyList());

        final List<FrontpagePluginInterface> frontpagePlugins = sut.getFrontpagePlugins();
        assertThat(frontpagePlugins).isEmpty();
        verify(pluginRegistry).getPlugins();
    }
}
