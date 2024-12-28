package com.manajementugas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
public class TaskManagementApp {

    public static void main(String[] args) {
        // Menampilkan Tampilan Menu Awal
        SwingUtilities.invokeLater(() -> new MainMenu());
    }

    // Tampilan Menu Awal
    public static class MainMenu {
        public MainMenu() {
            JFrame frame = new JFrame("Manajemen Tugas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLayout(new BorderLayout());

            // Panel untuk menampung komponen
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.decode("#434343"));

            /*Spacer
            panel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Label Judul
            JLabel titleLabel = new JLabel("");
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setFont(new Font("Pristina", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            panel.add(titleLabel); */

            // Spacer
            panel.add(Box.createRigidArea(new Dimension(0, 20)));

            //Logo
            ImageIcon imageIcon = new ImageIcon("D:\\logo.png"); // Replace with the actual file path
            Image scaledImage = imageIcon.getImage().getScaledInstance(300, 110, Image.SCALE_SMOOTH); // Resize as needed
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(imageLabel);

            // Spacer
            panel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Tombol Tambahkan Tugas
            JButton addButton = new JButton("Tambahkan Tugas");
            addButton.setPreferredSize(new Dimension(150, 30));
            addButton.setMaximumSize(new Dimension(150, 30));
            addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addButton.setBackground(Color.decode("#434343"));
            addButton.setForeground(Color.WHITE);
            addButton.setFocusPainted(false);
            addButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            addButton.addActionListener(e -> {
                frame.setVisible(false);  // Menutup jendela menu awal
                new ToDoListApp();  // Menampilkan jendela manajemen tugas
            });
            panel.add(addButton);

            // Spacer
            panel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Tombol Daftar Tugas
            JButton listButton = new JButton("Daftar Tugas");
            listButton.setPreferredSize(new Dimension(150, 30));
            listButton.setMaximumSize(new Dimension(150, 30));
            listButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            listButton.setBackground(Color.decode("#434343"));
            listButton.setForeground(Color.WHITE);
            listButton.setFocusPainted(false);
            listButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            listButton.addActionListener(e -> {
                frame.setVisible(false);  // Menutup jendela menu awal
                new ToDoListApp.SavedTasksWindow();  // Menampilkan jendela daftar tugas tersimpan
            });
            panel.add(listButton);

            // Spacer
            panel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Menambahkan panel ke frame
            frame.add(panel, BorderLayout.CENTER);
            frame.getContentPane().setBackground(Color.decode("#434343"));  // Set background frame agar full
            frame.setLocationRelativeTo(null);  // Pusatkan frame di layar
            frame.setVisible(true);


            // Add panel to frame
            frame.add(panel, BorderLayout.CENTER);
            frame.getContentPane().setBackground(Color.decode("#434343")); // Set full background color
            frame.setLocationRelativeTo(null);  // Center frame on screen
            frame.setVisible(true);

            // Spacer
            panel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Button to Exit
            JButton exitButton = new JButton("Keluar");
            exitButton.setPreferredSize(new Dimension(150, 30));
            exitButton.setMaximumSize(new Dimension(150, 30));
            exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            exitButton.setBackground(Color.decode("#434343"));
            exitButton.setForeground(Color.WHITE);
            exitButton.setFocusPainted(false);
            exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            exitButton.addActionListener(e -> {
                System.exit(0); // Exit the application
            });
            panel.add(exitButton);

            // Spacer
            panel.add(Box.createRigidArea(new Dimension(0, 30)));

            // Label untuk menampilkan kutipan motivasi
            JLabel quoteLabel = new JLabel("<html>Kutipan Motivasi: <i>Loading...</i></html>");
            quoteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            quoteLabel.setFont(new Font("Serif", Font.ITALIC, 14));
            quoteLabel.setForeground(Color.WHITE);
            panel.add(quoteLabel);

            // Menambahkan panel ke frame
            frame.add(panel, BorderLayout.CENTER);
            frame.getContentPane().setBackground(Color.decode("#434343"));  // Set background frame agar full
            frame.setLocationRelativeTo(null);  // Pusatkan frame di layar
            frame.setVisible(true);

            // Memuat kutipan motivasi dari API
            loadMotivationalQuote(quoteLabel);
        }

