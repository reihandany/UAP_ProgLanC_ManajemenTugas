package com.manajementugas;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagementAppTest {

    private TaskManagementApp.MainMenu.ToDoListApp toDoListApp;
    private File taskFile;

    @BeforeEach
    void setUp() {
        // Inisialisasi instance ToDoListApp untuk setiap test
        toDoListApp = new TaskManagementApp.MainMenu.ToDoListApp();

        // Buat file sementara untuk pengujian
        taskFile = new File("test_tasks.txt");
        try {
            if (!taskFile.exists()) {
                taskFile.createNewFile();
            }
        } catch (IOException e) {
            fail("Tidak dapat membuat file tugas sementara.");
        }
    }

    @AfterEach
    void tearDown() {
        // Hapus file tugas setelah pengujian
        if (taskFile.exists()) {
            taskFile.delete();
        }
    }

    @Test
    void testAddTask() {
        // Simulasikan penambahan tugas
        JTextField taskField = toDoListApp.taskField;
        taskField.setText("Belajar Unit Testing");

        // Jalankan metode tambah tugas
        toDoListApp.addTask();

        // Verifikasi tugas telah ditambahkan ke list model
        assertEquals(1, toDoListApp.listModel.size(), "Tugas tidak ditambahkan ke list.");

        JPanel taskPanel = toDoListApp.listModel.getElementAt(0);
        JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
        assertEquals("Belajar Unit Testing", checkBox.getText(), "Teks tugas tidak sesuai.");
    }

    @Test
    void testDeleteTask() {
        // Tambahkan tugas untuk pengujian
        JTextField taskField = toDoListApp.taskField;
        taskField.setText("Tugas untuk Dihapus");
        toDoListApp.addTask();

        // Tandai tugas sebagai dipilih
        JPanel taskPanel = toDoListApp.listModel.getElementAt(0);
        JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
        checkBox.setSelected(true);

        // Jalankan metode hapus tugas
        toDoListApp.deleteTask();

        // Verifikasi tugas telah dihapus
        assertEquals(0, toDoListApp.listModel.size(), "Tugas tidak dihapus dari list.");
    }

    @Test
    void testSaveAndRemoveSelectedTasks() {
        // Tambahkan tugas untuk pengujian
        JTextField taskField = toDoListApp.taskField;
        taskField.setText("Tugas untuk Disimpan");
        toDoListApp.addTask();

        // Tandai tugas sebagai dipilih
        JPanel taskPanel = toDoListApp.listModel.getElementAt(0);
        JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
        checkBox.setSelected(true);

        // Simpan tugas ke file
        toDoListApp.saveAndRemoveSelectedTasks();

        // Verifikasi tugas telah dihapus dari list model
        assertEquals(0, toDoListApp.listModel.size(), "Tugas tidak dihapus dari list setelah disimpan.");

        // Verifikasi tugas disimpan ke file
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line = reader.readLine();
            assertNotNull(line, "File tugas kosong.");
            assertTrue(line.contains("Tugas untuk Disimpan"), "Tugas tidak disimpan ke file.");
        } catch (IOException e) {
            fail("Gagal membaca file tugas.");
        }
    }

    @Test
    void testReadSavedTasks() {
        // Tulis tugas ke file secara manual
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            writer.write("Tugas Tersimpan | test_image_path.jpg");
            writer.newLine();
        } catch (IOException e) {
            fail("Gagal menulis ke file tugas.");
        }

        // Buat instance SavedTasksWindow
        TaskManagementApp.MainMenu.ToDoListApp.SavedTasksWindow savedTasksWindow =
                new TaskManagementApp.MainMenu.ToDoListApp.SavedTasksWindow();

        // Verifikasi tugas dimuat ke tabel
        assertEquals(1, savedTasksWindow.tableModel.getRowCount(), "Tugas tidak dimuat ke tabel.");

        String taskName = (String) savedTasksWindow.tableModel.getValueAt(0, 1);
        assertEquals("Tugas Tersimpan | test_image_path.jpg", taskName, "Nama tugas yang dimuat tidak sesuai.");

        savedTasksWindow.dispose(); // Tutup jendela setelah pengujian
    }
}
