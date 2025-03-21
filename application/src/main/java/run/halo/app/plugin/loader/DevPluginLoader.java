package run.halo.app.plugin.loader;

import java.nio.file.Files;
import java.nio.file.Path;
import org.pf4j.DevelopmentPluginLoader;
import org.pf4j.PluginClassLoader;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginManager;
import run.halo.app.plugin.PluginProperties;

public class DevPluginLoader extends DevelopmentPluginLoader {

    private final PluginProperties pluginProperties;

    public DevPluginLoader(
        PluginManager pluginManager,
        PluginProperties pluginProperties
    ) {
        super(pluginManager);
        this.pluginProperties = pluginProperties;
    }

    @Override
    public ClassLoader loadPlugin(Path pluginPath, PluginDescriptor pluginDescriptor) {
        var classesDirectories = pluginProperties.getClassesDirectories();
        if (classesDirectories != null) {
            classesDirectories.forEach(
                classesDirectory -> pluginClasspath.addClassesDirectories(classesDirectory)
            );
        }
        var libDirectories = pluginProperties.getLibDirectories();
        if (libDirectories != null) {
            libDirectories.forEach(
                libDirectory -> pluginClasspath.addJarsDirectories(libDirectory)
            );
        }
        return super.loadPlugin(pluginPath, pluginDescriptor);
    }

    @Override
    public boolean isApplicable(Path pluginPath) {
        // Currently we only support a plugin loading from directory in dev mode.
        return Files.isDirectory(pluginPath);
    }

    @Override
    protected PluginClassLoader createPluginClassLoader(Path pluginPath,
        PluginDescriptor pluginDescriptor) {
        return new HaloPluginClassLoader(this.pluginManager, pluginDescriptor,
            this.getClass().getClassLoader());
    }
}
