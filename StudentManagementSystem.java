import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.io.*;


public class StudentManagementSystem {
        private static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(248, 249, 250));
        field.setBackground(new Color(52, 58, 64));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(86, 182, 194), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(248, 249, 250));
        field.setBackground(new Color(52, 58, 64));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(86, 182, 194), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(248, 249, 250));
        return label;
}
   public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
            File usersFile = new File(FileHandler.USERS_FILE);
            if (!usersFile.exists()) {
                // Create a modern panel with gradient background
                JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                        int w = getWidth();
                        int h = getHeight();
                        GradientPaint gp = new GradientPaint(0, 0, new Color(40, 44, 52), w, h, new Color(86, 182, 194));
                        g2d.setPaint(gp);
                        g2d.fillRect(0, 0, w, h);
                    }
                };
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(86, 182, 194), 2),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
                panel.setOpaque(false);

                // Style text fields and labels
                JTextField idField = createStyledTextField();
                JTextField nameField = createStyledTextField();
                JPasswordField passwordField = createStyledPasswordField();

                // Create styled labels
                JLabel[] labels = {
                    createStyledLabel("Admin ID:"),
                    createStyledLabel("Admin Name:"),
                    createStyledLabel("Password:")
                };

                // Add components with styled appearance
                panel.add(labels[0]);
                panel.add(idField);
                panel.add(labels[1]);
                panel.add(nameField);
                panel.add(labels[2]);
                panel.add(passwordField);

                // Create a custom option pane with styled appearance
                JOptionPane optionPane = new JOptionPane(
                    panel,
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION
                ) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        GradientPaint gp = new GradientPaint(0, 0, new Color(40, 44, 52), getWidth(), getHeight(), new Color(33, 37, 41));
                        g2d.setPaint(gp);
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                        super.paintComponent(g);
                    }
                };

                JDialog dialog = optionPane.createDialog("Create Initial Admin Account");
                dialog.setBackground(new Color(40, 44, 52));

                // Show dialog and process result
                dialog.setVisible(true);
                int result = (Integer) optionPane.getValue();

                if (result == JOptionPane.OK_OPTION) {
                    String id = idField.getText();
                    String name = nameField.getText();
                    String password = new String(passwordField.getPassword());

                    if (!id.isEmpty() && !name.isEmpty() && !password.isEmpty()) {
                        FileHandler.saveUser(id, name, password, "Admin");
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "All fields are required!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }
            new MainPage();
        });
    }
}


class MainPage extends JFrame {
    public MainPage() {
        setTitle("School Management System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0, 0, 0));

        // Updated title styling
        JLabel title = new JLabel("Student Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(248, 249, 250));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        
        // Create main panel with padding and background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(181,181,181));
        //mainPanel.setForeground(new Color(248,249,250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create buttons with updated styling
        JButton[] buttons = {
            createStyledButton("Student"),
            createStyledButton("Teacher"),
            createStyledButton("Admin"),
            createStyledButton("Exit")
        };
        
        // Add buttons to panel with updated layout
        for (int i = 0; i < buttons.length; i++) {
            gbc.gridy = i;
            mainPanel.add(buttons[i], gbc);
            
            final int index = i;
            buttons[i].addActionListener(e -> {
                switch(index) {
                    case 0: new StudentLogin(); break;
                    case 1: new TeacherLogin(); break;
                    case 2: new AdminLogin(); break;
                    case 3: System.exit(0); break;
                }
            });
        }
        
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 60));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 105, 217));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));
            }
        });
        
        return button;
    }
}
class FileHandler {
    public static final String USERS_FILE = "users.txt";
    private static final String MARKS_FILE = "marks.txt";
    
