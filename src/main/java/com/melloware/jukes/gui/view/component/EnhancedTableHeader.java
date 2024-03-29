package com.melloware.jukes.gui.view.component;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * In Windows Explorer, you can change the column width to fit the widest cell
 * by double clicking on the column header separators.
 * Use this TableHeader with your JTable to do something similar.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 */
public final class EnhancedTableHeader
    extends JTableHeader {
    private final JTable table;

    public EnhancedTableHeader(TableColumnModel cm, JTable table) {
        super(cm);
        this.table = table;
        addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    doMouseClicked(e);
                }
            });
    }

    public int getRequiredColumnWidth(TableColumn column) {
        int modelIndex = column.getModelIndex();
        TableCellRenderer renderer;
        Component component;
        int requiredWidth = 0;
        int rows = table.getRowCount();
        for (int i = 0; i < rows; i++) {
            renderer = table.getCellRenderer(i, modelIndex);
            Object valueAt = table.getValueAt(i, modelIndex);
            component = renderer.getTableCellRendererComponent(table, valueAt, false, false, i, modelIndex);
            requiredWidth = Math.max(requiredWidth, component.getPreferredSize().width + 2);
        }
        return requiredWidth;
    }

    /**
     * Autosizes all columns to fit to width of their data.
     */
    public void autoSizeColumns() {
        TableColumnModel tableColumnModel = table.getColumnModel();
        int col_count = tableColumnModel.getColumnCount();

        for (int i = 0; i < col_count; i++) {
            TableColumn col = tableColumnModel.getColumn(i);
            col.setMinWidth(this.getRequiredColumnWidth(col));
        }
    }

    public void doMouseClicked(MouseEvent e) {
        if (!getResizingAllowed()) {
            return;
        }
        if (e.getClickCount() != 2) {
            return;
        }
        TableColumn column = getResizingColumn(e.getPoint(), columnAtPoint(e.getPoint()));
        if (column == null) {
            return;
        }
        int oldMinWidth = column.getMinWidth();
        column.setMinWidth(getRequiredColumnWidth(column));
        setResizingColumn(column);
        table.doLayout();
        column.setMinWidth(oldMinWidth);
    }

    private TableColumn getResizingColumn(Point p, int column) {
        if (column == -1) {
            return null;
        }
        Rectangle r = getHeaderRect(column);
        r.grow(-3, 0);
        if (r.contains(p)) {
            return null;
        }
        int midPoint = r.x + (r.width / 2);
        int columnIndex;
        if (getComponentOrientation().isLeftToRight()) {
            columnIndex = (p.x < midPoint) ? (column - 1) : column;
        } else {
            columnIndex = (p.x < midPoint) ? column : (column - 1);
        }
        if (columnIndex == -1) {
            return null;
        }
        return getColumnModel().getColumn(columnIndex);
    }
}