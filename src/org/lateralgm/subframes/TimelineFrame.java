/*
 * Copyright (C) 2007 IsmAvatar <IsmAvatar@gmail.com>
 * Copyright (C) 2008 Quadduc <quadduc@gmail.com>
 * 
 * This file is part of LateralGM.
 * LateralGM is free software and comes with ABSOLUTELY NO WARRANTY.
 * See LICENSE for details.
 */

package org.lateralgm.subframes;

import static java.lang.Integer.MAX_VALUE;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Collections;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lateralgm.compare.ResourceComparator;
import org.lateralgm.components.ActionList;
import org.lateralgm.components.ActionListEditor;
import org.lateralgm.components.GMLTextArea;
import org.lateralgm.components.NumberField;
import org.lateralgm.components.impl.ResNode;
import org.lateralgm.messages.Messages;
import org.lateralgm.resources.Timeline;
import org.lateralgm.resources.Timeline.PTimeline;
import org.lateralgm.resources.sub.Moment;

public class TimelineFrame extends InstantiableResourceFrame<Timeline,PTimeline> implements
		ListSelectionListener
	{
	private static final long serialVersionUID = 1L;

	public JButton add, change, delete, duplicate;
	public JButton shift, merge, clear;

	public JList moments;
	public ActionList actions;
	public GMLTextArea code;

	public TimelineFrame(Timeline res, ResNode node)
		{
		super(res,node);
		GroupLayout layout = new GroupLayout(getContentPane());
		setLayout(layout);

		JPanel side1 = new JPanel();
		makeSide1(side1);

		JPanel side2 = new JPanel(new BorderLayout());
		side2.setMaximumSize(new Dimension(90,Integer.MAX_VALUE));
		JLabel lab = new JLabel(Messages.getString("TimelineFrame.MOMENTS")); //$NON-NLS-1$
		side2.add(lab,BorderLayout.NORTH);
		moments = new JList(res.moments.toArray());
		moments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		moments.addListSelectionListener(this);
		JScrollPane scroll = new JScrollPane(moments);
		scroll.setPreferredSize(new Dimension(90,260));
		side2.add(scroll,BorderLayout.CENTER);

		JComponent editor;
		if (false)
			{
			code = new GMLTextArea(""); //$NON-NLS-1$
			editor = new JScrollPane(code);
			}
		else
			{
			actions = new ActionList(this);
			editor = new ActionListEditor(actions);
			}

		layout.setHorizontalGroup(layout.createSequentialGroup()
		/**/.addComponent(side1,DEFAULT_SIZE,PREFERRED_SIZE,PREFERRED_SIZE)
		/**/.addComponent(side2)
		/**/.addComponent(editor));
		layout.setVerticalGroup(layout.createParallelGroup()
		/**/.addComponent(side1)
		/**/.addComponent(side2)
		/**/.addComponent(editor));

		pack();

		moments.setSelectedIndex(0);
		}

	private void makeSide1(JPanel side1)
		{
		GroupLayout layout = new GroupLayout(side1);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		side1.setLayout(layout);

		JLabel lab = new JLabel(Messages.getString("TimelineFrame.NAME")); //$NON-NLS-1$

		add = new JButton(Messages.getString("TimelineFrame.ADD")); //$NON-NLS-1$
		add.addActionListener(this);
		change = new JButton(Messages.getString("TimelineFrame.CHANGE")); //$NON-NLS-1$
		change.addActionListener(this);
		delete = new JButton(Messages.getString("TimelineFrame.DELETE")); //$NON-NLS-1$
		delete.addActionListener(this);
		duplicate = new JButton(Messages.getString("TimelineFrame.DUPLICATE")); //$NON-NLS-1$
		duplicate.addActionListener(this);

		shift = new JButton(Messages.getString("TimelineFrame.SHIFT")); //$NON-NLS-1$
		shift.addActionListener(this);
		merge = new JButton(Messages.getString("TimelineFrame.MERGE")); //$NON-NLS-1$
		merge.addActionListener(this);
		clear = new JButton(Messages.getString("TimelineFrame.CLEAR")); //$NON-NLS-1$
		clear.addActionListener(this);

		save.setText(Messages.getString("TimelineFrame.SAVE")); //$NON-NLS-1$

		layout.setHorizontalGroup(layout.createParallelGroup()
		/**/.addGroup(layout.createSequentialGroup()
		/*		*/.addComponent(lab)
		/*		*/.addComponent(name,DEFAULT_SIZE,120,MAX_VALUE))
		/**/.addGroup(layout.createSequentialGroup()
		/*		*/.addGroup(layout.createParallelGroup()
		/*				*/.addComponent(add,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE)
		/*				*/.addComponent(delete,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE))
		/*		*/.addGroup(layout.createParallelGroup()
		/*				*/.addComponent(change,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE)
		/*				*/.addComponent(duplicate,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE)))
		/**/.addGroup(layout.createSequentialGroup()
		/*		*/.addComponent(shift,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE)
		/*		*/.addComponent(merge,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE))
		/**/.addComponent(clear,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE)
		/**/.addComponent(save,DEFAULT_SIZE,DEFAULT_SIZE,MAX_VALUE));
		layout.setVerticalGroup(layout.createSequentialGroup()
		/**/.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		/*		*/.addComponent(lab)
		/*		*/.addComponent(name))
		/**/.addGap(32)
		/**/.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		/*		*/.addComponent(add)
		/*		*/.addComponent(change))
		/**/.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		/*		*/.addComponent(delete)
		/*		*/.addComponent(duplicate))
		/**/.addGap(32)
		/**/.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		/*		*/.addComponent(shift)
		/*		*/.addComponent(merge))
		/**/.addComponent(clear)
		/**/.addGap(32,32,MAX_VALUE)
		/**/.addComponent(save));
		}

	protected boolean areResourceFieldsEqual()
		{
		return new ResourceComparator().areEqual(res.moments,resOriginal.moments);
		}

	public void commitChanges()
		{
		actions.save();
		res.setName(name.getText());
		}

	public void actionPerformed(ActionEvent e)
		{
		if (!(e.getSource() instanceof JButton))
			{
			super.actionPerformed(e);
			return;
			}
		JButton but = (JButton) e.getSource();
		if (but == add || but == change || but == duplicate)
			{
			Moment m = (Moment) moments.getSelectedValue();
			if (m == null && but != add) return;
			int sn = (m == null) ? -1 : m.stepNo;
			String msg = Messages.getString("TimelineFrame.MOM_NUM"); //$NON-NLS-1$
			String ttl;
			if (but == add)
				ttl = "TimelineFrame.MOM_ADD"; //$NON-NLS-1$
			else if (but == change)
				ttl = "TimelineFrame.MOM_CHANGE"; //$NON-NLS-1$
			else
				ttl = "TimelineFrame.MOM_DUPLICATE"; //$NON-NLS-1$
			ttl = Messages.getString(ttl);
			JPanel pane = new JPanel();
			pane.add(new JLabel(msg));
			NumberField field = new NumberField(sn + (but == add ? 1 : 0));
			//			field.setPreferredSize(new Dimension(80,20));
			pane.add(field);
			int ret = JOptionPane.showConfirmDialog(this,pane,ttl,JOptionPane.OK_CANCEL_OPTION);
			if (ret == JOptionPane.CANCEL_OPTION) return;
			ret = field.getIntValue();
			if (ret == sn) return;
			int p = -Collections.binarySearch(res.moments,ret) - 1;
			if (but == add)
				{
				if (p < 0)
					{
					moments.setSelectedIndex(-p);
					return;
					}
				Moment m2 = new Moment();
				m2.stepNo = ret;
				res.moments.add(p,m2);
				}
			else if (but == change)
				{
				if (p < 0)
					{
					JOptionPane.showMessageDialog(this,Messages.getString("TimelineFrame.MOM_EXIST")); //$NON_NLS-1$ //$NON-NLS-1$
					return;
					}
				if (ret > sn) p--;
				m.stepNo = ret;
				res.moments.remove(moments.getSelectedIndex()); //should never be -1
				res.moments.add(p,m);
				}
			else
				{
				if (p >= 0)
					{
					JOptionPane.showMessageDialog(this,Messages.getString("TimelineFrame.MOM_EXIST")); //$NON_NLS-1$ //$NON-NLS-1$
					return;
					}
				Moment m2 = m.copy();
				m2.stepNo = ret;
				res.moments.add(p,m2);
				}
			moments.setListData(res.moments.toArray());
			moments.setSelectedIndex(p);
			return;
			}
		if (but == delete)
			{
			int p = moments.getSelectedIndex();
			if (p == -1) return;
			if (res.moments.get(p).actions.size() != 0)
				{
				String msg = Messages.getString("TimelineFrame.MOM_DELETE"); //$NON-NLS-1$
				String ttl = Messages.getString("TimelineFrame.MOM_CONFIRM"); //$NON-NLS-1$
				int r = JOptionPane.showConfirmDialog(this,msg,ttl,JOptionPane.YES_NO_OPTION);
				if (r == JOptionPane.NO_OPTION) return;
				}
			res.moments.remove(p);
			moments.setListData(res.moments.toArray());
			moments.setSelectedIndex(Math.min(res.moments.size() - 1,p));
			return;
			}
		if (but == clear)
			{
			if (res.moments.size() == 0) return;
			String msg = Messages.getString("TimelineFrame.MOM_CLEAR"); //$NON-NLS-1$
			String ttl = Messages.getString("TimelineFrame.MOM_CONFIRM"); //$NON-NLS-1$
			int r = JOptionPane.showConfirmDialog(this,msg,ttl,JOptionPane.YES_NO_OPTION);
			if (r == JOptionPane.NO_OPTION) return;
			res.moments.clear();
			moments.setListData(res.moments.toArray());
			}
		if (but == shift)
			{
			if (res.moments.size() == 0) return;
			Moment m = (Moment) moments.getSelectedValue();
			int sn = (m == null) ? 0 : m.stepNo;
			String ttl = Messages.getString("TimelineFrame.MOM_SHIFT"); //$NON-NLS-1$
			JPanel pane = new JPanel(new GridLayout(0,2));
			pane.add(new JLabel(Messages.getString("TimelineFrame.MOM_START"))); //$NON-NLS-1$
			NumberField iStart = new NumberField(sn);
			iStart.setPreferredSize(new Dimension(80,20));
			pane.add(iStart);
			pane.add(new JLabel(Messages.getString("TimelineFrame.MOM_END"))); //$NON-NLS-1$
			NumberField iEnd = new NumberField(sn);
			iEnd.setPreferredSize(new Dimension(80,20));
			pane.add(iEnd);
			pane.add(new JLabel(Messages.getString("TimelineFrame.MOM_AMOUNT"))); //$NON-NLS-1$
			NumberField iAmt = new NumberField(1);
			iAmt.setPreferredSize(new Dimension(80,20));
			pane.add(iAmt);
			int ret = JOptionPane.showConfirmDialog(this,pane,ttl,JOptionPane.OK_CANCEL_OPTION);
			if (ret == JOptionPane.CANCEL_OPTION) return;
			int p = res.shiftMoments(iStart.getIntValue(),iEnd.getIntValue(),iAmt.getIntValue());
			moments.setListData(res.moments.toArray());
			//this is actually the *old* position of first shifted moment, the same as GM does it.
			//if we wanted to, we could find the new position, but it's a lot of work for nothing
			moments.setSelectedIndex(p);
			return;
			}
		if (but == merge)
			{
			if (res.moments.size() == 0) return;
			Moment m = (Moment) moments.getSelectedValue();
			int sn = (m == null) ? 0 : m.stepNo;
			String ttl = Messages.getString("TimelineFrame.MOM_MERGE"); //$NON-NLS-1$
			JPanel pane = new JPanel(new GridLayout(0,2));
			pane.add(new JLabel(Messages.getString("TimelineFrame.MOM_START"))); //$NON-NLS-1$
			NumberField iStart = new NumberField(0,Integer.MAX_VALUE,sn);
			iStart.setPreferredSize(new Dimension(80,20));
			pane.add(iStart);
			pane.add(new JLabel(Messages.getString("TimelineFrame.MOM_END"))); //$NON-NLS-1$
			NumberField iEnd = new NumberField(0,Integer.MAX_VALUE,sn);
			iEnd.setPreferredSize(new Dimension(80,20));
			pane.add(iEnd);
			int ret = JOptionPane.showConfirmDialog(this,pane,ttl,JOptionPane.OK_CANCEL_OPTION);
			if (ret == JOptionPane.CANCEL_OPTION) return;
			actions.save(); //prevents "fresh" actions from being overwritten
			int p = res.mergeMoments(iStart.getIntValue(),iEnd.getIntValue());
			moments.setListData(res.moments.toArray());
			moments.setSelectedIndex(p);
			return;
			}
		super.actionPerformed(e);
		}

	//Moments selection changed
	public void valueChanged(ListSelectionEvent e)
		{
		if (e.getValueIsAdjusting()) return;
		Moment m = (Moment) moments.getSelectedValue();
		actions.setActionContainer(m);
		}

	@Override
	public Dimension getMinimumSize()
		{
		Dimension p = getContentPane().getSize();
		Dimension l = getContentPane().getMinimumSize();
		Dimension s = getSize();
		l.width += s.width - p.width;
		l.height += s.height - p.height;
		return l;
		}
	}
