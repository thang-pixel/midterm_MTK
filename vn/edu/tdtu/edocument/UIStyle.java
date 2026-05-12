package vn.edu.tdtu.edocument;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class UIStyle {
    // --- COLORS ---
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Professional Blue
    public static final Color ACCENT_COLOR = new Color(52, 152, 219);     // Lighter Blue
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250); // Light Gray background
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(44, 62, 80);         // Dark Blue/Gray text
    public static final Color TEXT_LIGHT = new Color(127, 140, 141);      // Muted text
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);     // Emerald Green
    public static final Color DANGER_COLOR = new Color(192, 57, 43);      // Pomegranate Red
    public static final Color CONSOLE_BG = new Color(33, 37, 43);        // VS Code-ish Dark

    // --- FONTS ---
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font CONSOLE_FONT = new Font("Consolas", Font.PLAIN, 13);

    public static void applyGlobalStyle() {
        try {
            // Set basic Swing defaults to look more modern
            UIManager.put("Button.font", MAIN_FONT);
            UIManager.put("Label.font", MAIN_FONT);
            UIManager.put("TextField.font", MAIN_FONT);
            UIManager.put("ComboBox.font", MAIN_FONT);
            UIManager.put("Table.font", MAIN_FONT);
            UIManager.put("TableHeader.font", BOLD_FONT);
            
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("Label.foreground", TEXT_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(232, 241, 250));
        table.setSelectionForeground(TEXT_COLOR);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setIntercellSpacing(new Dimension(0, 1));
        
        // Header styling
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }
    
    public static Border createSectionBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            title,
            0, 0, BOLD_FONT, PRIMARY_COLOR
        );
    }
}
