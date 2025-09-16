import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
public class cgpa extends JFrame {
    private JTextField[] semesters;
    private JTextField targetCGPA;
    private JLabel result;
    private final float[] credits = {19.5f, 22.5f, 20f, 21f, 26f, 18f, 16f, 17f};
    public cgpa() {
        setTitle("CGPA Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(11, 2, 5, 5));
        semesters = new JTextField[8];
        for (int i = 0; i < 8; i++) {
            add(new JLabel("Semester " + (i + 1) + " SGPA:"));
            semesters[i] = new JTextField();
            add(semesters[i]);
        }
        add(new JLabel("Target CGPA (optional):"));
        targetCGPA = new JTextField();
        add(targetCGPA);
        JButton calc = new JButton("Calculate");
        calc.addActionListener(new Calculate());
        add(new JLabel());
        add(calc);
        result = new JLabel("CGPA: ");
        add(new JLabel());
        add(result);
        getContentPane().setBackground(new Color(240, 240, 240));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private class Calculate implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                float total = 0;
                float creditSum = 0;
                int nextSemester = -1;
                for (int i = 0; i < 8; i++) {
                    String text = semesters[i].getText().trim();
                    if (!text.isEmpty()) {
                        float sgpa = Float.parseFloat(text);
                        if (sgpa < 0 || sgpa > 10) {
                            JOptionPane.showMessageDialog(cgpa.this, "SGPA must be 0-10", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        total += sgpa * credits[i];
                        creditSum += credits[i];
                    } else if (nextSemester == -1) {
                        nextSemester = i; // first empty semester
                    }
                }
                float currentCGPA = total / creditSum;
                DecimalFormat df = new DecimalFormat("#.##");
                result.setText("Current CGPA: " + df.format(currentCGPA));

                String targetText = targetCGPA.getText().trim();
                if (!targetText.isEmpty() && nextSemester != -1) {
                    float target = Float.parseFloat(targetText);
                    if (target < 0 || target > 10) {
                        JOptionPane.showMessageDialog(cgpa.this, "Target CGPA must be 0-10", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    float neededSGPA = (target * (creditSum + credits[nextSemester]) - total) / credits[nextSemester];
                    if (neededSGPA > 10) {
                        semesters[nextSemester].setText("Impossible");
                    } else if (neededSGPA < 0) {
                        semesters[nextSemester].setText("Already reached");
                    } else {
                        semesters[nextSemester].setText(df.format(neededSGPA));
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(cgpa.this, "Enter valid numbers only", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(cgpa::new);
    }
}
