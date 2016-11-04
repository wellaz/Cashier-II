/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JLabel;

/**
 *
 * @author Wellington
 */
public class TimeListener implements ActionListener {
    public JLabel timelbl = new JLabel();

        @Override
        public void actionPerformed(ActionEvent e) {
            Calendar now = Calendar.getInstance();
            int hr = now.get(Calendar.HOUR_OF_DAY);
            int min = now.get(Calendar.MINUTE);
            int sec = now.get(Calendar.SECOND);
            int AM_PM = now.get(Calendar.AM_PM);

            String day_night;
            if (AM_PM == 1) {
                day_night = "PM";
            } else {
                day_night = "AM";
            }
            timelbl.setText("TIME " + hr + ":" + min + ":" + sec + " " + day_night);
        }
    }
