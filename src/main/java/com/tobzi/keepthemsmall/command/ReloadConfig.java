package com.tobzi.keepthemsmall.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.tobzi.keepthemsmall.KeepThemSmall;
import com.tobzi.keepthemsmall.config.ModConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class ReloadConfig{

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(KeepThemSmall.MOD_ID)
                .then(literal("reload")
                        .requires((ServerCommandSource source) -> source.hasPermissionLevel(2))
                        .executes((CommandContext<ServerCommandSource> context) -> {
                            ModConfig.load();

                            context.getSource().sendFeedback(() -> Text.literal("Keep Them Small configuration reloaded."), false);

                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}