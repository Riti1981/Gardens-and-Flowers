import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FlowerBouquetGame extends JFrame {

    private JPanel leftPanel, rightPanel;
    private JLabel infoLabel, flowerImageLabel;
    private JButton addButton;

    private DefaultListModel<String> bouquetModel;
    private JList<String> bouquetList;

    private ArrayList<Flower> flowers = new ArrayList<>();
    private Flower selectedFlower = null;

    public FlowerBouquetGame() {
        setTitle("Wildflower Bouquet Builder");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        loadFlowerData();

        // LEFT PANEL: Flower image and info only
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(520, 650));

        flowerImageLabel = new JLabel("Select a flower to see its picture.");
        flowerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flowerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        flowerImageLabel.setPreferredSize(new Dimension(450, 300));
        flowerImageLabel.setMaximumSize(new Dimension(450, 300));
        flowerImageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        leftPanel.add(flowerImageLabel);

        infoLabel = new JLabel("<html><div style='text-align:center; width:400px;'>Select a flower to learn more!</div></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        leftPanel.add(infoLabel);

        addButton = new JButton("Add to Bouquet");
        addButton.setEnabled(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setBackground(new Color(255, 215, 0));
        addButton.addActionListener(e -> addToBouquet());
        leftPanel.add(addButton);

        add(leftPanel, BorderLayout.WEST);

        // RIGHT PANEL: Flower buttons and bouquet list
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 20));

        JLabel title = new JLabel("Pick Your Flowers:");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(title);

        for (Flower f : flowers) {
            JButton btn = new JButton(f.name);
            btn.setMaximumSize(new Dimension(230, 32));
            btn.addActionListener(e -> displayFlower(f));

            rightPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            rightPanel.add(btn);
        }

        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(new JLabel("Your Bouquet:"));

        bouquetModel = new DefaultListModel<>();
        bouquetList = new JList<>(bouquetModel);

        JScrollPane scroll = new JScrollPane(bouquetList);
        scroll.setPreferredSize(new Dimension(230, 220));
        rightPanel.add(scroll);

        add(rightPanel, BorderLayout.CENTER);
    }

    private void loadFlowerData() {
        try {
            File file = new File("flower.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) continue;

                String desc = scanner.nextLine().trim();
                String img = scanner.nextLine().trim();

                flowers.add(new Flower(name, desc, img));
            }

            scanner.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading flower.txt");
        }
    }

    private void displayFlower(Flower f) {
        selectedFlower = f;

        infoLabel.setText("<html><div style='text-align:center; width:400px;'>" +
                "<b>" + f.name + "</b><br><br>" + f.description +
                "</div></html>");

        ImageIcon icon = new ImageIcon(f.imagePath);

        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(260, 260, Image.SCALE_SMOOTH);
            flowerImageLabel.setIcon(new ImageIcon(img));
            flowerImageLabel.setText("");
        } else {
            flowerImageLabel.setIcon(null);
            flowerImageLabel.setText("[ Image not found: " + f.imagePath + " ]");
        }

        addButton.setEnabled(true);
    }

    private void addToBouquet() {
        if (selectedFlower != null) {
            bouquetModel.addElement(selectedFlower.name);
        }
    }

    static class Flower {
        String name, description, imagePath;

        Flower(String n, String d, String i) {
            name = n;
            description = d;
            imagePath = i;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FlowerBouquetGame().setVisible(true);
        });
    }
}
