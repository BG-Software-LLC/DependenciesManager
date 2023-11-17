package com.bgsoftware.common.dependencies;

import com.bgsoftware.common.reflection.ReflectField;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

public class DependenciesManager {

    private DependenciesManager() {

    }

    public static void inject(JavaPlugin plugin) {
        ClassLoader classLoader = plugin.getClass().getClassLoader();

        ReflectField<Set<String>> PLUGIN_CLASS_LOADER_SEEN_ILLEGAL_ACCESS = new ReflectField<>(
                classLoader.getClass(), Set.class, "seenIllegalAccess");

        if (!PLUGIN_CLASS_LOADER_SEEN_ILLEGAL_ACCESS.isValid())
            return;

        YamlConfiguration pluginFile = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("plugin.yml")));

        List<String> classDependencies = pluginFile.getStringList("class-depends");

        if (classDependencies.isEmpty())
            return;

        Set<String> seenIllegalAccess = PLUGIN_CLASS_LOADER_SEEN_ILLEGAL_ACCESS.get(classLoader);

        seenIllegalAccess.addAll(classDependencies);
    }

}