        private void loadMotivationalQuote(JLabel quoteLabel) {
            // Membuat permintaan ke API ZenQuotes
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://zenquotes.io/api/random"))
                    .build();

            // Mengirim permintaan secara asinkron
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        // Parsing hasil API dan menampilkan kutipan di JLabel
                        String quote = parseQuote(response);
                        SwingUtilities.invokeLater(() -> quoteLabel.setText("<html>Kutipan Motivasi: <i>" + quote + "</i></html>"));
                    })
                    .exceptionally(e -> {
                        // Jika gagal, tampilkan pesan error
                        SwingUtilities.invokeLater(() -> quoteLabel.setText("<html>Kutipan Motivasi: <i>Gagal memuat kutipan.</i></html>"));
                        return null;
                    });
        }

        private String parseQuote(String response) {
            // Parsing JSON response untuk mendapatkan kutipan
            try {
                int start = response.indexOf("\"q\":\"") + 4;
                int end = response.indexOf("\",\"a\":");
                return response.substring(start, end);
            } catch (Exception e) {
                return "Kutipan tidak tersedia.";
            }
        }

        // Kelas Renderer untuk Custom Checkbox pada List
        public static class TaskCellRenderer extends DefaultListCellRenderer {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Periksa jika value adalah JPanel dan memiliki child component
                if (value instanceof JPanel panel && panel.getComponentCount() > 0) {
                    // Ambil component pertama (JCheckBox) dari panel
                    JCheckBox checkBox = (JCheckBox) panel.getComponent(0);

                    // Atur warna background dan foreground sesuai status seleksi
                    checkBox.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                    checkBox.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                    return panel;  // Kembalikan seluruh JPanel untuk dirender
                }
                // Jika value tidak sesuai, kembalikan default renderer
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        }

        // Jendela Aplikasi Manajemen Tugas
        public static class ToDoListApp extends JFrame {
            DefaultListModel<JPanel> listModel;
            private JList<JPanel> taskList;
            JTextField taskField;
            private JButton addButton, deleteButton, saveButton, backButton;

            public ToDoListApp() {
                setTitle("Manajemen Tugas (To-Do List)");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(400, 400);
                setLocationRelativeTo(null);

                // Model dan Komponen List
                listModel = new DefaultListModel<>();
                taskList = new JList<>(listModel);
                taskList.setCellRenderer(new TaskCellRenderer());
                taskList.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int index = taskList.locationToIndex(e.getPoint());
                        if (index != -1) {
                            JPanel taskPanel = listModel.get(index);
                            JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
                            checkBox.setSelected(!checkBox.isSelected());
                            taskList.repaint();
                        }
                    }
                });
                JScrollPane scrollPane = new JScrollPane(taskList);

                // Panel Input Tugas
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BorderLayout());

                taskField = new JTextField();
                addButton = new JButton("Tambah");
                inputPanel.add(taskField, BorderLayout.CENTER);
                inputPanel.add(addButton, BorderLayout.EAST);

                // Panel Tombol Aksi
                JPanel buttonPanel = new JPanel();
                deleteButton = new JButton("Hapus");
                saveButton = new JButton("Simpan");
                backButton = new JButton("Kembali");

                buttonPanel.add(deleteButton);
                buttonPanel.add(saveButton);
                buttonPanel.add(backButton);

                // Menambahkan Komponen ke Frame
                add(inputPanel, BorderLayout.NORTH);
                add(scrollPane, BorderLayout.CENTER);
                add(buttonPanel, BorderLayout.SOUTH);

                // Event Listener
                addButton.addActionListener(e -> addTask());
                deleteButton.addActionListener(e -> deleteTask());
                saveButton.addActionListener(e -> saveAndRemoveSelectedTasks());
                backButton.addActionListener(e -> {
                    dispose();  // Menutup jendela saat tombol Kembali ditekan
                    new MainMenu();  // Kembali ke Menu Utama
                });

                setVisible(true);
            }

            // Bagian dari ToDoListApp yang sudah ada

            void addTask() {
                String task = taskField.getText().trim();

                if (!task.isEmpty()) {
                    // Menambahkan opsi untuk memilih gambar terkait tugas
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Pilih Gambar untuk Tugas");
                    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gambar", "jpg", "png", "gif"));

                    int result = fileChooser.showOpenDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        // Mendapatkan file gambar yang dipilih
                        File selectedFile = fileChooser.getSelectedFile();
                        String imagePath = selectedFile.getAbsolutePath(); // Simpan path gambar

                        // Membuat panel untuk tugas dan gambar
                        JPanel taskPanel = new JPanel(new BorderLayout());
                        JCheckBox checkBox = new JCheckBox(task);  // Tambahkan JCheckBox dengan teks tugas
                        taskPanel.add(checkBox, BorderLayout.WEST);

                        // Menambahkan gambar sebagai label di sebelah kanan tugas
                        ImageIcon imageIcon = new ImageIcon(imagePath);
                        Image image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Menyesuaikan ukuran gambar
                        JLabel imageLabel = new JLabel(new ImageIcon(image));
                        taskPanel.add(imageLabel, BorderLayout.EAST); // Menambahkan gambar di sebelah kanan checkbox
                        taskPanel.setPreferredSize(new Dimension(300, 60));

                        // Simpan path gambar dan tugas dalam objek
                        taskPanel.putClientProperty("imagePath", imagePath); // Menyimpan path gambar dalam panel

                        // Tambahkan panel ke model list
                        listModel.addElement(taskPanel);
                        taskField.setText("");  // Kosongkan input text
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Tugas tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            void deleteTask() {
                boolean taskDeleted = false; // Flag untuk mengecek apakah ada tugas yang dihapus

                for (int i = listModel.size() - 1; i >= 0; i--) {
                    JPanel taskPanel = listModel.get(i);
                    JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
                    if (checkBox.isSelected()) {
                        listModel.remove(i); // Hapus tugas dari listModel
                        taskDeleted = true; // Tandai bahwa ada tugas yang dihapus
                    }
                }

                // Tampilkan notifikasi jika ada tugas yang dihapus
                if (taskDeleted) {
                    JOptionPane.showMessageDialog(this, "Tugas yang dipilih berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Tidak ada tugas yang dipilih untuk dihapus.", "Informasi", JOptionPane.WARNING_MESSAGE);
                }
            }

            private JPanel createTaskPanel(String task) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                JCheckBox checkBox = new JCheckBox(task);
                panel.add(checkBox, BorderLayout.CENTER);
                return panel;
            }

            void saveAndRemoveSelectedTasks() {
                boolean hasSelectedTask = false;

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt", true))) {
                    for (int i = listModel.size() - 1; i >= 0; i--) {
                        JPanel taskPanel = listModel.getElementAt(i);
                        JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
                        if (checkBox.isSelected()) {
                            hasSelectedTask = true;
                            String task = checkBox.getText();

                            // Mengambil path gambar dari taskPanel
                            String imagePath = (String) taskPanel.getClientProperty("imagePath");

                            // Menyimpan tugas dan path gambar ke file
                            writer.write(task + " | " + imagePath);
                            writer.newLine();
                            listModel.remove(i);
                        }
                    }

                    if (hasSelectedTask) {
                        JOptionPane.showMessageDialog(this, "Tugas yang dipilih berhasil disimpan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Harus ada tugas yang ditandai atau dipilih!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Jendela Daftar Tugas Tersimpan
            public static class SavedTasksWindow extends JFrame {
                private JTable table;
                DefaultTableModel tableModel;

                public SavedTasksWindow() {
                    setTitle("Daftar Tugas Tersimpan");
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    setSize(400, 400);
                    setLocationRelativeTo(null);

                    String[] columnNames = {"No", "Nama Tugas", "Status", "Tandai"};
                    tableModel = new DefaultTableModel(columnNames, 0) {
                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                            if (columnIndex == 3) {
                                return Boolean.class; // Kolom "Tandai" untuk JCheckBox
                            }
                            return String.class;
                        }
                    };

                    try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
                        String line;
                        int no = 1;
                        while ((line = reader.readLine()) != null) {
                            tableModel.addRow(new Object[]{no++, line, "Belum", false});
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal membaca file tugas tersimpan!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    table = new JTable(tableModel);
                    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                    // Kombobox untuk memilih status (Belum/Selesai)
                    JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Belum", "Selesai"});
                    table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(statusComboBox));

                    JScrollPane scrollPane = new JScrollPane(table);
                    add(scrollPane, BorderLayout.CENTER);

                    // Panel untuk tombol aksi
                    JPanel buttonPanel = new JPanel();
                    JButton deleteButton = new JButton("Hapus");
                    JButton backButton = new JButton("Kembali");

                    buttonPanel.add(deleteButton);
                    buttonPanel.add(backButton);
                    add(buttonPanel, BorderLayout.SOUTH);

                    // Event listener untuk tombol "Hapus"
                    deleteButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            deleteSelectedTasks(SavedTasksWindow.this);
                        }
                    });

                    // Event listener untuk tombol "Kembali"
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Menutup jendela dan kembali ke MainMenu
                            dispose();
                            new MainMenu();
                        }
                    });

                    setVisible(true);
                }

                // Metode untuk menghapus tugas yang dipilih
                private static void deleteSelectedTasks(SavedTasksWindow savedTasksWindow) {
                    int[] selectedRows = savedTasksWindow.table.getSelectedRows();
                    if (selectedRows.length > 0) {
                        // Menghapus tugas yang dipilih dari tabel
                        ArrayList<String> tasksToDelete = new ArrayList<>();
                        for (int row : selectedRows) {
                            String task = (String) savedTasksWindow.tableModel.getValueAt(row, 1); // Mengambil nama tugas
                            tasksToDelete.add(task);
                        }

                        // Membaca file tugas dan menghapus tugas yang dipilih
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"));
                            ArrayList<String> tasks = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                tasks.add(line);
                            }
                            reader.close();

                            // Menulis ulang file tanpa tugas yang dipilih
                            BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"));
                            for (String task : tasks) {
                                if (!tasksToDelete.contains(task)) {
                                    writer.write(task);
                                    writer.newLine();
                                }
                            }
                            writer.close();

                            // Menghapus baris yang dipilih dari tabel
                            for (int i = selectedRows.length - 1; i >= 0; i--) {
                                savedTasksWindow.tableModel.removeRow(selectedRows[i]);
                            }

                            JOptionPane.showMessageDialog(savedTasksWindow, "Tugas yang dipilih berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(savedTasksWindow, "Gagal menghapus tugas!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(savedTasksWindow, "Pilih tugas yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }
}