/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class DoneMessagePanel extends JPanel{
    
    public DoneMessagePanel(){
        this.setBackground(new Color(0.5f, 0.5f, 1f));
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel done = new JLabel("D O N E !");
        done.setFont(new Font("",Font.BOLD,19));
        this.add(done);
    }
}
