//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package commands;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class MyCommandExecutor implements CommandExecutor {
    public MyCommandExecutor() {
    }

    public CommandResult execute(CommandSource arg0, CommandContext arg1) throws CommandException {
        Player player = (Player)arg1.getOne("player").get();
        player.sendMessage(new Text[]{Texts.of(new Object[]{"Command bien re?ue !", TextColors.RED})});
        return CommandResult.success();
    }
}
