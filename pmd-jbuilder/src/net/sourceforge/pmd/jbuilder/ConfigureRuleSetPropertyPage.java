package net.sourceforge.pmd.jbuilder;

import com.borland.primetime.help.HelpTopic;
import com.borland.primetime.help.ZipHelpTopic;
import com.borland.primetime.properties.PropertyPage;
import net.sourceforge.pmd.Rule;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;


/**
 * <p>Title: JBuilder OpenTool for PMD</p>
 * <p>Description: Provides an environemnt for using the PMD aplication from within JBuilder</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: InfoEther</p>
 *
 * @author David Craine
 * @version 1.0
 */

public class ConfigureRuleSetPropertyPage extends PropertyPage {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JSplitPane splitPaneConfRuleSets = new JSplitPane();
    private JScrollPane spRuleSets = new JScrollPane();
    private JScrollPane spRules = new JScrollPane();
    private JList listRuleSets = new JList();
    private JList listRules = new JList();
    private DefaultListModel dlmRuleSets = new DefaultListModel();
    private DefaultListModel dlmRules = new DefaultListModel();
    static ConfigureRuleSetPropertyPage currentInstance = null;
    private JTabbedPane tpInfo = new JTabbedPane();
    private JTextArea taExamples = new JTextArea();
    private JScrollPane spExamples = new JScrollPane();
    private JScrollPane spDescription = new JScrollPane();
    private JTextArea taDescription = new JTextArea();
    private Border border1;
    private TitledBorder titledBorderDescription;
    private TitledBorder titledBorderExample;

