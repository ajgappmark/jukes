package com.melloware.jukes.gui.view.dialogs;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.uif.application.Application;
import com.melloware.jukes.gui.tool.Resources;
import com.melloware.jukes.gui.view.MainFrame;
import com.melloware.jukes.util.MessageUtil;
import com.melloware.jukes.ws.FreeDBItem;

/**
 * The table model for displaying results in the FreeDB search dialog.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 * AZ Development 2010
 */
public final class FreeDBTableModel
    extends AbstractTableModel {

    private static final Log LOG = LogFactory.getLog(FreeDBTableModel.class);
    private Object[] data;
    private final String[] columnNames = { Resources.getString("label.artist"),
    		Resources.getString("label.disc"), Resources.getString("label.year"),
    		Resources.getString("label.tracks"), Resources.getString("label.genre") };

    /**
     * Constructor that takes a collection.
     */
    public FreeDBTableModel() {
        super();
        LOG.debug("FreeDBTableModel created.");
        this.data = null;
    }

    /**
     * Constructor that takes a collection.
     */
    public FreeDBTableModel(Object[] aData) {
        super();
        LOG.debug("FreeDBTableModel created.");
        this.data = aData;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Gets the data.
     * <p>
     * @return Returns the data.
     */
    public Object[] getData() {
        return this.data;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        if (data == null) {
            return 0;
        } else {
            return data.length;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        try {
            if (data == null) {
                return "";
            }
            if (row >= data.length) {
                return "";
            }
            FreeDBItem item = (FreeDBItem)data[row];
            Object value = null;
            switch (col) {
                case 0: {
                    value = item.getArtist();
                    break;
                }
                case 1: {
                    value = item.getDisc();
                    break;
                }
                case 2: {
                    value = item.getReleaseYear();
                    break;
                }
                case 3: {
                    if (item.getTracks().isEmpty()) {
                        value = "No";
                    } else {
                        value = "Yes";
                    }
                    break;
                }
                case 4: {
                    value = item.getGenre();
                    break;
                }
                default: {
                    break;
                }
            }
            return (value == null) ? "" : value;
        } catch (Exception ex) {
        	final MainFrame mainFrame = (MainFrame) Application.getDefaultParentFrame();
       	    final String errorMessage = Resources.getString("messages.ErrorLoadingResults");
    	    MessageUtil.showError(mainFrame, errorMessage);
            LOG.error(errorMessage);
            return "";
        }
    }

    /**
     * Sets the data.
     * <p>
     * @param aData The data to set.
     */
    public void setData(Object[] aData) {
        this.data = aData;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}