package dev.cookiecode.rika2mqtt.plugins.internal.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
public class PluginSyncManagerTest {

    @Mock
    private HttpPluginDownloader httpPluginDownloader;

    @InjectMocks
    private PluginSyncManager pluginSyncManager;

    @Test
    void synchronizeShouldInvokeDownloadPlugin2TimesGivenThereAreTwoPluginsToDownload()
            throws Exception {
        // GIVEN
        final var pluginAUrl = "http://plugin-a.jar";
        final var pluginBUrl = "http://plugin-b.jar";
        final var pluginsUrls = List.of(new URL(pluginAUrl), new URL(pluginBUrl));

        final var pluginsDir = "some/dir/plugins";

        // WHEN
        pluginSyncManager.synchronize(pluginsDir, pluginsUrls);

        // THEN
        verify(httpPluginDownloader, times(2)).downloadPlugin(any(URL.class), anyString());
    }

}
