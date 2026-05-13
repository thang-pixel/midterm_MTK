package vn.edu.tdtu.edocument;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIStyle {
    // --- COLORS ---
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Professional Blue
    public static final Color SECONDARY_COLOR = new Color(149, 165, 166);  // Silver
    public static final Color ACCENT_COLOR = new Color(52, 152, 219);     // Lighter Blue
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);     // Emerald Green
    public static final Color DANGER_COLOR = new Color(192, 57, 43);      // Pomegranate Red
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250); 
    public static final Color TEXT_COLOR = new Color(44, 62, 80);

    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void applyButtonStyle(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(BOLD_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void applyTableStyle(JTable table) {
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(232, 241, 250));
        table.setSelectionForeground(TEXT_COLOR);
        table.setFont(MAIN_FONT);
        table.getTableHeader().setFont(BOLD_FONT);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
    }
}
