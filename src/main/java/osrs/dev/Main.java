package osrs.dev;

import com.formdev.flatlaf.FlatDarkLaf;
import lombok.Getter;
import osrs.dev.dumper.Dumper;
import osrs.dev.reader.CollisionMap;
import osrs.dev.ui.UIFrame;
import osrs.dev.util.ConfigManager;
import osrs.dev.util.ThreadPool;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Launches the collision map viewer
 */
public class Main
{
    @Getter
    private static CollisionMap collision;
    @Getter
    private static ConfigManager configManager;
    private static UIFrame frame;

    /**
     * Main method
     * @param args command line arguments
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception
    {
        FlatDarkLaf.setup();
        load();
        SwingUtilities.invokeLater(() -> {
            frame = new UIFrame();
            frame.setVisible(true);
            if(collision != null)
            {
                frame.update();
            }
            frame.requestInitialFocus();
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ThreadPool.shutdown();
            configManager.saveConfig();
        }));
    }

    /**
     * Load the collision map
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if an error occurs
     */
    public static void load() throws IOException, ClassNotFoundException {
        configManager = new ConfigManager();
        Dumper.OUTPUT_MAP = new File(configManager.outputPath());
        if(Dumper.OUTPUT_MAP.exists())
        {
            collision = CollisionMap.load(Dumper.OUTPUT_MAP.getPath());
        }
    }
}
