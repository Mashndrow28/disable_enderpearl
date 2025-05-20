package com.mash.disableenderpearl;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class DisableEnderPearlMod implements ModInitializer {
    @Override
    public void onInitialize() {
        EnderPearlConfig.load();
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("disableenderpearl")
                .requires(source -> source.hasPermissionLevel(2)) // Only allow OPs
                .then(CommandManager.literal("reload")
                        .executes(ctx -> {
                            EnderPearlConfig.load();
                            ctx.getSource().sendFeedback(() ->
                                    Text.literal("[DisableEnderPearl] Config reloaded successfully."), false);
                            return 1;
                        })
                )
        );
    }
}