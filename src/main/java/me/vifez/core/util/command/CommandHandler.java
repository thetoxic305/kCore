package me.vifez.core.util.command;

import me.vifez.core.chat.commands.CommandSpyCommand;
import me.vifez.core.kCoreConstant;
import me.vifez.core.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class CommandHandler {

    private Plugin plugin;

    private SimpleCommandMap commandMap;

    public CommandHandler(Plugin plugin) {
        this.plugin = plugin;
        try {
            this.commandMap = (SimpleCommandMap) Reflection.getValue(Bukkit.getPluginManager(), true, "commandMap");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(Command command) {
        try {
            PluginCommand pluginCommand = (PluginCommand) Reflection.instantiateObject(PluginCommand.class, command.getLabel(), plugin);
            pluginCommand.setExecutor((sender, cmd, label, args) -> {
                if (command.isPlayerOnly() && !PlayerUtil.isPlayer(sender)) {
                    sender.sendMessage(kCoreConstant.PLAYER_ONLY);
                    return true;
                }
                command.execute(sender, args);
                return true;
            });

            pluginCommand.setTabCompleter((sender, cmd, label, args) -> {
                if (command.getPermission() != null && command.getPermission().equals("") && !sender.hasPermission(command.getPermission()) || command.isPlayerOnly() && !PlayerUtil.isPlayer(sender))
                    return Collections.emptyList();

                List<String> completions = new ArrayList<>(command.complete(sender, args));
                completions.removeIf(completion -> !completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
                return completions;
            });

            Reflection.setValue(pluginCommand, pluginCommand.getClass().getSuperclass(), true, "aliases", command.getAliases());
            Reflection.setValue(pluginCommand, pluginCommand.getClass().getSuperclass(), true, "activeAliases", command.getAliases());
            Reflection.setValue(pluginCommand, pluginCommand.getClass().getSuperclass(), true, "permission", command.getPermission());
            Reflection.setValue(pluginCommand, pluginCommand.getClass().getSuperclass(), true, "permissionMessage", kCoreConstant.NO_PERMISSION);
            this.commandMap.register(plugin.getName(), pluginCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Reflection {

        static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
            Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (!DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
                    continue;
                }
                constructor.setAccessible(true);
                return constructor;
            }
            throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter types");
        }

        static Object instantiateObject(Class<?> clazz, Object... arguments) throws Exception {
            return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
        }

        static Field getField(Class<?> clazz, boolean declared, String fieldName) throws Exception {
            Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
            field.setAccessible(true);
            return field;
        }

        static Object getValue(Object instance, Class<?> clazz, boolean declared, String fieldName) throws Exception {
            return getField(clazz, declared, fieldName).get(instance);
        }

        static Object getValue(Object instance, boolean declared, String fieldName) throws Exception {
            return getValue(instance, instance.getClass(), declared, fieldName);
        }

        static void setValue(Object instance, Class<?> clazz, boolean declared, String fieldName, Object value) throws Exception {
            getField(clazz, declared, fieldName).set(instance, value);
        }

        enum DataType {
            ;

            private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<>();
            private final Class<?> primitive;
            private final Class<?> reference;

            static {
                for (DataType type : values()) {
                    CLASS_MAP.put(type.primitive, type);
                    CLASS_MAP.put(type.reference, type);
                }
            }

            DataType(Class<?> primitive, Class<?> reference) {
                this.primitive = primitive;
                this.reference = reference;
            }

            public Class<?> getPrimitive() {
                return primitive;
            }

            static DataType fromClass(Class<?> clazz) {
                return CLASS_MAP.get(clazz);
            }

            static Class<?> getPrimitive(Class<?> clazz) {
                DataType type = fromClass(clazz);
                return type == null ? clazz : type.getPrimitive();
            }

            static Class<?>[] getPrimitive(Class<?>[] classes) {
                int length = classes == null ? 0 : classes.length;
                Class<?>[] types = new Class<?>[length];
                for (int index = 0; index < length; index++) {
                    types[index] = getPrimitive(classes[index]);
                }
                return types;
            }

            static Class<?>[] getPrimitive(Object[] objects) {
                int length = objects == null ? 0 : objects.length;
                Class<?>[] types = new Class<?>[length];
                for (int index = 0; index < length; index++) {
                    types[index] = getPrimitive(objects[index].getClass());
                }
                return types;
            }

            static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
                if (primary == null || secondary == null || primary.length != secondary.length) {
                    return false;
                }
                for (int index = 0; index < primary.length; index++) {
                    Class<?> primaryClass = primary[index];
                    Class<?> secondaryClass = secondary[index];
                    if (primaryClass.equals(secondaryClass) || primaryClass.isAssignableFrom(secondaryClass)) {
                        continue;
                    }
                    return false;
                }
                return true;
            }
        }
    }

}
