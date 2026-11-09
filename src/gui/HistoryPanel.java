package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/** Read-only chronological move table displayed beside the board. */
public final class HistoryPanel extends JPanel {
    /** Swing serialization identifier. */
    private static final long serialVersionUID = 1L;
    /** Preferred sidebar width, also reserved by board size settings. */
    public static final int PANEL_WIDTH = 360;
    /** Table cells are display-only; moves originate in the controller. */
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] {"Move", "Side", "Piece", "From", "To", "Capture"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    /** Scrollable move table. */
    private final JTable table = new JTable(model);

    /** Constructs an empty sidebar on the EDT. */
    public HistoryPanel() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("History panel must be constructed on the EDT.");
        }
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        setPreferredSize(new Dimension(PANEL_WIDTH, 500));
        add(new JLabel("Move history"), BorderLayout.NORTH);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(5).setPreferredWidth(110);
        table.getAccessibleContext().setAccessibleName("Move history");
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Displays a controller snapshot and scrolls to the newest move.
     * @param entries chronological entries; an empty list clears the table
     */
    public void setHistory(List<HistoryEntry> entries) {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("History updates must run on the EDT.");
        }
        model.setRowCount(0);
        for (HistoryEntry entry : entries) {
            model.addRow(new Object[] {entry.number(), entry.side(), entry.piece(),
                    entry.from(), entry.to(), entry.captured()});
        }
        if (model.getRowCount() > 0) {
            int last = model.getRowCount() - 1;
            table.setRowSelectionInterval(last, last);
            table.scrollRectToVisible(table.getCellRect(last, 0, true));
            // Revalidation may update the viewport after this event finishes.
            SwingUtilities.invokeLater(this::scrollToLatest);

        }
    }

    /** Scrolls after layout, using the current count so resets cannot target stale rows. */
    private void scrollToLatest() {
        int last = model.getRowCount() - 1;
        if (last >= 0) table.scrollRectToVisible(table.getCellRect(last, 0, true));
    }

    /**
     * Exposes the read-only table for accessibility and component verification.
     * @return history table
     */
    public JTable getTable() { return table; }
}