    public static void saveUser(String id, String name, String password, String role) {
        try (FileWriter fw = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("%s,%s,%s,%s,Active\n", id, name, password, role));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean validateLogin(String id, String password, String role) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id) && parts[2].equals(password) && 
                    parts[3].equals(role) && parts[4].equals("Active")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void saveMark(String studentId, String subject, String mark, String teacherId) {
        try (FileWriter fw = new FileWriter(MARKS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("%s,%s,%s,%s\n", studentId, subject, mark, teacherId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String[]> getMarks(String studentId) {
        List<String[]> marks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MARKS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(studentId)) {
                    marks.add(new String[]{parts[1], parts[2]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return marks;
    }
    
    public static List<String[]> getTeacherStudents(String teacherId) {
        List<String[]> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[3].equals("Student") && parts[4].equals("Active")) {
                    students.add(new String[]{parts[0], parts[1]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }
    public static boolean resetPassword(String userId, String newPassword) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(userId)) {
                    // Replace the password (index 2) with new password
                    line = parts[0] + "," + parts[1] + "," + newPassword + "," + parts[3] + "," + parts[4];
                    found = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
                for (String line : lines) {
                    bw.write(line + "\n");
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    

}
class StudentLogin extends JFrame {
    private String studentId;
    public StudentLogin() {
    setTitle("Student Login");
        setSize(700, 600);
        setLocationRelativeTo(null);
        
        // Create main panel with gradient background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 44, 52), getWidth(), getHeight(), new Color(86, 182, 194));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
            }
        };
        panel.setOpaque(false);
        
        // Add title label
        JLabel titleLabel = new JLabel("Student Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(248, 249, 250));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 20, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth for other components
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Add components
        addComponent(panel, new JLabel("Student ID:"), gbc, 0, 1);
        JTextField idField = new JTextField(20);
        addComponent(panel, idField, gbc, 1, 1);

        addComponent(panel, new JLabel("Password:"), gbc, 0, 2);
        JPasswordField passwordField = new JPasswordField(20);
        addComponent(panel, passwordField, gbc, 1, 2);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());
            if (FileHandler.validateLogin(id, password, "Student")) {
                this.studentId = id;
                showStudentDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
    
            }
                                
        });
        addComponent(panel, loginButton, gbc, 0, 3, 1, 1);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        addComponent(panel, backButton, gbc, 1, 3, 1, 1);
        add(panel);
        setVisible(true);
    }
    
    private void addComponent(JPanel panel, JComponent component, 
                            GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(component, gbc);
    }
    
    private void addComponent(JPanel panel, JComponent component, 
                            GridBagConstraints gbc, int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        panel.add(component, gbc);
    }
    
    private void showStudentDashboard() {
        Frame dashboard = new JFrame("Student Dashboard");
        dashboard.setSize(700, 600);
        dashboard.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dashboard.dispose();
            new MainPage();
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);
        mainPanel.add(topPanel, BorderLayout.SOUTH);
        mainPanel.setBackground(new Color(181,181,181));
        mainPanel.setForeground(new Color(248,249,250)); 
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Marks Panel
        JPanel marksPanel = new JPanel(new BorderLayout());
        String[] columns = {"Subject", "Mark"};
        JTable marksTable = new JTable(new DefaultTableModel(columns, 0));
        marksPanel.add(new JScrollPane(marksTable), BorderLayout.CENTER);
        marksPanel.setBackground(new Color(181,181,181));
        marksPanel.setForeground(new Color(248,249,250)); 
        
        // Subjects Panel
        JPanel subjectsPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> subjectsList = new DefaultListModel<>();
        subjectsList.addElement("Mathematics");
        subjectsList.addElement("Database");
        subjectsList.addElement("Digital Logic Devices");
        subjectsList.addElement("Object oriented programming");
        subjectsList.addElement("Fndamentals of software Enginerring");
        JList<String> subjects = new JList<>(subjectsList);
        subjectsPanel.add(new JScrollPane(subjects), BorderLayout.CENTER);
        subjectsPanel.setBackground(new Color(181,181,181));
        subjectsPanel.setForeground(new Color(248,249,250));
        
        tabbedPane.addTab("Marks", marksPanel);
        tabbedPane.addTab("Subjects", subjectsPanel);
        
        dashboard.add(tabbedPane);
        dashboard.setVisible(true);
        List<String[]> studentMarks = FileHandler.getMarks(studentId);
        DefaultTableModel model = (DefaultTableModel) marksTable.getModel();
        for (String[] mark : studentMarks) {
            model.addRow(mark);
        }
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        dashboard.add(mainPanel);
        dashboard.setVisible(true);
    }
}

class TeacherLogin extends JFrame {
    private String teacherId;
    public TeacherLogin() {
        setTitle("Teacher Login");
        setSize(700, 600);
        setLocationRelativeTo(null);
        
        // Create main panel with gradient background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 44, 52), getWidth(), getHeight(), new Color(86, 182, 194));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);
        
        // Add title label
        JLabel titleLabel = new JLabel("Teacher Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(248, 249, 250));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 20, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth for other components
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        // Add components
        addComponent(panel, new JLabel("Teacher ID:"), gbc, 0, 1);
        JTextField idField = new JTextField(20);
        addComponent(panel, idField, gbc, 1, 1);

        addComponent(panel, new JLabel("Password:"), gbc, 0, 2);
        
        JPasswordField passwordField = new JPasswordField(20);
        addComponent(panel, passwordField, gbc, 1, 2);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());  
            if (FileHandler.validateLogin(id, password, "Teacher")) { 
                this.teacherId = id;
                showTeacherDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
    
        });
        addComponent(panel, loginButton, gbc, 0, 3, 1,1);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        addComponent(panel, backButton, gbc, 1, 3, 1, 1);
        
        add(panel);
        setVisible(true);
                
        
    }
    
    private void addComponent(JPanel panel, JComponent component, 
                            GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(component, gbc);
    }
    
    private void addComponent(JPanel panel, JComponent component, 
                            GridBagConstraints gbc, int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        panel.add(component, gbc);
    }

    
    private void showTeacherDashboard() {
        JFrame dashboard = new JFrame("Teacher Dashboard");
        dashboard.setSize(800, 600);
        dashboard.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(181,181,181));
        mainPanel.setForeground(new Color(248,249,250));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dashboard.dispose();
            new MainPage();
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);
        mainPanel.add(topPanel, BorderLayout.SOUTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Students Panel
        JPanel studentsPanel = new JPanel(new BorderLayout());
        String[] studentColumns = {"Student ID", "Name", "Class"};
        DefaultTableModel studentModel = new DefaultTableModel(studentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        List<String[]> students = FileHandler.getTeacherStudents(teacherId);
        for (String[] student : students) {
            studentModel.addRow(new Object[]{student[0], student[1], "Class A"});
        }
        
        JTable studentsTable = new JTable(studentModel);
        studentsPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);
        // Marks Assignment Panel
        JPanel marksPanel = new JPanel(new BorderLayout());
        JPanel marksControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Add controls for mark assignment
        JComboBox<String> studentSelect = new JComboBox<>(new String[]{"Select Student", "S001 - John Doe", "S002 - Jane Smith", "S003 - Bob Wilson"});
        JComboBox<String> subjectSelect = new JComboBox<>(new String[]{"Select Subject", "Mathematics", "Digital Logic Devices", "Object oreinted Programming","Fundamental of Software Engineering","Database"});
        JTextField markField = new JTextField(5);
        JButton assignButton = new JButton("Assign/Update Mark");
        
        marksControlPanel.add(new JLabel("Student: "));
        marksControlPanel.add(studentSelect);
        marksControlPanel.add(new JLabel("Subject: "));
        marksControlPanel.add(subjectSelect);
        marksControlPanel.add(new JLabel("Mark: "));
        marksControlPanel.add(markField);
        marksControlPanel.add(assignButton);

        
        String[] marksColumns = {"Student ID", "Student Name", "Subject", "Mark"};
        DefaultTableModel marksModel = new DefaultTableModel(marksColumns, 0);
        JTable marksTable = new JTable(marksModel);
        
        assignButton.addActionListener(e -> {
            if (studentSelect.getSelectedIndex() > 0 && subjectSelect.getSelectedIndex() > 0) {
                String studentInfo = (String) studentSelect.getSelectedItem();
                String studentId = studentInfo.split(" - ")[0];
                String studentName = studentInfo.split(" - ")[1];
                String subject = (String) subjectSelect.getSelectedItem();
                String mark = markField.getText();
                
                // Check if mark already exists for this student and subject
                boolean found = false;
                for (int i = 0; i < marksModel.getRowCount(); i++) {
                    if (marksModel.getValueAt(i, 0).equals(studentId) && 
                        marksModel.getValueAt(i, 2).equals(subject)) {
                        marksModel.setValueAt(mark, i, 3);
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    marksModel.addRow(new Object[]{studentId, studentName, subject, mark});
                }
                
                markField.setText("");
                studentSelect.setSelectedIndex(0);
                subjectSelect.setSelectedIndex(0);
            }
        });
        
        marksPanel.add(marksControlPanel, BorderLayout.NORTH);
        marksPanel.add(new JScrollPane(marksTable), BorderLayout.CENTER);
      
        
        tabbedPane.addTab("Students", studentsPanel);
        tabbedPane.addTab("Marks", marksPanel);
        
        dashboard.add(tabbedPane);
        dashboard.setVisible(true);
        
        String[] studentOptions = new String[students.size() + 1];
        studentOptions[0] = "Select Student";
        for (int i = 0; i < students.size(); i++) {
            String[] student = students.get(i);
            studentOptions[i + 1] = student[0] + " - " + student[1];
        }
        studentSelect.setModel(new DefaultComboBoxModel<>(studentOptions));
        
        // Update assign button action
        assignButton.addActionListener(e -> {
            if (studentSelect.getSelectedIndex() > 0 && subjectSelect.getSelectedIndex() > 0) {
                String studentInfo = (String) studentSelect.getSelectedItem();
                String studentId = studentInfo.split(" - ")[0];
                String subject = (String) subjectSelect.getSelectedItem();
                String mark = markField.getText();
                
                if (mark.matches("\\d+")) {
                    FileHandler.saveMark(studentId, subject, mark, teacherId);
                    marksModel.addRow(new Object[]{studentId, 
                                                  studentInfo.split(" - ")[1], 
                                                  subject, 
                                                  mark});
                    markField.setText("");
                    studentSelect.setSelectedIndex(0);
                    subjectSelect.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(dashboard, "Please enter a valid numeric mark");
                }
            } else {
                JOptionPane.showMessageDialog(dashboard, "Please select both student and subject");
            }
        });
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        dashboard.add(mainPanel);
        dashboard.setVisible(true);
    }
    
}

class AdminLogin extends JFrame {
    private String adminId;
    public AdminLogin(){
        setTitle("Student Login");
        setSize(700, 600);
        setLocationRelativeTo(null);
        
        // Create main panel with gradient background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 44, 52), getWidth(), getHeight(), new Color(86, 182, 194));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);
        
        // Add title label
        JLabel titleLabel = new JLabel("Student Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(248, 249, 250));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 20, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth for other components
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        
        
        addComponent(panel, new JLabel("Admin ID:"), gbc, 0, 1);
        JTextField idField = new JTextField(20);
        addComponent(panel, idField, gbc, 1, 1);
        
        addComponent(panel, new JLabel("Password:"), gbc, 0, 2);
        JPasswordField passwordField = new JPasswordField(20);
        addComponent(panel, passwordField, gbc, 1, 2);
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());
        
            if (FileHandler.validateLogin(id, password, "Admin")) {
                showAdminDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });
        addComponent(panel, loginButton, gbc, 0, 3, 1, 1);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        addComponent(panel, backButton, gbc, 1, 3,1, 1);
        
        add(panel);
        setVisible(true);
    }
        
    private void addComponent(JPanel panel, JComponent component, 
                            GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(component, gbc);
    }
    
    private void addComponent(JPanel panel, JComponent component, 
                            GridBagConstraints gbc, int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        panel.add(component, gbc);
    }
    
    private void showAdminDashboard() {
        JFrame dashboard = new JFrame("Admin Dashboard");
        dashboard.setSize(800, 600);
        dashboard.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(181,181,181));
        mainPanel.setForeground(new Color(248,249,250));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dashboard.dispose();
            new MainPage();
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);
        mainPanel.add(topPanel, BorderLayout.SOUTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
               // Users Management Panel
        JPanel usersPanel = new JPanel(new BorderLayout());
        String[] userColumns = {"ID", "Name", "Role", "Status"};
        DefaultTableModel userModel = new DefaultTableModel(userColumns, 0);
        JTable usersTable = new JTable(userModel);
         try (BufferedReader br = new BufferedReader(new FileReader(FileHandler.USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                userModel.addRow(new Object[]{parts[0], parts[1], parts[3], parts[4]});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(dashboard, "Error loading users: " + e.getMessage());
        }
        
        usersPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        usersPanel.setBackground(new Color(181,181,181));
        usersPanel.setForeground(new Color(248,249,250));
        
        
        // Account Management Panel
        JPanel accountPanel = new JPanel(new GridBagLayout());
        accountPanel.setBackground(new Color(181,181,181));
        accountPanel.setForeground(new Color(248,249,250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton createAccountBtn = new JButton("Create New Account");
        JButton resetPasswordBtn = new JButton("Reset Password");
        JButton deactivateAccountBtn = new JButton("Deactivate Account");
        
        gbc.gridy = 0; accountPanel.add(createAccountBtn, gbc);
        gbc.gridy = 1; accountPanel.add(resetPasswordBtn, gbc);
        gbc.gridy = 2; accountPanel.add(deactivateAccountBtn, gbc);
        
                // Create Account Panel
        JPanel createAccountPanel = new JPanel(new GridBagLayout());
        createAccountPanel.setBorder(BorderFactory.createTitledBorder("Create New Account"));
        createAccountPanel.setBackground(new Color(181,181,181));
        createAccountPanel.setForeground(new Color(248,249,250));

        JTextField newIdField = new JTextField(15);
        JTextField newNameField = new JTextField(15);
        JPasswordField newPasswordField = new JPasswordField(15);
        JComboBox<String> roleSelect = new JComboBox<>(new String[]{"Student", "Teacher", "Admin"});
        JButton createButton = new JButton("Create Account");
        JButton createBackButton = new JButton("Back");
        
        addFormField(createAccountPanel, "ID:", newIdField, 0);
        addFormField(createAccountPanel, "Name:", newNameField, 1);
        addFormField(createAccountPanel, "Password:", newPasswordField, 2);
        addFormField(createAccountPanel, "Role:", roleSelect, 3);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        createAccountPanel.add(createButton, gbc);
        gbc.gridx = 1;
        createAccountPanel.add(createBackButton, gbc);
        
        createBackButton.addActionListener(e -> {
            accountPanel.removeAll();
            gbc.gridy = 0; accountPanel.add(createAccountBtn, gbc);
            gbc.gridy = 1; accountPanel.add(resetPasswordBtn, gbc);
            gbc.gridy = 2; accountPanel.add(deactivateAccountBtn, gbc);
            accountPanel.revalidate();
            accountPanel.repaint();
        });
        
        // Reset Password Panel
        JPanel resetPanel = new JPanel(new GridBagLayout());
        resetPanel.setBorder(BorderFactory.createTitledBorder("Reset Password"));
        resetPanel.setBackground(new Color(181,181,181));
        resetPanel.setForeground(new Color(248,249,250));

        JTextField resetIdField = new JTextField(15);
        JPasswordField newPassField = new JPasswordField(15);
        JButton resetButton = new JButton("Reset Password");
        JButton resetBackButton = new JButton("Back");
        
        addFormField(resetPanel, "User ID:", resetIdField, 0);
        addFormField(resetPanel, "New Password:", newPassField, 1);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        resetPanel.add(resetButton, gbc);
        gbc.gridx = 1;
        resetPanel.add(resetBackButton, gbc);
        
        resetBackButton.addActionListener(e -> {
            accountPanel.removeAll();
            gbc.gridy = 0; accountPanel.add(createAccountBtn, gbc);
            gbc.gridy = 1; accountPanel.add(resetPasswordBtn, gbc);
            gbc.gridy = 2; accountPanel.add(deactivateAccountBtn, gbc);
            accountPanel.revalidate();
            accountPanel.repaint();
        });
        
        // Deactivate Account Panel
        JPanel deactivatePanel = new JPanel(new GridBagLayout());
        deactivatePanel.setBorder(BorderFactory.createTitledBorder("Deactivate Account"));
        deactivatePanel.setBackground(new Color(181,181,181));
        deactivatePanel.setForeground(new Color(248,249,250));

        JTextField deactivateIdField = new JTextField(15);
        JButton deactivateButton = new JButton("Deactivate Account");
        JButton deactivateBackButton = new JButton("Back");
        
        addFormField(deactivatePanel, "User ID:", deactivateIdField, 0);
        
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        deactivatePanel.add(deactivateButton, gbc);
        gbc.gridx = 1;
        deactivatePanel.add(deactivateBackButton, gbc);
        
        deactivateBackButton.addActionListener(e -> {
            accountPanel.removeAll();
            gbc.gridy = 0; accountPanel.add(createAccountBtn, gbc);
            gbc.gridy = 1; accountPanel.add(resetPasswordBtn, gbc);
            gbc.gridy = 2; accountPanel.add(deactivateAccountBtn, gbc);
            accountPanel.revalidate();
            accountPanel.repaint();
        });
        
        // Add action listeners to main buttons
        createAccountBtn.addActionListener(e -> {
            accountPanel.removeAll();
            accountPanel.add(createAccountPanel);
            accountPanel.revalidate();
            accountPanel.repaint();
        });
        
        resetPasswordBtn.addActionListener(e -> {
            accountPanel.removeAll();
            accountPanel.add(resetPanel);
            accountPanel.revalidate();
            accountPanel.repaint();
        });
        
        deactivateAccountBtn.addActionListener(e -> {
            accountPanel.removeAll();
            accountPanel.add(deactivatePanel);
            accountPanel.revalidate();
            accountPanel.repaint();
        });
        
        // Keep the existing action listeners for the form buttons
        createButton.addActionListener(e -> {
            String id = newIdField.getText();
            String name = newNameField.getText();
            String password = new String(newPasswordField.getPassword());
            String role = (String) roleSelect.getSelectedItem();
            if (!id.isEmpty() && !name.isEmpty() && !password.isEmpty()) {
                FileHandler.saveUser(id, name, password, role);
                userModel.addRow(new Object[]{id, name, role, "Active"});
                newIdField.setText("");
                newNameField.setText("");
                newPasswordField.setText("");
                roleSelect.setSelectedIndex(0);
            }
        });
        
        resetButton.addActionListener(e -> {
            String id = resetIdField.getText();
            String newPassword = new String(newPassField.getPassword());
            if (!id.isEmpty() && !newPassword.isEmpty()) {
                if (FileHandler.resetPassword(id, newPassword)) {
                    JOptionPane.showMessageDialog(dashboard, 
                        "Password reset successful for user: " + id);
                    resetIdField.setText("");
                    newPassField.setText("");
                } else {
                    JOptionPane.showMessageDialog(dashboard,
                        "Failed to reset password. User ID not found or error occurred.");
                }
            } else {
                JOptionPane.showMessageDialog(dashboard, "Please fill all fields!");
            }
        });
        
        deactivateButton.addActionListener(e -> {
            String id = deactivateIdField.getText();
            if (!id.isEmpty()) {
                for (int i = 0; i < userModel.getRowCount(); i++) {
                    if (userModel.getValueAt(i, 0).equals(id)) {
                        userModel.setValueAt("Inactive", i, 3);
                        break;
                    }
                }
                deactivateIdField.setText("");
            }
        });
        
        // Add all panels to account management
     
        tabbedPane.addTab("Users", usersPanel);
        tabbedPane.addTab("Account Management", accountPanel);
        
        dashboard.add(tabbedPane);
        dashboard.setVisible(true);
        createButton.addActionListener(e -> {
            String id = newIdField.getText();
            String name = newNameField.getText();
            String password = new String(newPasswordField.getPassword());
            String role = (String) roleSelect.getSelectedItem();
            
            if (!id.isEmpty() && !name.isEmpty() && password.length() > 0) {
                FileHandler.saveUser(id, name, password, role);
                userModel.addRow(new Object[]{id, name, role, "Active"});
                JOptionPane.showMessageDialog(dashboard, "User created successfully!");
                
                newIdField.setText("");
                newNameField.setText("");
                newPasswordField.setText("");
                roleSelect.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(dashboard, "Please fill all fields!");
            }
        });
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        dashboard.add(mainPanel);
        dashboard.setVisible(true);
    }
    
    private void addFormField(JPanel panel, String label, JComponent field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    
    }
}