    public ConfigureRuleSetPropertyPage() {
        currentInstance = this;
        try {
            jbInit();
            init2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void reinit() {
        //get the list if rule sets and populate the listRuleSets model
        dlmRuleSets.clear();
        for (Iterator iter = ActiveRuleSetPropertyGroup.currentInstance.ruleSets.values().iterator(); iter.hasNext();) {
            RuleSetProperty rsp = (RuleSetProperty) iter.next();
            dlmRuleSets.addElement(rsp.getActiveRuleSet().getName());
            listRuleSets.updateUI();
        }
        this.updateUI();
    }

    /**
     * This methiod is called by JBuilder when the user presses "OK" in the property dialog
     */
    public void writeProperties() {
        /**
         * Go through all the ruleSetProperties objects and revalidate them to persist
         * their global properties
         */
        for (Iterator iter = ActiveRuleSetPropertyGroup.currentInstance.ruleSets.values().iterator(); iter.hasNext();) {
            RuleSetProperty rsp = (RuleSetProperty) iter.next();
            rsp.revalidateRules();
        }
    }

    /**
     * This methiod is called by JBuilder
     */
    public HelpTopic getHelpTopic() {
        return new ZipHelpTopic(null,
                getClass().getResource("/html/configure-ruleset-props.html").toString());
    }

    /**
     * This methiod is called by JBuilder
     */
    public void readProperties() {
        /**
         * Go through all the ruleSetProperties objects and reset them to the
         * GlobalProeprty values
         */
        for (Iterator iter = ActiveRuleSetPropertyGroup.currentInstance.ruleSets.values().iterator(); iter.hasNext();) {
            RuleSetProperty rsp = (RuleSetProperty) iter.next();
            rsp.resetRuleSelectionState();
        }
        this.listRules.updateUI();
    }

    /**
     * JBuilder-constructed initialization
     *
     * @throws Exception
     */
    private void jbInit() throws Exception {
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151));
        titledBorderDescription = new TitledBorder(border1, "Description");
        titledBorderExample = new TitledBorder(border1, "Example Code");
        this.setLayout(borderLayout1);
        spRuleSets.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151)), "Rule Sets"));
        spRules.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151)), "Rules"));
        listRuleSets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listRules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taExamples.setEditable(false);
        //spExamples.setPreferredSize(new Dimension(70,150));
        spExamples.setAutoscrolls(false);
        spExamples.setBorder(titledBorderExample);
        tpInfo.setTabPlacement(JTabbedPane.BOTTOM);
        tpInfo.setPreferredSize(new Dimension(543, 180));
        taDescription.setEditable(false);
        spDescription.setBorder(titledBorderDescription);
        tpInfo.add(spDescription, "Description");
        tpInfo.add(spExamples, "Example Code");
        spDescription.getViewport().add(taDescription, null);
        spExamples.getViewport().add(taExamples, null);
        this.add(splitPaneConfRuleSets, BorderLayout.CENTER);
        splitPaneConfRuleSets.add(spRuleSets, JSplitPane.TOP);
        spRuleSets.getViewport().add(listRuleSets, null);
        splitPaneConfRuleSets.add(spRules, JSplitPane.BOTTOM);
        spRules.getViewport().add(listRules, null);
        splitPaneConfRuleSets.setDividerLocation(200);
        this.add(tpInfo, BorderLayout.SOUTH);
    }

    /**
     * additional intiialzation
     */
    private void init2() {
        listRules.setCellRenderer(new CheckCellRenderer());
        CheckListener cl = new CheckListener(listRules);
        listRules.addMouseListener(cl);
        listRules.addKeyListener(cl);
        listRules.setModel(dlmRules);

        listRuleSets.setModel(dlmRuleSets);
        listRuleSets.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                listRuleSets_valueChanged(e);
            }
        });
        //get the list if rule sets and populate the listRuleSets model
        for (Iterator iter = ActiveRuleSetPropertyGroup.currentInstance.ruleSets.values().iterator(); iter.hasNext();) {
            RuleSetProperty rsp = (RuleSetProperty) iter.next();
            dlmRuleSets.addElement(rsp.getActiveRuleSet().getName());
            listRuleSets.updateUI();
        }

    }


    /**
     * When a ruleset is selected we need to update the listRules list
     *
     * @param e selection event
     */
    private void listRuleSets_valueChanged(ListSelectionEvent e) {
        this.dlmRules.clear();
        String selectedRuleSet = ((JList) e.getSource()).getSelectedValue().toString();
        //get the RuleSetProperty for this ruleset
        RuleSetProperty rsp = (RuleSetProperty) ActiveRuleSetPropertyGroup.currentInstance.ruleSets.get(selectedRuleSet);
        //iterate over the rules from of the rule set
        //We need to iterate over the originalRuleSet because the active rule set only
        //has the rules that have been enabled
        for (Iterator iter = rsp.getOriginalRuleSet().getRules().iterator(); iter.hasNext();) {
            Rule rule = (Rule) iter.next();
            String ruleName = rule.getName();
            dlmRules.addElement(new RuleData(ruleName, rsp));
        }
        taExamples.setText("");

    }

    class CheckListener implements MouseListener, KeyListener {
        protected JList list;

        public CheckListener(JList list) {
            this.list = list;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getX() < 20)
                doCheck();
            //display the example
            int index = list.getSelectedIndex();
            if (index < 0) return;
            RuleData rd = (RuleData) list.getModel().getElementAt(index);
            //set the example text
            String example = rd.rsp.getOriginalRuleSet().getRuleByName(rd.ruleName).getExample();
            if (example != null) {
                taExamples.setText(example);
                taExamples.setCaretPosition(0);
            } else {
                taExamples.setText("");
            }
            //set the description text
            String description = rd.rsp.getOriginalRuleSet().getRuleByName(rd.ruleName).getDescription();
            if (description != null) {
                taDescription.setText(description);
                taDescription.setCaretPosition(0);
            } else {
                taDescription.setText("");
            }
            //set the border titleds
            titledBorderDescription.setTitle("Description: " + rd.ruleName);
            titledBorderExample.setTitle("Example Code: " + rd.ruleName);
            spDescription.updateUI();
            spExamples.updateUI();

        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == ' ')
                doCheck();
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        protected void doCheck() {
            int index = list.getSelectedIndex();
            if (index < 0)
                return;
            RuleData rd = (RuleData) list.getModel().getElementAt(index);
            rd.invertSelected();

            //get the currently selected rule set
            String ruleSetName = listRuleSets.getSelectedValue().toString();
            //get the RuleSetProperty object
            RuleSetProperty rsp = (RuleSetProperty) ActiveRuleSetPropertyGroup.currentInstance.ruleSets.get(ruleSetName);
            //update the selection setting for this rule in the rule set property
            rsp.setRuleSelected(rd.getName(), rd.isSelected());

            list.repaint();
        }
    }


    class CheckCellRenderer extends JPanel
            implements ListCellRenderer {
        protected JCheckBox check;
        protected Border m_noFocusBorder = new EmptyBorder(1, 1, 1, 1);


        public CheckCellRenderer() {
            super();
            setOpaque(true);
            setBorder(m_noFocusBorder);
        }

        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.
        public Component getListCellRendererComponent(JList list,
                                                      Object value, // value to display
                                                      int index, // cell index
                                                      boolean isSelected, // is the cell selected
                                                      boolean cellHasFocus)       // the list and the cell have the focus
        {
            RuleData rd = (RuleData) value;
            JCheckBox c = new JCheckBox(rd.getName(), rd.isSelected());
            //c.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            //c.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            c.setBackground(Color.white);
            c.setFont(list.getFont());

            return c;
        }

    }
}

