//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package commands;

import com.google.common.base.Optional;
import java.util.List;
import org.spongepowered.api.Game;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

public class InfosCommand implements CommandCallable {
    private final Game game;
    private final Optional<Text> help;
    private final Optional<Text> desc;

    public InfosCommand(Game game) {
        this.help = Optional.of(Texts.of(new Object[]{"description ikka", TextColors.RED}));
        this.desc = Optional.of(Texts.of(new Object[]{"help ikka", TextColors.RED}));
        this.game = game;
    }

    public Optional<Text> getHelp(CommandSource arg0) {
        return this.help;
    }

    public Optional<Text> getShortDescription(CommandSource arg0) {
        return this.desc;
    }

    public List<String> getSuggestions(CommandSource arg0, String arg1) throws CommandException {
        return null;
    }

    public Text getUsage(CommandSource arg0) {
        return Texts.of(new Object[]{"usage ikka", TextColors.RED});
    }

    public Optional<CommandResult> process(CommandSource arg0, String arg1) throws CommandException {
        arg0.sendMessage(new Text[]{Texts.of(new Object[]{"Command bien re?ue !", TextColors.RED})});
        this.game.getServer().broadcastMessage(Texts.of(new Object[]{"Evenement re?u !", TextColors.RED}));
        return Optional.of(CommandResult.success());
    }

    public boolean testPermission(CommandSource arg0) {
        return true;
    }
}
