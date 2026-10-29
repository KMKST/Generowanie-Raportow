import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneratorRaportow extends JFrame {

    private JTextArea textArea;
    private JButton btnGeneruj;
    private JProgressBar progressBar;
    private JMenuItem itemZapisz;

    public GeneratorRaportow() {
        setTitle("INF.04 - Generator Raportów");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menuPlik = new JMenu("Plik");

        itemZapisz = new JMenuItem("Zapisz Raport");
        itemZapisz.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem itemWyjdz = new JMenuItem("Wyjście");

        itemZapisz.addActionListener(e -> zapiszPlik());
        itemWyjdz.addActionListener(e -> System.exit(0));

        menuPlik.add(itemZapisz);
        menuPlik.addSeparator();
        menuPlik.add(itemWyjdz);
        menuBar.add(menuPlik);
        setJMenuBar(menuBar);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelDolny = new JPanel();
        panelDolny.setLayout(new FlowLayout());

        btnGeneruj = new JButton("Generuj Raport w Tle");
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        panelDolny.add(btnGeneruj);
        panelDolny.add(progressBar);

        add(panelDolny, BorderLayout.SOUTH);

        btnGeneruj.addActionListener(e -> uruchomZadanieWTle());
    }

    private void uruchomZadanieWTle() {
        btnGeneruj.setEnabled(false);
        textArea.setText("");
        progressBar.setValue(0);

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(300);
                    int postep = i * 10;
                    publish(postep);
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int ostatniStan = chunks.get(chunks.size() - 1);
                progressBar.setValue(ostatniStan);
                textArea.append("Wygenerowano wiersz raportu (stan: " + ostatniStan + "%)\n");
            }

            @Override
            protected void done() {
                btnGeneruj.setEnabled(true);
                JOptionPane.showMessageDialog(GeneratorRaportow.this, "Generowanie raportu zakończone!");
            }
        };

        worker.execute();
    }

    private void zapiszPlik() {
        if (textArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brak tekstu do zapisu! Najpierw wygeneruj raport.", "Ostrzeżenie", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz raport");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki tekstowe (*.txt)", "txt"));

        int wynik = fileChooser.showSaveDialog(this);

        if (wynik == JFileChooser.APPROVE_OPTION) {
            File plik = fileChooser.getSelectedFile();

            if (!plik.getName().toLowerCase().endsWith(".txt")) {
                plik = new File(plik.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(plik)) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(this, "Plik został pomyślnie zapisany!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Błąd podczas zapisu pliku: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GeneratorRaportow().setVisible(true);
        });
    }
}
